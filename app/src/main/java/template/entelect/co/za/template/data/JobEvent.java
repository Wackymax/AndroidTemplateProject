package template.entelect.co.za.template.data;

import java.io.Serializable;

/**
 * Created by rushil.ojageer on 2016/05/03.
 */
public class JobEvent implements Serializable {

    protected Status status;

    public enum Status {
        ADDED,
        COMPLETED_SUCCESS,
        COMPLETED_FAILED,
        CANCELLED
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
