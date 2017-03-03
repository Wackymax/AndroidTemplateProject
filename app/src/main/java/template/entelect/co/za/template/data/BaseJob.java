package template.entelect.co.za.template.data;

import android.content.OperationApplicationException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.squareup.otto.Bus;

import java.io.IOException;

import template.entelect.co.za.template.common.log.Logger;
import template.entelect.co.za.template.domain.model.BaseDataModel;

/**
 * Created by Rushil on 3/14/2016.
 */
public abstract class BaseJob<T extends BaseDataModel> extends Job {

    public static final int PRIORITY = 1;
    private Handler handler;
    private Class<? extends JobEvent> eventClass;

    public BaseJob(Bus bus, Class<? extends JobEvent> eventClass) {
        super(new Params(PRIORITY));
        this.eventClass = eventClass;
        this.handler = new SaveTaskHandler(bus);
    }

    @Override
    public void onAdded() {

        try {

            JobEvent event = eventClass.newInstance();
            event.setStatus(JobEvent.Status.ADDED);
            Message message = handler.obtainMessage();
            message.getData().putSerializable(SaveTaskHandler.KEY_EVENT, event);
            message.sendToTarget();

        } catch (InstantiationException e) {
            Logger.e(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            Logger.e(e.getMessage(), e);
        }
    }

    @Override
    public void onRun() throws Throwable {
        try {

            JobEvent event = eventClass.newInstance();
            Message message = handler.obtainMessage();
            message.getData().clear();

            try {
                doInBackground();
                event.setStatus(JobEvent.Status.COMPLETED_SUCCESS);
            } catch (Exception ex) {
                Logger.e(ex.getMessage(), ex);
                event.setStatus(JobEvent.Status.COMPLETED_FAILED);
            } finally {
                message.getData().putSerializable(SaveTaskHandler.KEY_EVENT, event);
                message.sendToTarget();
            }

        } catch (InstantiationException e) {
            Logger.e(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            Logger.e(e.getMessage(), e);
        }
    }

    @Override
    protected void onCancel() {

        try {

            JobEvent event = eventClass.newInstance();
            event.setStatus(JobEvent.Status.CANCELLED);
            Message message = handler.obtainMessage();
            message.getData().putSerializable(SaveTaskHandler.KEY_EVENT, event);
            message.sendToTarget();

        } catch (InstantiationException e) {
            Logger.e(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            Logger.e(e.getMessage(), e);
        }
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }

    protected abstract void doInBackground() throws RemoteException, OperationApplicationException, IOException;

    static class SaveTaskHandler extends Handler {

        public static final String KEY_EVENT = "KEY_EVENT";

        private Bus bus;

        public SaveTaskHandler(Bus bus) {
            super(Looper.getMainLooper());
            this.bus = bus;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            msg.getData();

            JobEvent event = (JobEvent) msg.getData().getSerializable(KEY_EVENT);

            if (event != null) {
                Log.i("CREATE_JOB", event.getStatus().toString());
                bus.post(event);
            }
        }
    }

}
