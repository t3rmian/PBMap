package io.github.t3r1jj.pbmap.model;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.Serializable;

import io.github.t3r1jj.pbmap.main.DrawableUtils;
import io.github.t3r1jj.pbmap.model.map.Place;
import io.github.t3r1jj.pbmap.model.map.Space;

public class Info implements Serializable {
    private final String url;
    private final String addressId;
    private final String nameId;
    private final String rawName;
    private final String descriptionId;
    private final String logoPath;

    public Info(Space space) {
        this.nameId = space.getNameResIdString();
        this.rawName = space.getId();
        this.descriptionId = space.getDescriptionResIdString();
        this.url = space.getUrl();
        this.logoPath = space.getLogoPath();
        this.addressId = space.getAddressResId();
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
        String name = getStringResource(context, nameId);
        name = (name == null ? rawName : name);
        return name.replace("_", " ").replace("\n", " ").trim();
    }

    public String getDescription(Context context) {
        return getStringResource(context, descriptionId);
    }

    public String getAddress(Context context) {
        return getStringResource(context, addressId);
    }

    private String getStringResource(Context context, String aString) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        if (resId == 0) {
            return null;
        }
        return context.getString(resId);
    }

    public String getUrl() {
        return url;
    }
}
