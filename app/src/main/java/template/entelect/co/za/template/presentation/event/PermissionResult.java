package template.entelect.co.za.template.presentation.event;

/**
 * Created by hennie.brink on 2017/03/02.
 */

public class PermissionResult {
    public final boolean permissionGranted;
    public final RequestPermissionEvent.AppPermission appPermission;

    public PermissionResult(boolean permissionGranted, RequestPermissionEvent.AppPermission appPermission) {
        this.permissionGranted = permissionGranted;
        this.appPermission = appPermission;
    }
}
