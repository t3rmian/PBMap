package io.github.t3r1jj.pbmap.model;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.Serializable;

import io.github.t3r1jj.pbmap.model.i18n.Translator;
import io.github.t3r1jj.pbmap.model.map.Place;
import io.github.t3r1jj.pbmap.model.map.Space;

public class Info implements Serializable {
    private final String url;
    private final String id;
    private final String logoPath;

    public Info(Space space) {
        this.id = space.getId();
        this.url = space.getUrl();
        this.logoPath = space.getLogoPath();
    }

    public Drawable createLogo(Context context) {
        ImageView logo = Place.createLogo(context, logoPath);
        if (logo != null) {
            Drawable prototype = logo.getDrawable();
            BitmapDrawable drawable = new BitmapDrawable(context.getResources(), DrawableUtils.drawableToBitmap(prototype));
            drawable.setColorFilter(0xff000000, PorterDuff.Mode.MULTIPLY);
            return drawable;
        }
        return null;
    }

    public String getName(Context context) {
        String name = new Translator(context.getResources()).translateName(id);
        return Translator.postFormat(name);
    }

    public String getDescription(Context context) {
        String res = Translator.getResIdString(id, Translator.DESCRIPTION_PREFIX);
        return new Translator(context.getResources()).translate(res);
    }

    public String getAddress(Context context) {
        String res = Translator.getResIdString(id, Translator.ADDRESS_PREFIX);
        return new Translator(context.getResources()).translate(res);
    }

    public String getUrl() {
        return url;
    }
}
