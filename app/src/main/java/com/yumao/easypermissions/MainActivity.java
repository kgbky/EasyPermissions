package com.yumao.easypermissions;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.yumao.easyperlibrary.EasyPermissions;
import com.yumao.easyperlibrary.RequestPermissionsDenied;
import com.yumao.easyperlibrary.RequestPermissionsDeniedNeverRequest;
import com.yumao.easyperlibrary.RequestPermissionsGranted;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //step 1
    public void openCamera(View view) {
        EasyPermissions.requestPermissions(this, 100, Manifest.permission.CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //step 2
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //step 3
    @RequestPermissionsGranted(100)
    public void cameraPermissionGranted() {
        Intent intent = new Intent(this, CameraPreviewActivity.class);
        startActivity(intent);
    }

    //step 3
    @RequestPermissionsDenied(100)
    public void cameraPermissionDenied() {
        Toast.makeText(this, "用户拒绝权限", Toast.LENGTH_SHORT).show();
    }

    //step 3
    @RequestPermissionsDeniedNeverRequest(100)
    public void cameraPermissionDeniedNotShowUI() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    public void openFragmentPermissionActivity(View view) {
        Intent intent = new Intent(this, FragmentPermissionActivity.class);
        startActivity(intent);
    }
}
