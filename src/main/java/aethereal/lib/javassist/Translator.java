package aethereal.lib.javassist;

public interface Translator extends javassist.Translator {
    default void a(ClassPool pool) throws NotFoundException, CannotCompileException {
        try {
            start(pool);
        } catch (javassist.NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (javassist.CannotCompileException e) {
            throw new CannotCompileException(e.getMessage(), e);
        }
    }

    default void a(ClassPool pool, String className) throws NotFoundException, CannotCompileException {
    }
}
