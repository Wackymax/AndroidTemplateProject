package template.entelect.co.za.template.data.adapter;

import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import za.co.cporm.model.util.CPOrmCursor;

/**
 * Created by rushil.ojageer on 2016/05/07.
 */
public abstract class CursorRecyclerViewAdapter<Model, ViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<ViewHolder> {

    private int layoutId;
    protected CPOrmCursor<Model> cursor;
    private boolean dataValid;
    private int rowIdColumn;
    private DataSetObserver dataSetObserver;

    public CursorRecyclerViewAdapter(int layoutId) {

        this.layoutId = layoutId;
        this.dataSetObserver = new NotifyingDataSetObserver();
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return createViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!dataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        onBindViewHolder(holder, cursor.inflate());
    }

    @Override
    public int getItemCount() {
        if (dataValid && cursor != null) {
            return cursor.getCount();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (dataValid && cursor != null && cursor.moveToPosition(position)) {
            return cursor.getLong(rowIdColumn);
        }
        return 0;
    }

    public void changeCursor(CPOrmCursor<Model> cursor) {
        CPOrmCursor<Model> old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    public CPOrmCursor<Model> swapCursor(CPOrmCursor<Model> newCursor) {
        if (newCursor == cursor) {
            return null;
        }

        final CPOrmCursor<Model> oldCursor = cursor;

        if (oldCursor != null && dataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(dataSetObserver);
        }

        cursor = newCursor;

        if (cursor != null) {
            if (dataSetObserver != null) {
                cursor.registerDataSetObserver(dataSetObserver);
            }
            rowIdColumn = newCursor.getColumnIndexOrThrow("_id");
            dataValid = true;
            notifyDataSetChanged();
        } else {
            rowIdColumn = -1;
            dataValid = false;
            notifyDataSetChanged();
        }

        return oldCursor;
    }

    public abstract ViewHolder createViewHolder(View view);

    public abstract void onBindViewHolder(ViewHolder viewHolder, Model item);

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            dataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            dataValid = false;
            notifyDataSetChanged();
        }
    }
}
