package template.entelect.co.za.template.data.loader;

/**
 * Created by hennie.brink on 2016/07/25.
 */
public interface AdditionalLoaderTask<T> {

    void loadInBackground(T data);
}
