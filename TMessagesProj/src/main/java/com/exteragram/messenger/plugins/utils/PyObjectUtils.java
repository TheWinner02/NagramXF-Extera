package com.exteragram.messenger.plugins.utils;

import android.text.TextUtils;
import com.chaquo.python.PyException;
import com.chaquo.python.PyObject;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import okhttp3.internal.url._UrlKt;
import org.lsposed.lsparanoid.Deobfuscator$exteraGramDev$TMessagesProj;

/* JADX INFO: loaded from: classes.dex */
public final class PyObjectUtils {
    public static final PyObjectUtils INSTANCE = new PyObjectUtils();

    private PyObjectUtils() {
    }

    private final void closeQuietly(PyObject pyObject) {
        if (pyObject != null) {
            try {
                pyObject.close();
            } catch (PyException unused) {
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3, types: [T] */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @JvmStatic
    public static final <T> T toJavaCompat(PyObject pyObject, Class<T> clazz) throws Throwable {
        if (pyObject == null) {
            return null;
        }
        try {
            return (T) pyObject.toJava(clazz);
        } catch (Throwable unused) {
            return null;
        }
    }

    @JvmStatic
    public static final String getString(PyObject pyObject, String key, String defaultValue) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-101346065008175L);
        return getString(pyObject, key, defaultValue, false);
    }

    @JvmStatic
    public static final String getString(PyObject pyObject, String key, String defaultValue, boolean fromMap) {
        String string;
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-101294525400623L);
        if (pyObject != null && !TextUtils.isEmpty(key)) {
            PyObject pyObjectCallAttr = null;
            try {
                pyObjectCallAttr = fromMap ? pyObject.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-101311705269807L), key) : (PyObject) pyObject.get((Object) key);
                if (pyObjectCallAttr != null && (string = pyObjectCallAttr.toString()) != null) {
                    defaultValue = string;
                }
                INSTANCE.closeQuietly(pyObjectCallAttr);
                return defaultValue;
            } catch (PyException | ClassCastException unused) {
            } finally {
                INSTANCE.closeQuietly(pyObjectCallAttr);
            }
        }
        return defaultValue;
    }

    @JvmStatic
    public static final boolean getBoolean(PyObject pyObject, String key, boolean defaultValue) throws Throwable {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-101397604615727L);
        if (pyObject != null && !TextUtils.isEmpty(key)) {
            PyObject pyObject2 = null;
            try {
                PyObject pyObject3 = (PyObject) pyObject.get((Object) key);
                if (pyObject3 != null) {
                    try {
                        defaultValue = pyObject3.toBoolean();
                    } catch (PyException | ClassCastException unused) {
                        pyObject2 = pyObject3;
                        INSTANCE.closeQuietly(pyObject2);
                        return defaultValue;
                    } catch (Throwable th) {
                        INSTANCE.closeQuietly(pyObject3);
                        throw th;
                    }
                }
                INSTANCE.closeQuietly(pyObject3);
                return defaultValue;
            } catch (Throwable unused2) {
            }
        }
        return defaultValue;
    }

    @JvmStatic
    public static final int getInt(PyObject pyObject, String key, int defaultValue) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-101414784484911L);
        return getInt(pyObject, key, defaultValue, false);
    }

    @JvmStatic
    public static final int getInt(PyObject pyObject, String key, int defaultValue, boolean fromMap) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-101363244877359L);
        if (pyObject != null && !TextUtils.isEmpty(key)) {
            PyObject pyObjectCallAttr = null;
            try {
                pyObjectCallAttr = fromMap ? pyObject.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-101380424746543L), key) : (PyObject) pyObject.get((Object) key);
                if (pyObjectCallAttr != null) {
                    defaultValue = pyObjectCallAttr.toInt();
                }
                INSTANCE.closeQuietly(pyObjectCallAttr);
                return defaultValue;
            } catch (PyException | ClassCastException unused) {
            } finally {
                INSTANCE.closeQuietly(pyObjectCallAttr);
            }
        }
        return defaultValue;
    }

    @JvmStatic
    public static final String[] getStringArray(PyObject pyObject, String key, String[] defaultValue) throws Throwable {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-102016079906351L);
        if (pyObject != null && !TextUtils.isEmpty(key)) {
            PyObject pyObject2 = null;
            try {
                PyObject pyObject3 = (PyObject) pyObject.get((Object) key);
                if (pyObject3 != null) {
                    try {
                        String[] strArr = (String[]) pyObject3.toJava(String[].class);
                        if (strArr != null && strArr.length != 0) {
                            defaultValue = strArr;
                        }
                    } catch (PyException | ClassCastException unused) {
                        pyObject2 = pyObject3;
                        INSTANCE.closeQuietly(pyObject2);
                        return defaultValue;
                    } catch (Throwable th) {
                        th = th;
                        pyObject2 = pyObject3;
                        INSTANCE.closeQuietly(pyObject2);
                        throw th;
                    }
                }
                INSTANCE.closeQuietly(pyObject3);
                return defaultValue;
            } catch (Throwable unused2) {
            }
        }
        return defaultValue;
    }
}
