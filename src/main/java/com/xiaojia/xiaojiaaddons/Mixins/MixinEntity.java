package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.ColorName;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Entity.class})
public abstract class MixinEntity {

    @Shadow
    protected DataWatcher dataWatcher;
    private String lastString = "";
    private String lastResult = "";

    @Inject(
            method = {"getCustomNameTag"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void getCustomString(CallbackInfoReturnable var1) {
        String var2 = this.dataWatcher.getWatchableObjectString(2);
        if (Configs.ColorNameNameTag) {
            if (var2.equals(this.lastString)) {
                var1.setReturnValue(this.lastResult);
            } else {
                String var3 = ColorName.addColorNameWithPrefix(var2);
                this.lastResult = var3;
                this.lastString = var2;
                var1.setReturnValue(var3);
            }

        }
    }
}
