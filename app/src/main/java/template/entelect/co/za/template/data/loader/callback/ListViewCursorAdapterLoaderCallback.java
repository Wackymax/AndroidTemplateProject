package template.entelect.co.za.template.data.loader.callback;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;

import template.entelect.co.za.template.data.loader.CursorAsyncLoader;
import za.co.cporm.model.CPDefaultRecord;
import za.co.cporm.model.query.Select;
import za.co.cporm.model.util.CPOrmCursor;

public class ListViewCursorAdapterLoaderCallback<T extends CPDefaultRecord> implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context context;
    private CursorAdapter listAdapter;
    private Select<T> select;
    private CustomLoaderCallbacks<CPOrmCursor<T>> callback;

    public ListViewCursorAdapterLoaderCallback(Context context, CursorAdapter listAdapter, Select<T> select, CustomLoaderCallbacks<CPOrmCursor<T>> callbacks) {
        this.context = context;
        this.listAdapter = listAdapter;
        this.select = select;
        this.callback = callbacks;
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
        listAdapter.changeCursor(data);

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
