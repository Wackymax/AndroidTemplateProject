package template.entelect.co.za.template.common.util;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by hennie.brink on 2017/03/02.
 */

public class MainThreadBus extends Bus {
    public MainThreadBus() {
    }

    public MainThreadBus(String identifier) {
        super(identifier);
    }

    public MainThreadBus(ThreadEnforcer enforcer) {
        super(enforcer);
    }

    public MainThreadBus(ThreadEnforcer enforcer, String identifier) {
        super(enforcer, identifier);
    }

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    MainThreadBus.super.post(event);
                }
            });
        }
    }
}
