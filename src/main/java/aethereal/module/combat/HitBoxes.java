package aethereal.module.combat;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.BoundingBoxEvent;
import aethereal.setting.SliderSetting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

@ModuleRegister(a = "Hit Boxes", b = "Увеличивает хитбокс игроков, упрощая попадания по ним", c = Category.Combat)
public class HitBoxes extends Module {
    private final SliderSetting b = new SliderSetting("Расширение X и Z", 0.0f, 0.0f, 1.0f, 0.1f);
    private final SliderSetting c = new SliderSetting("Расширение Y", 0.0f, 0.0f, 1.0f, 0.1f);

    public HitBoxes() {
        a(this.b, this.c);
    }

    @EventTarget
    public void a(BoundingBoxEvent event) {
        PlayerEntity player = (PlayerEntity) event.c();
        if (player instanceof PlayerEntity) {
            Box box = event.b();
            Box changedBox = new Box(box.minX - ((double) (this.b.h().floatValue() / 2.0f)), box.minY, box.minZ - ((double) (this.b.h().floatValue() / 2.0f)), box.maxX + ((double) (this.b.h().floatValue() / 2.0f)), box.maxY + ((double) this.c.h().floatValue()), box.maxZ + ((double) (this.b.h().floatValue() / 2.0f)));
            if (player.getId() != aM_.player.getId() && !Delta.h().d().e().d(player.getName().getString())) {
                event.a(changedBox);
            }
        }
    }
}
