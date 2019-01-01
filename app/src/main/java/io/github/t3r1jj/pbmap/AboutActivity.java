package io.github.t3r1jj.pbmap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setUpToolbar();
        setUpIcon();
        setUpRate();
        setUpReport();
        setUpSupport();
        setUpAuthor();
        setUpVersion();
        setUpAttributions();
        setUpLicenses();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        Intent share = createShareIntent();
        shareActionProvider.setShareIntent(share);
        return super.onCreateOptionsMenu(menu);
    }

    private Intent createShareIntent() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getPackageName());
        return share;
    }

    private void setUpIcon() {
        View report = findViewById(R.id.about_icon);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getString(R.string.about_project_link);
                openUrl(url);
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpRate() {
        View rate = findViewById(R.id.about_rate);
        rate.setOnClickListener(new OnRateClickListener());
    }

    private void setUpReport() {
        View report = findViewById(R.id.about_report);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getString(R.string.about_report_link);
                openUrl(url);
            }
        });
    }

    private void setUpSupport() {
        View support = findViewById(R.id.about_support);
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getString(R.string.about_support_link);
                openUrl(url);
            }
        });
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void setUpAuthor() {
        TextView authorText = (TextView) findViewById(R.id.about_author);
        authorText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setUpVersion() {
        TextView versionText = (TextView) findViewById(R.id.about_version);
        versionText.setText(getString(R.string.about_version, BuildConfig.VERSION_NAME));
    }

    private void setUpAttributions() {
        String[] titles = getResources().getStringArray(R.array.attribution_titles);
        String[] descriptions = getResources().getStringArray(R.array.attribution_descriptions);
        SwipeItem[] attributions = new SwipeItem[titles.length];
        for (int i = 0; i < titles.length; i++) {
            attributions[i] = new SwipeItem(i, titles[i], descriptions[i]);
        }
        final SwipeSelector swipeSelector = (SwipeSelector) findViewById(R.id.swipe_selector);
        swipeSelector.setItems(attributions);
    }

    private void setUpLicenses() {
        View licenses = findViewById(R.id.about_licenses);
        licenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LicensesDialogFragment().show(getFragmentManager(), "LICENSES");
            }
        });
    }

    public static class LicensesDialogFragment extends DialogFragment {

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
                    alertDialog.show();
                }
            });

            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    alertDialog.hide();
                }
            });

            return alertDialog;
        }
    }

    private class OnRateClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String packageName = getApplicationContext().getPackageName();
            Intent rate = new Intent(Intent.ACTION_VIEW);
            try {
                Uri marketRateUri = Uri.parse("market://details?id=" + packageName);
                startRateActivity(rate, marketRateUri);
            } catch (ActivityNotFoundException e) {
                Uri defaultRateUri = Uri.parse("https://play.google.com/store/apps/details?id=" + packageName);
                tryStartingDefaultRating(rate, defaultRateUri);
            }
        }

        private void tryStartingDefaultRating(Intent rate, Uri rateUri) {
            try {
                startRateActivity(rate, rateUri);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(AboutActivity.this, getString(R.string.could_not_open_android_market), Toast.LENGTH_SHORT).show();
            }
        }

        private void startRateActivity(Intent rate, Uri rateUri) {
            rate.setData(rateUri);
            startActivity(rate);
        }
    }

}
