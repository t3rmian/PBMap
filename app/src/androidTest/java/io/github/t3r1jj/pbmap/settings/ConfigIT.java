package io.github.t3r1jj.pbmap.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import io.github.t3r1jj.pbmap.main.MapApplication;
import io.github.t3r1jj.pbmap.model.dictionary.MeasurementSystem;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ConfigIT {

    private SharedPreferences preferences;
    private Context targetContext;

    @Before
    public void setUp() {
        targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(targetContext);
        preferences.edit().clear().apply();
    }

    @Test
    public void initPreferences_EN_SI() {
        Config.getInstance().initPreferences(targetContext, Locale.ENGLISH);
        assertEquals(MeasurementSystem.SI.toString(), preferences.getString(MapApplication.UNIT_SYSTEM, null));
    }

    @Test
    public void initPreferences_PL_SI() {
        Config.getInstance().initPreferences(targetContext, Locale.GERMANY);
        assertEquals(MeasurementSystem.SI.toString(), preferences.getString(MapApplication.UNIT_SYSTEM, null));
    }

    @Test
    public void initPreferences_US_US() {
        Config.getInstance().initPreferences(targetContext, Locale.US);
        assertEquals(MeasurementSystem.US.toString(), preferences.getString(MapApplication.UNIT_SYSTEM, null));
    }
}