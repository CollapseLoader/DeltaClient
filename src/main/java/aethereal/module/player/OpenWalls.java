package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.Delta;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import net.minecraft.block.*;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;

import java.util.Set;

@ModuleRegister(name = "Open Walls", description = "Позволяет открывать хранилища сквозь стены", category = Category.Player)
public class OpenWalls extends Module {
    private final Set<Class<?>> b = Set.of(new Class[]{AbstractChestBlock.class, FurnaceBlock.class, CraftingTableBlock.class, SpawnerBlock.class, ShulkerBoxBlock.class, AnvilBlock.class, BeaconBlock.class, BlastFurnaceBlock.class, BrewingStandBlock.class, CampfireBlock.class, CartographyTableBlock.class, GrindstoneBlock.class, LecternBlock.class, LoomBlock.class, SmokerBlock.class, StonecutterBlock.class, BarrelBlock.class});

    public Set<Class<?>> q() {
        return this.b;
    }

    public BlockHitResult a(ClientPlayerEntity player) {
        Vec3d start = player.getCameraPosVec(1.0f);
        Vec3d end = start.add(player.getRotationVec(1.0f).multiply(player.getBlockInteractionRange()));
        return player.getWorld().raycast(new RaycastContext(start, end, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player) {
            public VoxelShape getBlockShape(BlockState state, BlockView world, BlockPos pos) {
                for (Class<?> clazz : Delta.getInstance().getModuleProcessor().t().a().q()) {
                    if (clazz.isInstance(state.getBlock())) {
                        return super.getBlockShape(state, world, pos);
                    }
                }
                return VoxelShapes.empty();
            }
        });
    }
}
