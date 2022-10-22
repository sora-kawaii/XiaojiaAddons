package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Events.CheckEntityRenderEvent;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({RenderManager.class})
public class MixinRenderManager {
   @Inject(
      method = {"shouldRender"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void shouldRender(Entity var1, ICamera var2, double var3, double var5, double var7, CallbackInfoReturnable var9) {
      if (MinecraftForge.EVENT_BUS.post(new CheckEntityRenderEvent(var1, var2, var3, var5, var7))) {
         var9.setReturnValue(false);
      }

   }
}
