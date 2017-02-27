package io.github.t3r1jj.pbmap.model;

import android.content.Context;

import org.simpleframework.xml.Attribute;

import io.github.t3r1jj.pbmap.view.PlaceView;
import io.github.t3r1jj.pbmap.view.SpaceView;

public class Space extends Place {
    @Attribute(name = "reference_map_path", required = false)
    protected String referenceMapPath;
    @Attribute(name = "description_res_name", required = false)
    protected String descriptionResName;

    @Override
    public PlaceView createView(Context context) {
        return new SpaceView(context, this);
    }

    public String getReferenceMapPath() {
        return referenceMapPath;
    }

    public void setReferenceMapPath(String referenceMapPath) {
        this.referenceMapPath = referenceMapPath;
    }

    public String getDescriptionResName() {
        return descriptionResName;
    }

}
