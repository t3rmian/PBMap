package io.github.t3r1jj.pbmap.model;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import io.github.t3r1jj.pbmap.model.map.Space;

public class Info implements Serializable {
    public String title;
    public String descriptionResName;
    public String url;
    private String logoPath;

    public Info(Space space) {
        this.title = space.getName();
        this.descriptionResName = space.getDescriptionResName();
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

}
