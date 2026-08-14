package aethereal.lib.jsoup;

public final class Jsoup {
    private Jsoup() {
    }

    public static Document a(String html) {
        return new Document(org.jsoup.Jsoup.parse(html));
    }

    public static JsoupConnection b(String url) {
        return JsoupConnection.b(url);
    }
}
