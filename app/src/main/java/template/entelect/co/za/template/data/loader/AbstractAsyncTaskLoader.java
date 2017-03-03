package template.entelect.co.za.template.data.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.HashMap;
import java.util.Map;

import za.co.cporm.model.query.Select;

/**
 * Created by Rushil on 3/12/2016.
 */

public abstract class AbstractAsyncTaskLoader<T> extends AsyncTaskLoader<T> {

    private final ForceLoadContentObserver mObserver;

    private Map<Integer, T> data;
    private AdditionalLoaderTask<T> additionalLoaderTask;
    protected Select query;
    private Select originalQuery;

    private boolean pagingEnabled;
    private int pageNumber = 0;
    private int pageSize = 50;

    public AbstractAsyncTaskLoader(Context context, Select query) {
        super(context);
        this.query = query;
        this.originalQuery = query;
        this.mObserver = new ForceLoadContentObserver();
        setUpdateThrottle(200);
    }

    public AbstractAsyncTaskLoader<T> withAdditionalLoaderTask(AdditionalLoaderTask<T> additionalLoaderTask) {
        this.additionalLoaderTask = additionalLoaderTask;
        return this;
    }

    public abstract T loadInBackground();

    @Override
    public void deliverResult(T data) {

        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            releaseResources();
            return;
        }

        if(this.data == null){
            this.data = new HashMap<>();
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        T oldData = this.data.get(pageNumber);
        this.data.put(pageNumber, data);

        if (additionalLoaderTask != null) {
            additionalLoaderTask.loadInBackground(data);
        }

        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if (data != null) {
            // Deliver any previously loaded data immediately.
            if(pagingEnabled){

                for (int i = 0; i < pageNumber; i++) {
                    deliverResult(data.get(i));
                }
            }
            else deliverResult(data.get(pageNumber));
        }

        if (takeContentChanged() || data == null) {
            // When the observer detects a change, it should call onContentChanged()
            // on the Loader, which will cause the next call to takeContentChanged()
            // to return true. If this is ever the case (or if the current data is
            // null), we force a new load.
            forceLoad();
        }

        getContext().getContentResolver().registerContentObserver(query.asContentResolverValue().getItemUri(), true, mObserver);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();


    }

    @Override
    protected void onStopLoading() {
        // The Loader is in a stopped state, so we should attempt to cancel the
        // current load (if there is one).
        cancelLoad();

        // Note that we leave the observer as is. Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }

    @Override
    protected void onReset() {
        // Ensure the loader has been stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'mData'.
        if (data != null) {
            releaseResources();
        }
        getContext().getContentResolver().unregisterContentObserver(mObserver);
    }

    @Override
    public void onCanceled(T data) {
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(data);
    }

    private void releaseResources() {

        this.data = null;
    }

    protected void enablePaging(){
        pagingEnabled = true;
    }

    protected void setPageSize(int pageSize){
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getCurrentPage() {
        return this.pageNumber;
    }

    public int getOffset(){
        return pageNumber * pageSize;
    }

    public void loadNextPage(){
        if(pagingEnabled){
            pageNumber = data.size();
            onContentChanged();
        }
    }

    public void loadPreviousPage(){
        if(pagingEnabled && pageNumber > 0){
            pageNumber--;
            onContentChanged();
        }
    }

    public boolean isPagingEnabled(){
        return pagingEnabled;
    }
}
