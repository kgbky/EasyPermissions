package com.yumao.easypermissions;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yumao.easyperlibrary.EasyPermissions;
import com.yumao.easyperlibrary.RequestPermissionsDenied;
import com.yumao.easyperlibrary.RequestPermissionsDeniedNeverRequest;
import com.yumao.easyperlibrary.RequestPermissionsGranted;

public class PermissionFragment extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_permission, container, false);
        view.findViewById(R.id.button).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        //step 1
        EasyPermissions.requestPermissions(this, 100,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_CONTACTS);
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
        Toast.makeText(getContext(), "获取到所有权限", Toast.LENGTH_SHORT).show();
    }

    //step 3
    @RequestPermissionsDenied(100)
    public void cameraPermissionDenied() {
        Toast.makeText(getContext(), "用户拒绝一个或多个权限", Toast.LENGTH_SHORT).show();
    }

    //step 3
    @RequestPermissionsDeniedNeverRequest(100)
    public void cameraPermissionDeniedNeverRequest() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

}