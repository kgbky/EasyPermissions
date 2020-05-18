package com.yumao.easyperlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class EasyPermissions {

    private static final String TAG = "EasyPermissions";

    public static void requestPermissions(@NonNull Activity activity, int requestCode, @NonNull String... perms) {
        ActivityCompat.requestPermissions(activity, perms, requestCode);
    }

    public static void requestPermissions(@NonNull Fragment fragment, int requestCode, @NonNull String... perms) {
        fragment.requestPermissions(perms, requestCode);
    }

    /**
     * Check if the calling context has a set of permissions.
     *
     * @param context the calling context.
     * @param perms   one ore more permissions, such as {@code android.Manifest.permission.CAMERA}.
     * @return true if all permissions are already granted,
     * false if at least one permission is not yet granted.
     */
    public static boolean hasPermissions(@NonNull Context context, @NonNull String... perms) {
        // Always return true for SDK < M, let the system deal with the permissions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.w(TAG, "hasPermissions: API version < M, returning true by default");
            return true;
        }

        for (String perm : perms) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(context, perm) ==
                    PackageManager.PERMISSION_GRANTED);
            if (!hasPerm) {
                return false;
            }
        }

        return true;
    }

    /**
     * @param requestCode  标识请求的来源
     * @param permissions  请求的权限列表
     * @param grantResults 请求权限的结果列表
     * @param receiver     处理请求结果的对象
     */
    public static void onRequestPermissionsResult(int requestCode,
                                                  @NonNull String[] permissions,
                                                  @NonNull int[] grantResults,
                                                  @NonNull Object receiver) {
        //Make a collection of granted and denied permissions from the request.
        ArrayList<String> granted = new ArrayList<>();
        ArrayList<String> denied = new ArrayList<>();
        ArrayList<String> death = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String perm = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(perm);
            } else {
                if (receiver instanceof Activity) {
                    boolean b = ActivityCompat.shouldShowRequestPermissionRationale((Activity) receiver, perm);
                    if (b)
                        denied.add(perm);
                    else
                        death.add(perm);
                } else if (receiver instanceof Fragment) {
                    boolean b = ((Fragment) receiver).shouldShowRequestPermissionRationale(perm);
                    if (b)
                        denied.add(perm);
                    else
                        death.add(perm);
                } else {
                    throw new EasyPermissionsException("onRequestPermissionsResult()参数错误");
                }
            }
        }

        //反射调用RequestPermissionsGranted 注释的方法
        Method[] methods = receiver.getClass().getDeclaredMethods();
        try {
            // Report denied permissions, if any. 需要比对requestCode
            if (!denied.isEmpty()) {
                //反射调用RequestPermissionsDenied 注释的方法
                for (Method m : methods) {
                    if (m.isAnnotationPresent(RequestPermissionsDenied.class)
                            && m.getAnnotation(RequestPermissionsDenied.class).value() == requestCode) {
                        m.invoke(receiver);
                    }
                }
            } else if (!death.isEmpty()) {
                //反射调用RequestPermissionsDeniedNeverRequest 注释的方法
                for (Method m : methods) {
                    if (m.isAnnotationPresent(RequestPermissionsDeniedNeverRequest.class)
                            && m.getAnnotation(RequestPermissionsDeniedNeverRequest.class).value() == requestCode) {
                        m.invoke(receiver);
                    }
                }
            } else {
                for (Method m : methods) {
                    if (m.isAnnotationPresent(RequestPermissionsGranted.class)
                            && m.getAnnotation(RequestPermissionsGranted.class).value() == requestCode) {
                        m.invoke(receiver);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}