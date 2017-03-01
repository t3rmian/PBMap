package io.github.t3r1jj.pbmap.search;

import android.content.Context;
import android.database.Cursor;

public class Search extends SearchListProvider {

    private final Context context;

    public Search(Context context) {
        this.context = context;
        whereClause = "lower(" + SUGGESTIONS_COLUMN_SUGGESTION + ") = ?";
        selectionPrePostFix = "";
    }

    @Override
    public Context getBaseContext() {
        return context;
    }

    public SearchSuggestion find(String userQuery) {
        Cursor cursor = query(null, null, null, new String[]{userQuery}, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String place = cursor.getString(cursor.getColumnIndex(SUGGESTIONS_COLUMN_PLACE));
                String mapPath = cursor.getString(cursor.getColumnIndex(SUGGESTIONS_COLUMN_MAP_PATH));
                cursor.close();
                return new SearchSuggestion(place, mapPath);
            }
            cursor.close();
        }
        return null;
    }
}
