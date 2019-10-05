package io.github.t3r1jj.pbmap.about;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import io.github.t3r1jj.pbmap.BuildConfig;
import io.github.t3r1jj.pbmap.R;

public class LicensesDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        WebView webView = (WebView) View.inflate(getActivity(), R.layout.dialog_licenses, null);
        webView.loadUrl(BuildConfig.NOTICE_PATH);
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), getTheme())
                .setTitle(getString(R.string.about_licenses))
                .setView(webView)
                .setPositiveButton(android.R.string.ok, null)
                .create();

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                alertDialog.show();
            }
        });

        alertDialog.setOnShowListener(dialogInterface -> alertDialog.hide());

        return alertDialog;
    }
}
