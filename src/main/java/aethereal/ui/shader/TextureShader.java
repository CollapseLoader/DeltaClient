package aethereal.ui.shader;

import net.minecraft.client.gl.Uniform;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.util.Identifier;
import org.joml.Vector4f;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class TextureShader extends Shader {
    public Uniform c;
    public Uniform d;
    public Uniform e;

    public TextureShader() {
        super(Identifier.of("delta", "core/rect/texture_rect"), VertexFormats.POSITION_TEXTURE_COLOR);
    }

    @Override
    protected void b() {
        this.c = a("uSize");
        this.d = a("uRadius");
        this.e = a("uSmoothness");
    }

    public void a(float width, float height) {
        if (this.c != null) {
            this.c.set(width, height);
        }
    }

    public void a(Vector4f radius) {
        if (this.d != null) {
            this.d.set(radius.x, radius.z, radius.w, radius.y);
        }
    }

    public void a(float smoothness) {
        if (this.e != null) {
            this.e.set(smoothness);
        }
    }

    public Identifier b(String nickname) {
        UUID uuid;
        if (nickname == null || nickname.isEmpty()) {
            uuid = new UUID(0L, 0L);
        } else {
            uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + nickname).getBytes(StandardCharsets.UTF_8));
        }
        UUID uuid2 = uuid;
        return DefaultSkinHelper.getSkinTextures(uuid2).texture();
    }
}
