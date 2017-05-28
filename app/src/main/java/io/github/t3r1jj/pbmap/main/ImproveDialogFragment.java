package io.github.t3r1jj.pbmap.main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import io.github.t3r1jj.pbmap.R;

public class ImproveDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int horizontalMargin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        layoutParams.setMargins(horizontalMargin, 0, horizontalMargin, 0);
        final EditText descriptionEditText = new EditText(getActivity());
        descriptionEditText.setLayoutParams(layoutParams);
        linearLayout.addView(descriptionEditText);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.improve)
                .setMessage(R.string.improve_message)
                .setView(linearLayout)
                .setPositiveButton(R.string.report, null)
                .setNegativeButton(R.string.cancel, null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(descriptionEditText, InputMethodManager.SHOW_IMPLICIT);
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String description = descriptionEditText.getText().toString();
                        if (description.isEmpty()) {
                            descriptionEditText.setError(getString(R.string.required));
                            return;
                        }
                        dialog.dismiss();
                        Controller controller = ((MapActivity) getActivity()).getController();
                        controller.onImprovePressed((MotionEvent) getArguments().getParcelable(MarkerDialogFragment.MOTION_EVENT_KEY), description);
                    }
                });

            }
        });
        return dialog;
    }
}
