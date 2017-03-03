package template.entelect.co.za.template.data;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.CursorAdapter;

import java.util.List;

import template.entelect.co.za.template.data.adapter.CursorRecyclerViewAdapter;
import template.entelect.co.za.template.data.loader.AdditionalLoaderTask;
import template.entelect.co.za.template.data.loader.callback.CustomLoaderCallbacks;
import za.co.cporm.model.query.Select;
import za.co.cporm.model.util.CPOrmCursor;

/**
 * Created by hennie.brink on 2017/03/03.
 */

public interface QueryResult<T> {

    <K extends T> LoaderManager.LoaderCallbacks<List<K>> asListLoader(CustomLoaderCallbacks<List<K>> callback);

    LoaderManager.LoaderCallbacks<Cursor> asCursorLoader(CursorAdapter adapter);

    LoaderManager.LoaderCallbacks<Cursor> asCursorLoader(CursorRecyclerViewAdapter adapter);

    LoaderManager.LoaderCallbacks<Cursor> asCursorLoader(CursorAdapter adapter, CustomLoaderCallbacks<CPOrmCursor<T>> callback);

    LoaderManager.LoaderCallbacks<Cursor> asCursorLoader(CursorRecyclerViewAdapter adapter, CustomLoaderCallbacks<CPOrmCursor<T>> callback);

    <K extends Cursor> LoaderManager.LoaderCallbacks<K> asCursorLoader(CustomLoaderCallbacks<K> adapter);

    <K extends T> LoaderManager.LoaderCallbacks<K> getFirstAsync(CustomLoaderCallbacks<K> callback);

    <K extends T> LoaderManager.LoaderCallbacks<K> getLastAsync(CustomLoaderCallbacks<K> callback);

    LoaderManager.LoaderCallbacks<Integer> getCountAsync(CustomLoaderCallbacks<Integer> callback);

    T getFirst();

    T getLast();

    int getCount();

    Select<T> getQuery();

    List<T> getList();

    Cursor getCursor();

    int getQueryId();

    QueryResult<T> withLimit(int limit);

    void withAdditionalLoaderTask(AdditionalLoaderTask additionalLoaderTask);
}
