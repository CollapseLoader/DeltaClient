package aethereal.util;


import aethereal.lib.reflections.ReflectionsException;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public interface NameHelper {
    List<String> l = Arrays.asList("boolean", "char", "byte", "short", "int", "long", "float", "double", "void");
    List<Class<?>> m = Arrays.asList(Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Void.TYPE);
    List<String> n = Arrays.asList("Z", "C", "B", "S", "I", "J", "F", "D", "V");

    default String c(AnnotatedElement element) {
        if (element.getClass().equals(Class.class)) {
            return g((Class) element);
        }
        if (element.getClass().equals(Constructor.class)) {
            return a((Constructor<?>) element);
        }
        if (element.getClass().equals(Method.class)) {
            return a((Method) element);
        }
        if (element.getClass().equals(Field.class)) {
            return a((Field) element);
        }
        return null;
    }

    default String g(Class<?> type) {
        int dim = 0;
        while (type.isArray()) {
            dim++;
            type = type.getComponentType();
        }
        return type.getName() + String.join("", Collections.nCopies(dim, "[]"));
    }

    default String a(Constructor<?> constructor) {
        return String.format("%s.<init>(%s)", constructor.getName(), String.join(", ", b(constructor.getParameterTypes())));
    }

    default String a(Method method) {
        return String.format("%s.%s(%s)", method.getDeclaringClass().getName(), method.getName(), String.join(", ", b(method.getParameterTypes())));
    }

    default String a(Field field) {
        return String.format("%s.%s", field.getDeclaringClass().getName(), field.getName());
    }

    default Collection<String> a(Collection<? extends AnnotatedElement> elements) {
        return elements.stream().map(this::c).filter((v0) -> {
            return Objects.nonNull(v0);
        }).collect(Collectors.toList());
    }

    default Collection<String> b(AnnotatedElement... elements) {
        return a(Arrays.asList(elements));
    }

    default <T> T a(String str, Class<T> cls, ClassLoader... classLoaderArr) {
        if (cls.equals(Class.class)) {
            return (T) a(str, classLoaderArr);
        }
        if (cls.equals(Constructor.class)) {
            return (T) d(str, classLoaderArr);
        }
        if (cls.equals(Method.class)) {
            return (T) c(str, classLoaderArr);
        }
        if (cls.equals(Field.class)) {
            return (T) e(str, classLoaderArr);
        }
        if (cls.equals(java.lang.reflect.Member.class)) {
            return (T) b(str, classLoaderArr);
        }
        return null;
    }

    default Class<?> a(String typeName, ClassLoader... loaders) {
        String type;
        String type2;
        if (l.contains(typeName)) {
            return m.get(l.indexOf(typeName));
        }
        if (typeName.contains("[")) {
            int i = typeName.indexOf("[");
            String type3 = typeName.substring(0, i);
            String array = typeName.substring(i).replace("]", "");
            if (l.contains(type3)) {
                type2 = n.get(l.indexOf(type3));
            } else {
                type2 = "L" + type3 + ";";
            }
            type = array + type2;
        } else {
            type = typeName;
        }
        for (ClassLoader classLoader : ClasspathHelper.a(loaders)) {
            if (type.contains("[")) {
                try {
                    return Class.forName(type, false, classLoader);
                } catch (Throwable th) {
                }
            }
            try {
                return classLoader.loadClass(type);
            } catch (Throwable th2) {
            }
        }
        return null;
    }

    default java.lang.reflect.Member b(String descriptor, ClassLoader... loaders) throws ReflectionsException {
        int p0 = descriptor.lastIndexOf(40);
        String memberKey = p0 != -1 ? descriptor.substring(0, p0) : descriptor;
        String methodParameters = p0 != -1 ? descriptor.substring(p0 + 1, descriptor.lastIndexOf(41)) : "";
        int p1 = Math.max(memberKey.lastIndexOf(46), memberKey.lastIndexOf("$"));
        String className = memberKey.substring(0, p1);
        String memberName = memberKey.substring(p1 + 1);
        Class<?>[] parameterTypes = null;
        if (!methodParameters.isEmpty()) {
            String[] parameterNames = methodParameters.split(",");
            parameterTypes = (Class[]) Arrays.stream(parameterNames).map(name -> {
                return a(name.trim(), loaders);
            }).toArray(x$0 -> {
                return new Class[x$0];
            });
        }
        try {
            for (Class<?> aClass = a(className, loaders); aClass != null; aClass = aClass.getSuperclass()) {
                try {
                    if (!descriptor.contains("(")) {
                        return aClass.isInterface() ? aClass.getField(memberName) : aClass.getDeclaredField(memberName);
                    }
                    if (descriptor.contains("init>")) {
                        return aClass.isInterface() ? aClass.getConstructor(parameterTypes) : aClass.getDeclaredConstructor(parameterTypes);
                    }
                    return aClass.isInterface() ? aClass.getMethod(memberName, parameterTypes) : aClass.getDeclaredMethod(memberName, parameterTypes);
                } catch (Exception e) {
                }
            }
            return null;
        } catch (Exception e2) {
            return null;
        }
    }

    @Nullable
    default <T extends AnnotatedElement> T b(String descriptor, Class<T> resultType, ClassLoader[] loaders) {
        java.lang.reflect.Member member = b(descriptor, loaders);
        if (member == null || !member.getClass().equals(resultType)) {
            return null;
        }
        return (T) member;
    }

    @Nullable
    default Method c(String descriptor, ClassLoader... loaders) throws ReflectionsException {
        return b(descriptor, Method.class, loaders);
    }

    default Constructor<?> d(String descriptor, ClassLoader... loaders) throws ReflectionsException {
        return (Constructor) b(descriptor, Constructor.class, loaders);
    }

    @Nullable
    default Field e(String descriptor, ClassLoader... loaders) {
        return b(descriptor, Field.class, loaders);
    }

    default <T> Collection<T> a(Collection<String> names, Class<T> resultType, ClassLoader... loaders) {
        return names.stream().map(name -> {
            return a(name, resultType, loaders);
        }).filter(Objects::nonNull).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    default Collection<Class<?>> a(Collection<String> names, ClassLoader... loaders) {
        @SuppressWarnings("unchecked")
        Collection<Class<?>> classes = (Collection<Class<?>>) (Collection<?>) a(names, (Class<Object>) (Class<?>) Class.class, loaders);
        return classes;
    }
}
