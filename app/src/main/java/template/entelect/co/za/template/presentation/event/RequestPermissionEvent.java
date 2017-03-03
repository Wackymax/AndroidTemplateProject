package template.entelect.co.za.template.presentation.event;

import android.Manifest;

/**
 * Created by hennie.brink on 2017/03/02.
 */

public class RequestPermissionEvent {

    public final AppPermission appPermission;

    public RequestPermissionEvent(AppPermission appPermission) {
        this.appPermission = appPermission;
    }

    public enum AppPermission {
        CAMERA(Manifest.permission.CAMERA, 100),
        LOCATION(Manifest.permission.ACCESS_FINE_LOCATION, 200),
        INTERNET(Manifest.permission.ACCESS_FINE_LOCATION, 300);

        public final String permission;
        public final int permissionCode;

        AppPermission(String permission, int permissionCode) {
            this.permission = permission;
            this.permissionCode = permissionCode;
        }

        public static AppPermission findPermissionByCode(int code) {

            for (AppPermission permission : AppPermission.values()) {

                if (permission.permissionCode == code)
                    return permission;
            }
            return null;
        }
    }
}
