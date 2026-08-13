package aethereal.lib.jsoup;

public final class Jsoup {
    private Jsoup() {
    }

    public static Document a(String html) {
        return new Document(org.jsoup.Jsoup.parse(html));
    }

    public static Connection_2 b(String url) {
        return Connection_2.b(url);
    }
}
