package aethereal.cosmetic;

import net.minecraft.util.Identifier;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class CosmeticsGeoModel extends GeoModel<Cosmetic> {
    private final Cosmetic cosmetic;

    public CosmeticsGeoModel(Cosmetic cosmetic) {
        this.cosmetic = cosmetic;
    }

    @Override
    public Identifier getModelResource(Cosmetic cosmetic, GeoRenderer<Cosmetic> renderer) {
        return null;
    }

    @Override
    public Identifier getTextureResource(Cosmetic cosmetic, GeoRenderer<Cosmetic> renderer) {
        return cosmetic.getImage();
    }

    @Override
    public Identifier getAnimationResource(Cosmetic cosmetic) {
        return null;
    }

    @Override
    public BakedGeoModel getBakedModel(Identifier resource) {
        BakedGeoModel model = this.cosmetic.getBakedModel();
        getAnimationProcessor().setActiveModel(model);
        return model;
    }

    @Override
    public Animation getAnimation(Cosmetic animatable, String name) {
        if (this.cosmetic.getBakedAnimations() == null) {
            return null;
        }
        return this.cosmetic.getBakedAnimations().getAnimation(name);
    }
}
