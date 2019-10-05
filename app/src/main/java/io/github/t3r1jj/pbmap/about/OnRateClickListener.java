package io.github.t3r1jj.pbmap.about;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import io.github.t3r1jj.pbmap.R;

class OnRateClickListener implements View.OnClickListener {

    private final Context context;

    OnRateClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        String packageName = context.getApplicationContext().getPackageName();
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
            Toast.makeText(context, context.getString(R.string.could_not_open_android_market), Toast.LENGTH_SHORT).show();
        }
    }

    private void startRateActivity(Intent rate, Uri rateUri) {
        rate.setData(rateUri);
        context.startActivity(rate);
    }
}
