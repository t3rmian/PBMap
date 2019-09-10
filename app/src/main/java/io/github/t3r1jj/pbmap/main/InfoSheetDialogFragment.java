package io.github.t3r1jj.pbmap.main;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.model.Info;

/**
 * Sheet that displays additional {@link Info} when passed in {@link Bundle} as {@link #INFO_KEY} argument
 */
public class InfoSheetDialogFragment extends BottomSheetDialogFragment {

    static final String INFO_KEY = "INFO_KEY";
    private Info info;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info = (Info) Objects.requireNonNull(getArguments()).getSerializable(INFO_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.info_dialog, container, false);
        TextView titleText = rootView.findViewById(R.id.info_title);
        titleText.setText(info.getName(getActivity()));
        TextView addressText = rootView.findViewById(R.id.info_address);
        addressText.setText(info.getAddress(getActivity()));
        TextView descriptionText = rootView.findViewById(R.id.info_description);
        descriptionText.setText(info.getDescription(getActivity()));
        ImageView logo = rootView.findViewById(R.id.info_logo);
        logo.setImageDrawable(info.createLogo(getActivity()));
        TextView url = rootView.findViewById(R.id.info_url);
        url.setText(info.getUrl());
        url.setMovementMethod(LinkMovementMethod.getInstance());
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(INFO_KEY, info);
    }

}
