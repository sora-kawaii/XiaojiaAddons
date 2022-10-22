package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.EntityUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HideCreepers {
   @SubscribeEvent
   public void onTick(TickEndEvent event) {
      if (Checker.enabled) {
         if (Configs.HideCreepers) {
            if (MinecraftUtils.getWorld() != null) {
               if (!SkyblockUtils.isInDungeon()) {
                  Iterator var2 = EntityUtils.getEntities().iterator();

                  while(var2.hasNext()) {
                     Entity var3 = (Entity)var2.next();
                     if (var3 instanceof EntityCreeper) {
                        var3.posY = var3.lastTickPosY = 9999.0;
                     }
                  }

               }
            }
         }
      }
   }
}
