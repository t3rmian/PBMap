package io.github.t3r1jj.pbmap.model.map;

import android.widget.ImageView;

import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SpotTestIT {

    private static final String data = "<spot id=\"gym/test\" logo_path=\"test_logo\">" +
            "\t<coordinates>\t\t<coordinate lat=\"53.11883933267212\" lng=\"23.14608460083008\" alt=\"150.0\"/>\t</coordinates>" +
            "</spot>";

    private final Serializer serializer = new Persister();

    @Test
    @SmallTest
    public void createLogo() throws Exception {
        Spot space = serializer.read(Spot.class, data);
        ImageView logo = space.createLogo(InstrumentationRegistry.getInstrumentation().getContext());
        assertNotNull(logo);
    }

    @Test
    @SmallTest
    public void createLogo_404() throws Exception {
        String dataLogo404 = data.replace("test_logo", "unknown");
        Spot space = serializer.read(Spot.class, dataLogo404);
        ImageView logo = space.createLogo(InstrumentationRegistry.getInstrumentation().getContext());
        assertNull(logo);
    }
}