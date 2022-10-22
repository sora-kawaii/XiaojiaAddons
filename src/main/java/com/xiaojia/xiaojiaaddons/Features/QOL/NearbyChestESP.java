package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.BlockChangeEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;
import java.util.Iterator;

public class NearbyChestESP {

    private final HashSet chests = new HashSet();

    private final HashSet checked = new HashSet();

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load var1) {
        this.chests.clear();
        this.checked.clear();
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Checker.enabled) {
            if (this.isEnabled()) {
                EntityPlayerSP var2 = MinecraftUtils.getPlayer();
                if (var2 != null) {
                    BlockPos var3 = var2.getPosition();

                    for (int var4 = var3.getX() - 10; var4 <= var3.getX() + 10; ++var4) {
                        for (int var5 = var3.getY() - 10; var5 <= var3.getY() + 10; ++var5) {
                            for (int var6 = var3.getZ() - 10; var6 <= var3.getZ() + 10; ++var6) {
                                BlockPos var7 = new BlockPos(var4, var5, var6);
                                if (!this.checked.contains(var7) && !MinecraftUtils.getWorld().isAirBlock(var7) && MinecraftUtils.getWorld().getBlockState(var7).getBlock() == Blocks.chest) {
                                    this.chests.add(var7);
                                }

                                this.checked.add(var7);
                            }
                        }
                    }

                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent var1) {
        if (Checker.enabled) {
            if (this.isEnabled()) {
                this.chests.removeIf((var0) -> {
                    return MinecraftUtils.getWorld().getBlockState((BlockPos) var0).getBlock() != Blocks.chest;
                });
                Iterator var2 = this.chests.iterator();

                while (var2.hasNext()) {
                    BlockPos var3 = (BlockPos) var2.next();
                    if (MathUtils.distanceSquareFromPlayer(var3) <= 1000.0) {
                        GuiUtils.enableESP();
                        GuiUtils.drawBoxAtBlock(var3.getX(), var3.getY(), var3.getZ(), 185, 65, 65, 100, 1, 1, 0.0F);
                        GuiUtils.disableESP();
                    }
                }

            }
        }
    }

    private boolean isEnabled() {
        return SkyblockUtils.isInCrystalHollows() && Configs.ChestESPCrystalHollows;
    }

    @SubscribeEvent
    public void onBlockChange(BlockChangeEvent var1) {
        if (Checker.enabled) {
            if (this.isEnabled()) {
                if (var1.oldBlock.getBlock() == Blocks.chest && var1.newBlock.getBlock() == Blocks.air) {
                    this.chests.remove(var1.position);
                }

                if (var1.oldBlock.getBlock() != Blocks.chest && var1.newBlock.getBlock() == Blocks.chest) {
                    this.chests.add(var1.position);
                }

            }
        }
    }
}
