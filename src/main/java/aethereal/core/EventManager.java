package aethereal.core;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private static final Map<Class<? extends Event>, List<a>> a = new HashMap<>();

    private EventManager() {
    }

    public static void a(Object object) {
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (!a(method)) {
                a(method, object);
            }
        }
    }

    public static void a(Object object, Class<? extends Event> eventClass) {
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (!a(method, eventClass)) {
                a(method, object);
            }
        }
    }

    public static void b(Object object) {
        for (List<a> dataList : a.values()) {
            for (a data : dataList) {
                if (data.a().equals(object)) {
                    dataList.remove(data);
                }
            }
        }
        a(true);
    }

    public static void b(Object object, Class<? extends Event> eventClass) {
        if (a.containsKey(eventClass)) {
            for (a data : a.get(eventClass)) {
                if (data.a().equals(object)) {
                    a.get(eventClass).remove(data);
                }
            }
            a(true);
        }
    }

    private static void a(Method method, Object obj) {
        Class<?> cls = method.getParameterTypes()[0];
        final a aVar = new a(obj, method, method.getAnnotation(EventTarget.class).a());
        if (!aVar.b().isAccessible()) {
            aVar.b().setAccessible(true);
        }
        if (a.containsKey(cls)) {
            boolean z = false;
            for (a aVar2 : a.get(cls)) {
                if (aVar2.a() == obj && aVar2.b().equals(method)) {
                    z = true;
                    break;
                }
            }
            if (!z) {
                a.get(cls).add(aVar);
                b((Class<? extends Event>) cls);
                return;
            }
            return;
        }
        a.put((Class<? extends Event>) cls, new CopyOnWriteArrayList<a>() {
            @Serial
            private static final long serialVersionUID = 666;

            {
                add(aVar);
            }
        });
    }

    public static void a(Class<? extends Event> indexClass) {
        Iterator<Map.Entry<Class<? extends Event>, List<a>>> mapIterator = a.entrySet().iterator();
        while (mapIterator.hasNext()) {
            if (mapIterator.next().getKey().equals(indexClass)) {
                mapIterator.remove();
                return;
            }
        }
    }

    public static void a(boolean onlyEmptyEntries) {
        Iterator<Map.Entry<Class<? extends Event>, List<a>>> mapIterator = a.entrySet().iterator();
        while (mapIterator.hasNext()) {
            if (!onlyEmptyEntries || mapIterator.next().getValue().isEmpty()) {
                mapIterator.remove();
            }
        }
    }

    private static void b(Class<? extends Event> indexClass) {
        List<a> sortedList = new CopyOnWriteArrayList<>();
        for (byte priority : Priority.f) {
            for (a data : a.get(indexClass)) {
                if (data.c() == priority) {
                    sortedList.add(data);
                }
            }
        }
        a.put(indexClass, sortedList);
    }

    private static boolean a(Method method) {
        return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventTarget.class);
    }

    private static boolean a(Method method, Class<? extends Event> eventClass) {
        return a(method) || !method.getParameterTypes()[0].equals(eventClass);
    }

    public static void a(Event event) {
        List<a> dataList = a.get(event.getClass());
        if (dataList != null) {
            for (a data : dataList) {
                a(data, event);
            }
        }
    }

    private static void a(a data, Event argument) {
        try {
            data.b().invoke(data.a(), argument);
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e2) {
        } catch (InvocationTargetException e3) {
        }
    }

    record a(Object a, Method b, byte c) {
    }
}
