package com.xiaojia.xiaojiaaddons.Events;

import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class GuiContainerEvent extends Event {
   @Cancelable
   public static class ScreenDrawnEvent extends GuiContainerEvent {
   }

   @Cancelable
   public static class DrawSlotEvent extends GuiContainerEvent {
   
      public Slot slot;

      public DrawSlotEvent(Slot var1) {
         this.slot = var1;
      }

      public static class Pre extends DrawSlotEvent {
         public Pre(Slot var1) {
            super(var1);
         }
      }

      public static class Post extends DrawSlotEvent {
         public Post(Slot var1) {
            super(var1);
         }
      }
   }
}
