package io.github.t3r1jj.pbmap;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import io.github.t3r1jj.pbmap.search.SearchListProvider;

public class MainActivity extends AppCompatActivity {

    private Controller controller;
    private final String initialMapPath = "data/1.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = new Controller(this);
        handleIntent(getIntent());
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
    protected void onNewIntent(Intent intent) {
        Log.d("MainActivity", "onNewIntent");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("MainActivity", "Handling query: " + query);
//            SearchRecentSuggestions suggestions =
//                    new SearchRecentSuggestions(this,
//                            SearchListProvider.AUTHORITY,
//                            SearchListProvider.MODE);
//            suggestions.saveRecentQuery(query, null);
        } else {
            try {
                controller.loadMap(initialMapPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setMapView(View view) {
        ViewGroup contentView = (ViewGroup) findViewById(R.id.content_main);
        contentView.removeAllViews();
        contentView.addView(view);
    }
}
