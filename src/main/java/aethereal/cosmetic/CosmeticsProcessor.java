package aethereal.cosmetic;


import aethereal.config.BaseProcessor;
import aethereal.core.EventTarget;
import aethereal.event.BackendEvent;
import aethereal.network.PacketSecurity;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.loading.json.raw.Model;
import software.bernie.geckolib.loading.json.typeadapter.KeyFramesAdapter;
import software.bernie.geckolib.loading.object.BakedAnimations;
import software.bernie.geckolib.loading.object.BakedModelFactory;
import software.bernie.geckolib.loading.object.GeometryTree;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class CosmeticsProcessor extends BaseProcessor {
    private final List<Cosmetic> cosmetics = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService bootstrapper = Executors.newSingleThreadScheduledExecutor();

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void lambda$setup$0(EntityType<? extends LivingEntity> type, LivingEntityRenderer<?, ?, ?> renderer, LivingEntityFeatureRendererRegistrationCallback.RegistrationHelper helper, EntityRendererFactory.Context context) {
        if (type == EntityType.PLAYER) {
            FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> playerRenderer = (FeatureRendererContext) renderer;
            helper.register(new CosmeticsRenderer(playerRenderer));
        }
    }

    @Override

    public void setup() {
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(new LivingEntityFeatureRendererRegistrationCallback() {
            public void registerRenderers(EntityType<? extends LivingEntity> class_1299Var, LivingEntityRenderer<?, ?, ?> class_922Var, LivingEntityFeatureRendererRegistrationCallback.RegistrationHelper registrationHelper, EntityRendererFactory.Context class_5618Var) {
                CosmeticsProcessor.lambda$setup$0(class_1299Var, class_922Var, registrationHelper, class_5618Var);
            }
        });
    }

    public List<Cosmetic> getCosmetics() {
        return this.cosmetics;
    }

    public ScheduledExecutorService getBootstrapper() {
        return this.bootstrapper;
    }

    @EventTarget
    public void onBackend(BackendEvent event) {
        if (event.isReceive() && "cosmetics".equals(event.getPacket().getId())) {
            PacketSecurity security = event.getPacket().getSecurity();
            String payload = event.getPacket().getPayload();
            String action = security.extractString(payload, "action");
            String type = security.extractString(payload, "type");
            String uuid = security.extractString(payload, "uuid");
            String cosmetic = security.extractString(payload, "cosmetic");
            if (cosmetic != null && uuid != null) {
                if ("WEAR".equals(action)) {
                    if (CosmeticsType.COSMETIC.name().equals(type)) {
                        this.bootstrapper.execute(() -> {
                            registerCosmetic(security, UUID.fromString(uuid), cosmetic);
                        });
                    }
                } else if ("UNWEAR".equals(action) && CosmeticsType.COSMETIC.name().equals(type)) {
                    this.bootstrapper.execute(() -> {
                        unregisterCosmetic(UUID.fromString(uuid), CosmeticsCategory.valueOf(security.extractString(cosmetic, "category")));
                    });
                }
            }
        }
        if (event.isClose()) {
            this.cosmetics.clear();
        }
    }

    private void registerCosmetic(PacketSecurity security, UUID uuid, String cosmetic) {
        CosmeticsCategory category = CosmeticsCategory.valueOf(security.extractString(cosmetic, "category"));
        String name = security.extractString(cosmetic, "name");
        String geometry = security.extractString(cosmetic, "geometry");
        String animations = security.extractString(cosmetic, "animation");
        byte[] texture = java.util.Base64.getDecoder().decode(security.extractString(cosmetic, "texture"));
        float scale = Float.parseFloat(security.extractString(cosmetic, "scale"));
        Vector3f offset = new Vector3f(Float.parseFloat(security.extractString(cosmetic, "x")), Float.parseFloat(security.extractString(cosmetic, "y")), Float.parseFloat(security.extractString(cosmetic, "z")));
        BakedGeoModel bakedModel = BakedModelFactory.getForNamespace("delta").constructGeoModel(GeometryTree.fromModel(KeyFramesAdapter.GEO_GSON.fromJson(geometry, Model.class)));
        BakedAnimations bakedAnimations = animations == null ? null : KeyFramesAdapter.GEO_GSON.fromJson(JsonParser.parseString(animations).getAsJsonObject().getAsJsonObject("animations"), BakedAnimations.class);
        NativeImage image;
        try {
            image = NativeImage.read(new ByteArrayInputStream(texture));
        } catch (IOException e) {
            return;
        }
        mc.execute(() -> {
            register(new Cosmetic(name, uuid, category, scale, offset, bakedModel, bakedAnimations, image));
        });
    }

    public void register(Cosmetic cosmetic) {
        this.cosmetics.removeIf(existing -> {
            return existing.getType() == cosmetic.getType() && existing.getCategory() == cosmetic.getCategory() && Objects.equals(existing.getUuid(), cosmetic.getUuid());
        });
        this.cosmetics.add(cosmetic);
    }

    public void unregisterCosmetic(UUID uuid, CosmeticsCategory category) {
        this.cosmetics.removeIf(existing -> {
            return existing.getType() == CosmeticsType.COSMETIC && existing.getCategory() == category && Objects.equals(existing.getUuid(), uuid);
        });
    }

    @Override
    public void unSetup() {
        this.cosmetics.clear();
    }
}
