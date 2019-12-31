package io.github.t3r1jj.pbmap.search;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.MatrixCursor;

import androidx.annotation.NonNull;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import io.github.t3r1jj.pbmap.BuildConfig;
import io.github.t3r1jj.pbmap.model.map.PBMap;
import io.github.t3r1jj.pbmap.model.map.route.RouteGraph;

public class OldMapsDao extends ContextWrapper {
    private static final String mapsPath = BuildConfig.ASSETS_MAP_DIR;
    private static final String firstMapFilename = BuildConfig.FIRST_MAP_FILENAME;
    private final Serializer serializer = new Persister();

    public OldMapsDao(Context base) {
        super(base);
    }

    public List<SearchSuggestion> getMapSuggestions() {
        List<SearchSuggestion> searchSuggestions = new LinkedList<>();
        for (String mapPath : getMapFilenames()) {
            String assetsPath = mapsPath + "/" + mapPath;
            XPathFactory factory = XPathFactory.newInstance();
            XPath xPath = factory.newXPath();
            try {
                Attr name = (Attr) xPath.evaluate("(//*/@id)[1]", new InputSource(openAsset(assetsPath)), XPathConstants.NODE);
                Element element = name.getOwnerElement();
                if (!"true".equals(element.getAttribute("hidden"))) {
                    SearchSuggestion searchSuggestion = new SearchSuggestion(name.getValue(), assetsPath);
                    searchSuggestion.setLogoName(element.getAttribute("logo_path"));
                    if (element.hasAttribute("rank")) {
                        searchSuggestion.setRank(Integer.parseInt(element.getAttribute("rank")));
                    }
                    searchSuggestions.add(searchSuggestion);
                }
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
        }
        return searchSuggestions;
    }

    /**
     * @return default map
     */
    public PBMap loadMap() {
        return loadMap(mapsPath + "/" + firstMapFilename);
    }

    private PBMap loadMap(String assetsPath) {
        PBMap map = null;
        try {
            map = serializer.read(PBMap.class, openAsset(assetsPath));
            map.setReferenceMapPath(assetsPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * @param ignoreMaps if false, a map_id@map_id entries are added
     * @return list of all queryable places defined in the database
     */
    private List<SearchSuggestion> getSearchSuggestions(boolean ignoreMaps) {
        List<SearchSuggestion> searchSuggestions = new LinkedList<>();
        for (String mapPath : getMapFilenames()) {
            String assetsPath = mapsPath + "/" + mapPath;
            XPathFactory factory = XPathFactory.newInstance();
            XPath xPath = factory.newXPath();
            try {
                NodeList names = (NodeList) xPath.evaluate("//*/@id", new InputSource(openAsset(assetsPath)), XPathConstants.NODESET);
                String mapId = null;
                for (int i = 0; i < names.getLength(); i++) {
                    Attr name = (Attr) names.item(i);
                    if (i == 0) {
                        mapId = name.getValue();
                        if (ignoreMaps) {
                            continue;
                        }
                    }
                    SearchSuggestion searchSuggestion = new SearchSuggestion(name.getValue(), assetsPath);
                    searchSuggestion.setMapId(mapId);
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

    public Cursor query(String[] columns, String[] selectionArgs, boolean searchById) {
        prepareQueryArguments(selectionArgs, searchById);
        List<Object[]> results = findQueryResults(selectionArgs, searchById);
        Collections.sort(results, (o1, o2) -> {
            int byName = String.valueOf(o1[1]).compareTo(String.valueOf(o2[1]));
            return byName == 0 ? String.valueOf(o1[2]).compareTo(String.valueOf(o2[2])) : byName;
        });
        MatrixCursor matrixCursor = new MatrixCursor(columns);
        for (Object[] result : results) {
            matrixCursor.addRow(result);
        }
        return matrixCursor;
    }

    private void prepareQueryArguments(String[] selectionArgs, boolean searchById) {
        if (!searchById) {
            for (int i = 0; i < selectionArgs.length; i++) {
                selectionArgs[i] = ".*" + escape(selectionArgs[i]) + ".*";
            }
        }
    }

    private String escape(String selectionArg) {
        return selectionArg.trim().replace("[", "\\[").replace("]", "\\]");
    }

    @NonNull
    private List<Object[]> findQueryResults(String[] selectionArgs, boolean searchById) {
        List<SearchSuggestion> searchSuggestions = getSearchSuggestions(!searchById);
        List<Object[]> results = new ArrayList<>();
        for (SearchSuggestion suggestion : searchSuggestions) {
            String name = searchById ? suggestion.getPlaceId().toUpperCase() : suggestion.getName(getBaseContext()).toUpperCase();
            String map = searchById ? suggestion.getMapId().toUpperCase() : suggestion.getMapName(getBaseContext()).toUpperCase();
            if (queryMatchesSuggestion(selectionArgs, name, map)) {
                results.add(new Object[]{
                        suggestion.getNameResId(getBaseContext()),
                        name,
                        map,
                        suggestion.getPlaceId(),
                        suggestion.getMapPath(),
                        suggestion.getPlaceId().toUpperCase() + "@" + suggestion.getMapId().toUpperCase()
                });
            }
        }
        return results;
    }

    private boolean queryMatchesSuggestion(String[] selectionArgs, String place, String map) {
        String[] placeAndMap = new String[]{place, map};
        for (int i = 0; i < selectionArgs.length; i++) {
            if (!placeAndMap[i].matches(selectionArgs[i])) {
                return false;
            }
        }
        return true;
    }

    public RouteGraph loadGraph(PBMap map) {
        RouteGraph routeGraph = null;
        try {
            routeGraph = serializer.read(RouteGraph.class, openAsset(map.getGraphPath()));
            routeGraph.setPath(map.getGraphPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routeGraph;
    }

}

