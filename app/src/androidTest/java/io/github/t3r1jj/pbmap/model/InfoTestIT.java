package io.github.t3r1jj.pbmap.model;

import android.graphics.drawable.Drawable;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import io.github.t3r1jj.pbmap.model.map.Space;

import static io.github.t3r1jj.pbmap.testing.TestUtils.CaseInsensitiveSubstringMatcher.containsIgnoringCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InfoTestIT {

    private Space space;

    @Before
    public void setUp() {
        space = mock(Space.class);
        whenGetResIdReturnWithPrefix("test");
    }

    @Test
    public void createLogo() {
        Info info = new Info(space);
        Drawable logo = info.createLogo(InstrumentationRegistry.getInstrumentation().getContext());
        assertNull(logo);
    }

    @Test
    public void createLogo_UnknownAsset() {
        when(space.getLogoPath()).thenReturn("unknown");
        Info info = new Info(space);
        Drawable logo = info.createLogo(InstrumentationRegistry.getInstrumentation().getContext());
        assertNull(logo);
    }

    @Test
    public void createLogo_KnownAsset() {
        when(space.getLogoPath()).thenReturn("test_logo.png");
        Info info = new Info(space);
        Drawable logo = info.createLogo(InstrumentationRegistry.getInstrumentation().getContext());
        assertNotNull(logo);
    }

    @Test
    public void getName() {
        Info info = new Info(space);
        String name = info.getName(InstrumentationRegistry.getInstrumentation().getTargetContext());
        assertEquals("test", name);
    }

    @Test
    public void getName_Known() {
        whenGetResIdReturnWithPrefix("deanery");
        Info info = new Info(space);
        String name = info.getName(InstrumentationRegistry.getInstrumentation().getTargetContext());
        assertEquals("Deanery", name);
    }

    @Test
    public void getDescription() {
        Info info = new Info(space);
        String description = info.getDescription(InstrumentationRegistry.getInstrumentation().getTargetContext());
        assertNull(description);
    }

    @Test
    public void getDescription_Known() {
        whenGetResIdReturnWithPrefix("about");
        Info info = new Info(space);
        String description = info.getDescription(InstrumentationRegistry.getInstrumentation().getTargetContext());
        assertThat(description, containsIgnoringCase("pbmap"));
    }

    @Test
    public void getAddress() {
        Info info = new Info(space);
        String address = info.getAddress(InstrumentationRegistry.getInstrumentation().getTargetContext());
        assertNull(address);
    }

    @Test
    public void getAddress_Known() {
        whenGetResIdReturnWithPrefix("pb_wi");
        Info info = new Info(space);
        String address = info.getAddress(InstrumentationRegistry.getInstrumentation().getTargetContext());
        assertThat(address, containsIgnoringCase("faculty"));
        assertThat(address, containsIgnoringCase("computer science"));
    }

    @Test
    public void getUrl() {
        Info info = new Info(space);
        String url = info.getUrl();
        assertNull(url);
    }

    @Test
    public void getUrl_NotEmpty() {
        when(space.getUrl()).thenReturn("url");
        Info info = new Info(space);
        String url = info.getUrl();
        assertEquals("url", url);
    }

    private void whenGetResIdReturnWithPrefix(String prefix) {
        when(space.getId()).thenReturn(prefix);
        when(space.getNameResIdString()).thenReturn(prefix + "_name");
        when(space.getAddressResId()).thenReturn(prefix + "_address");
        when(space.getDescriptionResIdString()).thenReturn(prefix + "_description");
    }
}