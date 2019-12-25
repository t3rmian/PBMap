package io.github.t3r1jj.pbmap.main.drawer;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import io.github.t3r1jj.pbmap.R;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
abstract class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    int currentSelectedId = 1;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;

    private View fragmentContainerView;
    private boolean fromSavedInstanceState;
    private boolean userLearnedDrawer;
    private NavigationView navigationView;
    private DrawerArrowDrawable initialLogo;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            currentSelectedId = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            fromSavedInstanceState = true;
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navigationView = (NavigationView) inflater.inflate(
                R.layout.fragment_drawer, container, false);
        navigationView.setNavigationItemSelectedListener(item -> {
            selectItem(item.getItemId());
            return true;
        });
        return navigationView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (drawerLayout != null && isDrawerOpen() && isAdded()) {
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.name_app);
    }

    private boolean isDrawerOpen() {
        return drawerLayout != null && drawerLayout.isDrawerOpen(fragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        fragmentContainerView = getActivity().findViewById(fragmentId);
        this.drawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        this.drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        drawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                NavigationDrawerFragment.this.drawerLayout,                    /* DrawerLayout object */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (isAdded()) {
                    userLearnedDrawer();
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                if (newState == DrawerLayout.STATE_SETTLING && !isDrawerOpen()) {
                    NavigationDrawerFragment.this.onDrawerOpening();
                }
            }
        };
        initialLogo = drawerToggle.getDrawerArrowDrawable();

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!userLearnedDrawer && !fromSavedInstanceState) {
            this.drawerLayout.openDrawer(fragmentContainerView);
            userLearnedDrawer();
        }

        // Defer code dependent on restoration of previous instance state.
        this.drawerLayout.post(() -> drawerToggle.syncState());

        this.drawerLayout.addDrawerListener(drawerToggle);
    }

    protected abstract void onDrawerOpening();


    private void userLearnedDrawer() {
        if (!userLearnedDrawer) {
            userLearnedDrawer = true;
            SharedPreferences sp = PreferenceManager
                    .getDefaultSharedPreferences(getActivity());
            sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
        }
    }

    void selectItem(int itemId) {
        if (navigationView == null) {
            return;
        }
        Menu menu = navigationView.getMenu();
        if (itemId == 0) {
            menu.getItem(0).setCheckable(true);
            menu.getItem(0).setChecked(true);
        } else {
            highlightItem(itemId);
        }
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(fragmentContainerView);
        }
    }

    final void highlightItem(int itemId) {
        navigationView.getMenu().findItem(currentSelectedId).setChecked(false);
        currentSelectedId = itemId;
        navigationView.getMenu().findItem(currentSelectedId).setCheckable(true);
        navigationView.getMenu().findItem(currentSelectedId).setChecked(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, currentSelectedId);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    public void setLogo(Drawable drawable) {
        if (drawable == null) {
            drawerToggle.setDrawerArrowDrawable(initialLogo);
        } else {
            drawerToggle.setDrawerArrowDrawable(new DrawerArrowDrawable(getActivity()) {
                @Override
                public void draw(Canvas canvas) {
                    drawable.setBounds(initialLogo.getBounds());
                    drawable.draw(canvas);
                }
            });
        }
    }
}
