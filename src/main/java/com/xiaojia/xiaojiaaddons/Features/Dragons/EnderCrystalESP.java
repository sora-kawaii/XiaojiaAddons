package com.xiaojia.xiaojiaaddons.Features.Dragons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import java.util.HashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;

public class EnderCrystalESP extends RenderEntityESP {

   public static final String ENDERCRYSTAL_STRING = "Crystal";

   public boolean enabled() {
      return SkyblockUtils.isInDragon();
   }

   public boolean shouldRenderESP(EntityInfo var1) {
      return true;
   }

   public boolean shouldDrawString(EntityInfo var1) {
      return true;
   }

   public EntityInfo getEntityInfo(Entity var1) {
      if (var1 instanceof EntityEnderCrystal && Configs.CrystalESP) {
         HashMap var2 = new HashMap();
         var2.put("entity", var1);
         var2.put("r", 255);
         var2.put("g", 0);
         var2.put("b", 0);
         var2.put("drawString", EntityInfo.EnumDraw.DRAW_KIND);
         var2.put("kind", "Crystal");
         var2.put("fontColor", 14423100);
         var2.put("isFilled", true);
         var2.put("isESP", true);
         return new EntityInfo(var2);
      } else {
         return null;
      }
   }
}
