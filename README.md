   **Android运行时权限请求库**

#### 1、添加依赖
Add it in your root build.gradle at the end of repositories:

    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }

Add the dependency in app module build.gradle

    dependencies {
         implementation 'com.github.kgbky:EasyPermissions:1.0.0'
        }

#### 2、如何使用

//step1 请求权限
```java
private static final int REQUEST_CODE=100;
EasyPermissions.requestPermissions(this,REQUEST_CODE,Manifest.permission.CAMERA);
```

//step2 覆写Activity或者Fragment的onRequestPermissionsResult()方法
```java
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
}
```

//step3 通过注解注册回调方法
```java
//成功获取到所有权限
@RequestPermissionsGranted(REQUEST_CODE)
public void cameraPermissionGranted() {
    Intent intent = new Intent(this, CameraPreviewActivity.class);
    tartActivity(intent);
}

//用户拒绝了一个或多个权限
@RequestPermissionsDenied(REQUEST_CODE)
public void cameraPermissionDenied() {
    Toast.makeText(this, "用户拒绝权限", Toast.LENGTH_SHORT).show();
}

//用户拒绝一个或多个权限,且禁止系统再次询问
@RequestPermissionsDeniedNeverRequest(REQUEST_CODE)
public void cameraPermissionDeniedNotShowUI() {
    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    Uri uri = Uri.fromParts("package", getPackageName(), null);
    intent.setData(uri);
    startActivity(intent);
}
```