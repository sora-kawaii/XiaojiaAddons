package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.RenderEntityModelEvent;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({RendererLivingEntity.class})
public abstract class MixinRenderLivingEntity {

    @Shadow
    protected ModelBase mainModel;

    @Inject(
            method = {"canRenderName"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void canRenderName(EntityLivingBase var1, CallbackInfoReturnable var2) {
        if (Configs.RenderSelfName && var1.getName().equals(MinecraftUtils.getPlayer().getName())) {
            var2.setReturnValue(true);
        }

    }

    @Inject(
            method = {"renderModel"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void renderModel(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, CallbackInfo var8) {
        if (MinecraftForge.EVENT_BUS.post(new RenderEntityModelEvent(var1, var2, var3, var4, var5, var6, var7, this.mainModel))) {
            var8.cancel();
        }

    }
}
