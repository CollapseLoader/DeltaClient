package aethereal.cosmetic;

import aethereal.core.Interface;
import lombok.Generated;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.loading.object.BakedAnimations;
import software.bernie.geckolib.renderer.GeoObjectRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtil;

import java.util.UUID;

public class Cosmetic implements GeoAnimatable {
    private final AnimatableInstanceCache cache;
    private final GeoObjectRenderer<Cosmetic> renderer;
    private final String name;
    private final UUID uuid;
    private final CosmeticsType type;
    private final CosmeticsCategory category;
    private final Vector3f offset;
    private final float scale;
    private final BakedGeoModel bakedModel;
    private final BakedAnimations bakedAnimations;
    private final Identifier image;

    public Cosmetic(String name, UUID uuid, CosmeticsCategory category, float scale, Vector3f offset, BakedGeoModel bakedModel, BakedAnimations bakedAnimations, NativeImage image) {
        this.cache = GeckoLibUtil.createInstanceCache(this);
        this.name = name;
        this.uuid = uuid;
        this.type = CosmeticsType.COSMETIC;
        this.category = category;
        this.offset = offset;
        this.scale = scale;
        this.bakedModel = bakedModel;
        this.bakedAnimations = bakedAnimations;
        this.image = Identifier.of("delta", "cosmetics/" + name);
        this.renderer = new GeoObjectRenderer<>(new CosmeticsGeoModel(this));
        Interface.mc.getTextureManager().registerTexture(this.image, new NativeImageBackedTexture(image));
    }

    public Cosmetic(String name, UUID uuid, BakedAnimations bakedAnimations) {
        this.cache = GeckoLibUtil.createInstanceCache(this);
        this.name = name;
        this.uuid = uuid;
        this.type = CosmeticsType.EMOTION;
        this.category = null;
        this.offset = new Vector3f(0.0f, 0.0f, 0.0f);
        this.scale = 1.0f;
        this.bakedModel = null;
        this.bakedAnimations = bakedAnimations;
        this.image = null;
        this.renderer = null;
    }

    @Generated
    public AnimatableInstanceCache getCache() {
        return this.cache;
    }

    @Generated
    public GeoObjectRenderer<Cosmetic> getRenderer() {
        return this.renderer;
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public UUID getUuid() {
        return this.uuid;
    }

    @Generated
    public CosmeticsType getType() {
        return this.type;
    }

    @Generated
    public CosmeticsCategory getCategory() {
        return this.category;
    }

    @Generated
    public Vector3f getOffset() {
        return this.offset;
    }

    @Generated
    public float getScale() {
        return this.scale;
    }

    @Generated
    public BakedGeoModel getBakedModel() {
        return this.bakedModel;
    }

    @Generated
    public BakedAnimations getBakedAnimations() {
        return this.bakedAnimations;
    }

    @Generated
    public Identifier getImage() {
        return this.image;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        if (this.bakedAnimations != null && !this.bakedAnimations.animations().isEmpty()) {
            controllers.add(new AnimationController<>(this, "main", 0, state -> {
                return state.setAndContinue(RawAnimation.begin().thenLoop(this.bakedAnimations.animations().keySet().iterator().next()));
            }));
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public double getTick(Object entity) {
        return RenderUtil.getCurrentTick();
    }
}
