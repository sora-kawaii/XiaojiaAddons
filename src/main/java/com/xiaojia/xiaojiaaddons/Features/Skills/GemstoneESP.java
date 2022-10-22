package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.BlockChangeEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GemstoneESP {

    private final ConcurrentHashMap gemstones = new ConcurrentHashMap();

    private final HashSet checked = new HashSet();

    private BlockPos lastChecked = null;

    private boolean isScanning = false;

    private static boolean isEnabled() {
        return MinecraftUtils.getPlayer() != null && MinecraftUtils.getWorld() != null && Configs.GemstoneESP && SkyblockUtils.isInCrystalHollows();
    }

    private static Gemstone getGemstone(IBlockState var0) {
        if (var0.getBlock() == Blocks.stained_glass || Configs.IncludeGlassPanes && var0.getBlock() == Blocks.stained_glass_pane) {
            EnumDyeColor var1 = var0.getValue(BlockStainedGlass.COLOR);
            if (var1 == null) {
                var1 = var0.getValue(BlockStainedGlassPane.COLOR);
            }

            if (var1 == GemstoneESP.Gemstone.RUBY.dyeColor) {
                return GemstoneESP.Gemstone.RUBY;
            } else if (var1 == GemstoneESP.Gemstone.AMETHYST.dyeColor) {
                return GemstoneESP.Gemstone.AMETHYST;
            } else if (var1 == GemstoneESP.Gemstone.JADE.dyeColor) {
                return GemstoneESP.Gemstone.JADE;
            } else if (var1 == GemstoneESP.Gemstone.SAPPHIRE.dyeColor) {
                return GemstoneESP.Gemstone.SAPPHIRE;
            } else if (var1 == GemstoneESP.Gemstone.AMBER.dyeColor) {
                return GemstoneESP.Gemstone.AMBER;
            } else if (var1 == GemstoneESP.Gemstone.TOPAZ.dyeColor) {
                return GemstoneESP.Gemstone.TOPAZ;
            } else {
                return var1 == GemstoneESP.Gemstone.JASPER.dyeColor ? GemstoneESP.Gemstone.JASPER : null;
            }
        } else {
            return null;
        }
    }

    private static boolean isGemstoneEnabled(Gemstone var0) {
        switch (var0) {
            case RUBY:
                return Configs.RubyEsp;
            case AMETHYST:
                return Configs.AmethystEsp;
            case JADE:
                return Configs.JadeEsp;
            case SAPPHIRE:
                return Configs.SapphireEsp;
            case AMBER:
                return Configs.AmberEsp;
            case TOPAZ:
                return Configs.TopazEsp;
            case JASPER:
                return Configs.JasperEsp;
            default:
                return false;
        }
    }

    @SubscribeEvent
    public void onBlockChange(BlockChangeEvent var1) {
        if (isEnabled()) {
            if (var1.newBlock.getBlock() == Blocks.air) {
                this.gemstones.remove(var1.position);
            }

            if (var1.oldBlock.getBlock() == Blocks.air) {
                Gemstone var2 = getGemstone(var1.newBlock);
                if (var2 != null) {
                    this.gemstones.put(var1.position, var2);
                }
            }

        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent var1) {
        if (isEnabled()) {
            Iterator var2 = this.gemstones.entrySet().iterator();

            while (var2.hasNext()) {
                Map.Entry var3 = (Map.Entry) var2.next();
                if (isGemstoneEnabled((Gemstone) var3.getValue())) {
                    double var4 = Math.sqrt(((BlockPos) var3.getKey()).distanceSq(MathUtils.getX(MinecraftUtils.getPlayer()), MathUtils.getY(MinecraftUtils.getPlayer()), MathUtils.getZ(MinecraftUtils.getPlayer())));
                    if (var4 <= (double) (Configs.GemstoneRadius + 2)) {
                        int var6 = (int) Math.abs(100.0 - var4 / (double) Configs.GemstoneRadius * 100.0);
                        Color var7 = ((Gemstone) var3.getValue()).color;
                        GuiUtils.enableESP();
                        BlockPos var8 = (BlockPos) var3.getKey();
                        GuiUtils.drawBoxAtBlock(var8.getX(), var8.getY(), var8.getZ(), var7.getRed(), var7.getGreen(), var7.getBlue(), var6, 1, 1, 0.0F);
                        GuiUtils.disableESP();
                    }
                }
            }
        }

    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load var1) {
        this.gemstones.clear();
        this.checked.clear();
        this.lastChecked = null;
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (isEnabled() && !this.isScanning && (this.lastChecked == null || !this.lastChecked.equals(MinecraftUtils.getPlayer().getPosition()))) {
            this.isScanning = true;
            (new Thread(() -> {
                BlockPos var1 = MinecraftUtils.getPlayer().getPosition();
                this.lastChecked = var1;

                for (int var2 = var1.getX() - Configs.GemstoneRadius; var2 < var1.getX() + Configs.GemstoneRadius; ++var2) {
                    for (int var3 = var1.getY() - Configs.GemstoneRadius; var3 < var1.getY() + Configs.GemstoneRadius; ++var3) {
                        for (int var4 = var1.getZ() - Configs.GemstoneRadius; var4 < var1.getZ() + Configs.GemstoneRadius; ++var4) {
                            BlockPos var5 = new BlockPos(var2, var3, var4);
                            if (!this.checked.contains(var5) && !MinecraftUtils.getWorld().isAirBlock(var5)) {
                                Gemstone var6 = getGemstone(MinecraftUtils.getWorld().getBlockState(var5));
                                if (var6 != null) {
                                    this.gemstones.put(var5, var6);
                                }
                            }

                            this.checked.add(var5);
                        }
                    }
                }

                this.isScanning = false;
            })).start();
        }

    }

    public enum Gemstone {

        RUBY(new Color(188, 3, 29), EnumDyeColor.RED),

        AMETHYST(new Color(137, 0, 201), EnumDyeColor.PURPLE),

        JADE(new Color(157, 249, 32), EnumDyeColor.LIME),

        SAPPHIRE(new Color(60, 121, 224), EnumDyeColor.LIGHT_BLUE),

        AMBER(new Color(237, 139, 35), EnumDyeColor.ORANGE),

        TOPAZ(new Color(249, 215, 36), EnumDyeColor.YELLOW),

        JASPER(new Color(214, 15, 150), EnumDyeColor.MAGENTA);


        public Color color;

        public EnumDyeColor dyeColor;

        Gemstone(Color var3, EnumDyeColor var4) {
            this.color = var3;
            this.dyeColor = var4;
        }
    }
}
