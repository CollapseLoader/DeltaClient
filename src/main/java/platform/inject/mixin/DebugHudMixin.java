package platform.inject.mixin;


import aethereal.core.Delta;
import aethereal.core.Interface;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Mixin({DebugHud.class})
public class DebugHudMixin implements Interface {
    @ModifyReturnValue(method = {"getLeftText"}, at = {@At("RETURN")})
    private List<String> onGetLeftText(List<String> original) {
        return replaceText(original, true);
    }

    @ModifyReturnValue(method = {"getRightText"}, at = {@At("RETURN")})
    private List<String> onGetRightText(List<String> original) {
        return replaceText(original, false);
    }

    @Unique
    private List<String> replaceText(List<String> lines, boolean hasXyz) {
        if (Delta.h().d().t().h().m()) {
            Vec3d rayEnd = mc.getEntityRenderDispatcher().camera.getPos().add(Vec3d.fromPolar(mc.getEntityRenderDispatcher().camera.getPitch(), mc.getEntityRenderDispatcher().camera.getYaw()).multiply(20.0d));
            BlockHitResult class_3965VarMethod_17742 = Objects.requireNonNull(mc.world).raycast(new RaycastContext(mc.getEntityRenderDispatcher().camera.getPos(), rayEnd, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, mc.player));
            String cameraXYZ = String.format(Locale.ROOT, "%.3f / %.5f / %.3f", Double.valueOf(mc.getEntityRenderDispatcher().camera.getPos().x), Double.valueOf(mc.getEntityRenderDispatcher().camera.getPos().y), Double.valueOf(mc.getEntityRenderDispatcher().camera.getPos().z));
            String blockLine = "—";
            if (class_3965VarMethod_17742 instanceof BlockHitResult) {
                BlockHitResult blockHit = class_3965VarMethod_17742;
                if (class_3965VarMethod_17742.getType() == HitResult.Type.BLOCK) {
                    BlockPos pos = blockHit.getBlockPos();
                    blockLine = mc.world.getBlockState(pos).getBlock().getName().getString() + " [" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]";
                }
            }
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (hasXyz && line.contains("XYZ:") && line.contains(" / ")) {
                    line = line.replaceFirst("XYZ:\\s*[\\d.-]+\\s*/\\s*[\\d.-]+\\s*/\\s*[\\d.-]+", "XYZ: " + cameraXYZ);
                }
                if (line.contains("Targeted Block")) {
                    int prefixEnd = line.indexOf(58) + 1;
                    line = line.substring(0, Math.min(prefixEnd, line.length())).trim() + " " + blockLine;
                }
                lines.set(i, line);
            }
            return lines;
        }
        return lines;
    }
}
