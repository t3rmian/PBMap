package io.github.t3r1jj.pbmap;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.github.t3r1jj.pbmap.model.PBMap;
import io.github.t3r1jj.pbmap.search.SearchSuggestion;

public class PlacesDrawerFragment extends NavigationDrawerFragment {

    private List<SearchSuggestion> places = new ArrayList<>();
    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private PlaceNavigationDrawerCallbacks mCallbacks;
    private int previousPosition;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @NonNull
    @Override
    protected String[] getPlaceNames() {
        Dao dao = new Dao(getActivity());
        String[] mapNames;
        int i = 0;
        try {
            List<PBMap> maps = dao.loadMaps();
            for (PBMap map : maps) {
                if (map.isPrimary()) {
                    places.add(new SearchSuggestion(map.getName(), map.getPath()));
                }
            }
            Log.d(getClass().getSimpleName(), places.size() + " size");
            mapNames = new String[places.size() + 1];
            for (SearchSuggestion suggestion : places) {
                mapNames[i++] = suggestion.place;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mapNames = new String[1];
        }
        mapNames[i] = getString(R.string.about);
        return mapNames;
    }

    @Override
    protected void onDrawerClosed() {
        int placesCount = places.size();
        if (placesCount != 0) {
            if (mCurrentSelectedPosition == placesCount) {
                mDrawerListView.setItemChecked(previousPosition, true);
                mCurrentSelectedPosition = previousPosition;
            }
        }
    }

    @Override
    protected void selectItem(int position) {
        previousPosition = mCurrentSelectedPosition;
        super.selectItem(position);
        int placesCount = places.size();
        if (mCallbacks != null && placesCount != 0) {
            if (placesCount > position) {
                mCallbacks.onPlaceDrawerItemSelected(places.get(position));
            } else if (placesCount == position) {
                mCallbacks.onAboutDrawerItemSelected();
            }
        }
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (PlaceNavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement " + PlaceNavigationDrawerCallbacks.class.getSimpleName() + ".");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public interface PlaceNavigationDrawerCallbacks {

        void onPlaceDrawerItemSelected(SearchSuggestion suggestion);

        void onAboutDrawerItemSelected();
    }

}
