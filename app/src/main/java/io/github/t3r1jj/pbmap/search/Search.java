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
        Cursor cursor = query(null, null, null, new String[]{prepareQueryArguments(query, searchById)}, null);
        return search(cursor);
    }

    private String prepareQueryArguments(String query, boolean searchById) {
        if (searchById && query != null && !query.contains("@")) {
            return query + "@" + query;
        }
        return query;
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
