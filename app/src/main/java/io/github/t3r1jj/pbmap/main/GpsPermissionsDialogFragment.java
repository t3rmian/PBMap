package io.github.t3r1jj.pbmap.main;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import io.github.t3r1jj.pbmap.R;

public class GpsPermissionsDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity(), getTheme())
                .setTitle(R.string.location_permissions)
                .setMessage(getString(R.string.gps_permissions_disabled_message, getString(R.string.name_app)))
                .setNegativeButton(R.string.ok, null)
                .create();
    }
}
