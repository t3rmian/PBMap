package io.github.t3r1jj.pbmap;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setupToolbar();
        setupIcon();
        setupRate();
        setupReport();
        setupAuthor();
        setupVersion();
        setupAttributions();
    }

    private void setupIcon() {
        View report = findViewById(R.id.about_icon);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getString(R.string.config_project);
                openUrl(url);
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupRate() {
        View rate = findViewById(R.id.about_rate);
        rate.setOnClickListener(new OnRateClickListener());
    }

    private void setupReport() {
        View report = findViewById(R.id.about_report);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getString(R.string.config_report);
                openUrl(url);
            }
        });
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void setupAuthor() {
        TextView authorText = (TextView) findViewById(R.id.about_author);
        authorText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setupVersion() {
        TextView versionText = (TextView) findViewById(R.id.about_version);
        versionText.setText(getString(R.string.about_version, BuildConfig.VERSION_NAME));
    }

    private void setupAttributions() {
        String[] titles = getResources().getStringArray(R.array.attribution_titles);
        String[] descriptions = getResources().getStringArray(R.array.attribution_descriptions);
        String[] urls = getResources().getStringArray(R.array.attribution_urls);
        SwipeItem[] attributions = new SwipeItem[titles.length];
        for (int i = 0; i < titles.length; i++) {
            attributions[i] = new AttributionSwipeItem(i, titles[i], descriptions[i], urls[i]);
        }
        final SwipeSelector swipeSelector = (SwipeSelector) findViewById(R.id.swipe_selector);
        swipeSelector.setItems(attributions);
        View attributionSite = findViewById(R.id.about_attribution_site);
        attributionSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttributionSwipeItem selectedItem = (AttributionSwipeItem) swipeSelector.getSelectedItem();
                openUrl(selectedItem.url);
            }
        });
    }

    private class OnRateClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String packageName = getApplicationContext().getPackageName();
            Intent rate = new Intent(Intent.ACTION_VIEW);
            try {
                Uri marketRateUri = Uri.parse("market://details?id=" + packageName);
                startRateActivityAndLog(rate, marketRateUri);
            } catch (ActivityNotFoundException e) {
                Uri defaultRateUri = Uri.parse("https://play.google.com/store/apps/details?id=" + packageName);
                tryStartDefaultRating(rate, defaultRateUri);
            }
        }

        private void startRateActivityAndLog(Intent rate, Uri rateUri) {
            rate.setData(rateUri);
            startActivity(rate);
        }

        private void tryStartDefaultRating(Intent rate, Uri rateUri) {
            try {
                startRateActivityAndLog(rate, rateUri);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(AboutActivity.this, getString(R.string.could_not_open_android_market), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class AttributionSwipeItem extends SwipeItem {

        String url;

        public AttributionSwipeItem(Object value, String title, String description, String url) {
            super(value, title, description);
            this.url = url;
        }
    }

}
