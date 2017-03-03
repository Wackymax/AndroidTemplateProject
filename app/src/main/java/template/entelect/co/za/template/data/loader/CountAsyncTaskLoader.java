package template.entelect.co.za.template.data.loader;

import android.content.Context;

import za.co.cporm.model.query.Select;

/**
 * Created by Rushil on 3/12/2016.
 */
public class CountAsyncTaskLoader extends AbstractAsyncTaskLoader<Integer> {

    public CountAsyncTaskLoader(Context context, Select query) {
        super(context, query);
    }

    @Override
    public Integer loadInBackground() {
        return query.queryAsCount();
    }

}
