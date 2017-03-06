package io.github.t3r1jj.pbmap.search;

import android.database.Cursor;

interface SuggestionsDao {

    /**
     * Query the given table, returning a {@link Cursor} over the result set.
     *
     * @param table         The table id to compile the query against.
     * @param columns       A list of which columns to return. Passing null will
     *                      return all columns, which is discouraged to prevent reading
     *                      data from storage that isn't going to be used.
     * @param selection     A filter declaring which rows to return, formatted as an
     *                      SQL WHERE clause (excluding the WHERE itself). Passing null
     *                      will return all rows for the given table.
     * @param selectionArgs You may include ?s in selection, which will be
     *                      replaced by the values from selectionArgs, in order that they
     *                      appear in the selection. The values will be bound as Strings.
     * @param groupBy       A filter declaring how to group rows, formatted as an SQL
     *                      GROUP BY clause (excluding the GROUP BY itself). Passing null
     *                      will cause the rows to not be grouped.
     * @param regexWhere    regex String for default column when query done in memory
     * @param orderBy       How to order the rows, formatted as an SQL ORDER BY clause
     *                      (excluding the ORDER BY itself). Passing null will use the
     *                      default sort order, which may be unordered.
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
     * @see Cursor
     */

    Cursor query(String table, String[] columns, String selection,
                 String[] selectionArgs, String groupBy, String regexWhere,
                 String orderBy);
}
