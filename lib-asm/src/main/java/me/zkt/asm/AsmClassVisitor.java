package me.zkt.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhoukaitong on 2021/12/27.
 * Description:
 */
public class AsmClassVisitor extends ClassVisitor implements Opcodes {

    private static final String[] methodNames = {"onCreate", "onStart", "onResume", "onPause", "onStop", "onDestroy", "onHhHAHAH"};

    private String            mClassName;
    private ArrayList<String> names;

    public AsmClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
        List<String> list = Arrays.asList(methodNames);
        names = new ArrayList<>(list);
    }

    /**
     * 访问类的时候回调
     */
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.mClassName = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    /**
     * 访问到了类中的方法
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        //System.out.println("AsmClassVisitor : change method ----> " + name);
        if (mv != null) {
            TimeMethodVisitor timeMethodVisitor = new TimeMethodVisitor(mClassName, api, mv, access, name, desc);
            return new ReplaceMethodVisitor(mClassName, api, timeMethodVisitor, access, name, desc);
        }
        return mv;
    }

    /**
     * 匹配方法
     *
     * @param name 方法名
     */
    private boolean methodMatch(String name) {
        return names.contains(name);
    }
}