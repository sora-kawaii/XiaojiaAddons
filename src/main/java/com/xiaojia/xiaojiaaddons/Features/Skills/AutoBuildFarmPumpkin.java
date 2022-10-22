package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class AutoBuildFarmPumpkin {

    private static final ArrayList dirt = new ArrayList();
    private static final ArrayList light = new ArrayList();
    private static int from;
    private static int to;

    public static void setFarmingPoint(int var0, int var1) {
        float var2 = MathUtils.getX(MinecraftUtils.getPlayer());
        float var3 = MathUtils.getY(MinecraftUtils.getPlayer()) - 0.01F;
        float var4 = MathUtils.getZ(MinecraftUtils.getPlayer());
        from = var0;
        to = var1;
        dirt.clear();
        light.clear();
        BlockPos var5 = new BlockPos(var2, var3, var4);
        int var6 = var5.getX();
        int var7 = var5.getY();
        int var8 = var5.getZ();

        for (int var9 = from; var9 < to; ++var9) {
            int var10 = (var9 + 5555 - var8) % 5;
            if (var10 == 0) {
                dirt.add(new BlockPos(var6, var7, var9));
                dirt.add(new BlockPos(var6, var7 - 1, var9));
            } else if (var10 != 1 && var10 != 2) {
                if (var10 == 3) {
                    dirt.add(new BlockPos(var6, var7, var9));
                    dirt.add(new BlockPos(var6, var7 - 1, var9));
                } else {
                    light.add(new BlockPos(var6, var7 + 1, var9));
                    dirt.add(new BlockPos(var6, var7 - 1, var9));
                }
            } else {
                dirt.add(new BlockPos(var6, var7, var9));
            }
        }

        ChatLib.chat("Successfully set farming point!");
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent var1) {
        Iterator var2 = dirt.iterator();

        BlockPos var3;
        while (var2.hasNext()) {
            var3 = (BlockPos) var2.next();
            if (BlockUtils.isBlockAir(var3)) {
                GuiUtils.drawBoxAtBlock(var3, new Color(72, 50, 34, 120), 1, 1, 0.0F);
            }
        }

        var2 = light.iterator();

        while (var2.hasNext()) {
            var3 = (BlockPos) var2.next();
            if (BlockUtils.isBlockAir(var3)) {
                GuiUtils.drawBoxAtBlock(var3, new Color(60, 60, 222, 120), 1, 1, 0.0F);
            }
        }

    }

    @SubscribeEvent
    public void onChange(WorldEvent.Load var1) {
        dirt.clear();
        light.clear();
    }
}
