package io.github.t3r1jj.pbmap.search;

import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class SearchListProvider extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY = SearchListProvider.class.getName();
    public static final int MODE = SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES;
    protected String whereClause = "lower(" + SuggestionsDBHelper.SUGGESTIONS_COLUMN_SUGGESTION + ") LIKE ?";
    protected String selectionPrePostFix = "%";
    protected String orderBy = SuggestionsDBHelper.SUGGESTIONS_COLUMN_SUGGESTION;
    private SQLiteOpenHelper db;
    private String[] tableColumns = new String[]{
            SuggestionsDBHelper.SUGGESTIONS_COLUMN_ID,
            SuggestionsDBHelper.SUGGESTIONS_COLUMN_SUGGESTION,
            SuggestionsDBHelper.SUGGESTIONS_COLUMN_PLACE,
            SuggestionsDBHelper.SUGGESTIONS_COLUMN_MAP_PATH
    };

    public SearchListProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (selectionArgs != null && selectionArgs.length > 0 && selectionArgs[0].length() > 0) {
            if (db == null) {
                db = new SuggestionsDBHelper(getBaseContext());
            }
            selectionArgs[0] =  selectionPrePostFix + selectionArgs[0].toLowerCase() + selectionPrePostFix;
            return db.getReadableDatabase().query(SuggestionsDBHelper.SUGGESTIONS_TABLE_NAME, tableColumns, whereClause, selectionArgs, null, null, orderBy);
        } else {
            return super.query(uri, projection, selection, selectionArgs, sortOrder);
        }
    }

    protected Context getBaseContext() {
        return super.getContext();
    }

}
