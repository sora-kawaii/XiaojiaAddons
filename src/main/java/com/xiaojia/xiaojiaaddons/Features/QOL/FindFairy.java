package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Vector3i;
import com.xiaojia.xiaojiaaddons.Features.Skills.GemstoneESP;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Image;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import javax.vecmath.Vector2f;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FindFairy {

   private static int changed = 0;

   private Thread scanThread = null;

   private static int r = 0;

   private final KeyBind keyBind = new KeyBind("Find Fairy", 0);

   private static Vector3i v = null;

   private boolean should = false;

   public static Image defaultIcon = new Image("defaultPlayerIcon.png");

   private static final HashSet blockSet = new HashSet();

   private static BufferedImage map = new BufferedImage(78, 78, 6);

   public static void checkBlock(IBlockState var0, BlockPos var1) {
      int var2 = var1.getX();
      int var3 = var1.getZ();
      if (var2 < 462 || var2 > 562 || var3 < 462 || var3 > 562) {
         if (var0.getBlock() == Blocks.stained_glass || var0.getBlock() == Blocks.stained_glass_pane) {
            EnumDyeColor var4 = (EnumDyeColor)var0.getValue(BlockStainedGlass.COLOR);
            if (var4 == null) {
               var4 = (EnumDyeColor)var0.getValue(BlockStainedGlassPane.COLOR);
            }

            if (var4 == GemstoneESP.Gemstone.JASPER.dyeColor) {
               v = new Vector3i(var1.getX(), var1.getY(), var1.getZ());
               ChatLib.chat("Found jasper gemstone!");
            }
         }

      }
   }

   @SubscribeEvent
   public void onRenderOverlay(RenderGameOverlayEvent.Pre var1) {
      if (this.enabled()) {
         if (var1.type == ElementType.TEXT) {
            RenderUtils.start();
            RenderUtils.drawRect((new Color(0.0F, 0.0F, 0.0F, (float)Configs.CHBackgroundAlpha / 255.0F)).getRGB(), Configs.CHMapX, Configs.CHMapY, 25 * Configs.CHMapScale, 25 * Configs.CHMapScale);
            if (v != null) {
               map.setRGB((v.x - 202) / 8, (v.z - 202) / 8, (new Color(214, 15, 150)).getRGB());
            }

            RenderUtils.drawImage(new Image(map), (double)Configs.CHMapX, (double)Configs.CHMapY, (double)(25 * Configs.CHMapScale), (double)(25 * Configs.CHMapScale));
            int var2 = MathUtils.floor(MathUtils.getX(MinecraftUtils.getPlayer()));
            int var3 = MathUtils.floor(MathUtils.getZ(MinecraftUtils.getPlayer()));
            Vector2f var4 = new Vector2f((float)(Configs.CHMapScale * Configs.CHHeadScale) * 0.02F, (float)(Configs.CHMapScale * Configs.CHHeadScale) * 0.02F);
            RenderUtils.translate((float)Configs.CHMapX + (float)(var2 - 202) / 8.0F * ((float)(Configs.CHMapScale * 25) / 78.0F), (float)Configs.CHMapY + (float)(var3 - 202) / 8.0F * ((float)(Configs.CHMapScale * 25) / 78.0F));
            RenderUtils.translate(var4.x / 2.0F, var4.y / 2.0F);
            RenderUtils.rotate(MathUtils.getYaw() + 180.0F);
            RenderUtils.translate(-var4.x / 2.0F, -var4.y / 2.0F);
            RenderUtils.drawImage(defaultIcon, 0.0, 0.0, (double)var4.x, (double)var4.y);
            RenderUtils.end();
         }
      }
   }

   public static String getMessage() {
      return "r " + r + ", sz " + blockSet.size() + ", c " + changed;
   }

   public static String getBlock() {
      return v == null ? "&cNone" : String.format("&aFOUND! %d %d %d", v.x, v.y, v.z);
   }

   private boolean enabled() {
      return Checker.enabled && this.should && SkyblockUtils.isInCrystalHollows();
   }

   private void check(int var1, int var2, int var3) {
      new Vector3i(var1, var2, var3);
      BlockPos var5 = new BlockPos(var1, var2, var3);
      IBlockState var6 = BlockUtils.getBlockStateAt(var5);
      if (var6 != null && !BlockUtils.isBlockAir((float)var1, (float)var2, (float)var3)) {
         map.setRGB((var1 - 202) / 8, (var3 - 202) / 8, -1);
         checkBlock(var6, var5);
      }
   }

   @SubscribeEvent
   public void onTick(TickEndEvent event) {
      if (this.keyBind.isPressed()) {
         this.should = !this.should;
         ChatLib.chat(this.should ? "Find Fairy &aactivated" : "Find Fairy &cdeactivated");
      }

      if (this.enabled() && v == null) {
         if (this.scanThread == null || !this.scanThread.isAlive()) {
            this.scanThread = new Thread(() -> {
               label83:
               while(true) {
                  try {
                     if (this.enabled() && v == null) {
                        int var1 = MathUtils.floor(MathUtils.getX(MinecraftUtils.getPlayer()));
                        int var2 = MathUtils.floor(MathUtils.getZ(MinecraftUtils.getPlayer()));
                        r = 0;

                        while(true) {
                           if (r >= 400) {
                              continue label83;
                           }

                           for(int var3 = 0; var3 < 4; ++var3) {
                              int var4 = 40;

                              while(var4 <= 130) {
                                 if (this.enabled() && v == null) {
                                    int var5 = MathUtils.floor(MathUtils.getX(MinecraftUtils.getPlayer()));
                                    int var6 = MathUtils.floor(MathUtils.getZ(MinecraftUtils.getPlayer()));
                                    if (Math.abs(var5 - var1) <= 10 && Math.abs(var6 - var2) <= 10) {
                                       int var7;
                                       for(var7 = var1 - r; var7 <= var1 + r; ++var7) {
                                          this.check(var7, var4, var2 - r);
                                          this.check(var7, var4, var2 + r);
                                       }

                                       for(var7 = var2 - r; var7 <= var2 + r; ++var7) {
                                          this.check(var1 - r, var4, var7);
                                          this.check(var1 + r, var4, var7);
                                       }

                                       var4 += 2;
                                       continue;
                                    }

                                    return;
                                 }

                                 return;
                              }
                           }

                           ++r;
                        }
                     }
                  } catch (Exception var8) {
                     var8.printStackTrace();
                  }

                  return;
               }
            });
            this.scanThread.start();
         }

      }
   }

   @SubscribeEvent
   public void onWorldLoad(WorldEvent.Load var1) {
      blockSet.clear();
      v = null;
      changed = 0;
      map = new BufferedImage(78, 78, 6);
   }

   @SubscribeEvent
   public void onRenderWorld(RenderWorldLastEvent var1) {
      if (v != null) {
         GuiUtils.enableESP();
         GuiUtils.drawBoxAtBlock(v.x, v.y, v.z, 255, 255, 255, 255, 1, 1, 0.002F);
         GuiUtils.disableESP();
      }

   }
}
