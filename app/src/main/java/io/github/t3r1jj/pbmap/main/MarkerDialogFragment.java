package io.github.t3r1jj.pbmap.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.view.routing.GeoMarker;

public class MarkerDialogFragment extends DialogFragment {

    static final String MOTION_EVENT_KEY = "MOTION_EVENT_KEY";
    private MotionEvent event;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
        event = getArguments().getParcelable(MOTION_EVENT_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.popup_marker_choice, container, true);
        rootView.findViewById(R.id.popup_source_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkerDialogFragment.this.dismiss();
                Controller controller = ((MapActivity) getActivity()).getController();
                controller.onUserMarkerChoice(event, GeoMarker.Marker.SOURCE);
            }
        });
        rootView.findViewById(R.id.popup_destination_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkerDialogFragment.this.dismiss();
                Controller controller = ((MapActivity) getActivity()).getController();
                controller.onUserMarkerChoice(event, GeoMarker.Marker.DESTINATION);
            }
        });
        rootView.findViewById(R.id.popup_cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkerDialogFragment.this.dismiss();
            }
        });
        return rootView;
    }
}
