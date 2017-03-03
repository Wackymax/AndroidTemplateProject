package template.entelect.co.za.template.common;

import android.content.Context;

import template.entelect.co.za.template.common.environment.EnvironmentConfig;

/**
 * Created by hennie.brink on 2017/03/02.
 */

public class ApplicationSettings {

    //Todo: Fill in application specific settings

    public static final String APPLICATION_PACKAGE_NAME = "za.co.entelect.template";

    public static final String ANDROID_ACCOUNT_TYPE = APPLICATION_PACKAGE_NAME;
    public static final String AUTH_TOKEN_TYPE_FULL_ACCESS = "Full access";

    public static final String OAUTH_CLIENT_ID = "";
    public static final String OAUTH_CLIENT_SECRET = "";
    public static final int REST_API_PAGE_SIZE = 1000;

    public static final String AUTHORITY = APPLICATION_PACKAGE_NAME + ".provider";
    public static final String AUTHORITY_FILES = APPLICATION_PACKAGE_NAME +".fileprovider";
    public static final String PREF_SYNC_FAILED_RESOURCE = "sync_fail_resource";
    public static final String PREF_SYNC_FAILED_ESRI = "sync_fail_esri";
    public static final String PREF_API_VERSION_SUPPORTED = "api_version_supported";

    public static final Environment ENVIRONMENT = EnvironmentConfig.ENVIRONMENT;
    public static final String GRANT_TYPE_REFRESH = "refresh_token";

    public static final String GCM_SENDER_ID = "";

    public enum Environment {

        QA("",
                "",
                "", false, true, 10),

        UAT("",
                "",
                "", true, true, 10),

        PROD("",
                "",
                "", true, true, 10);

        private final String baseUrl;
        private final String authUrl;
        private final String serverGoogleClientId;
        private final boolean sendCrashReports;
        private final boolean stopSyncOnLookupFailure;
        private final int maxSyncRetries;

        Environment(String baseUrl, String authUrl, String serverGoogleClientId, boolean sendCrashReports, boolean stopSyncOnLookupFailure, int maxSyncRetries) {
            this.baseUrl = baseUrl;
            this.authUrl = authUrl;
            this.serverGoogleClientId = serverGoogleClientId;
            this.sendCrashReports = sendCrashReports;
            this.stopSyncOnLookupFailure = stopSyncOnLookupFailure;
            this.maxSyncRetries = maxSyncRetries;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public String getAuthUrl() {
            return authUrl;
        }

        public String getGoogleClientId(Context context) {

            return serverGoogleClientId;
        }

        public boolean isSendCrashReports() {

            return sendCrashReports;
        }

        public boolean isStopSyncOnLookupFailure() {

            return stopSyncOnLookupFailure;
        }

        public int getMaxSyncRetries() {

            return maxSyncRetries;
        }
    }
}
