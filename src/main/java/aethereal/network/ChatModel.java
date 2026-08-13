package aethereal.network;


import aethereal.util.CounterUtil;
import lombok.Generated;

public class ChatModel {
    private final String b;
    private final long c;
    private final String d;
    private final String e;
    private CounterUtil a = new CounterUtil();
    private a f;
    private String g;

    @Generated
    public ChatModel(String roomId, long room, String login, String message) {
        this.b = roomId;
        this.c = room;
        this.d = login;
        this.e = message;
    }

    @Generated
    public void a(CounterUtil counter) {
        this.a = counter;
    }

    @Generated
    public void a(a stage) {
        this.f = stage;
    }

    @Generated
    public void a(String delivery) {
        this.g = delivery;
    }

    @Generated
    public CounterUtil a() {
        return this.a;
    }

    @Generated
    public String b() {
        return this.b;
    }

    @Generated
    public long c() {
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

    @Generated
    public a f() {
        return this.f;
    }

    @Generated
    public String g() {
        return this.g;
    }

    public enum a {
        NICKNAME,
        CONFIRM_NICKNAME,
        DELIVERY
    }
}
