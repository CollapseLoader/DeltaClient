package aethereal.util;

import aethereal.lib.log4j.Provider;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public final class ProviderUtil {
    public static final List<Provider> b = new ArrayList<>();
    public static final ReentrantLock c = new ReentrantLock();

    private ProviderUtil() {
    }

    public static <T> T a(Class<T> type) {
        return java.util.ServiceLoader.load(type).findFirst().orElseThrow();
    }

    public static void a(Provider provider) {
        b.add(provider);
    }

    public static void a(URL url, ClassLoader classLoader) {
    }
}
