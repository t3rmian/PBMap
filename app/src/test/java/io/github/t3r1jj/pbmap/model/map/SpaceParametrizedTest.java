package io.github.t3r1jj.pbmap.model.map;

import android.content.Context;
import android.content.res.Resources;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class SpaceParametrizedTest {

    private static String getData(boolean hasUrl) {
        return "    <space id=\"PB_WI\" reference_map_path=\"data/pb_wi.xml\" " + (hasUrl ? "url=\"https://wi.pb.edu.pl\"" : "") + ">\n" +
                "        <coordinates>\n" +
                "            <coordinate alt=\"150\" lat=\"53.11696\" lng=\"23.14564\" />\n" +
                "            <coordinate alt=\"150\" lat=\"53.11726\" lng=\"23.14709\" />\n" +
                "            <coordinate alt=\"150\" lat=\"53.11641\" lng=\"23.14759\" />\n" +
                "            <coordinate alt=\"150\" lat=\"53.11611\" lng=\"23.14614\" />\n" +
                "        </coordinates>\n" +
                "    </space>";
    }

    @Parameterized.Parameter
    public boolean hasDescription;
    @Parameterized.Parameter(1)
    public boolean hasUrl;
    @Parameterized.Parameter(2)
    public boolean hasAddress;
    @Parameterized.Parameter(3)
    public boolean hasInfo;

    @Parameterized.Parameters(name = "{index}: hasDescription = {0}, hasUrl = {1}, hasAddress = {2}, hasInfo = {3}")
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {true, true, true, true},
                {true, true, false, true},
                {true, false, false, true},
                {false, false, false, false},
                {false, false, true, true},
                {false, true, true, true},
                {true, false, true, true},
                {false, true, false, true},
        };
        return Arrays.asList(data);
    }


    @Test
    public void hasInfo() throws Exception {
        Serializer serializer = new Persister();
        Space space = serializer.read(Space.class, getData(hasUrl));
        Context context = mock(Context.class);
        Resources resources = mock(Resources.class);
        when(context.getResources()).thenReturn(resources);
        when(resources.getIdentifier(eq("description_pb_wi"), eq("string"), any())).thenReturn(hasDescription ? 1 : 0);
        when(resources.getIdentifier(eq("address_pb_wi"), eq("string"), any())).thenReturn(hasAddress ? 1 : 0);
        when(context.getString(eq(1))).thenReturn("a");
        when(resources.getString(eq(1))).thenReturn("a");
        assertEquals(hasInfo, space.hasInfo(context));
    }
}