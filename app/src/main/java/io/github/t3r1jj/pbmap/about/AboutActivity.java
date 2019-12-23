package io.github.t3r1jj.pbmap.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

import io.github.t3r1jj.pbmap.BuildConfig;
import io.github.t3r1jj.pbmap.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setUpToolbar();
        setUpIcon();
        setUpRate();
        setUpReport();
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
        report.setOnClickListener(view -> {
            String url = getString(R.string.about_project_link);
            openUrl(url);
        });
    }

    @SuppressWarnings("ConstantConditions")
    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpRate() {
        View rate = findViewById(R.id.about_rate);
        rate.setOnClickListener(new OnRateClickListener(this));
    }

    private void setUpReport() {
        View report = findViewById(R.id.about_report);
        report.setOnClickListener(view -> {
            String url = getString(R.string.about_report_link);
            openUrl(url);
        });
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void setUpAuthor() {
        TextView authorText = findViewById(R.id.about_author_link);
        authorText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setUpVersion() {
        TextView versionText = findViewById(R.id.about_version);
        versionText.setText(getString(R.string.about_version, BuildConfig.VERSION_NAME));
    }

    private void setUpAttributions() {
        String[] titles = getResources().getStringArray(R.array.attribution_titles);
        String[] descriptions = getResources().getStringArray(R.array.attribution_descriptions);
        SwipeItem[] attributions = new SwipeItem[titles.length];
        for (int i = 0; i < titles.length; i++) {
            attributions[i] = new SwipeItem(i, titles[i], descriptions[i]);
        }
        final SwipeSelector swipeSelector = findViewById(R.id.swipe_selector);
        swipeSelector.setItems(attributions);
        findViewById(R.id.swipeselector_layout_circleContainer)
                .setPadding(0, (int) getResources().getDimension(R.dimen.activity_vertical_margin) / 2, 0, 0);
    }

    private void setUpLicenses() {
        View licenses = findViewById(R.id.about_licenses);
        licenses.setOnClickListener(view -> new LicensesDialogFragment().show(getFragmentManager(), "LICENSES"));
    }

}
