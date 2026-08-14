package aethereal.discord;



public class Session {
    private String a;
    private String b;
    private String c;
    private String d;
    private String e;

    public void a(String userID) {
        this.a = userID;
    }

    public void b(String userName) {
        this.b = userName;
    }

    public void c(String sessionID) {
        this.c = sessionID;
    }

    public void d(String goldenSeal) {
        this.d = goldenSeal;
    }

    public void e(String csrfToken) {
        this.e = csrfToken;
    }

    public String a() {
        return this.a;
    }

    public String b() {
        return this.b;
    }

    public String c() {
        return this.c;
    }

    public String d() {
        return this.d;
    }

    public String e() {
        return this.e;
    }
}
