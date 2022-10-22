package com.xiaojia.xiaojiaaddons.Features.Bestiary;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoScatha {

   private float pitch;

   private static final KeyBind keyBind = new KeyBind("Auto Scatha", 0);

   private boolean should = false;

   private float yaw;

   private Thread thread = null;

   private int y = 0;

   private static int IIIIlIIl(double var0, double var2) {
      double var4;
      return (var4 = var0 - var2) == 0.0 ? 0 : (var4 < 0.0 ? -1 : 1);
   }

   private static int llIlIIIl(float var0, float var1) {
      float var2;
      return (var2 = var0 - var1) == 0.0F ? 0 : (var2 < 0.0F ? -1 : 1);
   }

   private void stop() {
      this.stop(true);
   }

   @SubscribeEvent
   public void onTick(TickEndEvent event) {
      if (Checker.enabled) {
         if (Configs.AutoScatha) {
            EntityPlayerSP var2 = MinecraftUtils.getPlayer();
            if (var2 != null) {
               if (keyBind.isPressed()) {
                  this.should = !this.should;
                  ChatLib.chat(this.should ? "Auto Scatha &aactivated" : "Auto Scatha &cdeactivated");
                  if (!this.should) {
                     this.stop(false);
                  }

                  if (this.should) {
                     this.y = MathUtils.floor(MathUtils.getY(var2));
                     this.yaw = this.fit(MathUtils.getYaw());
                     this.pitch = 45.0F;
                     ControlUtils.changeDirection(this.yaw, this.pitch);
                     this.thread = new Thread(() -> {
                        try {
                           BlockPos var2x = MathUtils.getBlockPos();
                           long var3 = TimeUtils.curTime();

                           while(this.should) {
                              BlockPos var5 = MathUtils.getBlockPos();
                              if (!var5.equals(var2x)) {
                                 var2x = var5;
                                 var3 = TimeUtils.curTime();
                              }

                              if (TimeUtils.curTime() - var3 > 5000L) {
                                 ChatLib.chat("You're not moving, I don't know why. So stopped");
                                 this.stop();
                                 return;
                              }

                              if (IIIIlIIl((double)Math.abs(MathUtils.getYaw() - this.yaw), 0.01) > 0 && (double)Math.abs(MathUtils.getYaw() - this.yaw) < 359.99 || IIIIlIIl((double)Math.abs(MathUtils.getPitch() - this.pitch), 0.01) > 0) {
                                 ChatLib.chat("Detected yaw/pitch move, stopped.");
                                 this.stop();
                                 return;
                              }

                              var2.closeScreen();
                              BlockPos var6 = XiaojiaAddons.mc.objectMouseOver.getBlockPos();
                              Block var7 = BlockUtils.getBlockAt(var6);
                              if (var6 != null && var6.getY() >= this.y) {
                                 ControlUtils.holdLeftClick();
                              } else {
                                 ControlUtils.releaseLeftClick();
                              }

                              if (var6 != null && var7 != null && var7.getRegistryName().contains("bedrock") && var6.getY() >= this.y) {
                                 ChatLib.chat("Detected bedrock, stopped.");
                                 this.stop();
                                 return;
                              }

                              ControlUtils.holdForward();
                              Thread.sleep(10L);
                           }
                        } catch (Exception var8) {
                           var8.printStackTrace();
                        }

                     });
                     this.thread.start();
                  }
               }

               if (this.should) {
                  if (MathUtils.floor(MathUtils.getY(var2)) != this.y) {
                     ChatLib.chat("Y changed! Auto Scatha stoped. Remember to turn off mole!");
                     this.stop();
                  }

               }
            }
         }
      }
   }

   @SubscribeEvent
   public void onReceived(ClientChatReceivedEvent var1) {
      if (Checker.enabled) {
         if (Configs.AutoScatha) {
            if (ChatLib.removeFormatting(var1.message.getUnformattedText()).trim().equals("You hear the sound of something approaching...")) {
               ChatLib.chat("Worm spawned!");
               this.stop();
            }

         }
      }
   }

   private void stop(boolean var1) {
      ControlUtils.releaseLeftClick();
      ControlUtils.releaseForward();
      EntityPlayerSP var2 = MinecraftUtils.getPlayer();
      if (var1 && var2 != null) {
         var2.playSound("random.successful_hit", 1000.0F, 1.0F);
      }

      this.should = false;
      if (this.thread != null && this.thread.isAlive()) {
         this.thread.interrupt();
      }

   }

   private float fit(float var1) {
      if (var1 >= 45.0F && llIlIIIl(var1, 135.0F) <= 0) {
         return 90.0F;
      } else if (!(var1 >= 135.0F) && llIlIIIl(var1, -135.0F) > 0) {
         return var1 >= -45.0F && llIlIIIl(var1, 45.0F) <= 0 ? 0.0F : -90.0F;
      } else {
         return -180.0F;
      }
   }
}
