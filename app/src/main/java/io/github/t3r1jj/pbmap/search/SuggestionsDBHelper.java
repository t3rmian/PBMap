package io.github.t3r1jj.pbmap.search;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import io.github.t3r1jj.pbmap.BuildConfig;

import static io.github.t3r1jj.pbmap.search.SearchListProvider.SUGGESTIONS_COLUMN_ID;
import static io.github.t3r1jj.pbmap.search.SearchListProvider.SUGGESTIONS_COLUMN_MAP_PATH;
import static io.github.t3r1jj.pbmap.search.SearchListProvider.SUGGESTIONS_COLUMN_PLACE;
import static io.github.t3r1jj.pbmap.search.SearchListProvider.SUGGESTIONS_COLUMN_SUGGESTION;

/**
 * @deprecated use MapsDao {@see io.github.t3r1jj.pbmap.search.MapsDao} for SuggestionsDao {@see io.github.t3r1jj.pbmap.search.SuggestionsDao}
 * MapsDao uses static XML files in contrast to creating readonly database.
 */
@Deprecated
class SuggestionsDBHelper extends SQLiteOpenHelper implements SuggestionsDao {
    private static final String SUGGESTIONS_TABLE_NAME = "suggestions";
    private static final String DATABASE_NAME = "search_suggestions.db";
    private final MapsDao dao;
    private Context context;

    SuggestionsDBHelper(Context base) {
        super(base, DATABASE_NAME, null, BuildConfig.VERSION_CODE);
        this.context = base;
        dao = new MapsDao(context);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        if (i1 > i) {
            db.execSQL("DROP TABLE IF EXISTS " + SUGGESTIONS_TABLE_NAME);
        }
    }

    public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy) {

        return getReadableDatabase().query(SUGGESTIONS_TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + SUGGESTIONS_TABLE_NAME + " (" +
                        SUGGESTIONS_COLUMN_ID + " integer primary key autoincrement, " +
                        SUGGESTIONS_COLUMN_SUGGESTION + " text not null, " +
                        SUGGESTIONS_COLUMN_PLACE + " text not null, " +
                        SUGGESTIONS_COLUMN_MAP_PATH + " text not null" +
                        ")"
        );
        List<SearchSuggestion> suggestions = dao.getSearchSuggestions();
        for (SearchSuggestion suggestion : suggestions) {
            insertSuggestion(db, suggestion);
        }
    }

    private void insertSuggestion(SQLiteDatabase db, SearchSuggestion suggestion) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SUGGESTIONS_COLUMN_SUGGESTION, suggestion.place.toUpperCase());
        contentValues.put(SUGGESTIONS_COLUMN_PLACE, suggestion.place);
        contentValues.put(SUGGESTIONS_COLUMN_MAP_PATH, suggestion.mapPath);
        db.insert(SUGGESTIONS_TABLE_NAME, null, contentValues);
    }


}
