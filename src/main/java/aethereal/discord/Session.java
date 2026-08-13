package aethereal.discord;


import lombok.Generated;

public class Session {
    private String a;
    private String b;
    private String c;
    private String d;
    private String e;

    @Generated
    public void a(String userID) {
        this.a = userID;
    }

    @Generated
    public void b(String userName) {
        this.b = userName;
    }

    @Generated
    public void c(String sessionID) {
        this.c = sessionID;
    }

    @Generated
    public void d(String goldenSeal) {
        this.d = goldenSeal;
    }

    @Generated
    public void e(String csrfToken) {
        this.e = csrfToken;
    }

    @Generated
    public String a() {
        return this.a;
    }

    @Generated
    public String b() {
        return this.b;
    }

    @Generated
    public String c() {
        return this.c;
    }

    @Generated
    public String d() {
        return this.d;
    }

    @Generated
    public String e() {
        return this.e;
    }
}
