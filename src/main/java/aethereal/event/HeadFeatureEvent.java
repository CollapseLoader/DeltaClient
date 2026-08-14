package aethereal.event;

import aethereal.core.Event;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;

public class HeadFeatureEvent extends Event {
    private MatrixStack matrixStack;
    private VertexConsumerProvider vertexConsumerProvider;
    private PlayerEntity player;
    private ModelWithHead model;

    public HeadFeatureEvent(MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider, PlayerEntity player, ModelWithHead model) {
        this.matrixStack = matrix;
        this.vertexConsumerProvider = vertexConsumerProvider;
        this.player = player;
        this.model = model;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof HeadFeatureEvent other)) {
            return false;
        }
        if (!other.matches(this) || !super.equals(o)) {
            return false;
        }
        Object this$matrix = getMatrixStack();
        Object other$matrix = other.getMatrixStack();
        if (this$matrix == null) {
            if (other$matrix != null) {
                return false;
            }
        } else if (!this$matrix.equals(other$matrix)) {
            return false;
        }
        Object this$vertexConsumerProvider = getVertexConsumerProvider();
        Object other$vertexConsumerProvider = other.getVertexConsumerProvider();
        if (this$vertexConsumerProvider == null) {
            if (other$vertexConsumerProvider != null) {
                return false;
            }
        } else if (!this$vertexConsumerProvider.equals(other$vertexConsumerProvider)) {
            return false;
        }
        Object this$player = getPlayer();
        Object other$player = other.getPlayer();
        if (this$player == null) {
            if (other$player != null) {
                return false;
            }
        } else if (!this$player.equals(other$player)) {
            return false;
        }
        Object this$model = getModel();
        Object other$model = other.getModel();
        if (this$model == null) {
            return other$model == null;
        }
        return this$model.equals(other$model);
    }

    protected boolean matches(Object other) {
        return other instanceof HeadFeatureEvent;
    }

    public int hashCode() {
        int result = super.hashCode();
        Object $matrix = getMatrixStack();
        int result2 = (result * 59) + ($matrix == null ? 43 : $matrix.hashCode());
        Object $vertexConsumerProvider = getVertexConsumerProvider();
        int result3 = (result2 * 59) + ($vertexConsumerProvider == null ? 43 : $vertexConsumerProvider.hashCode());
        Object $player = getPlayer();
        int result4 = (result3 * 59) + ($player == null ? 43 : $player.hashCode());
        Object $model = getModel();
        return (result4 * 59) + ($model == null ? 43 : $model.hashCode());
    }

    public String toString() {
        return "HeadFeatureEvent(matrix=" + getMatrixStack() + ", vertexConsumerProvider=" + getVertexConsumerProvider() + ", player=" + getPlayer() + ", model=" + getModel() + ")";
    }

    public MatrixStack getMatrixStack() {
        return this.matrixStack;
    }

    public void setMatrixStack(MatrixStack matrix) {
        this.matrixStack = matrix;
    }

    public VertexConsumerProvider getVertexConsumerProvider() {
        return this.vertexConsumerProvider;
    }

    public void setVertexConsumerProvider(VertexConsumerProvider vertexConsumerProvider) {
        this.vertexConsumerProvider = vertexConsumerProvider;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }

    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }

    public ModelWithHead getModel() {
        return this.model;
    }

    public void setModel(ModelWithHead model) {
        this.model = model;
    }
}
