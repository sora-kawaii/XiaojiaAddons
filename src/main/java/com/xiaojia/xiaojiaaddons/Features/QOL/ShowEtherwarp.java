package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.vecmath.Vector3d;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class ShowEtherwarp {

    private static final HashSet invalidBlocks;

    static {
        invalidBlocks = new HashSet(Arrays.asList(Blocks.air, Blocks.skull, Blocks.ladder, Blocks.fire, Blocks.snow_layer, Blocks.portal, Blocks.carpet, Blocks.web, Blocks.tripwire, Blocks.redstone_wire, Blocks.tripwire_hook, Blocks.flower_pot, Blocks.red_flower, Blocks.yellow_flower, Blocks.double_plant, Blocks.torch, Blocks.redstone_torch, Blocks.unlit_redstone_torch, Blocks.vine, Blocks.reeds, Blocks.melon_stem, Blocks.pumpkin_stem, Blocks.carrots, Blocks.wheat, Blocks.potatoes, Blocks.nether_wart, Blocks.deadbush, Blocks.brown_mushroom, Blocks.red_mushroom, Blocks.cocoa, Blocks.tallgrass, Blocks.waterlily, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.powered_comparator, Blocks.powered_repeater, Blocks.powered_repeater, Blocks.unpowered_comparator, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.rail, Blocks.activator_rail, Blocks.detector_rail, Blocks.golden_rail));
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent var1) {
        if (Checker.enabled && Configs.ShowEtherwarp) {
            EntityPlayerSP var2 = MinecraftUtils.getPlayer();
            if (var2 != null && var2.isSneaking()) {
                ItemStack var3 = ControlUtils.getHeldItemStack();
                if (NBTUtils.getBooleanFromExtraAttributes(var3, "ethermerge")) {
                    int var4 = 57 + NBTUtils.getIntFromExtraAttributes(var3, "tuned_transmission");
                    Vec3 var5 = var2.getPositionEyes(var1.partialTicks);
                    Vector3d var6 = BlockUtils.getLookingAtVector(var4);
                    BlockPos var7;
                    if (this.valid(var6)) {
                        var7 = new BlockPos(var6.x, var6.y, var6.z);
                        GuiUtils.enableESP();
                        Color var8 = ColorUtils.getColorFromString(Configs.EtherwarpPointColor, new Color(0, 0, 0, 255));
                        Color var9 = ColorUtils.getColorFromString(Configs.EtherwarpPointBoundingColor, new Color(0, 0, 0, 255));
                        GuiUtils.drawSelectionFilledBoxAtBlock(var7, var8);
                        if (Configs.EtherwarpPointBoundingThickness != 0) {
                            GuiUtils.drawSelectionBoundingBoxAtBlock(var7, var9);
                        }

                        GuiUtils.disableESP();
                    }

                    if (var6 != null && Configs.ShowNearbyEtherwarp) {
                        var7 = new BlockPos(var6.x, var6.y, var6.z);
                        int var14 = Configs.NearbyEtherwarpRadius;

                        for (int var15 = -var14; var15 <= var14; ++var15) {
                            for (int var10 = -var14; var10 <= var14; ++var10) {
                                for (int var11 = -var14; var11 <= var14; ++var11) {
                                    BlockPos var12 = var7.add(var15, var10, var11);
                                    if (var12 != null) {
                                        ArrayList var13 = BlockUtils.getSurfaceMid(var5, var12);
                                        if (this.valid(new Vector3d((double) var12.getX() + 0.5, (double) var12.getY() + 0.5, (double) var12.getZ() + 0.5))) {
                                            GuiUtils.enableESP();
                                            var13.forEach((var1x) -> {
                                                if (BlockUtils.getNearestBlock(var5, ((BlockUtils.Face) var1x).mid) == null) {
                                                    Color var2_ = ColorUtils.getColorFromString(Configs.PossibleEtherwarpPointColor, new Color(0, 0, 0, 255));
                                                    GuiUtils.drawFilledFace((BlockUtils.Face) var1x, var2_);
                                                }

                                            });
                                            GuiUtils.disableESP();
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    private boolean valid(Vector3d var1) {
        if (var1 == null) {
            return false;
        } else {
            try {
                Vec3 var2 = MinecraftUtils.getPlayer().getPositionEyes(MathUtils.partialTicks);
                double var3 = MathUtils.distanceSquaredFromPoints(var1, new Vector3d(var2.xCoord, var2.yCoord, var2.zCoord));
                BlockPos var5 = new BlockPos(var1.x, var1.y, var1.z);
                Block var6 = BlockUtils.getBlockAt(var5);
                if (!(var3 < 25.0) || var6 != Blocks.chest && var6 != Blocks.ender_chest && var6 != Blocks.trapped_chest && var6 != Blocks.hopper && var6 != Blocks.crafting_table && var6 != Blocks.anvil && var6 != Blocks.enchanting_table && var6 != Blocks.furnace && var6 != Blocks.lit_furnace && var6 != Blocks.brewing_stand && var6 != Blocks.dispenser && var6 != Blocks.dropper) {
                    if (invalidBlocks.contains(var6)) {
                        return false;
                    } else {
                        for (int var7 = 1; var7 <= 2; ++var7) {
                            var5 = var5.up();
                            var6 = BlockUtils.getBlockAt(var5);
                            if (!invalidBlocks.contains(var6)) {
                                return false;
                            }
                        }

                        return true;
                    }
                } else {
                    return false;
                }
            } catch (Exception var8) {
                return false;
            }
        }
    }
}
