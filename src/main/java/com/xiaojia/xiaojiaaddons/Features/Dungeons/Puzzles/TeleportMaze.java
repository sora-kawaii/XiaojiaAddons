package com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Vector2i;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TeleportMaze {

   private boolean shouldCheckPossible = true;

   private final ArrayList shouldNot = new ArrayList();

   private HashSet possible = new HashSet();

   private boolean justPassPad = false;

   @SubscribeEvent
   public void onRenderWorld(RenderWorldLastEvent var1) {
      if (Checker.enabled) {
         Iterator var2 = this.shouldNot.iterator();

         while(var2.hasNext()) {
            BlockPos var3 = (BlockPos)var2.next();
            GuiUtils.drawBoundingBoxAtBlock(var3, new Color(255, 0, 0));
         }

         Color var5 = new Color(0, 0, 255);
         if (this.possible.size() == 1) {
            var5 = new Color(0, 255, 0);
         }

         Iterator var6 = this.possible.iterator();

         while(var6.hasNext()) {
            BlockPos var4 = (BlockPos)var6.next();
            GuiUtils.drawBoundingBoxAtBlock(var4, var5);
         }

      }
   }

   @SubscribeEvent
   public void onWorldLoad(WorldEvent.Load var1) {
      this.shouldNot.clear();
      this.possible.clear();
      this.justPassPad = false;
      this.shouldCheckPossible = false;
   }

   @SubscribeEvent
   public void onPacketReceived(PacketReceivedEvent var1) {
      if (Checker.enabled) {
         if (Configs.TeleportMazeSolver && SkyblockUtils.isInDungeon()) {
            if (var1.packet instanceof S08PacketPlayerPosLook) {
               if (this.justPassPad) {
                  this.justPassPad = false;
                  this.shouldCheckPossible = true;
               }
            }
         }
      }
   }

   @SubscribeEvent
   public void onTick(TickEndEvent event) {
      if (Checker.enabled) {
         if (Configs.TeleportMazeSolver && SkyblockUtils.isInDungeon()) {
            int var2 = MathUtils.getBlockX(MinecraftUtils.getPlayer());
            int var3 = 69;
            int var4 = MathUtils.getBlockZ(MinecraftUtils.getPlayer());
            if (this.shouldCheckPossible) {
               this.shouldCheckPossible = false;
               double var5 = (double)(-MathUtils.getYaw()) / 57.29577951308232;
               Vector2i[] var7 = new Vector2i[]{new Vector2i(1, -1), new Vector2i(1, 1), new Vector2i(-1, -1), new Vector2i(-1, 1)};
               Vector2i[] var8 = var7;
               int var9 = var7.length;

               int var10;
               int var12;
               int var14;
               for(var10 = 0; var10 < var9; ++var10) {
                  Vector2i var11 = var8[var10];
                  var12 = var2 + var11.x;
                  var14 = var4 + var11.y;
                  if (BlockUtils.isBlockTeleportPad(var12, var3, var14)) {
                     this.shouldNot.add(new BlockPos(var12, var3, var14));
                  }
               }

               HashSet var19 = new HashSet();

               for(var9 = 3; var9 < 50; ++var9) {
                  var10 = var2 + MathUtils.floor(Math.sin(var5) * (double)var9);
                  byte var20 = (byte)var3;
                  var12 = var4 + MathUtils.floor(Math.cos(var5) * (double)var9);

                  for(int var13 = -1; var13 <= 1; ++var13) {
                     for(var14 = -1; var14 <= 1; ++var14) {
                        int var15 = var10 + var13;
                        int var17 = var12 + var14;
                        if (BlockUtils.isBlockTeleportPad(var15, var20, var17)) {
                           var19.add(new BlockPos(var15, var20, var17));
                        }
                     }
                  }
               }

               if (this.possible.size() == 0) {
                  this.possible = var19;
               } else {
                  Stream var10001 = this.possible.stream();
                  var19.getClass();
                  this.possible = (HashSet)var10001.filter(var19::contains).collect(Collectors.toCollection(HashSet::new));
                  if (this.possible.size() == 0) {
                     this.shouldCheckPossible = true;
                  }
               }
            }

            var3 = MathUtils.floor((double)MathUtils.getY(MinecraftUtils.getPlayer()) - 0.5);
            if (BlockUtils.isBlockTeleportPad(var2, var3, var4)) {
               BlockPos var18 = new BlockPos(var2, var3, var4);
               if (!this.shouldNot.contains(var18)) {
                  this.shouldNot.add(var18);
               }

               this.justPassPad = true;
            }

         }
      }
   }
}
