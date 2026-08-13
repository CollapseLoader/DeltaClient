package aethereal.lib.javassist;

public abstract class CtBehavior {
    public void f(String src) throws CannotCompileException {
    }

    public void a(String src, boolean asFinally) throws CannotCompileException {
    }

    public int a(int lineNum, String src) throws CannotCompileException {
        return lineNum;
    }
}
