package io.github.t3r1jj.pbmap;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import io.github.t3r1jj.pbmap.search.Search;
import io.github.t3r1jj.pbmap.search.SearchSuggestion;

public class MainActivity extends AppCompatActivity {

    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = new Controller(this);
        handleIntent(getIntent());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!controller.isInitialized()) {
            try {
                controller.loadMap(getString(R.string.config_initial_map_path));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        ViewGroup contentView = (ViewGroup) findViewById(R.id.content_main);
        contentView.removeAllViews();
        contentView.addView(view);
    }
}
