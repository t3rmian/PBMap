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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.t3r1jj.pbmap.BuildConfig;
import io.github.t3r1jj.pbmap.R;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.about_rate)
    View rate;
    @BindView(R.id.about_version)
    TextView versionText;
    @BindView(R.id.about_author_link)
    TextView authorText;
    @BindView(R.id.about_licenses)
    View licenses;
    @BindView(R.id.swipe_selector)
    SwipeSelector swipeSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rate.setOnClickListener(new OnRateClickListener(this));
        versionText.setText(getString(R.string.about_version, BuildConfig.VERSION_NAME));
        authorText.setMovementMethod(LinkMovementMethod.getInstance());
        setUpAttributions();
        licenses.setOnClickListener(view -> new LicensesDialogFragment().show(getFragmentManager(), "LICENSES"));
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

    @OnClick(R.id.about_icon)
    void openAbout() {
        String url = getString(R.string.about_project_link);
        openUrl(url);
    }

    @OnClick(R.id.about_report)
    void openReport() {
        String url = getString(R.string.about_report_link);
        openUrl(url);
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void setUpAttributions() {
        String[] titles = getResources().getStringArray(R.array.attribution_titles);
        String[] descriptions = getResources().getStringArray(R.array.attribution_descriptions);
        SwipeItem[] attributions = new SwipeItem[titles.length];
        for (int i = 0; i < titles.length; i++) {
            attributions[i] = new SwipeItem(i, titles[i], descriptions[i]);
        }
        swipeSelector.setItems(attributions);
        findViewById(R.id.swipeselector_layout_circleContainer)
                .setPadding(0, (int) getResources().getDimension(R.dimen.activity_vertical_margin) / 2, 0, 0);
    }

}
