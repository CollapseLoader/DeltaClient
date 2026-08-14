package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;

public class HeadFeatureEvent extends Event implements IEvent {
    private MatrixStack a;
    private VertexConsumerProvider b;
    private PlayerEntity c;
    private ModelWithHead d;

    public HeadFeatureEvent(MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider, PlayerEntity player, ModelWithHead model) {
        this.a = matrix;
        this.b = vertexConsumerProvider;
        this.c = player;
        this.d = model;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof HeadFeatureEvent other)) {
            return false;
        }
        if (!other.a(this) || !super.equals(o)) {
            return false;
        }
        Object this$matrix = b();
        Object other$matrix = other.b();
        if (this$matrix == null) {
            if (other$matrix != null) {
                return false;
            }
        } else if (!this$matrix.equals(other$matrix)) {
            return false;
        }
        Object this$vertexConsumerProvider = c();
        Object other$vertexConsumerProvider = other.c();
        if (this$vertexConsumerProvider == null) {
            if (other$vertexConsumerProvider != null) {
                return false;
            }
        } else if (!this$vertexConsumerProvider.equals(other$vertexConsumerProvider)) {
            return false;
        }
        Object this$player = d();
        Object other$player = other.d();
        if (this$player == null) {
            if (other$player != null) {
                return false;
            }
        } else if (!this$player.equals(other$player)) {
            return false;
        }
        Object this$model = e();
        Object other$model = other.e();
        if (this$model == null) {
            return other$model == null;
        }
        return this$model.equals(other$model);
    }

    protected boolean a(Object other) {
        return other instanceof HeadFeatureEvent;
    }

    public int hashCode() {
        int result = super.hashCode();
        Object $matrix = b();
        int result2 = (result * 59) + ($matrix == null ? 43 : $matrix.hashCode());
        Object $vertexConsumerProvider = c();
        int result3 = (result2 * 59) + ($vertexConsumerProvider == null ? 43 : $vertexConsumerProvider.hashCode());
        Object $player = d();
        int result4 = (result3 * 59) + ($player == null ? 43 : $player.hashCode());
        Object $model = e();
        return (result4 * 59) + ($model == null ? 43 : $model.hashCode());
    }

    public void a(MatrixStack matrix) {
        this.a = matrix;
    }

    public void a(VertexConsumerProvider vertexConsumerProvider) {
        this.b = vertexConsumerProvider;
    }

    public void a(PlayerEntity player) {
        this.c = player;
    }

    public void a(ModelWithHead model) {
        this.d = model;
    }

    public String toString() {
        return "HeadFeatureEvent(matrix=" + b() + ", vertexConsumerProvider=" + c() + ", player=" + d() + ", model=" + e() + ")";
    }

    public MatrixStack b() {
        return this.a;
    }

    public VertexConsumerProvider c() {
        return this.b;
    }

    public PlayerEntity d() {
        return this.c;
    }

    public ModelWithHead e() {
        return this.d;
    }
}
