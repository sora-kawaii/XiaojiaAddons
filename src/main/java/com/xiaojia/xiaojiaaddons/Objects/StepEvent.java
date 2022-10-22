package com.xiaojia.xiaojiaaddons.Objects;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public abstract class StepEvent {

   private long fps = 60L;

   private long systemTime = Minecraft.getSystemTime();

   public abstract void execute();

   public StepEvent(long var1) {
      if (var1 <= 500L && var1 > 0L) {
         this.fps = var1;
      } else {
         System.out.println("Cannot create StepEvent with this fps, set fps to 60 as default.");
      }
   }

   @SubscribeEvent
   public void onRenderTick(TickEvent.RenderTickEvent var1) {
      while(this.systemTime < Minecraft.getSystemTime() + 1000L / this.fps) {
         this.systemTime += 1000L / this.fps;
         this.execute();
      }

   }
}
