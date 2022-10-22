package com.xiaojia.xiaojiaaddons.Features.Dragons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.EntityUtils;
import com.xiaojia.xiaojiaaddons.utils.HotbarUtils;
import com.xiaojia.xiaojiaaddons.utils.ShortbowUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoShootCrystal {

   private long lastShootTime = 0L;

   private final ArrayList shootQueue = new ArrayList();

   private static final KeyBind autoShootCrystalKeybind = new KeyBind("Auto Crystal", 0);

   private boolean isShooting = false;

   private boolean enabled = false;

   private int shootQueueP = 0;

   @SubscribeEvent
   public void onWorldLoad(WorldEvent.Load var1) {
      this.lastShootTime = 0L;
      this.shootQueueP = 0;
      this.shootQueue.clear();
   }

   @SubscribeEvent
   public void onTick(TickEndEvent event) {
      if (Checker.enabled && SkyblockUtils.isInDragon()) {
         if (Configs.AutoShootCrystal) {
            if (!this.enabled) {
               this.shootQueue.clear();
            } else if (this.shootQueue.size() == 0) {
               List var2 = EntityUtils.getEntities();
               Iterator var3 = var2.iterator();

               while(var3.hasNext()) {
                  Entity var4 = (Entity)var3.next();
                  if (var4 instanceof EntityEnderCrystal) {
                     this.shootQueue.add(new Vector3d(var4.posX, var4.posY, var4.posZ));
                  }
               }

            }
         }
      }
   }

   @SubscribeEvent
   public void onTickKeyBind(TickEndEvent var1) {
      if (Checker.enabled) {
         if (autoShootCrystalKeybind.isPressed()) {
            this.enabled = !this.enabled;
            ChatLib.chat(this.enabled ? "AutoShootCrystal &aactivated" : "AutoShootCrystal &cdeactivated");
         }

      }
   }

   private void rightClickTerminator() {
      ControlUtils.setHeldItemIndex(HotbarUtils.terminatorSlot);
      ControlUtils.rightClick();
   }

   @SubscribeEvent
   public void onTickShoot(TickEndEvent var1) {
      if (Checker.enabled && SkyblockUtils.isInDragon()) {
         if (Configs.AutoShootCrystal) {
            if (!this.isShooting && this.enabled) {
               if (this.shootQueueP >= this.shootQueue.size()) {
                  this.shootQueue.clear();
                  this.shootQueueP = 0;
               } else {
                  if (TimeUtils.curTime() - this.lastShootTime <= (long)Configs.AutoShootCrystalCD) {
                     return;
                  }

                  Vector3d var2 = (Vector3d)this.shootQueue.get(this.shootQueueP);
                  if (!ControlUtils.checkHotbarItem(HotbarUtils.terminatorSlot, "Terminator")) {
                     ChatLib.chat("Auto Crystal requires terminator in hotbar!");
                     return;
                  }

                  double var3 = var2.x;
                  double var5 = var2.y;
                  double var7 = var2.z;
                  Vector2d var9 = ShortbowUtils.calcYawPitchEnderCrystal(var3, var5, var7);
                  double var10 = var9.x;
                  double var12 = var9.y;
                  this.lastShootTime = TimeUtils.curTime();
                  (new Thread(() -> {
                     try {
                        this.isShooting = true;
                        ControlUtils.changeDirection((float)var10, (float)var12);
                        Thread.sleep(40L);
                        ControlUtils.changeDirection((float)var10, (float)var12);
                        this.rightClickTerminator();
                        Thread.sleep(20L);
                        ControlUtils.changeDirection((float)var10, (float)var12);
                        this.rightClickTerminator();
                     } catch (InterruptedException e) {
                        e.printStackTrace();
                     } finally {
                        this.isShooting = false;
                        ++this.shootQueueP;
                     }

                  })).start();
               }

            }
         }
      }
   }
}
