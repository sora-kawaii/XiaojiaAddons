package com.xiaojia.xiaojiaaddons.Objects;

import java.util.HashMap;
import net.minecraft.entity.Entity;

public class EntityInfo {

   private final int b;

   private final int g;

   private final EnumDraw drawString;

   private final String kind;

   private final boolean isFilled;

   private final float height;

   private final float scale;

   private final float yOffset;

   private final float width;

   private final boolean isESP;

   private final Entity entity;

   private final int fontColor;

   private final int r;

   public float getWidth() {
      return this.width;
   }

   public boolean isFilled() {
      return this.isFilled;
   }

   public int getG() {
      return this.g;
   }

   public EnumDraw getDrawString() {
      return this.drawString;
   }

   public float getHeight() {
      return this.height;
   }

   public String toString() {
      return this.entity.getName() + ": " + this.getKind();
   }

   public float getyOffset() {
      return this.yOffset;
   }

   public int getR() {
      return this.r;
   }

   public int getFontColor() {
      return this.fontColor;
   }

   public int getB() {
      return this.b;
   }

   public Entity getEntity() {
      return this.entity;
   }

   public boolean isESP() {
      return this.isESP;
   }

   public String getKind() {
      return this.kind;
   }

   public EntityInfo(HashMap var1) {
      this.entity = (Entity)var1.get("entity");
      this.r = (Integer)var1.getOrDefault("r", 0);
      this.g = (Integer)var1.getOrDefault("g", 255);
      this.b = (Integer)var1.getOrDefault("b", 0);
      this.width = (Float)var1.getOrDefault("width", 0.5F);
      this.height = (Float)var1.getOrDefault("height", 1.0F);
      this.yOffset = (Float)var1.getOrDefault("yOffset", 0.0F);
      this.drawString = (EnumDraw)var1.getOrDefault("drawString", EntityInfo.EnumDraw.DONT_DRAW_STRING);
      this.kind = (String)var1.get("kind");
      this.fontColor = (Integer)var1.get("fontColor");
      this.scale = (Float)var1.getOrDefault("scale", 1.0F);
      this.isFilled = (Boolean)var1.getOrDefault("isFilled", false);
      this.isESP = (Boolean)var1.getOrDefault("isESP", false);
   }

   public float getScale() {
      return this.scale;
   }

   public static enum EnumDraw {
   
      DRAW_KIND,
   
      DRAW_ARMORSTAND_HP,
   
      DRAW_ENTITY_HP,
   
      DONT_DRAW_STRING;
   }
}
