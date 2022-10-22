package com.xiaojia.xiaojiaaddons.Features.Nether;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import java.util.HashMap;
import net.minecraft.entity.Entity;

public class ConvergenceESP extends RenderEntityESP {
   public boolean enabled() {
      return SkyblockUtils.isInNether();
   }

   public boolean shouldDrawString(EntityInfo var1) {
      return true;
   }

   public EntityInfo getEntityInfo(Entity var1) {
      if (!Checker.enabled) {
         return null;
      } else if (!Configs.ConvergenceESP) {
         return null;
      } else {
         String var2 = ChatLib.removeFormatting(var1.getName());
         if (var2.equals("Convergence Center")) {
            HashMap var3 = new HashMap();
            var3.put("entity", var1);
            var3.put("yOffset", 0.0F);
            var3.put("kind", "Convergence Center");
            var3.put("drawString", EntityInfo.EnumDraw.DRAW_KIND);
            var3.put("width", 3.0F);
            var3.put("height", 6.0F);
            var3.put("fontColor", 16724787);
            var3.put("isESP", true);
            return new EntityInfo(var3);
         } else {
            return null;
         }
      }
   }

   public boolean shouldRenderESP(EntityInfo var1) {
      return true;
   }
}
