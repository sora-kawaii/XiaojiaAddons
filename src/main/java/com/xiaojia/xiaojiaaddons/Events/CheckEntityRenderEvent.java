package com.xiaojia.xiaojiaaddons.Events;

import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class CheckEntityRenderEvent extends Event {

    public Entity entity;
    double camY;
    ICamera camera;

    double camZ;

    double camX;

    public CheckEntityRenderEvent(Entity var1, ICamera var2, double var3, double var5, double var7) {
        this.entity = var1;
        this.camera = var2;
        this.camX = var3;
        this.camY = var5;
        this.camZ = var7;
    }
}
