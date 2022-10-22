package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Events.ItemDrawnEvent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({RenderItem.class})
public class MixinRenderItem {
    @Inject(
            method = {"renderItemOverlayIntoGUI"},
            at = {@At("RETURN")}
    )
    private void renderItemOverlayPost(FontRenderer var1, ItemStack var2, int var3, int var4, String var5, CallbackInfo var6) {
        if (MinecraftForge.EVENT_BUS.post(new ItemDrawnEvent(var1, var2, var3, var4, var5))) {
            var6.cancel();
        }

    }
}
