package template.entelect.co.za.template.domain.model;

import android.accounts.Account;
import android.content.Context;

import org.joda.time.DateTime;

import za.co.cporm.model.annotation.Column.Column;

/**
 * Created by hennie.brink on 2017/03/02.
 */
public abstract class BaseDataModel<T extends BaseDataModel> extends AbstractDataModel<T> {

    @Column(columnName = "created_user")
    private long createdUser;
    @Column(columnName = "created_timestamp")
    private DateTime createdTimestamp;
    @Column(columnName = "modified_user")
    private long modifiedUser;
    @Column(columnName = "modified_timestamp")
    private DateTime modifiedTimestamp;

    public long getCreatedUser() {

        return createdUser;
    }

    public void setCreatedUser(long createdUser) {

        this.createdUser = createdUser;
    }

    public DateTime getCreatedTimestamp() {

        return createdTimestamp;
    }

    public void setCreatedTimestamp(DateTime createdTimestamp) {

        this.createdTimestamp = createdTimestamp;
    }

    public long getModifiedUser() {

        return modifiedUser;
    }

    public void setModifiedUser(long modifiedUser) {

        this.modifiedUser = modifiedUser;
    }

    public DateTime getModifiedTimestamp() {

        return modifiedTimestamp;
    }

    public void setModifiedTimestamp(DateTime modifiedTimestamp) {

        this.modifiedTimestamp = modifiedTimestamp;
    }

    public void setId(long id) {

        this._id = id;
    }

    public void syncEventOccurred(Context context, Account account, SyncEvent event) {

    }

    public enum SyncEvent {
        INSERT,
        UPDATE,
        DELETE
    }
}
