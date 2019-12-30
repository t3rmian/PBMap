package io.github.t3r1jj.pbmap.model.map;

import android.content.Context;
import android.widget.ImageView;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import java.util.List;

import io.github.t3r1jj.pbmap.model.i18n.Translator;
import io.github.t3r1jj.pbmap.view.map.PlaceView;

public abstract class Place {

    @Attribute
    protected String id;
    @Attribute(name = "logo_path", required = false)
    private String logoPath;
    @Attribute(required = false)
    protected boolean hidden;
    @ElementList
    protected List<Coordinate> coordinates;

    public String getName(Context context) {
        String res = Translator.getResIdString(id, Translator.NAME_PREFIX);
        String translatedName = new Translator(context.getResources()).translate(res);
        if (translatedName == null) {
            return id.replace('_', ' ');
        }
        return translatedName;
    }

    public String getDescription(Context context) {
        String res = Translator.getResIdString(id, Translator.DESCRIPTION_PREFIX);
        return new Translator(context.getResources()).translate(res);
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

    abstract protected PlaceView createView(Context context);

    boolean hasInfo(Context context) {
        return getDescription(context) != null;
    }
}
