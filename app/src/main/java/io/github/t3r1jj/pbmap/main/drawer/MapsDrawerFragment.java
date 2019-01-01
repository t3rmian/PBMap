package io.github.t3r1jj.pbmap.main.drawer;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.search.MapsDao;
import io.github.t3r1jj.pbmap.search.SearchSuggestion;

public class MapsDrawerFragment extends NavigationDrawerFragment {

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
        MapsDao mapsDao = new MapsDao(getActivity());
        places = mapsDao.getMapSuggestions();
        Menu menu = navigationView.getMenu();
        int id = 0;
        for (SearchSuggestion searchSuggestion : places) {
            menu.add(R.id.maps_group, id--, Menu.NONE, searchSuggestion.getName(getActivity())).setIcon(android.R.drawable.ic_dialog_map);
        }
        super.selectItem(currentSelectedId);
        return navigationView;
    }

    @Override
    protected void onDrawerOpening() {
        String currentMapId = callbacks.getCurrentMapId();
        int id = 0;
        for (SearchSuggestion searchSuggestion : places) {
            if (searchSuggestion.getPlaceId().equals(currentMapId)) {
                currentSelectedId = id;
                super.highlightItem(currentSelectedId);
            }
            id--;
        }
    }

    @Override
    protected void selectItem(int itemId) {
        super.selectItem(itemId);
        if (callbacks != null) {
            if (itemId == R.id.menu_about) {
                callbacks.onAboutDrawerItemSelected();
            } else if (itemId == R.id.menu_help) {
                callbacks.onHelpDrawerItemSelected();
            } else {
                callbacks.onPlaceDrawerItemSelected(places.get(Math.abs(itemId)));
            }
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
