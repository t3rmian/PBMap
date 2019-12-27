package io.github.t3r1jj.pbmap.main.drawer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView;

import java.util.Collections;
import java.util.List;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.search.MapsLoader;
import io.github.t3r1jj.pbmap.search.SearchSuggestion;
import io.github.t3r1jj.pbmap.settings.SettingsActivity;

public class MapsDrawerFragment extends NavigationDrawerFragment {

    public static final int RECREATE_REQUEST_RESULT_CODE = Activity.RESULT_FIRST_USER;
    private List<SearchSuggestion> places;
    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private PlaceNavigationDrawerCallbacks callbacks;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NavigationView navigationView = (NavigationView) super.onCreateView(inflater, container, savedInstanceState);
        MapsLoader mapsDao = new MapsLoader(getActivity());
        places = mapsDao.getMapSuggestions();
        Collections.sort(places);
        Menu menu = navigationView.getMenu();
        int id = 0;
        for (SearchSuggestion searchSuggestion : places) {
            menu.add(R.id.maps_group, id--, Menu.NONE, searchSuggestion.getName(getActivity()))
                    .setIcon(searchSuggestion.getLogoId(getActivity()));
        }

        onUpdateSelection(() -> super.selectItem(currentSelectedId));
        return navigationView;
    }

    @Override
    protected void onDrawerOpening() {
        onUpdateSelection(() -> super.highlightItem(currentSelectedId));
    }

    private void onUpdateSelection(Runnable updateCallback) {
        String currentMapId = callbacks.getCurrentMapId();
        int id = 0;
        for (SearchSuggestion searchSuggestion : places) {
            if (searchSuggestion.getPlaceId().equals(currentMapId)) {
                currentSelectedId = id;
                updateCallback.run();
                return;
            }
            id--;
        }
    }

    @Override
    protected void selectItem(int itemId) {
        super.selectItem(itemId);
        if (callbacks != null) {
            handleItemCallback(itemId);
        }
    }

    private void handleItemCallback(int itemId) {
        if (itemId == R.id.menu_about) {
            callbacks.onAboutDrawerItemSelected();
        } else if (itemId == R.id.menu_help) {
            callbacks.onHelpDrawerItemSelected();
        } else if (itemId == R.id.menu_settings) {
            Activity activity = getActivity();
            Intent settingsIntent = new Intent(activity, SettingsActivity.class);
            activity.startActivityForResult(settingsIntent, RECREATE_REQUEST_RESULT_CODE);
        } else {
            callbacks.onPlaceDrawerItemSelected(places.get(Math.abs(itemId)));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callbacks = (PlaceNavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement " + PlaceNavigationDrawerCallbacks.class.getSimpleName() + ".");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public interface PlaceNavigationDrawerCallbacks {

        void onPlaceDrawerItemSelected(SearchSuggestion suggestion);

        void onAboutDrawerItemSelected();

        void onHelpDrawerItemSelected();

        String getCurrentMapId();
    }

}
