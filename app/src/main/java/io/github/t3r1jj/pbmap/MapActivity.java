package io.github.t3r1jj.pbmap;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import io.github.t3r1jj.pbmap.search.Search;
import io.github.t3r1jj.pbmap.search.SearchSuggestion;

public class MapActivity extends DrawerActivity {

    private Controller controller;
    private ViewGroup mapContainer;
    private FloatingActionButton infoButton;
    private MenuItem backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapContainer = (ViewGroup) findViewById(R.id.content_main);
        infoButton = (FloatingActionButton) findViewById(R.id.current_fab);
        controller = new Controller(this);
        handleIntent(getIntent());

        if (!controller.isInitialized()) {
            try {
                controller.loadMap(getString(R.string.config_initial_map_path));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void initializeContentView() {
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setIconified(false);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        backButton = menu.findItem(R.id.action_back);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                try {
                    controller.loadPreviousMap();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Search search = new Search(this);
            SearchSuggestion placeFound = search.find(intent.getStringExtra(SearchManager.QUERY));
            if (placeFound != null) {
                loadPlace(placeFound);
            } else {
                Toast.makeText(this, R.string.not_found, Toast.LENGTH_LONG).show();
            }
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            SearchSuggestion suggestion = new SearchSuggestion(intent);
            loadPlace(suggestion);
        }
    }

    private void loadPlace(SearchSuggestion suggestion) {
        try {
            controller.loadMap(suggestion);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMapView(View view) {
        mapContainer.removeAllViews();
        mapContainer.addView(view);
    }

    @SuppressWarnings("ConstantConditions")
    public void setLogo(ImageView view) {
        if (view == null) {
            getSupportActionBar().setLogo(null);
        } else {
            getSupportActionBar().setLogo(view.getDrawable());
        }
    }

    public void setBackButtonVisible(boolean visible) {
        backButton.setVisible(visible);
    }
}
