package io.github.t3r1jj.pbmap.search;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.github.t3r1jj.pbmap.BuildConfig;
import io.github.t3r1jj.pbmap.model.PBMap;
import io.github.t3r1jj.pbmap.model.Place;

class SuggestionsDBHelper extends SQLiteOpenHelper {
    static final String SUGGESTIONS_TABLE_NAME = "suggestions";
    static final String SUGGESTIONS_COLUMN_ID = BaseColumns._ID;
    static final String SUGGESTIONS_COLUMN_NAME = SearchManager.SUGGEST_COLUMN_TEXT_1;
    static final String SUGGESTIONS_COLUMN_MAP_PATH = "map_path";
    private Context context;
    private String mapsPath = "data";
    private static final String DATABASE_NAME = "search_suggestions.db";

    SuggestionsDBHelper(Context base) {
        super(base, DATABASE_NAME, null, BuildConfig.VERSION_CODE);
        this.context = base;
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
                        BaseColumns._ID + " integer primary key autoincrement, " +
                        SearchManager.SUGGEST_COLUMN_TEXT_1 + " text not null, " +
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
        List<PBMap> maps = loadMaps(mapsPath);
        insertSuggestions(db, maps);
    }

    private List<PBMap> loadMaps(String mapsPath) throws Exception {
        List<PBMap> maps = new ArrayList<>();
        Serializer serializer = new Persister();
        for (String mapPath : context.getAssets().list(mapsPath)) {
            PBMap map = serializer.read(PBMap.class, context.getAssets().open(mapsPath + "/" + mapPath));
            maps.add(map);
        }
        return maps;
    }

    private void insertSuggestions(SQLiteDatabase db, List<PBMap> maps) throws IOException {
        Set<Suggestion> suggestions = new HashSet<>();
        String[] mapPaths = context.getAssets().list(mapsPath);
        for (int i = 0; i < mapPaths.length; i++) {
            suggestions.add(new Suggestion(maps.get(i).getName(), mapsPath + "/" + mapPaths[i]));
        }
        for (int i = 0; i < mapPaths.length; i++) {
            for (Place place : maps.get(i).getPlaces()) {
                suggestions.add(new Suggestion(place.getName(), mapsPath + "/" + mapPaths[i]));
            }
        }
        for (Suggestion suggestion : suggestions) {
            insertSuggestion(db, suggestion);
        }
    }

    private void insertSuggestion(SQLiteDatabase db, Suggestion suggestion) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SUGGESTIONS_COLUMN_NAME, suggestion.name);
        contentValues.put(SUGGESTIONS_COLUMN_MAP_PATH, suggestion.mapPath);
        db.insert(SUGGESTIONS_TABLE_NAME, null, contentValues);
    }


    private static class Suggestion {
        String name;
        String mapPath;

        Suggestion(String name, String mapPath) {
            this.name = name;
            this.mapPath = mapPath;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Suggestion that = (Suggestion) o;

            return name.equals(that.name);

        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

}
