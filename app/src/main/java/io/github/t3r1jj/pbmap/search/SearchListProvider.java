package io.github.t3r1jj.pbmap.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class SearchListProvider extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY = SearchListProvider.class.getName();
    public static final int MODE = SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES;
    static final String SUGGESTIONS_COLUMN_ID = BaseColumns._ID;
    static final String SUGGESTIONS_COLUMN_SUGGESTION = SearchManager.SUGGEST_COLUMN_TEXT_1;
    static final String SUGGESTIONS_COLUMN_PLACE = SearchManager.SUGGEST_COLUMN_INTENT_DATA;
    static final String SUGGESTIONS_COLUMN_MAP_PATH = SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA;
    protected String whereClause = "lower(" + SUGGESTIONS_COLUMN_SUGGESTION + ") LIKE ?";
    protected String selectionPrePostFix = "%";
    protected String orderBy = SUGGESTIONS_COLUMN_SUGGESTION;
    private SuggestionsDao db;
    private String[] tableColumns = new String[]{
            SUGGESTIONS_COLUMN_ID,
            SUGGESTIONS_COLUMN_SUGGESTION,
            SUGGESTIONS_COLUMN_PLACE,
            SUGGESTIONS_COLUMN_MAP_PATH
    };

    public SearchListProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (selectionArgs != null && selectionArgs.length > 0 && selectionArgs[0].length() > 0) {
            if (db == null) {
                db = new MapsDao(getBaseContext());
            }
            selectionArgs[0] = selectionPrePostFix + selectionArgs[0].toLowerCase() + selectionPrePostFix;
            return db.query(null, tableColumns, whereClause, selectionArgs, null, null, orderBy);
        } else {
            return super.query(uri, projection, selection, selectionArgs, sortOrder);
        }
    }

    protected Context getBaseContext() {
        return super.getContext();
    }

}
