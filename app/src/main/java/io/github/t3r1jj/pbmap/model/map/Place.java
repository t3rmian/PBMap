package io.github.t3r1jj.pbmap.model.map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.github.t3r1jj.pbmap.view.map.PlaceView;

public abstract class Place {

    private static final String NAME_POSTFIX = "_name";
    private static final String DESCRIPTION_POSTFIX = "_description";
    @Attribute
    protected String id;
    @Attribute(name = "logo_path", required = false)
    protected String logoPath;
    @ElementList
    protected List<Coordinate> coordinates;

    public static String getNameResIdString(String id) {
        return id.toLowerCase() + NAME_POSTFIX;
    }

    public String getNameResIdString() {
        return getNameResIdString(id);
    }

    public String getDescriptionResIdString() {
        return id + DESCRIPTION_POSTFIX;
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

    private String getStringResource(Context context, String aString) {
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

    public void setId(String id) {
        this.id = id;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public ImageView createLogo(Context context) {
        if (logoPath != null) {
            try {
                InputStream inputStream = context.getAssets().open(logoPath);
                Drawable drawable = Drawable.createFromStream(inputStream, null);
                ImageView logo = new ImageView(context);
                logo.setImageDrawable(drawable);
                return logo;
            } catch (IllegalArgumentException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
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

}
