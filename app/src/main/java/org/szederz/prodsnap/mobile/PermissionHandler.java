package org.szederz.prodsnap.mobile;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionHandler {
    public static boolean grantPermissionTo(Activity activity, String permission) {
        if (isPermissionGrantedFor(activity, permission))
            return true;

        requestPermissionFor(activity, permission);
        return isPermissionGrantedFor(activity, permission);
    }

    private static void requestPermissionFor(Activity activity, String permission) {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, 200);
    }

    private static boolean isPermissionGrantedFor(Activity activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
