package template.entelect.co.za.template.data.loader.callback;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;

import template.entelect.co.za.template.data.adapter.CursorRecyclerViewAdapter;
import template.entelect.co.za.template.data.loader.CursorAsyncLoader;
import za.co.cporm.model.CPDefaultRecord;
import za.co.cporm.model.query.Select;
import za.co.cporm.model.util.CPOrmCursor;

public class RecyclerViewCursorAdapterLoaderCallback<T extends CPDefaultRecord> implements LoaderManager.LoaderCallbacks<Cursor> {

    private final Context context;
    private final CursorRecyclerViewAdapter<T, RecyclerView.ViewHolder> listAdapter;
    private final Select<T> select;
    private CustomLoaderCallbacks<CPOrmCursor<T>> callback;

    public RecyclerViewCursorAdapterLoaderCallback(Context context, CursorRecyclerViewAdapter<T, RecyclerView.ViewHolder> listAdapter, Select<T> select, CustomLoaderCallbacks<CPOrmCursor<T>> callback) {
        this.context = context;
        this.listAdapter = listAdapter;
        this.select = select;
        this.callback = callback;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (callback != null) {
            callback.showLoaderIndicator();
            callback.onLoaderStarted();
        }
        return new CursorAsyncLoader<>(context, select);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        listAdapter.changeCursor((CPOrmCursor<T>) data);

        if (callback != null) {
            callback.hideLoaderIndicator();
            callback.onLoaderFinished((CPOrmCursor<T>) data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        listAdapter.changeCursor(null);
    }
}
