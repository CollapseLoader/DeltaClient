package aethereal.lib.jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class JsoupConnection {
    private final org.jsoup.Connection delegate;
    private final Map<String, String> formData = new HashMap<>();

    private JsoupConnection(String url) {
        this.delegate = org.jsoup.Jsoup.connect(url);
    }

    public static JsoupConnection b(String url) {
        return new JsoupConnection(url);
    }

    public JsoupConnection c(String key, String value) {
        delegate.cookie(key, value);
        return this;
    }

    public JsoupConnection c(Map<String, String> cookies) {
        cookies.forEach(delegate::cookie);
        return this;
    }

    public JsoupConnection b(Map<String, String> headers) {
        headers.forEach(delegate::header);
        return this;
    }

    public JsoupConnection a(c method) {
        delegate.method(org.jsoup.Connection.Method.valueOf(method.name()));
        return this;
    }

    public JsoupConnection a(Map<String, String> data) {
        formData.putAll(data);
        return this;
    }

    public JsoupConnection a(String key, String value) {
        formData.put(key, value);
        return this;
    }

    public JsoupConnection c(boolean followRedirects) {
        delegate.followRedirects(followRedirects);
        return this;
    }

    public JsoupConnection a(int timeoutMs) {
        delegate.timeout(timeoutMs);
        return this;
    }

    public e e() throws IOException {
        formData.forEach(delegate::data);
        org.jsoup.Connection.Response response = delegate.execute();
        return new e(response);
    }

    public Document c() throws IOException {
        return new Document(delegate.get());
    }

    public enum c {
        GET, POST
    }

    public static class d {
        private final Map<String, String> cookies = new HashMap<>();

        public Map<String, String> e() {
            return cookies;
        }
    }

    public static class e {
        private final org.jsoup.Connection.Response response;

        e(org.jsoup.Connection.Response response) {
            this.response = response;
        }

        public Map<String, String> cookies() {
            return response.cookies();
        }

        public String k() {
            return response.body();
        }

        public Document j() throws IOException {
            return new Document(response.parse());
        }
    }
}
