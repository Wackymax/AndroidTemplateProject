package template.entelect.co.za.template;

import android.content.Context;
import android.util.Log;

import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.log.CustomLogger;
import com.squareup.otto.Bus;

import org.codejargon.feather.Provides;

import javax.inject.Singleton;

import template.entelect.co.za.template.common.inject.JobDependencyInjector;

/**
 * Created by hennie.brink on 2017/03/02.
 */

public class MainFeatherModule {
    private MainApplication context;

    public MainFeatherModule(MainApplication context) {

        this.context = context;
    }

    @Provides
    @Singleton
    public JobManager providesJobManager(Bus bus) {
        Configuration configuration = new Configuration.Builder(context)
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(3)//up to 3 consumers at a time
                .loadFactor(3)//3 jobs per consumer
                .consumerKeepAlive(120)//wait 2 minute
                .injector(new JobDependencyInjector(context))
                .customLogger(new CustomLogger() {
                    private static final String TAG = "JOB_QUEUE";

                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.i(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e(TAG, String.format(text, args), t);
                    }

                    @Override
                    public void e(String text, Object... args) {
                        Log.e(TAG, String.format(text, args));
                    }
                })
                .build();
        return new JobManager(context, configuration);
    }
}
