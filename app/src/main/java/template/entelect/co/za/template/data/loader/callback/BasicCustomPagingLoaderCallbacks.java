package template.entelect.co.za.template.data.loader.callback;

/**
 * Created by rushil.ojageer on 2016/03/17.
 */
public abstract class BasicCustomPagingLoaderCallbacks<T> implements CustomPagingLoaderCallbacks<T> {

    @Override
    public void showLoaderIndicator() {

    }

    @Override
    public void hideLoaderIndicator() {

    }

    @Override
    public void onLoaderStarted() {
    }

    public abstract void onLoaderFinished(T data, int offset, int pageSize);
}
