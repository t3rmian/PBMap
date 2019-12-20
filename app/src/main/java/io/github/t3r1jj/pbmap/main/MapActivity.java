package io.github.t3r1jj.pbmap.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
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

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.yariksoffice.lingver.Lingver;

import io.github.t3r1jj.pbmap.BuildConfig;
import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.about.AboutActivity;
import io.github.t3r1jj.pbmap.Config;
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

    static final String INTRODUCTION_FINISHED = "io.github.t3r1jj.pbmap.main.INTRODUCTION_FINISHED";
    private static final int REQUEST_LOCATION = 1;
    private DeviceServices deviceServices;
    private Controller controller;
    private ViewGroup mapContainer;
    private FloatingActionButton infoButton;
    private FloatingActionButton gpsButton;
    private FloatingActionMenu levelMenu;
    private FloatingActionMenu moreOptions;
    private FloatingActionButton levelUpButton;
    private FloatingActionButton levelDownButton;
    private FloatingActionButton levelRightButton;
    private FloatingActionButton levelLeftButton;
    private FloatingActionButton helpButton;
    private TextView distanceText;
    private MenuItem backButton;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private boolean explicitlyAskedForPermissions;
    private boolean showBackButton;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        deviceServices = new DeviceServices(this);
        controller = new Controller(this);
        handleIntent(getIntent(), true);

        super.onCreate(savedInstanceState);
        mapContainer = findViewById(R.id.content_main);

        setUpTexts();
        setUpButtons();
        setUpZoomControls();
        controller.postLoad();
    }

    private void setUpButtons() {
        moreOptions = findViewById(R.id.more_fab_menu);
        moreOptions.getMenuIconView().setContentDescription(getString(R.string.more_features));

        levelMenu = findViewById(R.id.level_fab_menu);
        levelMenu.getMenuIconView().setContentDescription(getString(R.string.floor));
        levelUpButton = findViewById(R.id.up_fab);

        Drawable triangleDrawable = getResources().getDrawable(R.drawable.triangle_up_drawable);
        DrawableCompat.setTint(triangleDrawable, ContextCompat.getColor(this, R.color.colorSecondaryText));
        levelUpButton.setImageDrawable(triangleDrawable);
        levelUpButton.setOnClickListener(v -> controller.onNavigationPerformed(PBMap.Navigation.UP));
        levelDownButton = findViewById(R.id.down_fab);
        triangleDrawable = getResources().getDrawable(R.drawable.triangle_down_drawable);
        DrawableCompat.setTint(triangleDrawable, ContextCompat.getColor(this, R.color.colorSecondaryText));
        levelDownButton.setImageDrawable(triangleDrawable);
        levelDownButton.setOnClickListener(v -> controller.onNavigationPerformed(PBMap.Navigation.DOWN));
        levelRightButton = findViewById(R.id.right_fab);
        triangleDrawable = getResources().getDrawable(R.drawable.triangle_down_drawable);
        DrawableCompat.setTint(triangleDrawable, ContextCompat.getColor(this, R.color.colorSecondaryText));
        triangleDrawable = rotateDrawable(triangleDrawable, -90);
        levelRightButton.setImageDrawable(triangleDrawable);
        levelRightButton.setOnClickListener(v -> controller.onNavigationPerformed(PBMap.Navigation.RIGHT));
        levelLeftButton = findViewById(R.id.left_fab);
        triangleDrawable = getResources().getDrawable(R.drawable.triangle_down_drawable);
        DrawableCompat.setTint(triangleDrawable, ContextCompat.getColor(this, R.color.colorSecondaryText));
        triangleDrawable = rotateDrawable(triangleDrawable, 90);
        levelLeftButton.setImageDrawable(triangleDrawable);
        levelLeftButton.setOnClickListener(v -> controller.onNavigationPerformed(PBMap.Navigation.LEFT));

        infoButton = findViewById(R.id.info_fab);
        infoButton.setOnClickListener(view -> controller.loadDescription());

        gpsButton = findViewById(R.id.gps_fab);
        gpsButton.setOnClickListener(view -> {
            if (deviceServices.doesNotHaveGpsPermissions()) {
                explicitlyAskedForPermissions = true;
                ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            } else {
                requestLocationOnDemand();
            }
        });
        helpButton = findViewById(R.id.help_fab);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.contains(INTRODUCTION_FINISHED)) {
            hideHelp();
        } else {
            helpButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink));
            helpButton.setOnClickListener(v -> onHelpDrawerItemSelected());
        }
    }

    private void hideHelp() {
        helpButton.clearAnimation();
        helpButton.setVisibility(View.GONE);
    }

    private Drawable rotateDrawable(Drawable drawable, float angle) {
        Bitmap originalBitmap = drawableToBitmap(drawable);
        Bitmap rotatedBitmap = Bitmap.createBitmap(originalBitmap.getHeight(), originalBitmap.getWidth(), Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(rotatedBitmap);
        int pivot = originalBitmap.getHeight() / 2;
        tempCanvas.rotate(angle, pivot, pivot);
        tempCanvas.drawBitmap(originalBitmap, 0, 0, null);
        return new BitmapDrawable(getResources(), rotatedBitmap);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
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

    enum LocationState {
        OFF, AEROPLANE, WIFI_OFF, ON
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (requestLocationUpdates() == LocationState.OFF) {
                    new GpsDialogFragment().show(getFragmentManager(), "gps");
                }
            } else {
                controller.updatePosition(null);
                if (explicitlyAskedForPermissions) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(MapActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                        new GpsPermissionsDialogFragment().show(getFragmentManager(), "gps_permissions");
                    }
                }
            }
        }
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
        distanceText = findViewById(R.id.distance);
        TextView versionText = findViewById(R.id.about_version);
        versionText.setText(getString(R.string.about_version, BuildConfig.VERSION_NAME + ", Map tiles by Stamen Design, under CC BY 3.0. Data by OpenStreetMap, under ODbL."));
    }

    @Override
    protected void initializeContentView() {
        setContentView(R.layout.activity_map);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        switch (item.getItemId()) {
            case R.id.action_back:
                controller.onNavigationPerformed(PBMap.Navigation.BACK);
                break;
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
            if (WebUriParser.containsWebUri(intent.getDataString())) {
                handleWebUri(intent, preload);
            } else {
                SearchSuggestion suggestion = new SearchSuggestion(intent);
                controller.loadMap(suggestion, preload);
            }
        } else if (WebUriParser.containsWebUri(referrer)) {
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
            e.printStackTrace();
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

    @SuppressWarnings("ConstantConditions")
    public void setLogo(ImageView view) {
        if (view == null) {
            super.setLogo(null);
        } else {
            super.setLogo(view.getDrawable());
        }
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
        if (visible) {
            levelMenu.setVisibility(View.VISIBLE);
        } else {
            levelMenu.setVisibility(View.GONE);
        }
    }

    public void setLevelButtonVisible(PBMap.Navigation navigation, boolean visible) {
        if (navigation == PBMap.Navigation.UP) {
            if (visible) {
                levelUpButton.setVisibility(View.VISIBLE);
            } else {
                levelUpButton.setVisibility(View.GONE);
            }
        } else if (navigation == PBMap.Navigation.DOWN) {
            if (visible) {
                levelDownButton.setVisibility(View.VISIBLE);
            } else {
                levelDownButton.setVisibility(View.GONE);
            }
        } else if (navigation == PBMap.Navigation.LEFT) {
            if (visible) {
                levelLeftButton.setVisibility(View.VISIBLE);
            } else {
                levelLeftButton.setVisibility(View.GONE);
            }
        } else if (navigation == PBMap.Navigation.RIGHT) {
            if (visible) {
                levelRightButton.setVisibility(View.VISIBLE);
            } else {
                levelRightButton.setVisibility(View.GONE);
            }
        } else {
            if (backButton != null) {
                backButton.setVisible(visible);
            }
            showBackButton = visible;
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
        final int levelMenuVisibility = levelMenu.getVisibility();
        if (levelMenuVisibility != View.VISIBLE) {
            levelMenu.setVisibility(View.VISIBLE);
        }
        final boolean backItemVisible = backButton.isVisible();
        if (!backItemVisible) {
            backButton.setVisible(true);
        }

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int[] attrs = new int[]{R.attr.actionBarSize};
        TypedArray ta = obtainStyledAttributes(attrs);
        int toolBarHeight = ta.getDimensionPixelSize(0, -1);
        ta.recycle();
        Rect screenRect = new Rect(0, toolBarHeight, width, height);
        new TapTargetSequence(this)
                .targets(
                        defaultWrap(TapTarget.forView(findViewById(R.id.action_search),
                                getString(R.string.action_search), getString(R.string.action_search_description))),
                        defaultWrap(TapTarget.forToolbarNavigationIcon(toolbar,
                                getString(R.string.menu), getString(R.string.menu_description))),
                        defaultWrap(TapTarget.forView(levelMenu.isOpened() ? levelMenu.getChildAt(levelMenu.getChildCount() - 2) : levelMenu.getChildAt(levelMenu.getChildCount() - 1),
                                getString(R.string.floor), getString(R.string.floor_description)))
                                .transparentTarget(true)
                        ,
                        defaultWrap(TapTarget.forView(moreOptions.isOpened() ? moreOptions.getChildAt(moreOptions.getChildCount() - 2) : moreOptions.getChildAt(moreOptions.getChildCount() - 1),
                                getString(R.string.more_features), getString(R.string.more_features_description)))
                                .transparentTarget(true)
                        ,
                        defaultWrap(TapTarget.forBounds(screenRect,
                                getString(R.string.map), getString(R.string.maps_description)))
                                .transparentTarget(true)
                                .targetRadius(getResources().getDimensionPixelSize(R.dimen.target_map_radius))
                        ,
                        defaultWrap(TapTarget.forView(findViewById(R.id.action_back) != null ? findViewById(R.id.action_back) : findViewById(R.id.action_search),
                                getString(R.string.action_back), getString(R.string.action_back_description)))

                )
                .continueOnCancel(true)
                .considerOuterCircleCanceled(true)
                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                        levelMenu.setVisibility(levelMenuVisibility);
                        backButton.setVisible(backItemVisible);
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MapActivity.this);
                        preferences.edit().putBoolean(INTRODUCTION_FINISHED, true).apply();
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                    }
                })
                .start();
    }

    TapTarget defaultWrap(TapTarget tapTarget) {
        tapTarget.targetCircleColor(R.color.colorAccent)
                .outerCircleColor(R.color.colorAccentSecondary)
                .textColor(R.color.colorSecondaryText)
                .titleTextColor(R.color.colorSecondaryText)
                .descriptionTextColor(R.color.colorSecondaryText)
                .tintTarget(false)
                .drawShadow(true)
                .outerCircleAlpha(1)
                .transparentTarget(false);
        return tapTarget;
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

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(PARCELABLE_KEY_CONTROLLER_MEMENTO)) {
            controller.restoreState((Controller.Memento) savedInstanceState.getParcelable(PARCELABLE_KEY_CONTROLLER_MEMENTO), this);
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
