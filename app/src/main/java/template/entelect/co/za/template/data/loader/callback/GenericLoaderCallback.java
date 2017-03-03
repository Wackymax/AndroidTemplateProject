package template.entelect.co.za.template.data.loader.callback;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

public class GenericLoaderCallback<D> implements LoaderManager.LoaderCallbacks<D> {

    private CustomLoaderCallbacks<D> customLoaderCallbacks;
    private Loader loader;

    public GenericLoaderCallback(Loader loader, CustomLoaderCallbacks<D> customLoaderCallbacks) {
        this.loader = loader;
        this.customLoaderCallbacks = customLoaderCallbacks;
    }

    @Override
    public Loader<D> onCreateLoader(int id, Bundle args) {

        if (customLoaderCallbacks != null) {
            customLoaderCallbacks.showLoaderIndicator();
            customLoaderCallbacks.onLoaderStarted();
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<D> loader, D data) {

        if (customLoaderCallbacks != null) {
            customLoaderCallbacks.hideLoaderIndicator();
            customLoaderCallbacks.onLoaderFinished(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<D> loader) {

    }
}
