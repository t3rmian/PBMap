package io.github.t3r1jj.pbmap.model;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import io.github.t3r1jj.pbmap.model.map.Space;

public class Info implements Serializable {
    public final String url;
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
        if (logoPath != null) {
            try {
                InputStream inputStream = context.getAssets().open(logoPath);
                return Drawable.createFromStream(inputStream, null);
            } catch (IllegalArgumentException | IOException e) {
                e.printStackTrace();
            }
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

}
