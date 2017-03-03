package template.entelect.co.za.template.data.loader;

import android.content.Context;

import java.util.List;

import za.co.cporm.model.query.Select;

/**
 * Created by Rushil on 3/12/2016.
 */
public class ListAsyncTaskLoader<Model> extends AbstractAsyncTaskLoader<List<Model>> {

    public ListAsyncTaskLoader(Context context, Select query) {
        super(context, query);
    }

    @Override
    public List<Model> loadInBackground() {

        if(isPagingEnabled()){

            Select<Model> altered = query.cloneFrom();
            altered.offset(getOffset())
                    .limit(getPageSize());

            return altered.queryAsList();
        }
        else return query.queryAsList();
    }
}
