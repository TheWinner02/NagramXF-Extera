package org.mvel2;

import java.io.Serializable;
import java.util.Map;

public class MVEL {
    public static Serializable compileExpression(String expression) {
        return expression;
    }

    public static Object executeExpression(Object compiled, Map factory, Class toType) {
        if (toType == Boolean.TYPE || toType == Boolean.class) {
            return Boolean.TRUE;
        }
        return null;
    }

    public static Object executeExpression(Object compiled, Object ctx, Map vars, Class toType) {
        if (toType == Boolean.TYPE || toType == Boolean.class) {
            return Boolean.TRUE;
        }
        return null;
    }

    public static Object executeExpression(Object compiled, Map vars) {
        return null;
    }

    public static Object executeExpression(Object compiled) {
        return null;
    }

    public static Object eval(String expression, Map vars) {
        return null;
    }
}
