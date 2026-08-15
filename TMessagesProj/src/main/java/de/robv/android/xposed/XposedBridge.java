package de.robv.android.xposed;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class XposedBridge {
    public static final ClassLoader BOOTCLASSLOADER = ClassLoader.getSystemClassLoader();
    public static boolean disableHooks = false;

    public static Set<XC_MethodHook.Unhook> hookAllConstructors(Class<?> hookClass, XC_MethodHook callback) {
        return new HashSet<>();
    }

    public static Set<XC_MethodHook.Unhook> hookAllMethods(Class<?> hookClass, String methodName, XC_MethodHook callback) {
        return new HashSet<>();
    }

    public static XC_MethodHook.Unhook hookMethod(Member hookMethod, XC_MethodHook callback) {
        if (hookMethod instanceof Method) {
            return callback.new Unhook((Method) hookMethod);
        }
        return null;
    }

    public static void unhookMethod(Member hookMethod, XC_MethodHook callback) {
    }

    public static Object invokeOriginalMethod(Member method, Object thisObject, Object[] args) throws Exception {
        if (method instanceof Method) {
            ((Method) method).setAccessible(true);
            return ((Method) method).invoke(thisObject, args);
        }
        return null;
    }

    public static void log(String text) {
    }

    public static void log(Throwable t) {
    }

    public static void disableProfileSaver() {
    }

    public static boolean disableHiddenApiRestrictions() {
        return true;
    }

    public static boolean deoptimizeMethod(Member member) {
        return true;
    }
}
