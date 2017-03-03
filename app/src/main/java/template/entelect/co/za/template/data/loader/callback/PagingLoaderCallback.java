package template.entelect.co.za.template.data.loader.callback;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import template.entelect.co.za.template.data.loader.AbstractAsyncTaskLoader;

public class PagingLoaderCallback<D> implements LoaderManager.LoaderCallbacks<D> {

    private CustomPagingLoaderCallbacks<D> customLoaderCallbacks;
    private AbstractAsyncTaskLoader loader;

    public PagingLoaderCallback(AbstractAsyncTaskLoader loader, CustomPagingLoaderCallbacks<D> customLoaderCallbacks) {
        this.loader = loader;
        this.customLoaderCallbacks = customLoaderCallbacks;
    }

    @Override
    public AbstractAsyncTaskLoader<D> onCreateLoader(int id, Bundle args) {

        if (customLoaderCallbacks != null) {
            customLoaderCallbacks.showLoaderIndicator();
            customLoaderCallbacks.onLoaderStarted();
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<D> loader, D data) {

        if (customLoaderCallbacks != null) {

            AbstractAsyncTaskLoader asyncTaskLoader = (AbstractAsyncTaskLoader) loader;

            customLoaderCallbacks.hideLoaderIndicator();
            customLoaderCallbacks.onLoaderFinished(data, asyncTaskLoader.getOffset(), asyncTaskLoader.getPageSize());
        }
    }

    @Override
    public void onLoaderReset(Loader<D> loader) {

    }
}
