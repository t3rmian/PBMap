package io.github.t3r1jj.pbmap.main;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;

import io.github.t3r1jj.pbmap.R;

public class GpsDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity(), getTheme())
                .setMessage(getString(R.string.gps_disabled_message, getString(R.string.name_app)))
                .setCancelable(false)
                .setPositiveButton(R.string.enable, (dialog, id) -> {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> dialog.cancel()).create();
    }
}
