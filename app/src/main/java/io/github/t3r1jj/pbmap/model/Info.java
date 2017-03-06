package io.github.t3r1jj.pbmap.model;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import io.github.t3r1jj.pbmap.model.map.Space;

public class Info implements Serializable {
    public String url;
    private String nameId;
    private String descriptionId;
    private String logoPath;

    public Info(Space space) {
        this.nameId = space.getNameResIdString();
        this.descriptionId = space.getDescriptionResIdString();
        this.url = space.getUrl();
        this.logoPath = space.getLogoPath();
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
        return getStringResource(context, nameId);
    }

    public String getDescription(Context context) {
        return getStringResource(context, descriptionId);
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
