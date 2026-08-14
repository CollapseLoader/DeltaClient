package aethereal.network;


import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.util.Identifier;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class AccountConstructor {
    private String a;
    private boolean b;
    private boolean c;

    public AccountConstructor() {
    }

    public AccountConstructor(String name, boolean selected, boolean favorited) {
        this.a = name;
        this.b = selected;
        this.c = favorited;
    }

    public AccountConstructor(String name) {
        this.a = name;
        this.b = true;
    }

    public void a(String name) {
        this.a = name;
    }

    public void a(boolean selected) {
        this.b = selected;
    }

    public void b(boolean favorited) {
        this.c = favorited;
    }

    public String b() {
        return this.a;
    }

    public boolean c() {
        return this.b;
    }

    public boolean d() {
        return this.c;
    }

    public Identifier a() {
        UUID uuid = (this.a == null || this.a.isEmpty()) ? new UUID(0L, 0L) : UUID.nameUUIDFromBytes(("OfflinePlayer:" + this.a).getBytes(StandardCharsets.UTF_8));
        return DefaultSkinHelper.getSkinTextures(uuid).texture();
    }
}
