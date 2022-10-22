package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Events.GuiContainerEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GuiContainer.class})
public abstract class MixinGuiContainer {
   @Inject(
      method = {"drawSlot"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onDrawSlot(Slot var1, CallbackInfo var2) {
      if (MinecraftForge.EVENT_BUS.post(new GuiContainerEvent.DrawSlotEvent.Pre(var1))) {
         var2.cancel();
      }

   }

   @Inject(
      method = {"drawSlot"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void onDrawSlotPost(Slot var1, CallbackInfo var2) {
      if (MinecraftForge.EVENT_BUS.post(new GuiContainerEvent.DrawSlotEvent.Post(var1))) {
         var2.cancel();
      }

   }

   @Inject(
      method = {"drawScreen"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void onScreenDrawn(CallbackInfo var1) {
      if (MinecraftForge.EVENT_BUS.post(new GuiContainerEvent.ScreenDrawnEvent())) {
         var1.cancel();
      }

   }
}
