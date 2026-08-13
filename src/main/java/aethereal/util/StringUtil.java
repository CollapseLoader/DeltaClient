package aethereal.util;

import aethereal.lib.log4j.SoftPool;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

public class StringUtil {
    static final String[] a = {"", StringUtils.a, "  ", "   ", "    ", "     ", "      ", "       ", "        ", "         ", "          ", "           ", "            ", "             ", "              ", "               ", "                ", "                 ", "                  ", "                   ", "                    "};
    private static final int e = 1024;
    private static final int f = 8192;
    private static final Pattern b = Pattern.compile("^/(?>(?>\\.\\.?/)+)");
    private static final Pattern c = Pattern.compile("^[a-zA-Z][a-zA-Z0-9+-.]*:");
    private static final Pattern d = Pattern.compile("[\\x00-\\x1f]*");
    private static final SoftPool<StringBuilder> g = new SoftPool<>(() -> {
        return new StringBuilder(1024);
    });

    public static String a(Collection<?> strings, String sep) {
        return a(strings.iterator(), sep);
    }

    public static String a(Iterator<?> strings, String sep) {
        if (!strings.hasNext()) {
            return "";
        }
        String start = strings.next().toString();
        if (!strings.hasNext()) {
            return start;
        }
        a j = new a(sep);
        j.a(start);
        while (strings.hasNext()) {
            j.a(strings.next());
        }
        return j.a();
    }

    public static String a(String[] strings, String sep) {
        return a(Arrays.asList(strings), sep);
    }

    public static String a(int width) {
        return a(width, 30);
    }

    public static String a(int width, int maxPaddingWidth) {
        Validate_2.a(width >= 0, "width must be >= 0");
        Validate_2.a(maxPaddingWidth >= -1);
        if (maxPaddingWidth != -1) {
            width = Math.min(width, maxPaddingWidth);
        }
        if (width < a.length) {
            return a[width];
        }
        char[] out = new char[width];
        for (int i = 0; i < width; i++) {
            out[i] = ' ';
        }
        return String.valueOf(out);
    }

    public static boolean a(String string) {
        if (string == null || string.isEmpty()) {
            return true;
        }
        int l = string.length();
        for (int i = 0; i < l; i++) {
            if (!b(string.codePointAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean b(String string) {
        return string != null && !string.isEmpty() && string.charAt(0) == '\n';
    }

    public static boolean c(String string) {
        if (string == null || string.isEmpty()) {
            return false;
        }
        int l = string.length();
        for (int i = 0; i < l; i++) {
            if (!Character.isDigit(string.codePointAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean b(int c2) {
        return c2 == 32 || c2 == 9 || c2 == 10 || c2 == 12 || c2 == 13;
    }

    public static boolean c(int c2) {
        return c2 == 32 || c2 == 9 || c2 == 10 || c2 == 12 || c2 == 13 || c2 == 160;
    }

    public static boolean d(int c2) {
        return c2 == 8203 || c2 == 173;
    }

    public static String d(String string) {
        StringBuilder sb = a();
        a(sb, string, false);
        return a(sb);
    }

    public static void a(StringBuilder accum, String string, boolean stripLeading) {
        boolean lastWasWhite = false;
        boolean reachedNonWhite = false;
        int len = string.length();
        int iCharCount = 0;
        while (true) {
            int i = iCharCount;
            if (i < len) {
                int c2 = string.codePointAt(i);
                if (c(c2)) {
                    if ((!stripLeading || reachedNonWhite) && !lastWasWhite) {
                        accum.append(' ');
                        lastWasWhite = true;
                    }
                } else if (!d(c2)) {
                    accum.appendCodePoint(c2);
                    lastWasWhite = false;
                    reachedNonWhite = true;
                }
                iCharCount = i + Character.charCount(c2);
            } else {
                return;
            }
        }
    }

    public static boolean a(String needle, String... haystack) {
        for (String str : haystack) {
            if (str.equals(needle)) {
                return true;
            }
        }
        return false;
    }

    public static boolean b(String needle, String[] haystack) {
        return Arrays.binarySearch(haystack, needle) >= 0;
    }

    public static boolean e(String string) {
        Validate_2.a(string);
        for (int i = 0; i < string.length(); i++) {
            int c2 = string.charAt(i);
            if (c2 > 127) {
                return false;
            }
        }
        return true;
    }

    public static URL a(URL base, String relUrl) throws MalformedURLException {
        String relUrl2 = g(relUrl);
        if (relUrl2.startsWith("?")) {
            relUrl2 = base.getPath() + relUrl2;
        }
        URL url = new URL(base, relUrl2);
        String fixedFile = b.matcher(url.getFile()).replaceFirst("/");
        if (url.getRef() != null) {
            fixedFile = fixedFile + "#" + url.getRef();
        }
        return new URL(url.getProtocol(), url.getHost(), url.getPort(), fixedFile);
    }

    public static String a(String baseUrl, String relUrl) {
        String baseUrl2 = g(baseUrl);
        String relUrl2 = g(relUrl);
        try {
            try {
                URL base = new URL(baseUrl2);
                return a(base, relUrl2).toExternalForm();
            } catch (MalformedURLException e2) {
                URL abs = new URL(relUrl2);
                return abs.toExternalForm();
            }
        } catch (MalformedURLException e3) {
            return c.matcher(relUrl2).find() ? relUrl2 : "";
        }
    }

    private static String g(String input) {
        return d.matcher(input).replaceAll("");
    }

    public static StringBuilder a() {
        return g.a();
    }

    public static String a(StringBuilder sb) {
        Validate_2.a(sb);
        String string = sb.toString();
        if (sb.length() <= 8192) {
            sb.delete(0, sb.length());
            g.a(sb);
        }
        return string;
    }

    public static java.util.stream.Collector<CharSequence, ?, String> f(String delimiter) {
        return java.util.stream.Collector.of(() -> {
            return new a(delimiter);
        }, (v0, v1) -> {
            v0.a(v1);
        }, (j1, j2) -> {
            j1.b(j2.a());
            return j1;
        }, (v0) -> {
            return v0.a();
        }, new java.util.stream.Collector.Characteristics[0]);
    }

    public static class a {
        final String b;
        StringBuilder a = StringUtil.a();
        boolean c = true;

        public a(String separator) {
            this.b = separator;
        }

        public a a(Object stringy) {
            Validate_2.a(this.a);
            if (!this.c) {
                this.a.append(this.b);
            }
            this.a.append(stringy);
            this.c = false;
            return this;
        }

        public a b(Object stringy) {
            Validate_2.a(this.a);
            this.a.append(stringy);
            return this;
        }

        public String a() {
            String string = StringUtil.a(this.a);
            this.a = null;
            return string;
        }
    }
}
