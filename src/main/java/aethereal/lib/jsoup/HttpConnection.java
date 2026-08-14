package aethereal.lib.jsoup;

import java.net.CookieManager;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class HttpConnection {
    private HttpConnection() {
    }

    public static class c {
        private final URL url;
        private final CookieManager b = new CookieManager();

        public c(URL url) {
            this.url = url;
        }

        public URL a() {
            return url;
        }

        public CookieManager r() {
            return b;
        }
    }

    public static class d {
        private final Map<String, String> cookies = new HashMap<>();

        public void add(String name, String value) {
            cookies.put(name, value);
        }

        public Map<String, String> e() {
            return cookies;
        }
    }
}
