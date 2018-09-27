package demo.zkttestdemo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.List;

import demo.zkttestdemo.BuildConfig;
import demo.zkttestdemo.MyApplication;
import demo.zkttestdemo.R;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by zkt on 2017/9/15.
 * -------------------------------------------------------------------
 * 图片剪裁 compile 'com.github.yalantis:ucrop:2.2.1'
 * 运行权限  compile "pub.devrel:easypermissions:1.0.0"
 * ----------------------------------------------------------------------
 * 1、在manifest中配置
 * <provider
 * android:name="android.support.v4.content.FileProvider"
 * android:authorities="com.sdwfqin.sample.fileprovider"
 * android:exported="false"
 * android:grantUriPermissions="true">
 * <meta-data
 * android:name="android.support.FILE_PROVIDER_PATHS"
 * android:resource="@xml/file_paths_public"/>
 * </provider>
 * --------------------------------------------------------------------------
 * 2、在 res 目录下新建文件夹 xml 然后创建资源文件 file_paths_public(名字随意，但是要和manifest中的名字匹配)
 * * <?xml version="1.0" encoding="utf-8"?>
 * <paths>
 * <!--照片-->
 * <external-path
 * name="my_images"
 * path="Pictures"/>
 * <!--下载-->
 * <paths>
 * <external-path
 * name="download"
 * path=""/>
 * </paths>
 * </paths>
 * -----------------------------------------------------------------------------------
 */

public class CameraCropUtil implements EasyPermissions.PermissionCallbacks {

    private Activity activity;

    // 7.0 以上的uri
    private Uri mProviderUri;
    // 7.0 以下的uri
    private Uri mUri;
    // 图片路径
    //    private String mFilepath = SDCardUtils.getSDCardPath() + "AndroidSamples";
    private File mFilepath = MyApplication.getContext().getExternalCacheDir();
    private static final String TAG = "CameraCropUtil";
    private static final int PERMISSIONS_CODE_1 = 101;
    private static final int REQUEST_CAMERA = 0x11;
    private static final int REQUEST_ALBUM = 0x22;
    // 所需要的权限
    private String[] mPerms = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    private String mAuthority = BuildConfig.APPLICATION_ID + ".fileProvider";

    public CameraCropUtil(Activity activity) {
        this.activity = activity;

        // 判断权限
        if (EasyPermissions.hasPermissions(activity, mPerms)) {
        } else {
            // 如果用户拒绝权限，第二次打开才会显示提示文字
            EasyPermissions.requestPermissions(activity, "使用拍照功能需要拍照权限！", PERMISSIONS_CODE_1, mPerms);
        }
    }

    public void camera() {
        camera(mAuthority);
    }

    /**
     * 拍照
     */
    private void camera(String authority) {
        File file = new File(mFilepath, System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Android7.0以上URI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri
            mProviderUri = FileProvider.getUriForFile(activity, authority, file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mProviderUri);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            mUri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        }
        try {
            activity.startActivityForResult(intent, REQUEST_CAMERA);
        } catch (ActivityNotFoundException anf) {
            Toast.makeText(MyApplication.getContext(), "摄像头未准备好！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 相册选图
     */
    public void selectImg() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(pickIntent, REQUEST_ALBUM);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        cropRawPhoto(mProviderUri);
                    } else {
                        cropRawPhoto(mUri);
                    }
                    break;
                case REQUEST_ALBUM:
                    Log.e(TAG, "onActivityResult: " + data.getData());
                    cropRawPhoto(data.getData());
                    break;
                case UCrop.REQUEST_CROP:
                    Log.e(TAG, "onActivityResult: " + UCrop.getOutput(data));
                    onImageResultListener.crop(UCrop.getOutput(data));
                    /*mCameraTv.setText(UCrop.getOutput(data) + "");
                    Glide.with(activity)
                            .load(UCrop.getOutput(data))
                            .crossFade()
                            .into(mCameraImg);*/
                    break;
                case UCrop.RESULT_ERROR:
                    //  mCameraTv.setText(UCrop.getError(data) + "");
                    onImageResultListener.error(UCrop.getError(data) + "");
                    break;
            }
        }
    }

    /**
     * 使用UCrop进行图片剪裁
     *
     * @param uri
     */
    private void cropRawPhoto(Uri uri) {

        UCrop.Options options = new UCrop.Options();
        // 修改标题栏颜色
        options.setToolbarColor(ContextCompat.getColor(activity, R.color.white));
        // 修改状态栏颜色
        options.setStatusBarColor(ContextCompat.getColor(activity, R.color.blue_dark));
        //字体颜色
        options.setToolbarWidgetColor(ContextCompat.getColor(activity, R.color.blue_dark));
        options.setToolbarTitle("裁剪");
        // 隐藏底部工具
        options.setHideBottomControls(true);
        // 图片格式
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        // 设置图片压缩质量
        options.setCompressionQuality(100);
        // 是否让用户调整范围(默认false)，如果开启，可能会造成剪切的图片的长宽比不是设定的
        // 如果不开启，用户不能拖动选框，只能缩放图片
        options.setFreeStyleCropEnabled(true);

        // 设置源uri及目标uri
        UCrop.of(uri, Uri.fromFile(new File(mFilepath, System.currentTimeMillis() + ".jpg")))
                // 长宽比
                .withAspectRatio(1, 1)
                // 图片大小
                .withMaxResultSize(300, 300)
                // 配置参数
                .withOptions(options)
                .start(activity);
    }


    OnImageResultListener onImageResultListener;

    public void setOnImageResultListener(OnImageResultListener onImageResultListener) {
        this.onImageResultListener = onImageResultListener;
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(activity, "没有权限可能会引起程序崩溃", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    public interface OnImageResultListener {

        void crop(Uri path);

        void error(String error);
    }
}
