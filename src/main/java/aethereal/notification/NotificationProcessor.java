package aethereal.notification;


import aethereal.config.BaseProcessor;
import aethereal.core.EventTarget;
import aethereal.event.DrawEvent;
import aethereal.event.TickEvent;
import aethereal.render.EasingList;
import net.minecraft.client.gui.screen.ChatScreen;

import java.util.ArrayList;
import java.util.List;

public class NotificationProcessor extends BaseProcessor {
    private final Notification b = new Notification("o", "Пример отображения уведомления", 0);
    private final List<Notification> c = new ArrayList<>();

    @Override

    public void setup() {
    }

    public Notification a() {
        return this.b;
    }

    public List<Notification> b() {
        return this.c;
    }

    @Override
    public void unSetup() {
    }

    @EventTarget
    public void a(DrawEvent event) {
        if (event.b() && !b().isEmpty()) {
            for (Notification notification : b()) {
                notification.a().a(0.0f, 1.0f, 0.3f, EasingList.g, event.g());
            }
        }
    }

    @EventTarget
    public void a(TickEvent event) {
        List<Notification> notifications = new ArrayList<>(b());
        notifications.remove(this.b);
        boolean preview = (mc.currentScreen instanceof ChatScreen) && notifications.isEmpty();
        if (!b().contains(this.b)) {
            b().add(this.b);
        }
        for (Notification notification : new ArrayList<>(b())) {
            if (notification == this.b) {
                notification.a().a(preview);
                if (!preview && notification.a().c() == 0.0f) {
                    b().remove(this.b);
                }
            } else {
                boolean finished = notification.b().a(notification.f() - 100);
                notification.a().a(!finished);
                if (finished && notification.a().c() == 0.0f) {
                    b().remove(notification);
                }
            }
        }
    }

    public void a(Notification notification) {
        int time = notification.f();
        notification.a(time + (((int) b().stream().filter(current -> {
            return current.f() >= time;
        }).filter(current2 -> {
            return (current2.f() - time) % 50 == 0;
        }).count()) * 50));
        b().add(notification);
    }
}
