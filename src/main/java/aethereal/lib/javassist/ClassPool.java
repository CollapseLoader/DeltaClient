package aethereal.lib.javassist;

public class ClassPool extends javassist.ClassPool {
    public ClassPool() {
        super();
    }

    public ClassPool(boolean makeDefault) {
        super(makeDefault);
    }

    public javassist.CtClass f(String name) throws javassist.NotFoundException {
        return get(name);
    }
}
