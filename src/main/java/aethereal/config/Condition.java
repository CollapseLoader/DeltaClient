package aethereal.config;

import aethereal.autobuy.ItemType;
import aethereal.render.AnimationUtil;

public interface Condition {
    AnimationUtil a();

    boolean b();

    boolean c();

    boolean d();

    void a(boolean z);

    void a(int i);

    void b(int i);

    String e();

    String f();

    int g();

    ItemType h();

    void a(ItemType itemType);
}
