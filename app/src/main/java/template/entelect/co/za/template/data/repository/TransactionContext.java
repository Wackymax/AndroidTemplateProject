package template.entelect.co.za.template.data.repository;

import android.content.Context;

import com.squareup.otto.Bus;

/**
 * Created by hennie.brink on 2017/01/20.
 */

public class TransactionContext {

    private final Context context;
    private final Bus eventBus;

    public TransactionContext(Context context, Bus eventBus) {

        this.context = context;
        this.eventBus = eventBus;
    }

    public Context getContext() {
        return context;
    }

    public Bus getEventBus() {
        return eventBus;
    }
}
