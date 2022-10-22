package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.M7Dragon;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.boss.EntityDragon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({RenderDragon.class})
public abstract class MixinRenderDragon extends RenderLiving {
   @Inject(
      method = {"getEntityTexture"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void replaceEntityTexture(EntityDragon var1, CallbackInfoReturnable var2) {
      M7Dragon.getEntityTexture(var1, var2);
   }

   public MixinRenderDragon(RenderManager var1, ModelBase var2, float var3) {
      super(var1, var2, var3);
   }

   @Inject(
      method = {"renderModel(Lnet/minecraft/entity/boss/EntityDragon;FFFFFF)V"},
      at = {@At("HEAD")}
   )
   private void onRenderModel(EntityDragon var1, float var2, float var3, float var4, float var5, float var6, float var7, CallbackInfo var8) {
      if (Configs.RemoveDragonHurtRender && SkyblockUtils.isInDungeon()) {
         var1.hurtTime = 0;
      }

   }
}
