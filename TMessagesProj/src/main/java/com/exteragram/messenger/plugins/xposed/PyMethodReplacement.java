package com.exteragram.messenger.plugins.xposed;

import com.chaquo.python.PyException;
import com.chaquo.python.PyObject;
import com.exteragram.messenger.plugins.PluginsController;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import java.lang.reflect.InvocationTargetException;
import org.telegram.messenger.FileLog;

public final class PyMethodReplacement extends XC_MethodReplacement implements AutoCloseable {
    private volatile boolean closed;
    private volatile boolean disabled;
    private final String pluginId;
    private final PyObject pythonCallback;
    private final PyObject replaceHook;

    public PyMethodReplacement(String str, PyObject pyObject) {
        if (pyObject == null) {
            throw new IllegalArgumentException("pyObject cannot be null");
        }
        this.pluginId = str;
        this.pythonCallback = pyObject;
        Object obj = pyObject.get("replace_hook");
        if (obj instanceof PyObject) {
            this.replaceHook = (PyObject) obj;
        } else {
            this.replaceHook = pyObject;
        }
    }

    public PyMethodReplacement(String str, PyObject pyObject, int i) {
        super(i);
        if (pyObject == null) {
            throw new IllegalArgumentException("pyObject cannot be null");
        }
        this.pluginId = str;
        this.pythonCallback = pyObject;
        Object obj = pyObject.get("replace_hook");
        if (obj instanceof PyObject) {
            this.replaceHook = (PyObject) obj;
        } else {
            this.replaceHook = pyObject;
        }
    }

    @Override
    public Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) {
        if (this.disabled || !PluginsController.INSTANCE.getInstance().isPluginActive$TMessagesProj(this.pluginId)) {
            try {
                return XposedBridge.invokeOriginalMethod(param.method, param.thisObject, param.args);
            } catch (InvocationTargetException e) {
                Throwable targetException = e.getTargetException();
                if (targetException instanceof RuntimeException) {
                    throw (RuntimeException) targetException;
                }
                throw new RuntimeException(targetException);
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
        Object java = null;
        PyObject pyObjectCall = null;
        try {
            pyObjectCall = this.replaceHook.call(param);
            if (pyObjectCall != null) {
                java = pyObjectCall.toJava(Object.class);
            }
            return java;
        } catch (Throwable th) {
            handleHookError(th);
            return null;
        } finally {
            if (pyObjectCall != null) {
                try { pyObjectCall.close(); } catch (Throwable ignored) {}
            }
        }
    }

    private void handleHookError(Throwable th) {
        FileLog.e("PyMethodReplacement error in " + pluginId, th);
    }

    @Override
    public void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;
        this.disabled = true;
        if (this.replaceHook != null) {
            this.replaceHook.close();
        }
    }
}
