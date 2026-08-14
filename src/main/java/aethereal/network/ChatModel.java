package aethereal.network;


import aethereal.util.CounterUtil;

public class ChatModel {
    private final String b;
    private final long c;
    private final String d;
    private final String e;
    private CounterUtil a = new CounterUtil();
    private a f;
    private String g;

    public ChatModel(String roomId, long room, String login, String message) {
        this.b = roomId;
        this.c = room;
        this.d = login;
        this.e = message;
    }

    public void a(CounterUtil counter) {
        this.a = counter;
    }

    public void a(a stage) {
        this.f = stage;
    }

    public void a(String delivery) {
        this.g = delivery;
    }

    public CounterUtil a() {
        return this.a;
    }

    public String b() {
        return this.b;
    }

    public long c() {
        return this.c;
    }

    public String d() {
        return this.d;
    }

    public String e() {
        return this.e;
    }

    public a f() {
        return this.f;
    }

    public String g() {
        return this.g;
    }

    public enum a {
        NICKNAME,
        CONFIRM_NICKNAME,
        DELIVERY
    }
}
