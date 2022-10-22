package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.BlockChangeEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;

public class TrapChestESP {

    private final HashSet checked = new HashSet();
    private final HashSet chests = new HashSet();
    private boolean isScanning = false;
    private BlockPos lastChecked = null;

    private static boolean isEnabled() {
        return MinecraftUtils.getPlayer() != null && MinecraftUtils.getWorld() != null && Configs.TrapChestESP && SkyblockUtils.isInDungeon();
    }

    @SubscribeEvent
    public void onBlockChange(BlockChangeEvent var1) {
        if (var1.newBlock.getBlock() == Blocks.air) {
            this.chests.remove(var1.position);
        }

        if (var1.oldBlock.getBlock() == Blocks.air && var1.newBlock.getBlock() == Blocks.trapped_chest) {
            this.chests.add(var1.position);
        }

    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (isEnabled() && !this.isScanning && (this.lastChecked == null || !this.lastChecked.equals(MinecraftUtils.getPlayer().getPosition()))) {
            this.isScanning = true;
            (new Thread(() -> {
                BlockPos var1 = MinecraftUtils.getPlayer().getPosition();
                this.lastChecked = var1;

                for (int var2 = var1.getX() - 15; var2 < var1.getX() + 15; ++var2) {
                    for (int var3 = var1.getY() - 15; var3 < var1.getY() + 15; ++var3) {
                        for (int var4 = var1.getZ() - 15; var4 < var1.getZ() + 15; ++var4) {
                            BlockPos var5 = new BlockPos(var2, var3, var4);
                            if (!this.checked.contains(var5) && BlockUtils.getBlockAt(var5) == Blocks.trapped_chest) {
                                synchronized (this.chests) {
                                    this.chests.add(var5);
                                }
                            }
                        }
                    }
                }

                this.isScanning = false;
            })).start();
        }

    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent var1) {
        if (isEnabled()) {
            synchronized (this.chests) {
                Iterator var3 = this.chests.iterator();

                while (var3.hasNext()) {
                    BlockPos var4 = (BlockPos) var3.next();
                    Color var5 = new Color(255, 0, 0);
                    GuiUtils.enableESP();
                    GuiUtils.drawBoxAtBlock(var4.getX(), var4.getY(), var4.getZ(), var5.getRed(), var5.getGreen(), var5.getBlue(), var5.getAlpha(), 1, 1, 0.0F);
                    GuiUtils.disableESP();
                }
            }
        }

    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load var1) {
        this.chests.clear();
        this.checked.clear();
        this.lastChecked = null;
    }
}
