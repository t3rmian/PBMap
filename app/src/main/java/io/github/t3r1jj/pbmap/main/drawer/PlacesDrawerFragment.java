package io.github.t3r1jj.pbmap.main.drawer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.search.MapsDao;
import io.github.t3r1jj.pbmap.search.SearchSuggestion;

public class PlacesDrawerFragment extends NavigationDrawerFragment {

    private List<SearchSuggestion> places;
    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private PlaceNavigationDrawerCallbacks callbacks;
    private int previousPosition;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @NonNull
    @Override
    protected List<String> getPlaceNames() {
        MapsDao mapsDao = new MapsDao(getActivity());
        places = mapsDao.getMapSuggestions();
        List<String> mapNames = new ArrayList<>();
        for (SearchSuggestion searchSuggestion : places) {
            mapNames.add(searchSuggestion.place);
        }
        mapNames.add(getString(R.string.about));
        return mapNames;
    }

    @Override
    protected void onDrawerClosed() {
        int placesCount = places.size();
        if (placesCount != 0) {
            if (currentSelectedPosition == placesCount) {
                drawerListView.setItemChecked(previousPosition, true);
                currentSelectedPosition = previousPosition;
            }
        }
    }

    @Override
    protected void selectItem(int position) {
        previousPosition = currentSelectedPosition;
        super.selectItem(position);
        if (places == null) {
            return;
        }
        int placesCount = places.size();
        if (callbacks != null && placesCount != 0) {
            if (placesCount > position) {
                callbacks.onPlaceDrawerItemSelected(places.get(position));
            } else if (placesCount == position) {
                callbacks.onAboutDrawerItemSelected();
            }
        }
    }

    @Override
    public void onAttach(Context activity) {
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
    }

}
