package aethereal.core;


import lombok.Generated;

public enum Category {
    Combat("V"),
    Movement("I"),
    Render("t"),
    Player("L"),
    Misc("D");

    private final String f;

    Category(String icon) {
        this.f = icon;
    }

    @Generated
    public String a() {
        return this.f;
    }
}
