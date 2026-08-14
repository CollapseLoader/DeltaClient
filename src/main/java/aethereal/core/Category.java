package aethereal.core;


public enum Category {
    Combat("V"),
    Movement("I"),
    Render("t"),
    Player("L"),
    Misc("D");

    private final String icon;

    Category(String icon) {
        this.icon = icon;
    }

    public String a() {
        return this.icon;
    }
}
