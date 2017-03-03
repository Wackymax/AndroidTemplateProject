package template.entelect.co.za.template.data.repository;

import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.text.TextUtils;

import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import template.entelect.co.za.template.MainApplication;
import template.entelect.co.za.template.data.BaseJob;
import template.entelect.co.za.template.data.CPOrmQueryResult;
import template.entelect.co.za.template.data.ItemCreateEvent;
import template.entelect.co.za.template.data.ItemDeleteEvent;
import template.entelect.co.za.template.data.QueryResult;
import template.entelect.co.za.template.domain.model.BaseDataModel;
import template.entelect.co.za.template.domain.model.SyncModel;
import za.co.cporm.model.CPOrm;
import za.co.cporm.model.TransactionHelper;
import za.co.cporm.model.generate.TableDetails;
import za.co.cporm.model.query.Select;
import za.co.cporm.model.util.CPOrmCursor;

public abstract class AbstractBaseQueryFactoryImpl<T extends SyncModel> extends AbstractQueryFactoryImpl<T> implements BaseQueryFactory<T>, Serializable {

    @Inject
    protected JobManager jobManager;
    protected Context context;
    private Class<T> classType;

    public AbstractBaseQueryFactoryImpl(Context context, Class<T> classType) {
        super(context, classType);
        this.context = context;
        this.classType = classType;
        ((MainApplication) context.getApplicationContext()).getFeather().injectFields(this);
    }

    public QueryResult<T> getById(long id) {
        Select<T> query = Select.from(classType)
                .whereEquals("_id", id)
                .whereEquals("is_deleted", false);
        return new CPOrmQueryResult<>(context, query);
    }

    public QueryResult<T> getForEntity(String joinColumn, long parentId) {
        Select<T> query = Select.from(classType)
                .whereEquals(joinColumn, parentId)
                .whereEquals("is_deleted", false);

        if(getAdditionalFilterCriteria() != null){
            query.where(getAdditionalFilterCriteria());
        }

        return new CPOrmQueryResult<>(context, applyDefaultSorting(query));
    }

    public QueryResult<T> getAll() {
        Select<T> select = Select.from(classType)
                .whereEquals("is_deleted", false);
        return new CPOrmQueryResult<>(context, applyDefaultSorting(select));
    }

    @Override
    public long validateAndSave(Bus bus, final T... items) {
        return validateAndSave(new TransactionContext(context, bus), items);
    }

    @Override
    public long validateAndSave(final TransactionContext transactionContext, final T... items) {
        //return a Task object which contains ID, status, item etc
        return jobManager.addJob(new BaseJob<T>(transactionContext.getEventBus(), ItemCreateEvent.class) {
            @Override
            protected void doInBackground() throws RemoteException, OperationApplicationException {
                List<SyncModel> itemsToSave = new ArrayList<>();
                List<T> oldItems = new ArrayList<>();
                for (T item : items) {
                    oldItems.add(getExistingItem(item));
                    itemsToSave.add(getItemToSave(item));
                }

                TransactionHelper.saveInTransaction(context, itemsToSave);
            }
        });
    }

    @Override
    public T getItemToSave(T item) {
        if (item.getCreatedTimestamp() == null) {
            item.setCreatedTimestamp(new DateTime());
            item.setCreatedUser(getCurrentUserId());
        }

        item.setModifiedTimestamp(new DateTime());
        item.setModifiedUser(getCurrentUserId());

        setSyncInformation(item);
        return item;
    }

    public T getExistingItem(T item) {
        T oldItem = null;
        if (item.getId() != null) {
            oldItem = getById(item.getId()).getFirst();
        }
        return oldItem;
    }

    protected <K extends SyncModel> void setSyncInformation(K item) {
        if (item.getId() != null) {
            item.setSyncFlag(SyncModel.SyncFlag.UPDATED);
        } else {
            item.setTransactionToken(UUID.randomUUID());
        }
    }

    //Todo: implement own user authentication logic
    protected long getCurrentUserId()  {
        return 0;
    }

    protected Select<T> applyDefaultSorting(Select<T> source) {
        return source.sortDesc("created_timestamp");
    }

    @Override
    public long delete(final long id, Bus bus) {
        //return a Task object which contains ID, status, item etc
        return jobManager.addJob(new BaseJob<T>(bus, ItemDeleteEvent.class) {
            @Override
            protected void doInBackground() throws RemoteException, OperationApplicationException {
                T item = getById(id).getFirst();

                if (item != null) {
                    item.deleteCascade(context, false);

                    List<BaseDataModel> logsToSave = new ArrayList<>();
                }

            }
        });
    }

    public static <K> String scriptData(Context context, Class<K> classType) {
        String template = "insert into %s (%s) values (%s);";
        StringBuilder sb = new StringBuilder();

        TableDetails tableDetails = CPOrm.findTableDetails(context, classType);

        List<String> columns = new ArrayList<>();
        for (TableDetails.ColumnDetails columnDetails : tableDetails.getColumns()) {
            columns.add(columnDetails.getColumnName());
        }
        String tableName = tableDetails.getTableName();
        String columnNames = TextUtils.join(",", columns);

        List<String> values = new ArrayList<>();
        CPOrmCursor<K> cursor = Select.from(classType).queryAsCursor();

        while (cursor.moveToNext()) {
            for (TableDetails.ColumnDetails columnDetails : tableDetails.getColumns()) {
                int columnIndex = cursor.getColumnIndex(columnDetails.getColumnName());
                if (columnDetails.getColumnTypeMapping().getSqlColumnTypeName().equals("TEXT")) {
                    values.add("'" + cursor.getString(columnIndex) + "'");
                } else {
                    values.add(cursor.getString(columnIndex));
                }
            }
            String valuesString = TextUtils.join(",", values);
            sb.append(String.format(template, tableName, columnNames, valuesString));
            sb.append("\n");
            values.clear();
        }

        cursor.close();
        return sb.toString();
    }


}
