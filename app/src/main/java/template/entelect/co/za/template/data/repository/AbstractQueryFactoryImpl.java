package template.entelect.co.za.template.data.repository;

import android.content.Context;

import com.path.android.jobqueue.JobManager;

import javax.inject.Inject;

import template.entelect.co.za.template.data.CPOrmQueryResult;
import template.entelect.co.za.template.data.QueryResult;
import template.entelect.co.za.template.domain.model.AbstractDataModel;
import za.co.cporm.model.query.Select;

public abstract class AbstractQueryFactoryImpl<T extends AbstractDataModel> extends BaseQueryFactoryImpl<T> implements QueryFactory<T> {

    @Inject
    protected JobManager jobManager;

    public AbstractQueryFactoryImpl(Context context, Class<T> classType) {
        super(context, classType);
    }

    public QueryResult<T> getForEntity(String joinColumn, long parentId) {
        Select<T> query = Select.from(classType)
                .whereEquals(joinColumn, parentId)
                .sortDesc("created_timestamp");

        if(getAdditionalFilterCriteria() != null){
            query.where(getAdditionalFilterCriteria());
        }

        return new CPOrmQueryResult<>(context, query);
    }

    //Todo: implement own user logic
    @Override
    public QueryResult<T> searchForUser(long userId, String query) {
        Select<T> select = Select.from(classType);

        for (String column : getSearchColumns()) {
            select = select.or().like(column, query);
        }

        select = select.whereEquals("is_deleted", false)
                .sortDesc("created_timestamp");


        if(getAdditionalFilterCriteria() != null){
            select.where(getAdditionalFilterCriteria());
        }

        return new CPOrmQueryResult<>(context, select);
    }
}
