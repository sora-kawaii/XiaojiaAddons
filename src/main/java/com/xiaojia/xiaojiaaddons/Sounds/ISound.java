package com.xiaojia.xiaojiaaddons.Sounds;

import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.ISound.AttenuationType;
import net.minecraft.util.ResourceLocation;

public class ISound implements ITickableSound {

   public net.minecraft.client.audio.ISound.AttenuationType type;

   public final ResourceLocation sound;

   public int delay = 0;

   public float xPos = 0.0F;

   public boolean donePlaying = false;

   public float zPos = 0.0F;

   public boolean repeat = true;

   public float volume = 1.0F;

   public float pitch = 1.0F;

   public float yPos = 0.0F;

   public net.minecraft.client.audio.ISound.AttenuationType getAttenuationType() {
      return this.type;
   }

   public ResourceLocation getSoundLocation() {
      return this.sound;
   }

   public boolean isDonePlaying() {
      return this.donePlaying;
   }

   public float getXPosF() {
      return this.xPos;
   }

   public boolean canRepeat() {
      return this.repeat;
   }

   public float getYPosF() {
      return this.yPos;
   }

   public float getVolume() {
      return this.volume;
   }

   public float getPitch() {
      return this.pitch;
   }

   public void update() {
   }

   public ISound(ResourceLocation var1, float var2, float var3) {
      this.type = AttenuationType.NONE;
      this.volume = var2;
      this.pitch = var3;
      this.sound = var1;
   }

   public float getZPosF() {
      return this.zPos;
   }

   public int getRepeatDelay() {
      return this.delay;
   }
}
