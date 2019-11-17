package io.github.t3r1jj.pbmap.model;

import android.graphics.drawable.Drawable;

import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import io.github.t3r1jj.pbmap.model.map.Space;

import static io.github.t3r1jj.pbmap.testing.TestUtils.containsIgnoringCase;
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
        whenGetResIdReturnWithPostfix("test");
    }

    @Test
    @SmallTest
    public void createLogo() {
        Info info = new Info(space);
        Drawable logo = info.createLogo(InstrumentationRegistry.getInstrumentation().getContext());
        assertNull(logo);
    }

    @Test
    @SmallTest
    public void createLogo_UnknownAsset() {
        when(space.getLogoPath()).thenReturn("unknown");
        Info info = new Info(space);
        Drawable logo = info.createLogo(InstrumentationRegistry.getInstrumentation().getContext());
        assertNull(logo);
    }

    @Test
    @SmallTest
    public void createLogo_KnownAsset() {
        when(space.getLogoPath()).thenReturn("test_logo");
        Info info = new Info(space);
        Drawable logo = info.createLogo(InstrumentationRegistry.getInstrumentation().getContext());
        assertNotNull(logo);
    }

    @Test
    @SmallTest
    public void getName() {
        Info info = new Info(space);
        String name = info.getName(InstrumentationRegistry.getInstrumentation().getTargetContext());
        assertEquals("test", name);
    }

    @Test
    @SmallTest
    public void getName_Known() {
        whenGetResIdReturnWithPostfix("deanery");
        Info info = new Info(space);
        String name = info.getName(InstrumentationRegistry.getInstrumentation().getTargetContext());
        assertEquals("Deanery", name);
    }

    @Test
    @SmallTest
    public void getDescription() {
        Info info = new Info(space);
        String description = info.getDescription(InstrumentationRegistry.getInstrumentation().getTargetContext());
        assertNull(description);
    }

    @Test
    @SmallTest
    public void getDescription_Known() {
        whenGetResIdReturnWithPostfix("about");
        when(space.getDescriptionResIdString()).thenReturn("about_description");
        Info info = new Info(space);
        String description = info.getDescription(InstrumentationRegistry.getInstrumentation().getTargetContext());
        assertThat(description, containsIgnoringCase("pbmap"));
    }

    @Test
    @SmallTest
    public void getAddress() {
        Info info = new Info(space);
        String address = info.getAddress(InstrumentationRegistry.getInstrumentation().getTargetContext());
        assertNull(address);
    }

    @Test
    @SmallTest
    public void getAddress_Known() {
        whenGetResIdReturnWithPostfix("pb_wi");
        Info info = new Info(space);
        String address = info.getAddress(InstrumentationRegistry.getInstrumentation().getTargetContext());
        assertThat(address, containsIgnoringCase("faculty"));
        assertThat(address, containsIgnoringCase("computer science"));
    }

    @Test
    @SmallTest
    public void getUrl() {
        Info info = new Info(space);
        String url = info.getUrl();
        assertNull(url);
    }

    @Test
    @SmallTest
    public void getUrl_NotEmpty() {
        when(space.getUrl()).thenReturn("url");
        Info info = new Info(space);
        String url = info.getUrl();
        assertEquals("url", url);
    }

    private void whenGetResIdReturnWithPostfix(String postfix) {
        when(space.getId()).thenReturn(postfix);
        when(space.getNameResIdString()).thenReturn("name_" + postfix);
        when(space.getAddressResId()).thenReturn("address_" + postfix);
        when(space.getDescriptionResIdString()).thenReturn("description_" + postfix);
    }
}