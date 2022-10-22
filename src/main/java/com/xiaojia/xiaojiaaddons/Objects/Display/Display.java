package com.xiaojia.xiaojiaaddons.Objects.Display;

import java.util.ArrayList;
import java.util.Iterator;

public class Display {

   private int textColor;

   private final ArrayList lines = new ArrayList();

   private Background background;

   private boolean shouldRender = true;

   private Order order;

   private Align align;

   private int renderY = 0;

   private int renderX = 0;

   private int backgroundColor;

   public void setAlign(String var1) {
      this.align = Align.valueOf(var1.toUpperCase());
   }

   public void setShouldRender(boolean var1) {
      this.shouldRender = var1;
   }

   public void setLine(int var1, DisplayLine var2) {
      while(this.lines.size() <= var1) {
         this.lines.add(new DisplayLine(""));
      }

      this.lines.set(var1, var2);
   }

   public void addLine(DisplayLine var1) {
      this.setLine(this.lines.size(), var1);
   }

   public void clearLines() {
      this.lines.clear();
   }

   public void setRenderLoc(int var1, int var2) {
      this.renderX = var1;
      this.renderY = var2;
   }

   public void setBackground(String var1) {
      this.background = Background.valueOf(var1.toUpperCase());
   }

   public void setTextColor(int var1) {
      this.textColor = var1;
   }

   public void setBackgroundColor(int var1) {
      this.backgroundColor = var1;
   }

   public void addLine(String var1) {
      this.setLine(this.lines.size(), var1);
   }

   public void setLine(int var1, String var2) {
      while(this.lines.size() <= var1) {
         this.lines.add(new DisplayLine(""));
      }

      this.lines.set(var1, new DisplayLine(var2));
   }

   public Display() {
      this.order = Order.DOWN;
      this.align = Align.LEFT;
      this.background = Background.NONE;
      this.backgroundColor = 1342177280;
      this.textColor = -1;
      DisplayHandler.registerDisplay(this);
   }

   public void render() {
      if (this.shouldRender && !this.lines.isEmpty()) {
         int var1 = 0;
         int var2 = 0;

         Iterator var3;
         DisplayLine var4;
         for(var3 = this.lines.iterator(); var3.hasNext(); var2 = Math.max(var2, var4.getTextWidth())) {
            var4 = (DisplayLine)var3.next();
         }

         var3 = this.lines.iterator();

         while(var3.hasNext()) {
            var4 = (DisplayLine)var3.next();
            var4.draw(this.renderX, this.renderY + var1, var2, this.background, this.backgroundColor, this.textColor, this.align);
            if (this.order == Order.UP) {
               var1 -= var4.getHeight();
            } else {
               var1 += var4.getHeight();
            }
         }

      }
   }

   public void setOrder(String var1) {
      this.order = Order.valueOf(var1.toUpperCase());
   }
}
