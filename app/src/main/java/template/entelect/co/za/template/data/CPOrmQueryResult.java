package template.entelect.co.za.template.data;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;

import java.util.List;

import template.entelect.co.za.template.data.adapter.CursorRecyclerViewAdapter;
import template.entelect.co.za.template.data.loader.AdditionalLoaderTask;
import template.entelect.co.za.template.data.loader.CountAsyncTaskLoader;
import template.entelect.co.za.template.data.loader.CursorAsyncLoader;
import template.entelect.co.za.template.data.loader.DefaultAdditionalLoaderTask;
import template.entelect.co.za.template.data.loader.FirstItemAsyncTaskLoader;
import template.entelect.co.za.template.data.loader.LastItemAsyncTaskLoader;
import template.entelect.co.za.template.data.loader.ListAsyncTaskLoader;
import template.entelect.co.za.template.data.loader.callback.CustomLoaderCallbacks;
import template.entelect.co.za.template.data.loader.callback.GenericLoaderCallback;
import template.entelect.co.za.template.data.loader.callback.ListViewCursorAdapterLoaderCallback;
import template.entelect.co.za.template.data.loader.callback.RecyclerViewCursorAdapterLoaderCallback;
import template.entelect.co.za.template.data.repository.QueryFactory;
import za.co.cporm.model.CPDefaultRecord;
import za.co.cporm.model.query.Select;
import za.co.cporm.model.util.CPOrmCursor;


public class CPOrmQueryResult<T extends CPDefaultRecord> implements QueryResult<T> {

    private transient final Context context;
    private final Select<T> query;
    private final int queryId;
    private transient AdditionalLoaderTask additionalLoaderTask;

    public CPOrmQueryResult(Context context, Select<T> query){

        this.context = context;
        this.query = query;
        this.queryId = Math.abs(query.toString().hashCode());
        this.additionalLoaderTask = new DefaultAdditionalLoaderTask();
    }

    public CPOrmQueryResult(Context context, Select<T> query, Class<? extends QueryFactory> originQueryFactory){

        this.context = context;
        this.query = query;
        this.queryId = Math.abs(query.toString().hashCode());
        this.additionalLoaderTask = new DefaultAdditionalLoaderTask();
    }

    public void withAdditionalLoaderTask(AdditionalLoaderTask additionalLoaderTask) {
        this.additionalLoaderTask = additionalLoaderTask;
    }

    @Override
    public <K extends T> LoaderManager.LoaderCallbacks<List<K>> asListLoader(CustomLoaderCallbacks<List<K>> callback) {
        Loader loader = new ListAsyncTaskLoader<>(context, query).withAdditionalLoaderTask(additionalLoaderTask);
        return new GenericLoaderCallback<>(loader, callback);
    }

    @Override
    public <K extends Cursor> LoaderManager.LoaderCallbacks<K> asCursorLoader(CustomLoaderCallbacks<K> callback) {
        Loader<K> loader = new CursorAsyncLoader(context, query);
        return new GenericLoaderCallback<>(loader, callback);
    }

    @Override
    public LoaderManager.LoaderCallbacks<Cursor> asCursorLoader(CursorAdapter adapter) {
        return asCursorLoader(adapter, null);
    }

    @Override
    public LoaderManager.LoaderCallbacks<Cursor> asCursorLoader(CursorAdapter adapter, CustomLoaderCallbacks<CPOrmCursor<T>> callback) {
        return new ListViewCursorAdapterLoaderCallback<>(context, adapter, query, callback);
    }

    @Override
    public LoaderManager.LoaderCallbacks<Cursor> asCursorLoader(CursorRecyclerViewAdapter adapter) {
        return asCursorLoader(adapter, null);
    }

    @Override
    public LoaderManager.LoaderCallbacks<Cursor> asCursorLoader(CursorRecyclerViewAdapter adapter, CustomLoaderCallbacks<CPOrmCursor<T>> callback) {
        return new RecyclerViewCursorAdapterLoaderCallback<>(context, adapter, query, callback);
    }

    @Override
    public <K extends T> LoaderManager.LoaderCallbacks<K> getFirstAsync(CustomLoaderCallbacks<K> callback) {
        Loader<K> loader = new FirstItemAsyncTaskLoader(context, query).withAdditionalLoaderTask(additionalLoaderTask);
        return new GenericLoaderCallback<>(loader, callback);
    }

    @Override
    public <K extends T> LoaderManager.LoaderCallbacks<K> getLastAsync(CustomLoaderCallbacks<K> callback) {
        Loader<K> loader = new LastItemAsyncTaskLoader(context, query).withAdditionalLoaderTask(additionalLoaderTask);
        return new GenericLoaderCallback<>(loader, callback);
    }

    @Override
    public LoaderManager.LoaderCallbacks<Integer> getCountAsync(CustomLoaderCallbacks<Integer> callback) {
        Loader loader = new CountAsyncTaskLoader(context, query).withAdditionalLoaderTask(additionalLoaderTask);
        return new GenericLoaderCallback<Integer>(loader, callback);
    }

    @Override
    public T getFirst() {
        return query.first();
    }

    @Override
    public T getLast() {
        return query.last();
    }

    @Override
    public int getCount() {
        return query.queryAsCount();
    }

    @Override
    public Select<T> getQuery() {
        return query;
    }

    @Override
    public List<T> getList() {
        return query.queryAsList();
    }

    @Override
    public Cursor getCursor() {
        return query.queryAsCursor();
    }

    @Override
    public int getQueryId() {
        return queryId;
    }

    @Override
    public QueryResult<T> withLimit(int limit) {

        Select<T> selectClone = query.cloneFrom().limit(limit);

        return new CPOrmQueryResult<T>(context, selectClone);
    }
}
