package aethereal.friend;


import lombok.Generated;

public class FriendConstructor {
    private String a;

    @Generated
    public FriendConstructor() {
    }

    @Generated
    public FriendConstructor(String name) {
        this.a = name;
    }

    @Generated
    public void a(String name) {
        this.a = name;
    }

    @Generated
    public String a() {
        return this.a;
    }
}
