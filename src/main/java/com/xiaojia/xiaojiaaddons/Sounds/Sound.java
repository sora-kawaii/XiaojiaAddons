package com.xiaojia.xiaojiaaddons.Sounds;

import net.minecraft.util.ResourceLocation;

public abstract class Sound {

    public ISound sound;

    public String name;

    public float volume;

    public Sound(String var1, float var2) {
        Sounds.sounds.add(this);
        this.name = var1;
        this.sound = new ISound(new ResourceLocation("xiaojiaaddons:" + var1), var2, 1.0F);
        this.volume = var2;
    }

    public Sound setRepeat(boolean var1) {
        this.sound.repeat = var1;
        return this;
    }
}
