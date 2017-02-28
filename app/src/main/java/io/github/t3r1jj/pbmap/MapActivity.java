package io.github.t3r1jj.pbmap;

import android.Manifest;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import io.github.t3r1jj.pbmap.model.gps.PBLocationListener;
import io.github.t3r1jj.pbmap.search.Search;
import io.github.t3r1jj.pbmap.search.SearchSuggestion;

public class MapActivity extends DrawerActivity
        implements PlacesDrawerFragment.PlaceNavigationDrawerCallbacks {

    private static final int REQUEST_LOCATION = 1;
    private Controller controller;
    private ViewGroup mapContainer;
    private FloatingActionButton infoButton;
    private FloatingActionButton gpsButton;
    private MenuItem backButton;
    private LocationManager locationManager;
    private PBLocationListener locationListener;
    private boolean explicitlyAskedForPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupVersion();
        mapContainer = (ViewGroup) findViewById(R.id.content_main);
        infoButton = (FloatingActionButton) findViewById(R.id.info_fab);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.loadDescription();
            }
        });
        gpsButton = (FloatingActionButton) findViewById(R.id.gps_fab);
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    explicitlyAskedForPermissions = true;
                    ActivityCompat.requestPermissions(MapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                } else {
                    requestLocationUpdates();
                }
            }
        });

        controller = new Controller(this);
        handleIntent(getIntent());

        if (!controller.isInitialized()) {
            try {
                controller.loadMap(getString(R.string.config_initial_map_path));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(getClass().getSimpleName(), "onResume");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (isGpsEnabled()) {
            if (locationListener == null) {
                locationListener = new PBLocationListener(controller);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            requestLocationUpdates();
        }
    }

    private boolean isGpsEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @SuppressWarnings("MissingPermission")
    private void requestLocationUpdates() {
        if (!isGpsEnabled()) {
            new GpsDialogFragment().show(getFragmentManager(), "gps");
            return;
        }
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(provider, 5, 5, locationListener);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            } else if (explicitlyAskedForPermissions) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(MapActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    new GpsPermissionsDialogFragment().show(getFragmentManager(), "gps_permissions");
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationListener != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(locationListener);
        }
    }

    public static class GpsDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setMessage(getString(R.string.gps_disabled_message, getString(R.string.app_name)))
                    .setCancelable(false)
                    .setPositiveButton(R.string.enable, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).create();
        }
    }

    private void setupVersion() {
        TextView versionText = (TextView) findViewById(R.id.about_version);
        versionText.setText(getString(R.string.about_version, BuildConfig.VERSION_NAME));
    }

    @Override
    protected void initializeContentView() {
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setIconified(false);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        backButton = menu.findItem(R.id.action_back);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                try {
                    controller.loadPreviousMap();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Search search = new Search(this);
            SearchSuggestion placeFound = search.find(intent.getStringExtra(SearchManager.QUERY));
            if (placeFound != null) {
                loadPlace(placeFound);
            } else {
                Toast.makeText(this, R.string.not_found, Toast.LENGTH_LONG).show();
            }
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            SearchSuggestion suggestion = new SearchSuggestion(intent);
            loadPlace(suggestion);
        }
    }

    private void loadPlace(SearchSuggestion suggestion) {
        try {
            controller.loadMap(suggestion);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMapView(View view) {
        mapContainer.removeAllViews();
        mapContainer.addView(view);
    }

    @SuppressWarnings("ConstantConditions")
    public void setLogo(ImageView view) {
        if (view == null) {
            getSupportActionBar().setLogo(null);
        } else {
            getSupportActionBar().setLogo(view.getDrawable());
        }
    }

    public void setBackButtonVisible(boolean visible) {
        if (backButton != null) {
            backButton.setVisible(visible);
        }
    }

    @Override
    public void onPlaceDrawerItemSelected(SearchSuggestion suggestion) {
        loadPlace(suggestion);
    }

    @Override
    public void onAboutDrawerItemSelected() {
        Intent aboutIntent = new Intent(this, AboutActivity.class);
        startActivity(aboutIntent);
    }


    public void popupInfo(Info info) {
        InfoSheetDialogFragment infoSheetDialogFragment = new InfoSheetDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(InfoSheetDialogFragment.INFO_KEY, info);
        infoSheetDialogFragment.setArguments(bundle);
        infoSheetDialogFragment.show(getSupportFragmentManager(), "info");
    }

    public static class InfoSheetDialogFragment extends BottomSheetDialogFragment {

        Info info;
        TextView titleText;
        TextView descriptionText;
        static String INFO_KEY = "INFO_KEY";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            info = (Info) getArguments().getSerializable(INFO_KEY);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.info_dialog, container, false);
            titleText = (TextView) rootView.findViewById(R.id.info_title);
            titleText.setText(info.title);
            descriptionText = (TextView) rootView.findViewById(R.id.info_description);
            if (info.descriptionResName != null) {
                descriptionText.setText(getStringResource(info.descriptionResName));
            }
            return rootView;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putSerializable(INFO_KEY, info);
        }

        String getStringResource(String aString) {
            String packageName = getActivity().getPackageName();
            int resId = getResources().getIdentifier(aString, "string", packageName);
            return getString(resId);
        }

    }

    public static class Info implements Serializable {
        String title;
        String descriptionResName;

        public Info(String title, String descriptionResName) {
            this.title = title;
            this.descriptionResName = descriptionResName;
        }
    }

    public static class GpsPermissionsDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.location_permissions)
                    .setMessage(getString(R.string.gps_permissions_disabled_message, getString(R.string.app_name)))
                    .setNegativeButton(R.string.ok, null)
                    .create();
        }
    }

    // TODO: Implement path

}
