package template.entelect.co.za.template.data.loader;

import android.content.Context;

import template.entelect.co.za.template.domain.model.AbstractDataModel;
import za.co.cporm.model.query.Select;

/**
 * Created by Rushil on 3/12/2016.
 */
public class FirstItemAsyncTaskLoader<T extends AbstractDataModel> extends AbstractAsyncTaskLoader<T> {

    public FirstItemAsyncTaskLoader(Context context, Select query) {
        super(context, query);
    }

    @Override
    public T loadInBackground() {
        return (T) query.first();
    }

}
