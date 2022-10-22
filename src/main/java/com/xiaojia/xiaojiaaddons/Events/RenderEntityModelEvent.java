package com.xiaojia.xiaojiaaddons.Events;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderEntityModelEvent extends Event {

   public float headYaw;

   public float scaleFactor;

   public ModelBase model;

   public float limbSwing;

   public float ageInTicks;

   public EntityLivingBase entity;

   public float limbSwingAmount;

   public float headPitch;

   public RenderEntityModelEvent(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, ModelBase var8) {
      this.entity = var1;
      this.limbSwing = var2;
      this.limbSwingAmount = var3;
      this.ageInTicks = var4;
      this.headYaw = var5;
      this.headPitch = var6;
      this.scaleFactor = var7;
      this.model = var8;
   }
}
