package demo.zkttestdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 捕捉全局异常信息
 * Created by zkt on 2018-2-5.
 */

public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {

    private static ExceptionCrashHandler mInstance;
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    public static ExceptionCrashHandler getInstance() {
        if (mInstance == null) {
            synchronized (ExceptionCrashHandler.class) {
                if (mInstance == null) {
                    mInstance = new ExceptionCrashHandler();
                }
            }
        }
        return mInstance;
    }

    // 这个context主要用来获取一些手机信息
    private Context mContext;

    public void init(Context context) {
        this.mContext = context;
        Thread.currentThread().setUncaughtExceptionHandler(this);
        mDefaultUncaughtExceptionHandler = Thread.currentThread().getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // 1.应用信息 包名 版本号 等
        // 2.崩溃的详细信息
        // 3.保存当前文件，等应用再次启动再上传（上传文件操作应放在MainActivity中）

        String crashFileName = saveInfoToSD(e);
        cacheCrashFile(crashFileName);

        //让系统默认处理一下，否则Log上看不到崩溃信息
        mDefaultUncaughtExceptionHandler.uncaughtException(t, e);
    }

    private String saveInfoToSD(Throwable e) {
        String fileName = null;
        StringBuilder sb = new StringBuilder();

        // 1.手机信息 + 应用信息  --》 obtainSimpleInfo
        for (Map.Entry<String, String> entry : obtainSimpleInfo(mContext).entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }

        // 2.崩溃的详细信息
        sb.append(obtainExceptionInfo(e));

        // 3.保存文件
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(mContext.getFilesDir() + File.separator + "crash" + File.separator);
            if (dir.exists()) {
                deleteDir(dir);
            }
            //重新创建
            if (!dir.exists()) {
                dir.mkdir();
            }
            try {
                fileName = dir.toString()
                        + File.separator
                        + getAssignTime("yyyy_MM_dd_HH_mm") + ".txt";
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        return fileName;
    }

    /**
     * 删除目录下的所有子文件
     *
     * @param dir
     * @return
     */
    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (File child : children) {
                child.delete();
            }
        }
        return true;
    }

    /**
     * 获取系统未捕捉的错误信息
     *
     * @param e
     * @return
     */
    private String obtainExceptionInfo(Throwable e) {
        // Throwable转换成String
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }

    private HashMap<String, String> obtainSimpleInfo(Context context) {
        HashMap<String, String> map = new HashMap<>();
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        map.put("versionName", packageInfo.versionName);
        map.put("versionCode", "" + packageInfo.versionCode);
        map.put("MODEL", "" + Build.MODEL);
        map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
        map.put("PRODUCT", "" + Build.PRODUCT);
        map.put("MOBILE_INFO", "" + getMobileInfo());

        return map;
    }

    /**
     * 获取手机的信息
     *
     * @return
     */
    private String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        try {
            //利用反射获取Build类的所有属性
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                sb.append(name + "=" + value);
                sb.append("\n");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String getAssignTime(String dateFormatStr) {
        DateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
        long currentTimeMillis = System.currentTimeMillis();
        return dateFormat.format(currentTimeMillis);
    }

    private void cacheCrashFile(String fileName) {
        SharedPreferences sp = mContext.getSharedPreferences("crash", Context.MODE_PRIVATE);
        sp.edit().putString("CRASH_FILE_NAME", fileName).apply();
    }

    /**
     * 获取崩溃文件（上传到服务器的时候用）
     *
     * @return
     */
    public File getCrashFile() {
        String crashFileName = mContext.getSharedPreferences("crash", Context.MODE_PRIVATE)
                .getString("CRASH_FILE_NAME", "");
        return new File(crashFileName);
    }
}
