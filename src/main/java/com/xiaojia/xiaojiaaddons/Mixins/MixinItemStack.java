package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.ColorName;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.ItemRename;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ItemStack.class})
public class MixinItemStack {

    @Shadow
    private NBTTagCompound stackTagCompound;

    @Inject(
            method = {"getDisplayName"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void getDisplayName(CallbackInfoReturnable var1) {
        try {
            if (this.stackTagCompound != null && this.stackTagCompound.hasKey("ExtraAttributes")) {
                NBTTagCompound var2 = this.stackTagCompound.getCompoundTag("ExtraAttributes");
                if (var2 != null && var2.hasKey("uuid")) {
                    String var3 = var2.getString("uuid");
                    if (!var3.equals("") && ItemRename.renameMap.containsKey(var3)) {
                        String var4 = "&r";
                        String var5 = (String) ItemRename.renameMap.get(var3);
                        if (this.stackTagCompound.hasKey("display", 10)) {
                            NBTTagCompound var6 = this.stackTagCompound.getCompoundTag("display");
                            if (var6.hasKey("Name", 8)) {
                                String var7 = ChatLib.removeColor(var6.getString("Name"));
                                var4 = ChatLib.getPrefix(var7);
                            }
                        }

                        var1.setReturnValue(ChatLib.addColor(var4 + var5));
                    }

                }
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }
    }

    @Inject(
            method = {"getDisplayName"},
            at = {@At("RETURN")},
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    public void getReturnValue(CallbackInfoReturnable var1, String var2) {
        if (Checker.enabled) {
            if (Configs.ColorNameItem) {
                var2 = ColorName.addColorName(var2);
                var1.setReturnValue(var2);
            }
        }
    }
}
