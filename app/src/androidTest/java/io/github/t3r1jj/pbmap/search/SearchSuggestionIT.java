package io.github.t3r1jj.pbmap.search;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.t3r1jj.pbmap.R;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class SearchSuggestionIT {

    @Test
    public void getMapName_PlaceIsRootMap() {
        Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String mapName = new SearchSuggestion("", "").getMapName(ctx);
        assertEquals(ctx.getString(R.string.map), mapName);
    }
}