package io.github.t3r1jj.pbmap.main.drawer;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import io.github.t3r1jj.pbmap.R;

public abstract class DrawerActivity extends AppCompatActivity {

    private NavigationDrawerFragment navigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeContentView();

        navigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        navigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                findViewById(R.id.drawer_layout));
    }

    protected void setLogo(Drawable drawable) {
        navigationDrawerFragment.setLogo(drawable);
    }

    protected abstract void initializeContentView();

}
