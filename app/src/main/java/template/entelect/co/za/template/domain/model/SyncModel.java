package template.entelect.co.za.template.domain.model;

import android.content.Context;

import org.joda.time.DateTime;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import template.entelect.co.za.template.common.log.Logger;
import template.entelect.co.za.template.domain.ReferencedRecordsFinder;
import za.co.cporm.model.annotation.Column.Column;
import za.co.cporm.model.annotation.Index;
import za.co.cporm.model.annotation.Indices;
import za.co.cporm.model.annotation.References;
import za.co.cporm.model.query.Select;

/**
 * Created by hennie.brink on 2017/03/02.
 */

@Indices(indices = {
        @Index(indexName = "IDX_SYNC_COLUMNS", indexColumns = {"modified_user, sync_flag"}),
        @Index(indexName = "IDX_SYNC_DELETED", indexColumns = {"is_deleted"})
})
public abstract class SyncModel<T extends SyncModel> extends BaseDataModel<T> {

    @Column(columnName = "sync_flag", notifyChanges = false)
    private int syncFlag = SyncFlag.CREATED.getSyncFlagInt();
    @Column(columnName = "sync_timestamp", required = false, notifyChanges = false)
    private DateTime syncTimestamp;
    @Column(columnName = "transaction_token", required = false)
    private UUID transactionToken;

    public boolean isSyncAllowed(Context context) {

        for (Field field : findReferenceFields(getClass())) {

            try {
                field.setAccessible(true);
                Class<? extends SyncModel> type = (Class<? extends SyncModel>) field.getType();
                Long id = (Long) field.get(this);
                if (id != null) {

                    SyncModel record = BaseDataModel.findById(type, id);
                    if (record != null && record.getSyncFlag() != SyncFlag.SYNCED.syncFlagInt) {
                        return false;
                    }
                }

            } catch (IllegalAccessException e) {
                Logger.e("Failed to access field " + field.getName() + " on class " + getClass().getSimpleName(), e);
            }
        }

        return true;
    }

    public void removeFailedFlag() {

        if ((this.syncFlag & SyncFlag.FAILED.getSyncFlagInt()) == SyncFlag.FAILED.getSyncFlagInt()) {
            this.syncFlag &= ~SyncFlag.FAILED.getSyncFlagInt();
        }
    }

    public int getSyncFlag() {

        return syncFlag;
    }

    protected <T extends SyncModel<T>> boolean isRecordSynced(Context context, Class<T> recordModel, long recordId) {

        T record = Select.from(recordModel).whereEquals("_id", recordId).include("sync_flag").first(context);

        return record != null && record.getSyncFlag() == SyncFlag.SYNCED.getSyncFlagInt();
    }

    public void setSyncFlag(SyncFlag syncFlag) {

        switch (syncFlag) {
            case CREATED: //We can only stay in a created state, not move back to it
                if (this.syncFlag == SyncFlag.CREATED.getSyncFlagInt()) {
                    this.syncFlag = syncFlag.getSyncFlagInt();
                }
                break;
            case UPDATED: //We can only move to an update state from a sync state
                if (this.syncFlag == SyncFlag.SYNCED.getSyncFlagInt()) {
                    this.syncFlag = syncFlag.getSyncFlagInt();
                }
                break;
            case SYNCED: //We should always be able to move to a synced state
                this.syncFlag = syncFlag.getSyncFlagInt();
                break;
            case FAILED: //We are always allowed to save the failed flag, but it must be bitwise, and not be applied more than once
                if ((this.syncFlag & SyncFlag.FAILED.getSyncFlagInt()) != SyncFlag.FAILED.getSyncFlagInt()) {
                    this.syncFlag = this.syncFlag | syncFlag.getSyncFlagInt();
                }
                break;
            default:
                throw new IllegalArgumentException("This sync flag has now flow defined");
        }
    }

    public DateTime getSyncTimestamp() {

        return syncTimestamp;
    }

    public void setSyncTimestamp(DateTime syncTimestamp) {

        this.syncTimestamp = syncTimestamp;
    }

    public Iterator<T> getReferencedRecordsIterator() {
        List<Select<T>> referencedRecordsQueries = ReferencedRecordsFinder.findReferencedRecords(this);
        List<T> list = new ArrayList<>();

        for (Select<T> referencedRecordQuery : referencedRecordsQueries) {
            list.addAll(referencedRecordQuery.queryAsList());
        }

        return list.iterator();
    }

    public UUID getTransactionToken() {
        return transactionToken;
    }

    public void setTransactionToken(UUID transactionToken) {
        this.transactionToken = transactionToken;
    }

    private List<Field> findReferenceFields(Class clazz) {

        List<Field> referenceFields = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {

            if (field.isAnnotationPresent(References.class) && SyncModel.class.isAssignableFrom(field.getType()))
                referenceFields.add(field);
        }

        Class superclass = clazz.getSuperclass();
        if (superclass != null && AbstractDataModel.class.isAssignableFrom(superclass)) {
            referenceFields.addAll(findReferenceFields(superclass));
        }

        return referenceFields;
    }

    public enum SyncFlag {
        SYNCED(0x1 << 1),
        UPDATED(0x1 << 2),
        CREATED(0x1 << 3),
        FAILED(0x1 << 4);

        private final int syncFlagInt;

        SyncFlag(int syncFlagInt) {

            this.syncFlagInt = syncFlagInt;
        }

        public int getSyncFlagInt() {

            return syncFlagInt;
        }
    }


}
