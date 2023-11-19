package com.zkt.ams;

import org.gradle.api.Project;
import org.gradle.util.ConfigureUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import groovy.lang.Closure;

//import org.gradle.util.ClosureBackedAction


public class BaseInfoExtension {

    public boolean enable = true;
    public String replaceType = "custom";
    public List<String> exclude = new ArrayList<>();

    public List<MethodExtension> methods = new ArrayList<>();

    public BaseInfoExtension(Project project) {

    }

    public void replace(String name, Closure closure) {
        MethodExtension methodExtension = new MethodExtension();
        methodExtension.name = name;
        try {

            ConfigureUtil.configure(closure, methodExtension);

            // new ClosureBackedAction<MethodExtension>(closure, Closure.DELEGATE_FIRST).execute(methodExtension);

        } catch (Throwable throwable) {
            try {
                Class clz = getClass().getClassLoader().loadClass("org.gradle.api.internal.ClosureBackedAction");
                Constructor constructor = clz.getConstructor(Closure.class, int.class);
                Object ClosureBackedAction = constructor.newInstance(closure, Closure.DELEGATE_FIRST);
                Method method = clz.getMethod("execute", Object.class);
                method.invoke(ClosureBackedAction, methodExtension);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        methods.add(methodExtension);
    }

    public void replace(String name) {
        MethodExtension methodExtension = new MethodExtension();
        methodExtension.name = name;
        methods.add(methodExtension);
    }
}