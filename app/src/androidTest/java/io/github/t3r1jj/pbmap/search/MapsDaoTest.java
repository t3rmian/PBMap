package io.github.t3r1jj.pbmap.search;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MapsDaoTest {

    private MapsDao dao;

    @Before
    public void setUp() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        dao = new MapsDao(appContext);
    }

    @Test
    public void getSearchSuggestions() {
        List<SearchSuggestion> searchSuggestions = dao.getSearchSuggestions(true);
        assertFalse(searchSuggestions.isEmpty());
    }

    @Test
    public void getMapSuggestions() {
        List<SearchSuggestion> searchSuggestions = dao.getMapSuggestions();
        assertFalse(searchSuggestions.isEmpty());
    }

    @TargetApi(19)
    @Test
    public void generatePlacesDocumentation() {
        List<SearchSuggestion> searchSuggestions = dao.getSearchSuggestions(false);
        Collections.sort(searchSuggestions, new Comparator<SearchSuggestion>() {
            @Override
            public int compare(SearchSuggestion o1, SearchSuggestion o2) {
                int mapComparison = o1.getMapId().compareTo(o2.getMapId());
                return mapComparison == 0 ? o1.getPlaceId().compareTo(o2.getPlaceId()) : mapComparison;
            }
        });
        String lastMapId = null;
        List<StringBuilder> stringBuilders = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilders.add(stringBuilder);
        String prefix = "";
        for (SearchSuggestion searchSuggestion : searchSuggestions) {
            if (stringBuilder.length() > 3000) {
                stringBuilder = new StringBuilder();
                stringBuilders.add(stringBuilder);
                prefix = "";
            }
            if (!searchSuggestion.getMapId().equals(lastMapId)) {
                lastMapId = searchSuggestion.getMapId();
                stringBuilder.append(prefix).append("* ").append(lastMapId.toLowerCase());
                prefix = "\n";
            }
            if (!searchSuggestion.getMapId().equals(searchSuggestion.getPlaceId())) {
                stringBuilder.append(prefix).append("  + ").append(searchSuggestion.getPlaceId().toLowerCase());
                prefix = "\n";
            }
        }

        final String TAG = "DOCUMENTATION";
        Log.i(TAG, "<START>");
        for (StringBuilder sb : stringBuilders) {
            Log.i(TAG, sb.toString());
        }
        Log.i(TAG, "<END>");
        assertTrue(!stringBuilder.toString().isEmpty());
    }
}