package template.entelect.co.za.template.domain.model;

import android.content.Context;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.List;

import template.entelect.co.za.template.domain.ReferencedRecordsFinder;
import za.co.cporm.model.CPDefaultRecord;
import za.co.cporm.model.annotation.Column.Column;
import za.co.cporm.model.annotation.Index;
import za.co.cporm.model.annotation.Indices;
import za.co.cporm.model.query.Select;

/**
 * Created by hennie.brink on 2017/03/02.
 */
@Indices(indices =
        {
                @Index(indexName = "IDX_SERVER_ID", indexColumns = {"server_id"})
        })
public abstract class AbstractDataModel<T extends AbstractDataModel> extends CPDefaultRecord<T> {

    @Column(columnName = "is_deleted")
    private boolean isDeleted;

    @Column(columnName = "server_id", required = false, notifyChanges = false)
    private long serverId = -1;

    public long getServerId() {

        return serverId;
    }

    public void setServerId(long serverId) {

        this.serverId = serverId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void deleteCascade(Context context, boolean force) {

        List<Select<T>> referenceRecords;

        referenceRecords = findReferencedRecords();

        if (referenceRecords != null) {
            for (Select<? extends AbstractDataModel> referencedSelect : referenceRecords) {

                for (AbstractDataModel record : referencedSelect.queryAsList()) {

                    record.deleteCascade(context, force);
                }
            }
        }

        if (force) {
            forceDelete(context);
        } else {
            delete(context);
        }
    }

    public List<Select<T>> findReferencedRecords() {

        return ReferencedRecordsFinder.findReferencedRecords(this);
    }

    private void forceDelete(Context context) {

        super.delete(context);
    }

    @Override
    public void delete(Context context) {
        isDeleted = true;
        save(context);
    }

    public static <T extends AbstractDataModel> long findByServerId(Class<T> classType, long id) {

        T first = Select.from(classType).whereEquals("server_id", id).include("_id").first();
        return first == null ? -1 : first.getId();
    }

    public static <T extends AbstractDataModel> long findServerId(Class<T> classType, long id) {

        T first = Select.from(classType).whereEquals("_id", id).include("server_id").first();
        return first == null ? -1 : first.getServerId();
    }

    public void unDelete(boolean save) {

        this.isDeleted = false;

        if (save) save();
    }

    public void copy(T destination) {
        for (Field field : getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                try {
                    field.setAccessible(true);
                    Object o = field.get(this);
                    field.set(destination, o);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
