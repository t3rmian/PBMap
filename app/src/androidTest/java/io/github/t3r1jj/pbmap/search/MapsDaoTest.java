package io.github.t3r1jj.pbmap.search;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertFalse;

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

}