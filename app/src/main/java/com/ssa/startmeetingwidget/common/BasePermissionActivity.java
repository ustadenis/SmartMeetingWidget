package com.ssa.startmeetingwidget.common;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

public abstract class BasePermissionActivity extends BaseActivity {
    private static final int REQUEST_PERMISSION = 188;

    abstract protected String[] getDesiredPermissions();

    abstract protected void onPermissionDenied();

    abstract protected void onPermissionGranted();

    private boolean mIsPermissionRequested = false;

    @SuppressWarnings("DesignForExtension")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!hasAllPermissions(getDesiredPermissions())) {
            mIsPermissionRequested = true;
            requestUserForPermission(getDesiredPermissions());
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (hasAllPermissions(getDesiredPermissions())) {
            onPermissionGranted();
        } else if (!mIsPermissionRequested) {
            onPermissionDenied();
        }
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            mIsPermissionRequested = false;
        }
    }

    protected final boolean hasAllPermissions(String[] perms) {
        for (String perm : perms) {
            if (!hasPermission(perm)) {
                return(false);
            }
        }

        return(true);
    }

    protected final boolean hasPermission(String perm) {
        return ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED;
    }

    protected final void requestUserForPermission(String[] permissions) {
        ActivityCompat.requestPermissions(this, getDeniedPermissions(permissions), REQUEST_PERMISSION);
    }

    private String[] getDeniedPermissions(String[] desired) {
        ArrayList<String> result = new ArrayList<>();

        for (String permission : desired) {
            if (!hasPermission(permission)) {
                result.add(permission);
            }
        }

        return result.toArray(new String[result.size()]);
    }
}
