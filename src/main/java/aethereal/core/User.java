package aethereal.core;


public record User(String uid, String username, String hwid, String role, String expire, String token) {

    @Override
    public String uid() {
        return this.uid;
    }

    @Override
    public String username() {
        return this.username;
    }

    @Override
    public String hwid() {
        return this.hwid;
    }

    @Override
    public String role() {
        return this.role;
    }

    @Override
    public String expire() {
        return this.expire;
    }

    @Override
    public String token() {
        return this.token;
    }
}
