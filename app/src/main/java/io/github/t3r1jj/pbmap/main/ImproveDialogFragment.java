package io.github.t3r1jj.pbmap.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import io.github.t3r1jj.pbmap.R;

/**
 * Displays dialog with improve input option for pressed coordinates
 * Supports {@link MapActivity} only
 * Requires setting argument bundle with {@link MarkerDialogFragment.MOTION_EVENT_KEY}
 */
public class ImproveDialogFragment extends DialogFragment {
    private MapActivity mapActivity;
    private EditText descriptionEditText;
    private AlertDialog dialog;
    private MotionEvent event;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mapActivity = (MapActivity) getActivity();
        Bundle arguments = Objects.requireNonNull(getArguments());
        event = Objects.requireNonNull(arguments.getParcelable(MarkerDialogFragment.MOTION_EVENT_KEY));

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int horizontalMargin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        layoutParams.setMargins(horizontalMargin, 0, horizontalMargin, 0);
        descriptionEditText = new EditText(getActivity());
        descriptionEditText.setId(android.R.id.edit);
        descriptionEditText.setLayoutParams(layoutParams);
        linearLayout.addView(descriptionEditText);
        dialog = new AlertDialog.Builder(mapActivity)
                .setTitle(R.string.improve)
                .setMessage(R.string.improve_message)
                .setView(linearLayout)
                .setPositiveButton(R.string.report, null)
                .setNegativeButton(R.string.cancel, null)
                .create();

        dialog.setOnShowListener(__ -> {
            InputMethodManager inputMethodManager = (InputMethodManager) mapActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(descriptionEditText, InputMethodManager.SHOW_IMPLICIT);
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                String description = descriptionEditText.getText().toString();
                if (description.isEmpty()) {
                    descriptionEditText.setError(getString(R.string.required));
                    return;
                }
                dialog.dismiss();
                Controller controller = mapActivity.getController();
                controller.onImprovePressed(event, description);
            });

        });
        return dialog;
    }
}
