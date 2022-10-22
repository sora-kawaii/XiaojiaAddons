package com.xiaojia.xiaojiaaddons.Objects.Display;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DisplayHandler {

   private static final ArrayList displays = new ArrayList();

   public static void registerDisplay(Display var0) {
      displays.add(var0);
   }

   @SubscribeEvent
   public void renderDisplays(RenderGameOverlayEvent.Text var1) {
      GlStateManager.pushMatrix();
      Iterator var2 = displays.iterator();

      while(var2.hasNext()) {
         Display var3 = (Display)var2.next();
         var3.render();
      }

      GlStateManager.popMatrix();
   }
}
