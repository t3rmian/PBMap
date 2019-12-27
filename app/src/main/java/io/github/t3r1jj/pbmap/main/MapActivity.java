package io.github.t3r1jj.pbmap.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.yariksoffice.lingver.Lingver;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.t3r1jj.pbmap.BuildConfig;
import io.github.t3r1jj.pbmap.Config;
import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.about.AboutActivity;
import io.github.t3r1jj.pbmap.main.drawer.DrawerActivity;
import io.github.t3r1jj.pbmap.main.drawer.MapsDrawerFragment;
import io.github.t3r1jj.pbmap.model.Info;
import io.github.t3r1jj.pbmap.model.gps.PBLocationListener;
import io.github.t3r1jj.pbmap.model.map.PBMap;
import io.github.t3r1jj.pbmap.model.map.Place;
import io.github.t3r1jj.pbmap.search.Search;
import io.github.t3r1jj.pbmap.search.SearchSuggestion;
import io.github.t3r1jj.pbmap.search.WebUriParser;

import static io.github.t3r1jj.pbmap.main.Controller.PARCELABLE_KEY_CONTROLLER_MEMENTO;
import static io.github.t3r1jj.pbmap.main.drawer.MapsDrawerFragment.RECREATE_REQUEST_RESULT_CODE;

public class MapActivity extends DrawerActivity
        implements MapsDrawerFragment.PlaceNavigationDrawerCallbacks {

    private static final int REQUEST_LOCATION = 1;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    MenuItem backButton;
    @BindView(R.id.level_fab_menu)
    FloatingActionMenu levelMenu;
    @BindView(R.id.more_fab_menu)
    FloatingActionMenu moreOptions;

    @BindView(R.id.content_main)
    ViewGroup mapContainer;
    @BindView(R.id.info_fab)
    FloatingActionButton infoButton;
    @BindView(R.id.up_fab)
    FloatingActionButton levelUpButton;
    @BindView(R.id.down_fab)
    FloatingActionButton levelDownButton;
    @BindView(R.id.right_fab)
    FloatingActionButton levelRightButton;
    @BindView(R.id.left_fab)
    FloatingActionButton levelLeftButton;
    @BindView(R.id.help_fab)
    FloatingActionButton helpButton;
    @BindView(R.id.distance)
    TextView distanceText;

    private DeviceServices deviceServices;
    private Controller controller;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private boolean explicitlyAskedForPermissions;
    private boolean showBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        deviceServices = new DeviceServices(this);
        controller = new Controller(this);
        handleIntent(getIntent(), true);

        super.onCreate(savedInstanceState);

        setUpTexts();
        setUpButtons();
        setUpZoomControls();
        controller.postLoad();
    }

    @Override
    protected void initializeContentView() {
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    private void setUpButtons() {
        moreOptions.getMenuIconView().setContentDescription(getString(R.string.more_features));
        levelMenu.getMenuIconView().setContentDescription(getString(R.string.floor));

        initButtonTriangle(levelUpButton, 0);
        initButtonTriangle(levelRightButton, 90);
        initButtonTriangle(levelDownButton, 180);
        initButtonTriangle(levelLeftButton, 270);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.contains(Tutorial.INTRODUCTION_FINISHED)) {
            hideHelp();
        } else {
            helpButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink));
            helpButton.setOnClickListener(v -> onHelpDrawerItemSelected());
        }
    }

    private void initButtonTriangle(ImageButton button, int angle) {
        Drawable triangleDrawable = getResources().getDrawable(R.drawable.triangle_up_drawable);
        DrawableCompat.setTint(triangleDrawable, ContextCompat.getColor(this, R.color.colorSecondaryText));
        triangleDrawable = DrawableUtils.rotateDrawable(getResources(), triangleDrawable, angle);
        button.setImageDrawable(triangleDrawable);
    }

    @Override
    public void onBackPressed() {
        if (controller == null || !controller.onNavigationPerformed(PBMap.Navigation.BACK)) {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.up_fab)
    void navigateUp() {
        controller.onNavigationPerformed(PBMap.Navigation.UP);
    }

    @OnClick(R.id.right_fab)
    void navigateRight() {
        controller.onNavigationPerformed(PBMap.Navigation.RIGHT);
    }

    @OnClick(R.id.down_fab)
    void navigateDown() {
        controller.onNavigationPerformed(PBMap.Navigation.DOWN);
    }

    @OnClick(R.id.left_fab)
    void navigateLeft() {
        controller.onNavigationPerformed(PBMap.Navigation.LEFT);
    }

    @OnClick(R.id.info_fab)
    void loadDescription() {
        controller.loadDescription();
    }

    @OnClick(R.id.gps_fab)
    void requestLocation() {
        if (deviceServices.doesNotHaveGpsPermissions()) {
            explicitlyAskedForPermissions = true;
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            requestLocationOnDemand();
        }
    }

    private void hideHelp() {
        helpButton.clearAnimation();
        helpButton.setVisibility(View.GONE);
    }

    private void requestLocationOnDemand() {
        switch (requestLocationUpdates()) {
            case OFF:
                controller.updatePosition(null);
                new GpsDialogFragment().show(getFragmentManager(), "gps");
                break;
            case ON:
                Toast.makeText(MapActivity.this, R.string.waiting_for_location, Toast.LENGTH_SHORT).show();
                break;
            case AEROPLANE:
                Toast.makeText(MapActivity.this, R.string.airplane_enabled, Toast.LENGTH_SHORT).show();
                break;
            case WIFI_OFF:
                Toast.makeText(MapActivity.this, getString(R.string.waiting_for_location) + "\n" + getString(R.string.wifi_disabled), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void setUpZoomControls() {
        ZoomControls zoomControls = findViewById(R.id.zoom_controls);
        zoomControls.setOnZoomOutClickListener(v -> controller.onZoom(false));
        zoomControls.setOnZoomInClickListener(v -> controller.onZoom(true));
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager = deviceServices.getLocationManager();
        if (deviceServices.doesNotHaveGpsPermissions() || requestLocationUpdates() == LocationState.OFF) {
            controller.updatePosition(null);
        }
    }

    /**
     * @return true if successfully requested location updates without any problems, assure that permissions are granted
     */
    @SuppressLint("MissingPermission")
    private LocationState requestLocationUpdates() {
        if (!deviceServices.isLocationEnabled(locationManager)) {
            return LocationState.OFF;
        }
        Criteria criteria = new Criteria();
        if (locationListener == null) {
            locationListener = new PBLocationListener(controller);
        }
        String provider = locationManager.getBestProvider(criteria, true);
        if (deviceServices.isAirplaneOn()) {
            locationManager.requestLocationUpdates(provider, 5, 5, locationListener);
            return LocationState.AEROPLANE;
        }
        if ("network".equals(provider) && deviceServices.isWifiDisabled()) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 5, locationListener);
                return LocationState.ON;
            } else {
                locationManager.requestLocationUpdates(provider, 5, 5, locationListener);
                return LocationState.WIFI_OFF;
            }
        }
        locationManager.requestLocationUpdates(provider, 5, 5, locationListener);
        return LocationState.ON;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (isPermissionGrantedButDisabled(grantResults)) {
                new GpsDialogFragment().show(getFragmentManager(), "gps");
            } else if (isPermissionAskedButRejected()) {
                new GpsPermissionsDialogFragment().show(getFragmentManager(), "gps_permissions");
            }
        }
    }

    private boolean isPermissionAskedButRejected() {
        controller.updatePosition(null);
        return explicitlyAskedForPermissions && !ActivityCompat.shouldShowRequestPermissionRationale(MapActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private boolean isPermissionGrantedButDisabled(@NonNull int[] grantResults) {
        return grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && requestLocationUpdates() == LocationState.OFF;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationListener != null) {
            if (deviceServices.doesNotHaveGpsPermissions()) {
                return;
            }
            //noinspection MissingPermission
            locationManager.removeUpdates(locationListener);
        }
    }

    private void setUpTexts() {
        TextView versionText = findViewById(R.id.about_version);
        versionText.setText(getString(R.string.about_version, BuildConfig.VERSION_NAME +
                ", Map tiles by Stamen Design, under CC BY 3.0. Data by OpenStreetMap, under ODbL."));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        backButton = menu.findItem(R.id.action_back);
        backButton.setVisible(showBackButton);

        SearchManager searchManager =
                (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setIconified(false);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_back) {
            controller.onNavigationPerformed(PBMap.Navigation.BACK);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent, false);
    }

    private void handleIntent(Intent intent, boolean preload) {
        String referrer = consumeReferrer();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            handleSearchQuery(intent, preload);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            if (WebUriParser.isWebUrl(intent.getDataString())) {
                handleWebUri(intent, preload);
            } else {
                SearchSuggestion suggestion = new SearchSuggestion(intent);
                controller.loadMap(suggestion, preload);
            }
        } else if (WebUriParser.isWebUrl(referrer)) {
            intent.setData(Uri.parse(referrer));
            handleWebUri(intent, preload);
        } else if (!controller.isInitialized()) {
            controller.loadMap();
        }
    }

    private String consumeReferrer() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String referrer = preferences.getString(InstallListener.REFERRER, null);
        if (referrer != null) {
            preferences.edit().remove(InstallListener.REFERRER).apply();
        }
        return referrer;
    }

    @SuppressWarnings("ConstantConditions")
    private void handleWebUri(Intent intent, boolean preload) {
        Uri uri = intent.getData();
        String query = WebUriParser.parseIntoCommonFormat(uri);
        if (query != null) {
            intent.putExtra(SearchManager.QUERY, query);
            handleSearchQuery(intent, preload);
        } else if (!controller.isInitialized()) {
            controller.loadMap();
        }
    }

    private void handleSearchQuery(Intent intent, boolean preload) {
        Search search = new Search(this);
        String searchQuery = intent.getStringExtra(SearchManager.QUERY);
        boolean searchById = !intent.hasExtra(SearchManager.USER_QUERY);
        SearchSuggestion placeFound = null;
        try {
            placeFound = search.findFirst(searchQuery, searchById);
            if (intent.hasExtra(SearchManager.EXTRA_DATA_KEY)) {
                Location location = intent.getParcelableExtra(SearchManager.EXTRA_DATA_KEY);
                placeFound.setLocationCoordinate(location);
            }
        } catch (Exception e) {
            Log.w(MapActivity.class.getName(), e);
        }
        if (placeFound == null) {
            Toast.makeText(this, R.string.not_found, Toast.LENGTH_LONG).show();
            if (!controller.isInitialized()) {
                controller.loadMap();
            }
        } else {
            controller.loadMap(placeFound, preload);
        }
    }

    public void setMapView(View view) {
        mapContainer.removeAllViews();
        mapContainer.addView(view);
    }

    public void setLogo(ImageView view) {
        super.setLogo(view == null ? null : view.getDrawable());
    }

    @SuppressWarnings("ConstantConditions")
    public void setTitle(String nameId) {
        int resId = getResources().getIdentifier(PBMap.getResIdString(nameId, Place.NAME_PREFIX), "string", getPackageName());
        if (resId > 0) {
            getSupportActionBar().setSubtitle(getString(resId).replace("\n", " ").trim());
        } else {
            getSupportActionBar().setSubtitle(nameId.replace('_', ' ').trim());
        }
    }

    public void setDistance(String distance) {
        if (distance == null) {
            distanceText.setVisibility(View.GONE);
        } else {
            distanceText.setText(distance);
            distanceText.setVisibility(View.VISIBLE);
        }
    }

    public void setInfoButtonVisible(boolean visible) {
        if (visible) {
            infoButton.setVisibility(moreOptions.isOpened() ? View.VISIBLE : View.INVISIBLE);
        } else {
            infoButton.setVisibility(View.GONE);
        }
    }

    public void setLevelMenuVisible(boolean visible) {
        setNotGone(levelMenu, visible);
    }

    public void setLevelButtonVisible(PBMap.Navigation navigation, boolean visible) {
        if (navigation == PBMap.Navigation.UP) {
            setNotGone(levelUpButton, visible);
        } else if (navigation == PBMap.Navigation.DOWN) {
            setNotGone(levelDownButton, visible);
        } else if (navigation == PBMap.Navigation.LEFT) {
            setNotGone(levelLeftButton, visible);
        } else if (navigation == PBMap.Navigation.RIGHT) {
            setNotGone(levelRightButton, visible);
        } else {
            if (backButton != null) {
                backButton.setVisible(visible);
            }
            showBackButton = visible;
        }
    }

    private void setNotGone(View view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPlaceDrawerItemSelected(SearchSuggestion suggestion) {
        controller.loadMap(suggestion, false);
    }

    @Override
    public void onAboutDrawerItemSelected() {
        Intent aboutIntent = new Intent(this, AboutActivity.class);
        startActivity(aboutIntent);
    }

    @Override
    public void onHelpDrawerItemSelected() {
        hideHelp();
        Tutorial tutorial = new Tutorial(this);
        tutorial.start();
    }

    @Override
    public String getCurrentMapId() {
        return controller.getCurrentMapId();
    }

    public void popupInfo(Info info) {
        InfoSheetDialogFragment infoSheetDialogFragment = new InfoSheetDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(InfoSheetDialogFragment.INFO_KEY, info);
        infoSheetDialogFragment.setArguments(bundle);
        infoSheetDialogFragment.show(getSupportFragmentManager(), "info");
    }

    public void askUserForMarkerChoice(final MotionEvent event) {
        MarkerDialogFragment markerDialogFragment = new MarkerDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MarkerDialogFragment.MOTION_EVENT_KEY, event);
        markerDialogFragment.setArguments(bundle);
        markerDialogFragment.show(getSupportFragmentManager(), "marker");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(PARCELABLE_KEY_CONTROLLER_MEMENTO)) {
            controller.restoreState(Objects.requireNonNull(savedInstanceState.getParcelable(PARCELABLE_KEY_CONTROLLER_MEMENTO)), this);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PARCELABLE_KEY_CONTROLLER_MEMENTO, controller.getCurrentState());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECREATE_REQUEST_RESULT_CODE && resultCode == RECREATE_REQUEST_RESULT_CODE) {
            Config.getInstance().initPreferences(this, Lingver.getInstance().getLocale());
            recreate();
        }
    }

    Controller getController() {
        return controller;
    }

    LocationListener getLocationListener() {
        return locationListener;
    }
}
