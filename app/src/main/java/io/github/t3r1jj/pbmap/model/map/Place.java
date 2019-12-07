package io.github.t3r1jj.pbmap.model.map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.view.map.PlaceView;

public abstract class Place {

    public static final String NAME_PREFIX = "name_";
    private static final String DESCRIPTION_PREFIX = "description_";
    @Attribute
    protected String id;
    @Attribute(name = "logo_path", required = false)
    protected String logoPath;
    @Attribute(required = false)
    protected boolean hidden;
    @ElementList
    protected List<Coordinate> coordinates;

    /**
     *
     * @param id of the place
     * @return name res id with special characters replaced by _ and with prepended prefix
     */
    public static String getResIdString(String id, String prefix) {
        return prefix + id.toLowerCase()
                .replace("/", "_")
                .replace(" ", "_")
                .replace("-", "_");
    }

    /**
     *
     * @return see {@link #getResIdString(String, String)} with name prefix
     */
    public String getNameResIdString() {
        return getResIdString(id, NAME_PREFIX);
    }

    /**
     *
     * @return see {@link #getResIdString(String, String)} with description prefix
     */
    public String getDescriptionResIdString() {
        return getResIdString(id, DESCRIPTION_PREFIX);
    }

    public String getName(Context context) {
        String translatedName = getStringResource(context, getNameResIdString());
        if (translatedName == null) {
            return id.replace('_', ' ');
        }
        return translatedName;
    }

    public String getDescription(Context context) {
        return getStringResource(context, getDescriptionResIdString());
    }

    String getStringResource(Context context, String aString) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        if (resId == 0) {
            return null;
        }
        return context.getString(resId);
    }

    public String getId() {
        return id;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public ImageView createLogo(Context context) {
        return createLogo(context, logoPath);
    }

    public static ImageView createLogo(Context context, String logoPath) {
        String packageName = context.getPackageName();
        if (logoPath == null) {
            return null;
        }
        int resId = context.getResources().getIdentifier(logoPath, "drawable", packageName);
        if (resId == 0) {
            return null;
        }
        ImageView logo = new ImageView(context);
        logo.setImageDrawable(context.getResources().getDrawable(resId));
        return logo;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id='" + id + '\'' +
                ", shape=" + coordinates +
                '}';
    }

    public Coordinate getCenter() {
        Coordinate center = new Coordinate();
        for (Coordinate coordinate : coordinates) {
            center.lng += coordinate.lng;
            center.lat += coordinate.lat;
            center.alt += coordinate.alt;
        }
        int size = coordinates.size();
        center.lng /= size;
        center.lat /= size;
        center.alt /= size;
        return center;
    }

    abstract public PlaceView createView(Context context);

    public boolean hasInfo(Context context) {
        return getDescription(context) != null;
    }
}
