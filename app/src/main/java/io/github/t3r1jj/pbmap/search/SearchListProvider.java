package io.github.t3r1jj.pbmap.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class SearchListProvider extends SearchRecentSuggestionsProvider {

    private static final String AUTHORITY = SearchListProvider.class.getName();
    private static final int MODE = SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES | SearchRecentSuggestionsProvider.DATABASE_MODE_2LINES;
    private static final String SUGGESTIONS_COLUMN_ID = BaseColumns._ID;
    private static final String SUGGESTIONS_COLUMN_SUGGESTION = SearchManager.SUGGEST_COLUMN_TEXT_1;
    private static final String SUGGESTIONS_COLUMN_SUGGESTION_2 = SearchManager.SUGGEST_COLUMN_TEXT_2;
    static final String SUGGESTIONS_COLUMN_PLACE = SearchManager.SUGGEST_COLUMN_INTENT_DATA;
    static final String SUGGESTIONS_COLUMN_MAP_PATH = SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA;
    private static final String SUGGESTIONS_COLUMN_PLACE_MAP = "_uri";
    boolean searchById = false;
    private MapsDao mapsDao;
    static final String[] tableColumns = new String[]{
            SUGGESTIONS_COLUMN_ID,
            SUGGESTIONS_COLUMN_SUGGESTION,
            SUGGESTIONS_COLUMN_SUGGESTION_2,
            SUGGESTIONS_COLUMN_PLACE,
            SUGGESTIONS_COLUMN_MAP_PATH,
            SUGGESTIONS_COLUMN_PLACE_MAP
    };

    public SearchListProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (mapsDao == null) {
            mapsDao = new MapsDao(getBaseContext());
        }
        if (containsSelectionArgs(selectionArgs)) {
            return mapsDao.query(tableColumns, prepareScopedSelectionArgs(selectionArgs), "id".equals(selection) || searchById);
        } else {
            return mapsDao.query(tableColumns, new String[]{".*"}, true);
        }
    }

    private boolean containsSelectionArgs(String[] selectionArgs) {
        return selectionArgs != null && selectionArgs.length > 0 && selectionArgs[0] != null && selectionArgs[0].length() > 0;
    }

    private String[] prepareScopedSelectionArgs(String[] selectionArgs) {
        String[] scopedSelectionArgs = selectionArgs[0].split("@");
        scopedSelectionArgs[0] = scopedSelectionArgs[0].toUpperCase();
        if (scopedSelectionArgs.length > 1) {
            scopedSelectionArgs[1] = scopedSelectionArgs[1].toUpperCase();
        }
        return scopedSelectionArgs;
    }

    Context getBaseContext() {
        return super.getContext();
    }


}
