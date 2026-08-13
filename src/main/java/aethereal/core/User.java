package aethereal.core;


import lombok.Generated;

public record User(String uid, String username, String hwid, String role, String expire, String token) {

    @Override
    @Generated
    public String uid() {
        return this.uid;
    }

    @Override
    @Generated
    public String username() {
        return this.username;
    }

    @Override
    @Generated
    public String hwid() {
        return this.hwid;
    }

    @Override
    @Generated
    public String role() {
        return this.role;
    }

    @Override
    @Generated
    public String expire() {
        return this.expire;
    }

    @Override
    @Generated
    public String token() {
        return this.token;
    }
}
