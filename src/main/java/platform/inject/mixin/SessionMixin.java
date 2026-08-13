package platform.inject.mixin;


import aethereal.core.Delta;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.session.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({Session.class})
public class SessionMixin {
    @ModifyReturnValue(method = {"getUsername"}, at = {@At("RETURN")})
    private String username(String original) {
        return (Delta.h() == null || Delta.h().d().h().a() == null) ? original : Delta.h().d().h().a().b();
    }
}
