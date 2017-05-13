package io.github.t3r1jj.pbmap.search;

import android.content.Context;
import android.database.Cursor;

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
        return search(cursor);
    }

    private SearchSuggestion search(Cursor cursor) {
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String placeId = cursor.getString(cursor.getColumnIndex(SUGGESTIONS_COLUMN_PLACE));
                String mapPath = cursor.getString(cursor.getColumnIndex(SUGGESTIONS_COLUMN_MAP_PATH));
                cursor.close();
                return new SearchSuggestion(placeId, mapPath);
            }
            cursor.close();
        }
        return null;
    }
}
