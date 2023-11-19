package me.zkt.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * Created by zhoukaitong on 2021/12/27.
 * Description:
 */
public class TimeMethodVisitor extends AdviceAdapter {


    private final String mClassName;
    private final String mMethodName;
    private int mLocal_start;
    private int mLocal_end;

    protected TimeMethodVisitor(String className, int api, MethodVisitor mv, int access, String name, String desc) {
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

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        //System.out.println("  调用变量visitFieldInsn opcode->" + opcode + " owner->" + owner + " name->" + name + " desc->" + desc);
        super.visitFieldInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        //System.out.println("  调用方法visitMethodInsn opcode->" + opcode + " owner->" + owner + " name->" + name + " desc->" + desc);
        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

    /**
     * 在visitCode中调用
     */
    @Override
    public void onMethodEnter() {
        //System.out.println("进入方法->" + mMethodName);
        super.onMethodEnter();
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
        mLocal_start = newLocal(Type.LONG_TYPE);
        mv.visitVarInsn(LSTORE, mLocal_start);
    }

    /**
     * 在visitInsn中调用
     */
    @Override
    public void onMethodExit(int opcode) {
        //System.out.println("退出方法->" + mMethodName);
        super.onMethodExit(opcode);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
        mv.visitVarInsn(LLOAD, mLocal_start);
        mv.visitInsn(LSUB);
        mLocal_end = newLocal(Type.LONG_TYPE);
        mv.visitVarInsn(LSTORE, mLocal_end);
        mv.visitLdcInsn(mClassName + " " + mMethodName + " " + "\u65b9\u6cd5\u8017\u65f6");
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
        mv.visitVarInsn(LLOAD, mLocal_end);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
        mv.visitLdcInsn("ms");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(POP);
    }
}
