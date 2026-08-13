package aethereal.core;


import aethereal.lib.javassist.CannotCompileException;
import aethereal.lib.javassist.CtBehavior;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Callback {
    public static final Map<String, Callback> a = new HashMap();
    private final String b;

    public Callback(String src) {
        String uuid = UUID.randomUUID().toString();
        a.put(uuid, this);
        this.b = "((javassist.tools.Callback) javassist.tools.Callback.callbacks.get(\"" + uuid + "\")).result(new Object[]{" + src + "});";
    }

    public static void a(CtBehavior behavior, Callback callback) throws CannotCompileException {
        behavior.f(callback.toString());
    }

    public static void b(CtBehavior behavior, Callback callback) throws CannotCompileException {
        behavior.a(callback.toString(), false);
    }

    public static void a(CtBehavior behavior, Callback callback, boolean asFinally) throws CannotCompileException {
        behavior.a(callback.toString(), asFinally);
    }

    public static int a(CtBehavior behavior, Callback callback, int lineNum) throws CannotCompileException {
        return behavior.a(lineNum, callback.toString());
    }

    public abstract void a(Object[] objArr);

    public String toString() {
        return a();
    }

    public String a() {
        return this.b;
    }
}
