package template.entelect.co.za.template.common.inject;

import android.content.Context;

import com.path.android.jobqueue.BaseJob;
import com.path.android.jobqueue.di.DependencyInjector;
import com.squareup.otto.Bus;

import org.codejargon.feather.Feather;

import java.lang.reflect.Field;

import template.entelect.co.za.template.MainApplication;
import template.entelect.co.za.template.common.log.Logger;

/**
 * Created by hennie.brink on 2017/03/02.
 */

public class JobDependencyInjector  implements DependencyInjector {

    private final Context context;
    private final Feather feather;

    public JobDependencyInjector(Context context) {
        this.context = context;
        this.feather = ((MainApplication) context.getApplicationContext()).getFeather();
    }

    @Override
    public void inject(BaseJob job) {

        feather.injectFields(job);

        for (Field field : job.getClass().getDeclaredFields()) {

            try {
                if (Context.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    field.set(job, context);
                }
            } catch (IllegalAccessException e) {
                Logger.e("Failed to inject field " + field.getName(), e);
            }
        }

    }
}
