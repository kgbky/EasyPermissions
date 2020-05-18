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

    EasyPermissions.requestPermissions(activity,requestCode,Manifest.permission.CAMERA);

//step2 覆写Activity或者Fragment的onRequestPermissionsResult()方法

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //step 2
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

//step3 通过注解注册回调方法

    //成功获取到所有权限
    @RequestPermissionsGranted(requestCode)
    public void cameraPermissionGranted() {
        Intent intent = new Intent(this, CameraPreviewActivity.class);
        startActivity(intent);
    }

    //用户拒绝了一个或多个权限
    @RequestPermissionsDenied(requestCode)
    public void cameraPermissionDenied() {
        Toast.makeText(this, "用户拒绝权限", Toast.LENGTH_SHORT).show();
    }

    //用户拒绝一个或多个权限,且禁止系统再次询问
    @RequestPermissionsDeniedNeverRequest(requestCode)
    public void cameraPermissionDeniedNotShowUI() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }