package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import java.util.HashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class FairySoul extends RenderEntityESP {
   public boolean shouldDrawString(EntityInfo var1) {
      return Configs.FairySoulESP;
   }

   public EntityInfo getEntityInfo(Entity var1) {
      if (!(var1 instanceof EntityArmorStand)) {
         return null;
      } else {
         EntityArmorStand var2 = (EntityArmorStand)var1;
         if (this.isFairySoul(var2)) {
            HashMap var3 = new HashMap();
            var3.put("entity", var1);
            var3.put("yOffset", -2.0F);
            var3.put("drawString", EntityInfo.EnumDraw.DRAW_KIND);
            var3.put("width", 0.35F);
            var3.put("height", 0.7F);
            var3.put("fontColor", 3407667);
            var3.put("isFilled", true);
            var3.put("kind", "Fairy Soul");
            return new EntityInfo(var3);
         } else {
            return null;
         }
      }
   }

   public boolean enabled() {
      return true;
   }

   private boolean isFairySoul(EntityArmorStand var1) {
      try {
         ItemStack var2 = var1.getEquipmentInSlot(4);
         if (var2 == null) {
            return false;
         } else {
            return var2.getItem() == Items.skull && var2.getMetadata() == 3 ? var2.getTagCompound().getCompoundTag("SkullOwner").getString("Id").equals("57a4c8dc-9b8e-3d41-80da-a608901a6147") : false;
         }
      } catch (Exception var3) {
         return false;
      }
   }

   public boolean shouldRenderESP(EntityInfo var1) {
      return Configs.FairySoulESP;
   }
}
