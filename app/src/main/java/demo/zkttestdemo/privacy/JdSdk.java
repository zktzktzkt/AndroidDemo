//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package demo.zkttestdemo.privacy;

import android.app.Application;
import android.content.Context;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdSdk {
    private static volatile JdSdk mInstance;
    private Application mApplication;
    private boolean mBuildConfigDebug;
    private boolean isGoogleChannel = false;
    private boolean isArm64Only = false;
    AtomicBoolean mChannelStatus = new AtomicBoolean(false);
    AtomicBoolean mArmStatus = new AtomicBoolean(false);

    public static synchronized JdSdk getInstance() {
        if (mInstance == null) {
            mInstance = new JdSdk();
        }

        return mInstance;
    }

    private JdSdk() {
    }

    public synchronized void setApplication(Application var1) {
        if (this.mApplication == null) {
            this.mApplication = var1;
        }

    }

    public Application getApplication() {
        if (this.mApplication == null) {
            throw new NullPointerException("mApplication is null, should call setApplication() when application init");
        } else {
            return this.mApplication;
        }
    }

    public Context getApplicationContext() {
        if (this.mApplication == null) {
            throw new NullPointerException("mApplication is null, should call setApplication() when application init");
        } else {
            return this.mApplication;
        }
    }

    public void setBuildConfigDebug(boolean var1) {
        this.mBuildConfigDebug = var1;
    }

    public boolean getBuildConfigDebug() {
        return this.mBuildConfigDebug;
    }

    public boolean isGoogleChannel() {
        return this.isGoogleChannel;
    }

    public void setGoogleChannel(boolean var1) {
        if (this.mChannelStatus.compareAndSet(false, true)) {
            this.isGoogleChannel = var1;
        }

    }

    public boolean isArm64Only() {
        return this.isArm64Only;
    }

    public void setArm64Only(boolean var1) {
        if (this.mArmStatus.compareAndSet(false, true)) {
            this.isArm64Only = var1;
        }

    }
}
