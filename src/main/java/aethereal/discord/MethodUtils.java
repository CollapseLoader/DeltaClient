package aethereal.discord;

import java.lang.reflect.Method;

public final class MethodUtils {
    private MethodUtils() {
    }

    public static Method a(Class<?> type, String name, Class<?>... parameterTypes) {
        try {
            return type.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Object b(Object target, String name, Object... args) throws ReflectiveOperationException {
        Class<?>[] parameterTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i] == null ? Object.class : args[i].getClass();
        }
        Method method = target.getClass().getMethod(name, parameterTypes);
        return method.invoke(target, args);
    }

    public static Object b(Object target, String name) throws ReflectiveOperationException {
        return target.getClass().getMethod(name).invoke(target);
    }
}
