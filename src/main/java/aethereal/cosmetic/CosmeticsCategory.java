package aethereal.cosmetic;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

import java.util.function.Function;

public enum CosmeticsCategory {
    f1((PlayerEntityModel model) -> {
        return model.body;
    }),
    f2((PlayerEntityModel model2) -> {
        return model2.body;
    }),
    f3_((PlayerEntityModel model3) -> {
        return model3.head;
    }),
    f4((PlayerEntityModel model4) -> {
        return model4.head;
    }),
    f5((PlayerEntityModel model5) -> {
        return model5.body;
    }),
    f6((PlayerEntityModel model6) -> {
        return model6.body;
    });

    private final Function<PlayerEntityModel, ModelPart> part;

    CosmeticsCategory(Function<PlayerEntityModel, ModelPart> part) {
        this.part = part;
    }

    public void transform(MatrixStack matrices, PlayerEntityModel model, Cosmetic cosmetic) {
        this.part.apply(model).rotate(matrices);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0f));
        matrices.scale(cosmetic.getScale(), cosmetic.getScale(), cosmetic.getScale());
        matrices.translate(cosmetic.getOffset().x, cosmetic.getOffset().y, cosmetic.getOffset().z());
    }
}
