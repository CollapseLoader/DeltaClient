package aethereal.module.player;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.BackendEvent;
import aethereal.event.TickEvent;
import aethereal.util.ServerUtil;
import net.minecraft.block.MapColor;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@ModuleRegister(name = "Captcha Solver", description = "!! ЭТОТ МОДУЛЬ НЕ РАБОТАЕТ, ТАК КАК ОН ЗАВЯЗАН С БЭКЕНДОМ !!", category = Category.Player)
public class CaptchaSolver extends Module {
    private byte[] b;

    @EventTarget
    public void a(TickEvent event) {
        if (ServerUtil.a.c() && Delta.getInstance().f().g()) {
            if (mc.crosshairTarget instanceof BlockHitResult hit) {
                mc.world.getEntitiesByClass(ItemFrameEntity.class, new Box(hit.getBlockPos()).expand(0.5d), frame -> {
                    return frame.getHeldItemStack().getItem() instanceof FilledMapItem;
                }).stream().findFirst().ifPresent(this::a);
            }
        }
    }

    @EventTarget
    public void onBackend(BackendEvent event) {
        String code;
        Packet packet = event.getPacket();
        if (event.isReceive() && "captcha".equals(packet.getId()) && (code = packet.getSecurity().extractString(packet.getPayload(), "code")) != null) {
            mc.player.networkHandler.sendChatMessage(code);
        }
    }

    private void a(ItemFrameEntity origin) {
        int i;
        int i2;
        World world = origin.getWorld();
        Direction facing = origin.getHorizontalFacing();
        boolean alongZ = facing.getAxis() == Direction.Axis.Z;
        boolean flip = facing == Direction.NORTH || facing == Direction.EAST;
        List<ItemFrameEntity> frames = world.getEntitiesByClass(ItemFrameEntity.class, origin.getBoundingBox().expand(16.0d), frame -> {
            return frame.getHorizontalFacing() == facing && (frame.getHeldItemStack().getItem() instanceof FilledMapItem) && FilledMapItem.getMapState(frame.getHeldItemStack(), world) != null;
        });
        int minU = Integer.MAX_VALUE;
        int maxU = Integer.MIN_VALUE;
        int minV = Integer.MAX_VALUE;
        int maxV = Integer.MIN_VALUE;
        Iterator<ItemFrameEntity> it = frames.iterator();
        while (it.hasNext()) {
            BlockPos pos = it.next().getAttachedBlockPos();
            int u = alongZ ? pos.getX() : pos.getZ();
            minU = Math.min(minU, u);
            maxU = Math.max(maxU, u);
            minV = Math.min(minV, pos.getY());
            maxV = Math.max(maxV, pos.getY());
        }
        int width = ((maxU - minU) + 1) * 128;
        int height = ((maxV - minV) + 1) * 128;
        BufferedImage image = new BufferedImage(width, height, 2);
        Graphics2D graphics = image.createGraphics();
        for (ItemFrameEntity frame2 : frames) {
            ItemStack stack = frame2.getHeldItemStack();
            MapState state = FilledMapItem.getMapState(stack, world);
            if (state != null && state.colors != null && state.colors.length == 16384) {
                BufferedImage tile = new BufferedImage(128, 128, 2);
                for (int i3 = 0; i3 < state.colors.length; i3++) {
                    int raw = state.colors[i3] & 255;
                    int color = raw < 4 ? 0 : MapColor.getRenderColor(raw);
                    tile.setRGB(i3 % 128, i3 / 128, color);
                }
                BlockPos pos2 = frame2.getAttachedBlockPos();
                int u2 = alongZ ? pos2.getX() : pos2.getZ();
                if (flip) {
                    i = maxU;
                    i2 = u2;
                } else {
                    i = u2;
                    i2 = minU;
                }
                int x = (i - i2) * 128;
                int y = (maxV - pos2.getY()) * 128;
                int rotation = frame2.getRotation() & 3;
                AffineTransform transform = AffineTransform.getTranslateInstance(x, y);
                transform.rotate(Math.toRadians(((double) rotation) * 90.0d), 64.0d, 64.0d);
                graphics.drawImage(tile, transform, null);
            }
        }
        graphics.dispose();
        a(image);
    }

    private void a(BufferedImage image) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", stream);
            byte[] bytes = stream.toByteArray();
            if (!Arrays.equals(bytes, this.b)) {
                this.b = bytes;
                Delta.getInstance().f().a(false, "captcha", "bytes", java.util.Base64.getEncoder().encodeToString(bytes));
            }
        } catch (Exception e) {
        }
    }
}
