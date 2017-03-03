package template.entelect.co.za.template;

import android.support.multidex.MultiDexApplication;

import net.danlew.android.joda.JodaTimeAndroid;

import org.codejargon.feather.Feather;

import butterknife.ButterKnife;
import template.entelect.co.za.template.common.ApplicationSettings;
import za.co.cporm.model.CPOrm;

/**
 * Created by hennie.brink on 2017/03/02.
 */

public class MainApplication extends MultiDexApplication {

    private static MainApplication rootContext;
    private Feather feather;



    @Override
    public void onCreate() {
        super.onCreate();

        JodaTimeAndroid.init(this);
        CPOrm.initialize(this);
        ButterKnife.setDebug(ApplicationSettings.ENVIRONMENT == ApplicationSettings.Environment.QA);

        rootContext = this;
        feather = Feather.with(
                new MainFeatherModule(this)
        );

        //Todo: uncomment this if you want to use crashlytics/fabric and imported the libs
//        if (ApplicationSettings.ENVIRONMENT != ApplicationSettings.Environment.QA) {
//            Fabric.with(this, new Crashlytics(), new Answers(), new Beta());
//            Crashlytics.getInstance().core.setString("ENVIRONMENT", EnvironmentSettings.ENVIRONMENT.toString());
//        }
    }

    public static MainApplication getRootContext() {
        return rootContext;
    }

    public Feather getFeather() {
        if (feather == null) {
            throw new NullPointerException("Feather has not been initialized.");
        }
        return feather;
    }
}
