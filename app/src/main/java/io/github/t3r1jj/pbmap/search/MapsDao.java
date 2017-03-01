package io.github.t3r1jj.pbmap.search;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.MatrixCursor;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import io.github.t3r1jj.pbmap.BuildConfig;
import io.github.t3r1jj.pbmap.model.map.PBMap;

public class MapsDao extends ContextWrapper implements SuggestionsDao {
    private static final String mapsPath = BuildConfig.ASSETS_MAP_DIR;
    private static final String firstMapFilename = BuildConfig.FIRST_MAP_FILENAME;
    private final Serializer serializer = new Persister();

    public MapsDao(Context base) {
        super(base);
    }

    public List<SearchSuggestion> getMapSuggestions() {
        List<SearchSuggestion> searchSuggestions = new LinkedList<>();
        for (String mapPath : getMapFilenames()) {
            String assetsPath = mapsPath + "/" + mapPath;
            XPathFactory factory = XPathFactory.newInstance();
            XPath xPath = factory.newXPath();
            try {
                Attr name = (Attr) xPath.evaluate("(//*/@name)[1]", new InputSource(openAsset(assetsPath)), XPathConstants.NODE);
                SearchSuggestion searchSuggestion = new SearchSuggestion(name.getValue(), assetsPath);
                searchSuggestions.add(searchSuggestion);
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
        }
        return searchSuggestions;
    }

    /**
     *
     * @return default map
     */
    public PBMap loadMap() {
        return loadMap(mapsPath + "/" + firstMapFilename);
    }

    public PBMap loadMap(String assetsPath) {
        PBMap map = null;
        try {
            map = serializer.read(PBMap.class, openAsset(assetsPath));
            map.setReferenceMapPath(assetsPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    List<SearchSuggestion> getSearchSuggestions() {
        List<SearchSuggestion> searchSuggestions = new LinkedList<>();
        for (String mapPath : getMapFilenames()) {
            String assetsPath = mapsPath + "/" + mapPath;
            XPathFactory factory = XPathFactory.newInstance();
            XPath xPath = factory.newXPath();
            try {
                NodeList names = (NodeList) xPath.evaluate("//*/@name", new InputSource(openAsset(assetsPath)), XPathConstants.NODESET);
                for (int i = 0; i < names.getLength(); i++) {
                    Attr name = (Attr) names.item(i);
                    SearchSuggestion searchSuggestion = new SearchSuggestion(name.getValue(), assetsPath);
                    searchSuggestions.add(searchSuggestion);
                }
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
        }
        return searchSuggestions;
    }

    private InputStream openAsset(String assetsPath) {
        try {
            return getAssets().open(assetsPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String[] getMapFilenames() {
        try {
            return getAssets().list(mapsPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[]{};
    }

    @Override
    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String whereRegex, String orderBy) {
        MatrixCursor matrixCursor = new MatrixCursor(columns);
        selectionArgs[0] = selectionArgs[0].replace("%", ".*");
        List<SearchSuggestion> searchSuggestions = getSearchSuggestions();
        int id = 1;
        for (SearchSuggestion suggestion : searchSuggestions)
            if (suggestion.place.toLowerCase().matches(selectionArgs[0].toLowerCase())) {
                matrixCursor.newRow()
                        .add(id++)
                        .add(suggestion.place.toUpperCase())
                        .add(suggestion.place)
                        .add(suggestion.mapPath);
            }
        return matrixCursor;
    }
}
