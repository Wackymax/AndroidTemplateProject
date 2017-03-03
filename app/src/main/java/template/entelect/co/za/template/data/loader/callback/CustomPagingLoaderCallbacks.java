package template.entelect.co.za.template.data.loader.callback;

/**
 * Created by Rushil on 3/13/2016.
 */
public interface CustomPagingLoaderCallbacks<T> {

    void showLoaderIndicator();

    void hideLoaderIndicator();

    void onLoaderStarted();

    void onLoaderFinished(T data, int offset, int pageSize);
}
