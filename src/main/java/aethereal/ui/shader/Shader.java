package aethereal.ui.shader;


import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.Defines;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.Uniform;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;

public abstract class Shader {
    protected final ShaderProgramKey a;
    protected ShaderProgram b;

    public Shader(Identifier identifier, VertexFormat vertexFormat) {
        this.a = new ShaderProgramKey(identifier, vertexFormat, Defines.EMPTY);
    }

    protected abstract void b();

    public ShaderProgramKey c() {
        return this.a;
    }

    public ShaderProgram d() {
        return this.b;
    }

    public void a() {
        this.b = RenderSystem.setShader(this.a);
        b();
    }

    protected Uniform a(String name) {
        if (this.b != null) {
            return this.b.getUniform(name);
        }
        return null;
    }
}
