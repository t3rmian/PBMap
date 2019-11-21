package io.github.t3r1jj.pbmap.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;

@SuppressLint("Registered")
public class Search extends SearchListProvider {

    private final Context context;

    public Search(Context context) {
        this.context = context;
    }

    @Override
    public Context getBaseContext() {
        return context;
    }

    public SearchSuggestion findFirst(String query, boolean searchById) {
        this.searchById = searchById;
        Cursor cursor = query(null, null, null, new String[]{query}, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return cursorToResult(cursor);
                }
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    private SearchSuggestion cursorToResult(Cursor cursor) {
        String placeId = cursor.getString(cursor.getColumnIndex(SUGGESTIONS_COLUMN_PLACE));
        String mapPath = cursor.getString(cursor.getColumnIndex(SUGGESTIONS_COLUMN_MAP_PATH));
        return new SearchSuggestion(placeId, mapPath);
    }
}
