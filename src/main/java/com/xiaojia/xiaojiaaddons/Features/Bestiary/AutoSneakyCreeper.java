package com.xiaojia.xiaojiaaddons.Features.Bestiary;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.vecmath.Vector2f;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoSneakyCreeper extends AutoWalk {

   private static final ArrayList positions = new ArrayList(Arrays.asList(new BlockPos(3, 152, 30), new BlockPos(-12, 153, 28), new BlockPos(-20, 154, 25), new BlockPos(-25, 154, 20), new BlockPos(-26, 153, 11), new BlockPos(-22, 154, 5), new BlockPos(-11, 153, -2), new BlockPos(5, 151, -12), new BlockPos(17, 151, -9), new BlockPos(29, 151, -5), new BlockPos(-29, 153, 2), new BlockPos(-30, 153, -8), new BlockPos(-24, 154, -17), new BlockPos(-18, 155, -24), new BlockPos(-6, 153, -35), new BlockPos(1, 154, -38), new BlockPos(8, 155, -36), new BlockPos(16, 156, -33), new BlockPos(24, 157, -35), new BlockPos(32, 158, -32), new BlockPos(37, 157, -26), new BlockPos(35, 152, -12), new BlockPos(37, 152, -2), new BlockPos(40, 151, 3), new BlockPos(41, 150, 16), new BlockPos(31, 150, 26), new BlockPos(22, 152, 31), new BlockPos(-16, 154, 3), new BlockPos(-13, 154, 8), new BlockPos(-13, 156, 12), new BlockPos(-5, 157, 17), new BlockPos(1, 158, 16), new BlockPos(5, 155, 15), new BlockPos(8, 152, 25), new BlockPos(25, 161, -24), new BlockPos(18, 164, -12), new BlockPos(13, 164, -5), new BlockPos(12, 160, 2)));

   private static final ArrayList edges = new ArrayList(Arrays.asList(new Pair(0, 1), new Pair(1, 2), new Pair(2, 3), new Pair(3, 4), new Pair(4, 5), new Pair(4, 10), new Pair(5, 27), new Pair(27, 6), new Pair(6, 7), new Pair(7, 8), new Pair(8, 9), new Pair(10, 11), new Pair(11, 12), new Pair(12, 13), new Pair(13, 14), new Pair(14, 15), new Pair(15, 16), new Pair(16, 17), new Pair(17, 18), new Pair(18, 19), new Pair(19, 20), new Pair(20, 21), new Pair(9, 22), new Pair(21, 22), new Pair(22, 23), new Pair(23, 24), new Pair(24, 25), new Pair(25, 26), new Pair(26, 0), new Pair(7, 17), new Pair(27, 28), new Pair(28, 29), new Pair(29, 30), new Pair(30, 31), new Pair(31, 32), new Pair(32, 33), new Pair(33, 0), new Pair(34, 35), new Pair(35, 36), new Pair(36, 37), new Pair(37, 32), new Pair(19, 34)));

   public ArrayList getEdges() {
      return edges;
   }

   @SubscribeEvent
   public void onRender(RenderWorldLastEvent var1) {
      if (Checker.enabled) {
         if (Configs.AutoSneakyCreeper && SkyblockUtils.isInGunpowderMines()) {
            if (this.goingTo != null) {
               if (!this.shouldShow && !Configs.DevTracing) {
                  return;
               }

               GuiUtils.enableESP();
               GuiUtils.drawBoxAtBlock(this.goingTo, new Color(60, 60, 222, 200), 1, 1, 0.002F);
               GuiUtils.drawString(this.index + "", (float)this.goingTo.getX() + 0.5F, (float)this.goingTo.getY() + 1.2F, (float)this.goingTo.getZ() + 0.5F, (new Color(0, 255, 0)).getRGB(), 0.8F, true);
               GuiUtils.disableESP();
            } else {
               if (!Configs.DevTracing) {
                  return;
               }

               GuiUtils.enableESP();
               Iterator var2 = positions.iterator();

               BlockPos var3;
               while(var2.hasNext()) {
                  var3 = (BlockPos)var2.next();
                  GuiUtils.drawBoxAtBlock(var3, new Color(60, 60, 222, 200), 1, 1, 0.002F);
               }

               var2 = edges.iterator();

               while(var2.hasNext()) {
                  Pair var9 = (Pair)var2.next();
                  int var4 = (Integer)var9.getKey();
                  int var5 = (Integer)var9.getValue();
                  BlockPos var6 = (BlockPos)positions.get(var4);
                  BlockPos var7 = (BlockPos)positions.get(var5);
                  GuiUtils.drawLine((float)var6.getX() + 0.5F, (float)var6.getY() + 0.5F, (float)var6.getZ() + 0.5F, (float)var7.getX() + 0.5F, (float)var7.getY() + 0.5F, (float)var7.getZ() + 0.5F, new Color(255, 0, 0), 2);
               }

               for(int var8 = 0; var8 < positions.size(); ++var8) {
                  var3 = (BlockPos)positions.get(var8);
                  GuiUtils.drawString(var8 + "", (float)var3.getX() + 0.5F, (float)var3.getY() + 1.2F, (float)var3.getZ() + 0.5F, (new Color(0, 255, 0)).getRGB(), 0.8F, true);
               }

               GuiUtils.disableESP();
            }

         }
      }
   }

   public ArrayList getPositions() {
      return positions;
   }

   public KeyBind getKeyBind() {
      return new KeyBind(this.getName(), 0);
   }

   public boolean enabled() {
      return Configs.AutoSneakyCreeper && SkyblockUtils.isInGunpowderMines();
   }

   private static void translateTo(double var0, double var2) {
      RenderUtils.translate((double)Configs.SNMapX + (var0 - -40.0) * (double)((float)(Configs.SNMapScale * 25) / 90.0F), (double)Configs.SNMapY + (var2 - -45.0) * (double)((float)(Configs.SNMapScale * 25) / 90.0F));
   }

   @SubscribeEvent
   public void onRenderOverlay(RenderGameOverlayEvent.Pre var1) {
      if (Checker.enabled) {
         if (this.enabled()) {
            if (Configs.SneakyCreeperMap) {
               if (var1.type == ElementType.TEXT) {
                  RenderUtils.start();
                  RenderUtils.drawRect((new Color(0.0F, 0.0F, 0.0F, (float)Configs.SNBackgroundAlpha / 255.0F)).getRGB(), Configs.SNMapX, Configs.SNMapY, 25 * Configs.SNMapScale, 25 * Configs.SNMapScale);
                  int var2 = MathUtils.floor(MathUtils.getX(MinecraftUtils.getPlayer()));
                  int var3 = MathUtils.floor(MathUtils.getZ(MinecraftUtils.getPlayer()));
                  Vector2f var4 = new Vector2f((float)(Configs.SNMapScale * Configs.SNHeadScale) * 0.02F, (float)(Configs.SNMapScale * Configs.SNHeadScale) * 0.02F);
                  RenderUtils.retainTransforms(false);
                  translateTo((double)var2, (double)var3);
                  RenderUtils.translate(var4.x / 2.0F, var4.y / 2.0F);
                  RenderUtils.rotate(MathUtils.getYaw() + 180.0F);
                  RenderUtils.translate(-var4.x / 2.0F, -var4.y / 2.0F);
                  RenderUtils.drawImage(this.defaultIcon, 0.0, 0.0, (double)var4.x, (double)var4.y);
                  Iterator var5 = positions.iterator();

                  while(var5.hasNext()) {
                     BlockPos var6 = (BlockPos)var5.next();
                     RenderUtils.retainTransforms(false);
                     translateTo((double)var6.getX(), (double)var6.getZ());
                     RenderUtils.drawRect((new Color(0, 0, 255)).getRGB(), -Configs.SNMapScale / 2, -Configs.SNMapScale / 2, Configs.SNMapScale, Configs.SNMapScale);
                  }

                  var5 = this.getCreepers().iterator();

                  while(var5.hasNext()) {
                     EntityCreeper var8 = (EntityCreeper)var5.next();
                     Color var7 = new Color(0, 255, 0);
                     if (this.toBeKilled.contains(var8.getEntityId())) {
                        var7 = new Color(255, 255, 0);
                     }

                     RenderUtils.retainTransforms(false);
                     translateTo((double)MathUtils.getX(var8), (double)MathUtils.getZ(var8));
                     RenderUtils.drawRect(var7.getRGB(), -Configs.SNMapScale / 2, -Configs.SNMapScale / 2, Configs.SNMapScale, Configs.SNMapScale);
                  }

                  if (this.goingTo != null) {
                     RenderUtils.retainTransforms(false);
                     translateTo((double)this.goingTo.getX(), (double)this.goingTo.getZ());
                     RenderUtils.drawRect((new Color(255, 0, 0)).getRGB(), -Configs.SNMapScale / 2, -Configs.SNMapScale / 2, Configs.SNMapScale, Configs.SNMapScale);
                  }

                  RenderUtils.end();
               }
            }
         }
      }
   }

   public double getJudgeDistance() {
      return 3.5;
   }

   public String getName() {
      return "Auto Sneaky Creeper";
   }
}
