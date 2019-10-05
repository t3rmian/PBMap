package io.github.t3r1jj.pbmap.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.view.map.routing.GeoMarker;

/**
 * Displays dialog with marker type options
 * Supports {@link MapActivity} only
 * Requires setting argument bundle with {@link MarkerDialogFragment#MOTION_EVENT_KEY}
 */
public class MarkerDialogFragment extends DialogFragment {

    static final String MOTION_EVENT_KEY = "MOTION_EVENT_KEY";
    private Controller controller;
    private MotionEvent event;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
        Bundle arguments = Objects.requireNonNull(getArguments());
        event = Objects.requireNonNull(arguments.getParcelable(MOTION_EVENT_KEY));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MapActivity activity = ((MapActivity) Objects.requireNonNull(getActivity()));
        controller = activity.getController();

        View rootView = inflater.inflate(R.layout.popup_marker_choice, container, true);
        rootView.findViewById(R.id.popup_source_button).setOnClickListener(v -> {
            MarkerDialogFragment.this.dismiss();
            controller.onUserMarkerChoice(event, GeoMarker.Marker.SOURCE);
        });
        rootView.findViewById(R.id.popup_destination_button).setOnClickListener(v -> {
            MarkerDialogFragment.this.dismiss();
            controller.onUserMarkerChoice(event, GeoMarker.Marker.DESTINATION);
        });
        rootView.findViewById(R.id.popup_improve_button).setOnClickListener(v -> {
            MarkerDialogFragment.this.dismiss();
            ImproveDialogFragment improveDialogFragment = new ImproveDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(MOTION_EVENT_KEY, event);
            improveDialogFragment.setArguments(bundle);
            improveDialogFragment.show(Objects.requireNonNull(getFragmentManager()), "IMPROVE_DIALOG");
        });
        rootView.findViewById(R.id.popup_cancel_button).setOnClickListener(v -> MarkerDialogFragment.this.dismiss());
        return rootView;
    }

}
