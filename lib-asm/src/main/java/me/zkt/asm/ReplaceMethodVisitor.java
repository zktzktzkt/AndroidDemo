package me.zkt.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * Created by zhoukaitong on 2021/12/27.
 * Description: 字节码替换
 */
public class ReplaceMethodVisitor extends AdviceAdapter {


    private final String mClassName;
    private final String mMethodName;

    protected ReplaceMethodVisitor(String className, int api, MethodVisitor mv, int access, String name, String desc) {
        super(api, mv, access, name, desc);
        this.mClassName = className;
        this.mMethodName = name;
    }

    /**
     * 此回调代表要开始执行方法了，只会调用一次
     */
    @Override
    public void visitCode() {
        super.visitCode();
    }

    /**
     * 每一行对应的字节码指令
     */
    @Override
    public void visitInsn(int opcode) {
        super.visitInsn(opcode);
    }

    /**
     * 调用new等关键字会回调
     */
    @Override
    public void visitTypeInsn(int opcode, String type) {
        // System.out.println("  调用visitTypeInsn opcode->" + opcode + " type->" + type);
        if (opcode == NEW) {
            if ("java/lang/Thread".equals(type)) {
                type = "com/zkt/ams/demo/ShadowThread";
            }
        }
        super.visitTypeInsn(opcode, type);
    }

    /**
     * 调用变量的时候回调
     */
    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        //System.out.println("  调用变量visitFieldInsn opcode->" + opcode + " owner->" + owner + " name->" + name + " desc->" + desc);

        //替换静态属性
        if (owner.equals("android/os/Build") && name.equals("BRAND") && GETSTATIC == opcode) {
            //发现有GETSTATIC android/os/Build.BRAND : Ljava/lang/String;的调用
            //因此我们在这里把原始调用改为：INVOKESTATIC com/zkt/ams/demo/BaseInfo/BaseInfo.getDeviceBrand ()Ljava/lang/String;
            opcode = INVOKESTATIC;
            owner = "com/zkt/ams/demo/BaseInfo";
            name = "getDeviceBrand";
            desc = "()Ljava/lang/String;";
        } else if (owner.equals("com/zkt/ams/demo/MainActivity2") && name.equals("aaa") && GETSTATIC == opcode) {
            opcode = INVOKESTATIC;
            owner = "com/zkt/ams/demo/BaseInfo";
            name = "getAAA";
            desc = "()Ljava/lang/String;";
        }
        //替换对象属性
        else if (owner.equals("android/util/DisplayMetrics") && name.equals("density") && GETFIELD == opcode) {
            mv.visitInsn(POP);
            opcode = INVOKESTATIC;
            owner = "com/zkt/ams/demo/BaseInfo";
            name = "getDensity";
            desc = "()F";
        }

        super.visitFieldInsn(opcode, owner, name, desc);
    }

    /**
     * 调用方法的时候回调
     */
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        System.out.println("  调用方法visitMethodInsn opcode->" + opcode + " owner->" + owner + " name->" + name + " desc->" + desc);

        //替换Thread构造函数为ShadowThread
        if (owner.equals("java/lang/Thread") && name.equals("<init>") && INVOKESPECIAL == opcode) {
            owner = "com/zkt/ams/demo/ShadowThread";
            name = "<init>";
        }
        else if (owner.equals("java/lang/Thread") && name.equals("start") && INVOKEVIRTUAL == opcode) {
            owner = "com/zkt/ams/demo/ShadowThread";
            name = "start";
        }

        //替换静态方法
       else if (owner.equals("android/os/Build") && name.equals("getRadioVersion") && INVOKESTATIC == opcode) {
            owner = "com/zkt/ams/demo/BaseInfo";
            name = "getRadioVersion";
            desc = "()Ljava/lang/String;";
        }

        //替换对象方法
        else if (owner.equals("android/content/pm/PackageManager") && name.equals("getInstalledPackages") && INVOKEVIRTUAL == opcode) {
            opcode = INVOKESTATIC;
            owner = "com/zkt/ams/demo/BaseInfo";
            name = "getInstalledPkgsWithAOP";
            desc = "(Landroid/content/pm/PackageManager;I)Ljava/util/List;";
        }

        //替换对象方法 测试pop 忽略栈顶的值，也就是忽略调用getPackageManager方法的对象
        else if (owner.equals("android/content/Context") && name.equals("getPackageManager") && INVOKEVIRTUAL == opcode) {
            mv.visitInsn(POP); //出栈，忽略调用方法的对象
            opcode = INVOKESTATIC;
            owner = "com/zkt/ams/demo/BaseInfo";
            name = "getPackageManager";
            desc = "()Landroid/content/pm/PackageManager;";
        }

        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

    /**
     * 在visitCode中调用
     */
    @Override
    public void onMethodEnter() {
        //System.out.println("进入方法->" + mMethodName);
        super.onMethodEnter();
    }

    /**
     * 在visitInsn中调用
     */
    @Override
    public void onMethodExit(int opcode) {
        //System.out.println("退出方法->" + mMethodName);
        super.onMethodExit(opcode);
    }
}
