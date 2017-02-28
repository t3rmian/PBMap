package io.github.t3r1jj.pbmap.model.map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "tiles_config")
class TilesConfig {
    @Attribute
    String path;
    @Attribute
    float zoom;
    @Attribute
    int width;
    @Attribute
    int height;

    @Override
    public String toString() {
        return "TilesConfig{" +
                "path='" + path + '\'' +
                ", zoom=" + zoom +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
