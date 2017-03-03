package template.entelect.co.za.template.data.repository;

import android.content.Context;

import com.path.android.jobqueue.JobManager;

import javax.inject.Inject;

import template.entelect.co.za.template.data.CPOrmQueryResult;
import template.entelect.co.za.template.data.QueryResult;
import za.co.cporm.model.CPDefaultRecord;
import za.co.cporm.model.query.DataFilterClause;
import za.co.cporm.model.query.Select;

public abstract class BaseQueryFactoryImpl<T extends CPDefaultRecord> implements QueryFactory<T> {

    @Inject
    protected JobManager jobManager;

    protected Context context;
    protected Class<T> classType;

    public BaseQueryFactoryImpl(Context context, Class<T> classType) {
        this.context = context;
        this.classType = classType;
    }

    public QueryResult<T> getById(long id) {
        Select<T> query = Select.from(classType)
                .whereEquals("_id", id);
        return new CPOrmQueryResult<>(context, query);
    }

    public QueryResult<T> getAll() {
        Select<T> select = Select.from(classType);
        return new CPOrmQueryResult<>(context, select);
    }

    @Override
    public QueryResult<T> searchForUser(long userId, String query) {
        Select<T> select = Select.from(classType);

        for (String column : getSearchColumns()) {
            select = select.or().like(column, query);
        }

        if(getAdditionalFilterCriteria() != null){
            select.where(getAdditionalFilterCriteria());
        }

        return new CPOrmQueryResult<>(context, select);
    }

    protected DataFilterClause getAdditionalFilterCriteria(){
        return null;
    }
}
