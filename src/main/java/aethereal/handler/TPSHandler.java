package aethereal.handler;

import aethereal.core.EventTarget;
import aethereal.event.PacketEvent;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;


public class TPSHandler extends BaseHandler {
    private long a = -1;
    private long b = -1;
    private float c = 20.0f;

    public float a() {
        return this.c;
    }

    @EventTarget
    public void a(PacketEvent event) {
        if (event.isReceive()) {
            WorldTimeUpdateS2CPacket class_2761VarD = (WorldTimeUpdateS2CPacket) event.getPacket();
            if (class_2761VarD instanceof WorldTimeUpdateS2CPacket) {
                WorldTimeUpdateS2CPacket packet = class_2761VarD;
                long now = System.currentTimeMillis();
                long worldTime = packet.time();
                if (this.b > 0 && worldTime > this.a) {
                    long ticksDelta = worldTime - this.a;
                    long msDelta = now - this.b;
                    this.c = Math.min(20.0f, (ticksDelta * 1000.0f) / msDelta);
                }
                this.b = now;
                this.a = worldTime;
            }
        }
    }
}
