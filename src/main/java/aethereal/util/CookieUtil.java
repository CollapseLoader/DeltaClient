package aethereal.util;


import aethereal.lib.jsoup.Connection_2;
import aethereal.lib.jsoup.HttpConnection;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class CookieUtil {
    private static final Map<String, List<String>> a = Collections.unmodifiableMap(new HashMap());
    private static final String b = "; ";
    private static final String c = "Cookie";
    private static final String d = "Cookie2";

    CookieUtil() {
    }

    static void a(HttpConnection.c req, HttpURLConnection con) throws IOException {
        Set<String> cookieSet = a(req);
        Set<String> cookies2 = null;
        Map<String, List<String>> storedCookies = req.r().get(a(req.a()), a);
        for (Map.Entry<String, List<String>> entry : storedCookies.entrySet()) {
            List<String> cookies = entry.getValue();
            if (cookies != null && !cookies.isEmpty()) {
                String key = entry.getKey();
                if (c.equals(key)) {
                    cookieSet.addAll(cookies);
                } else if (d.equals(key)) {
                    if (cookies2 == null) {
                        cookies2 = new HashSet<>();
                    }
                    cookies2.addAll(cookies);
                }
            }
        }
        if (!cookieSet.isEmpty()) {
            con.addRequestProperty(c, StringUtil.a(cookieSet, b));
        }
        if (cookies2 != null && !cookies2.isEmpty()) {
            con.addRequestProperty(d, StringUtil.a(cookies2, b));
        }
    }

    private static LinkedHashSet<String> a(HttpConnection.c req) {
        return new LinkedHashSet<>();
    }

    private static LinkedHashSet<String> a(Connection_2.d req) {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        for (Map.Entry<String, String> cookie : req.e().entrySet()) {
            set.add(cookie.getKey() + "=" + cookie.getValue());
        }
        return set;
    }

    static URI a(URL url) throws IOException {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            MalformedURLException ue = new MalformedURLException(e.getMessage());
            ue.initCause(e);
            throw ue;
        }
    }

    static void a(HttpConnection.c req, HttpConnection.d res, URL url, Map<String, List<String>> resHeaders) throws IOException {
        CookieManager manager = req.r();
        URI uri = a(url);
        manager.put(uri, resHeaders);
        for (Map.Entry<String, List<String>> entry : resHeaders.entrySet()) {
            String name = entry.getKey();
            List<String> values = entry.getValue();
            if (name.equalsIgnoreCase("Set-Cookie")) {
                for (String value : values) {
                    if (value != null) {
                        TokenQueue cd = new TokenQueue(value);
                        String cookieName = cd.g("=").trim();
                        String cookieVal = cd.e(";").trim();
                        res.d(cookieName, cookieVal);
                    }
                }
            }
        }
    }
}
