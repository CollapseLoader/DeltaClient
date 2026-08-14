package aethereal.discord;


public class Session {
    private String userID;
    private String userName;
    private String sessionID;
    private String goldenSeal;
    private String csrfToken;

    public void a(String userID) {
        this.userID = userID;
    }

    public void b(String userName) {
        this.userName = userName;
    }

    public void c(String sessionID) {
        this.sessionID = sessionID;
    }

    public void d(String goldenSeal) {
        this.goldenSeal = goldenSeal;
    }

    public void e(String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public String a() {
        return this.userID;
    }

    public String b() {
        return this.userName;
    }

    public String c() {
        return this.sessionID;
    }

    public String d() {
        return this.goldenSeal;
    }

    public String e() {
        return this.csrfToken;
    }
}
