package io.github.t3r1jj.pbmap.search;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.github.t3r1jj.pbmap.BuildConfig;
import io.github.t3r1jj.pbmap.Dao;
import io.github.t3r1jj.pbmap.model.PBMap;
import io.github.t3r1jj.pbmap.model.Place;

class SuggestionsDBHelper extends SQLiteOpenHelper {
    static final String SUGGESTIONS_TABLE_NAME = "suggestions";
    static final String SUGGESTIONS_COLUMN_ID = BaseColumns._ID;
    static final String SUGGESTIONS_COLUMN_SUGGESTION = SearchManager.SUGGEST_COLUMN_TEXT_1;
    static final String SUGGESTIONS_COLUMN_PLACE = SearchManager.SUGGEST_COLUMN_INTENT_DATA;
    static final String SUGGESTIONS_COLUMN_MAP_PATH = SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA;
    private Context context;
    private final Dao dao;
    private static final String DATABASE_NAME = "search_suggestions.db";

    SuggestionsDBHelper(Context base) {
        super(base, DATABASE_NAME, null, BuildConfig.VERSION_CODE);
        this.context = base;
        dao = new Dao(context);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        if (i1 > i) {
            db.execSQL("DROP TABLE IF EXISTS " + SUGGESTIONS_TABLE_NAME);
        }
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
        try {
            loadQueriesToDb(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadQueriesToDb(SQLiteDatabase db) throws Exception {
        List<PBMap> maps = dao.loadMaps();
        insertSuggestions(db, maps);
    }


    private void insertSuggestions(SQLiteDatabase db, List<PBMap> maps) throws IOException {
        Set<SearchSuggestion> suggestions = new HashSet<>();
        String[] mapPaths = context.getAssets().list(dao.getMapsPath());
        for (int i = 0; i < mapPaths.length; i++) {
            suggestions.add(new SearchSuggestion(maps.get(i).getName(), dao.getMapsPath() + "/" + mapPaths[i]));
        }
        for (int i = 0; i < mapPaths.length; i++) {
            for (Place place : maps.get(i).getPlaces()) {
                suggestions.add(new SearchSuggestion(place.getName(), dao.getMapsPath() + "/" + mapPaths[i]));
            }
        }
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
