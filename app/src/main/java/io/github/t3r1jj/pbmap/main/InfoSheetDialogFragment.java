package io.github.t3r1jj.pbmap.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.model.Info;

public class InfoSheetDialogFragment extends BottomSheetDialogFragment {

    static final String INFO_KEY = "INFO_KEY";
    private Info info;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info = (Info) getArguments().getSerializable(INFO_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.info_dialog, container, false);
        TextView titleText = (TextView) rootView.findViewById(R.id.info_title);
        titleText.setText(info.getName(getActivity()));
        TextView descriptionText = (TextView) rootView.findViewById(R.id.info_description);
        descriptionText.setText(info.getDescription(getActivity()));
        ImageView logo = (ImageView) rootView.findViewById(R.id.info_logo);
        logo.setImageDrawable(info.createLogo(getActivity()));
        TextView url = (TextView) rootView.findViewById(R.id.info_url);
        url.setText(info.url);
        url.setMovementMethod(LinkMovementMethod.getInstance());
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(INFO_KEY, info);
    }


}
