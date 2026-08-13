package aethereal.lib.jsoup;

import org.jsoup.select.Elements;

import java.util.function.Consumer;

public final class Element {
    private final org.jsoup.nodes.Element delegate;

    Element(org.jsoup.nodes.Element delegate) {
        this.delegate = delegate;
    }

    public String ac() {
        return delegate.text();
    }

    public String af() {
        return delegate.ownText();
    }

    public String c() {
        return delegate.text();
    }

    public String a_(String attributeKey) {
        return delegate.attr(attributeKey);
    }

    public boolean b_(String attributeKey) {
        return delegate.hasAttr(attributeKey);
    }

    public Element k(String cssQuery) {
        org.jsoup.nodes.Element element = delegate.selectFirst(cssQuery);
        return element == null ? null : new Element(element);
    }

    public Element j(String cssQuery) {
        return k(cssQuery);
    }

    public Elements select(String cssQuery) {
        return delegate.select(cssQuery);
    }

    public void forEach(Consumer<? super org.jsoup.nodes.Element> action) {
        delegate.select("input[type=hidden]").forEach(action);
    }
}
