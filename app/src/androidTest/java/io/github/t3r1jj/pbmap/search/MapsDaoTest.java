package io.github.t3r1jj.pbmap.search;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
        List<SearchSuggestion> searchSuggestions = dao.getSearchSuggestions();
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
        List<SearchSuggestion> searchSuggestions = dao.getSearchSuggestions();
        Collections.sort(searchSuggestions, new Comparator<SearchSuggestion>() {
            @Override
            public int compare(SearchSuggestion o1, SearchSuggestion o2) {
                int mapComparison = o1.getMapId().compareTo(o2.getMapId());
                return mapComparison == 0 ? o1.getPlaceId().compareTo(o2.getPlaceId()) : mapComparison;
            }
        });
        String lastMapId = null;
        StringBuilder stringBuilder = new StringBuilder();
        String prefix = "";
        for (SearchSuggestion searchSuggestion : searchSuggestions) {
            if (!searchSuggestion.getMapId().equals(lastMapId)) {
                lastMapId = searchSuggestion.getMapId();
                stringBuilder.append(prefix).append("- ").append(lastMapId);
                prefix = "\n";
            }
            stringBuilder.append(prefix).append("-- ").append(searchSuggestion.getPlaceId());
        }
        String placesMD = stringBuilder.toString();
        System.out.println(placesMD);
        assertTrue(!placesMD.isEmpty());
    }
}