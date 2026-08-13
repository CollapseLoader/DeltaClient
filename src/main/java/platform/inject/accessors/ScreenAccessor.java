package platform.inject.accessors;


import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({Screen.class})
public interface ScreenAccessor {
    @Invoker("addDrawableChild")
    <T extends Element> T invokeAddDrawableChild(T t);

    @Accessor("width")
    int getWidth();

    @Accessor("height")
    int getHeight();

    @Accessor("title")
    Text getTitle();
}
