# ![](app/src/main/res/mipmap-ldpi/ic_launcher.png) PBMap

PBMap is an offline map of mapped places (buildings, floors, rooms, etc.) at Bialystok University of Technology created for an Android project assignment. The application facilitates navigation by implementing positioning and routing systems. Furthermore it allows for easy searching for rooms through search bar (possible integration through Intent). A help feature and externally managed report/mapping system have been also implemented.

Main project features: Android, GPS, XML data storage, geomapping, simple communication with web-service, external integration through Intent.

The project was possible thanks to (among others) OpenStreetMap data, Stamen Design tiles and TileView library. More info can be found on the About app screen or in the NOTICE.html file.

PBMap requires Android 4.0+ and [can be downloaded from Google Play.](https://play.google.com/store/apps/details?id=io.github.t3r1jj.pbmap)

### Integration through Intent

The application has been designed to allow pinpointing places and coordinates on a map. This can be achieved by sending a search Intent to the app. To start PBMap with a search intent:

1. Define target package and class name:
````
private static final String PBMAP_PACKAGE_NAME = "io.github.t3r1jj.pbmap";
private static final String PBMAP_CLASS_NAME = "io.github.t3r1jj.pbmap.main.MapActivity";
````

2. Create an Intent with the target package/class name.
3. Set action as Intent.ACTION_SEARCH
4. Pass the data:

 a) To pinpoint a defined place:
  - put an extra String for a SearchManager.QUERY key in the following format: **place_id@map_id**
````
Intent sendIntent = new Intent();
sendIntent.setClassName(PBMAP_PACKAGE_NAME, PBMAP_CLASS_NAME);
sendIntent.setAction(Intent.ACTION_SEARCH);

sendIntent.putExtra(SearchManager.QUERY, "12b@pb_wi");

startActivity(sendIntent);
````
b) To pinpoint a coordinate on a given map:
 - put an extra String for a SearchManager.QUERY key with **map_id**
 - put an android Location object with coordinates of indicated place for a SearchManager.EXTRA_DATA_KEY key.
````
Intent sendIntent = new Intent();
sendIntent.setClassName(PBMAP_PACKAGE_NAME, PBMAP_CLASS_NAME);
sendIntent.setAction(Intent.ACTION_SEARCH);

sendIntent.putExtra(SearchManager.QUERY, "pb_campus");

Location customLocation = new Location("");
customLocation.setLatitude(53.11878);
customLocation.setLongitude(23.14878);

sendIntent.putExtra(SearchManager.EXTRA_DATA_KEY, location);

startActivity(sendIntent);
````

If PBMap is not installed an ActivityNotFoundException exception will be thrown. The list of map and place ids can be found at [the wiki page](https://github.com/T3r1jj/PBMap/wiki/Maps-and-places). For a fully working example refer to sample app from the repository.