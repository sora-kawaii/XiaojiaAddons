package com.xiaojia.xiaojiaaddons.Tests;

import com.xiaojia.xiaojiaaddons.Objects.ScoreBoard;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.EntityUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.SessionUtils;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TestM7 {
   public static void m7() {
      Iterator var0 = MinecraftUtils.getWorld().loadedEntityList.iterator();

      while(var0.hasNext()) {
         Entity var1 = (Entity)var0.next();
         if (var1 instanceof EntityArmorStand) {
            String var2 = EntityUtils.getHeadOwner(var1);
            ChatLib.chat("owner " + var2 + String.format(", %.2f %.2f %.2f", var1.posX, var1.posY, var1.posZ));
         }

         if (var1 instanceof EntityDragon) {
            ChatLib.chat(String.format("dragon at %.2f %.2f %.2f, health %.2f", var1.posX, var1.posY, var1.posZ, ((EntityDragon)var1).getHealth()));
         }
      }

      var0 = ScoreBoard.getLines().iterator();

      while(var0.hasNext()) {
         String var3 = (String)var0.next();
         ChatLib.chat(var3);
      }

   }

   @SubscribeEvent
   public void onEntityJoin(EntityJoinWorldEvent var1) {
      if (SessionUtils.isDev()) {
         if (!SessionUtils.isDev()) {
            Entity var2 = var1.entity;
            String var4;
            if (var2 instanceof EntityDragon) {
               ChatLib.chat(String.format("spawning dragon at %.2f %.2f %.2f", var2.posX, var2.posY, var2.posZ));
               Iterator var3 = ScoreBoard.getLines().iterator();

               while(var3.hasNext()) {
                  var4 = (String)var3.next();
                  ChatLib.chat(var4);
               }
            }

            if (var2 instanceof EntityArmorStand) {
               ItemStack var5 = ((EntityArmorStand)var2).getEquipmentInSlot(4);
               if (var5 == null) {
                  return;
               }

               var4 = ChatLib.removeFormatting(var5.getDisplayName()).trim();
               ChatLib.chat(String.format("spawning armorstand wearing helm %s at %.2f %.2f %.2f", var4, var2.posX, var2.posY, var2.posZ));
            }

         }
      }
   }

   public static void show() {
      Iterator var0 = MinecraftUtils.getWorld().loadedEntityList.iterator();

      while(var0.hasNext()) {
         Entity var1 = (Entity)var0.next();
         if (var1 instanceof EntityArmorStand) {
            String var2 = var1.getDisplayName().getFormattedText();
            ItemStack var3 = ((EntityArmorStand)var1).getEquipmentInSlot(4);
            String var4 = "";
            if (var3 != null) {
               var4 = var3.getDisplayName();
            }

            ChatLib.chat(String.format("name %s, helm %s, texture %s, %.2f %.2f %.2f", var2, var4, EntityUtils.getHeadTexture(var1), var1.posX, var1.posY, var1.posZ));
         }
      }

   }
}
