package io.github.t3r1jj.pbmap.main.external;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;

import com.yariksoffice.lingver.Lingver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;

import io.github.t3r1jj.pbmap.settings.SettingsLocaleStore;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
public class LocaleListenerTest {

    // Mock everything that Lingver needs and test persistence of the default Locale
    @Test
    @PrepareForTest({Lingver.class, Resources.class, ConfigurationCompat.class, LocaleListCompat.class})
    public void onReceive() throws NoSuchFieldException, IllegalAccessException {
        Field storeField = Lingver.class.getDeclaredField("store");
        storeField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(storeField, storeField.getModifiers() & ~Modifier.FINAL);
        Lingver lingver = mock(Lingver.class);
        Lingver.Companion lingverCompanion = PowerMockito.mock(Lingver.Companion.class);
        Resources resources = mock(Resources.class);
        mockStatic(Lingver.class);
        mockStatic(Lingver.Companion.class);
        mockStatic(Resources.class);
        mockStatic(ConfigurationCompat.class);
        Whitebox.setInternalState(
                Lingver.class, "Companion",
                lingverCompanion
        );
        PowerMockito.when(lingverCompanion.getInstance()).thenReturn(lingver);
        when(Resources.getSystem()).thenReturn(resources);
        LocaleListCompat localeList = mock(LocaleListCompat.class);
        when(ConfigurationCompat.getLocales(any())).thenReturn(localeList);
        Locale mockedLocale = Locale.ROOT;
        when(localeList.get(0)).thenReturn(mockedLocale);
        SharedPreferences prefs = mock(SharedPreferences.class);
        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        when(prefs.edit()).thenReturn(editor);
        when(editor.putString(any(), any())).thenReturn(editor);
        SettingsLocaleStore store = spy(new SettingsLocaleStore(mock(Context.class), Locale.FRANCE, prefs));
        storeField.set(lingver, store);
        LocaleListener localeListener = new LocaleListener(store);
        Intent intent = new Intent();
        Context context = mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);
        when(context.getResources()).thenReturn(resources);
        Configuration configuration = mock(Configuration.class);
        when(resources.getConfiguration()).thenReturn(configuration);
        configuration.locale = mockedLocale;
        localeListener.onReceive(context, intent);
        Locale defaultLocale = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).get(0);
        verify(store, times(1)).persistLocale(defaultLocale);
        assertEquals(defaultLocale, store.getDefaultLocale());
    }
}
