package template.entelect.co.za.template.common.log;

import android.util.Log;

import template.entelect.co.za.template.common.ApplicationSettings;

/**
 * Created by hennie.brink on 2017/03/02.
 */

public class Logger {

    private static String TAG = "GIS";

    private Logger() {
    }

    public static void init(String appName) {

        TAG = "GIS (" + appName + ")";
    }

    public static void v(String message) {

        Log.v(TAG, message);
        logToCrashlytics(message);
    }

    public static void i(String message) {

        Log.i(TAG, message);
        logToCrashlytics(message);
    }

    public static void d(String message) {

        Log.d(TAG, message);
        logToCrashlytics(message);
    }

    public static void w(String message) {

        Log.w(TAG, message);
        logToCrashlytics(message);
    }

    public static void e(String message) {

        Log.e(TAG, message);
        logToCrashlytics(message);
    }

    public static void e(String message, Throwable ex) {

        Log.e(TAG, message, ex);
        logToCrashlytics(message);
    }

    //Todo: uncomment if you are using crashlytics
    public static void report(String message, Throwable ex){
        e(message, ex);
//        if(ApplicationSettings.ENVIRONMENT == ApplicationSettings.Environment.PROD) {
//            Crashlytics.logException(ex);
//        }
    }

    private static void logToCrashlytics(String message) {

//        if(ApplicationSettings.ENVIRONMENT == ApplicationSettings.Environment.PROD) {
//            Crashlytics.log(message);
//        }
    }
}
