package template.entelect.co.za.template.data.loader;

import android.content.Context;
import android.support.v4.content.CursorLoader;

import za.co.cporm.model.query.Select;
import za.co.cporm.model.util.ContentResolverValues;

/**
 * Created by Rushil on 3/12/2016.
 */

public abstract class AbstractCursorLoader<T> extends CursorLoader {

    protected Select query;
    protected Select originalQuery;

    public AbstractCursorLoader(Context context, Select query) {
        super(context);
        this.query = query;
        this.originalQuery = query;

        resetValues(query);
    }

    private void resetValues(Select query) {
        ContentResolverValues contentResolverValues = query.asContentResolverValue();
        setProjection(contentResolverValues.getProjection());
        setSelection(contentResolverValues.getWhere());
        setSelectionArgs(contentResolverValues.getWhereArgs());
        setSortOrder(contentResolverValues.getSortOrder());
        setUri(contentResolverValues.getItemUri());
        setUpdateThrottle(200);
    }
}
