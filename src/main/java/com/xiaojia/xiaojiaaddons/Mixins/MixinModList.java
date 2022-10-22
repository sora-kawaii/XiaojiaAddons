package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import java.util.List;
import java.util.Map;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({FMLHandshakeMessage.ModList.class})
public abstract class MixinModList {

   @Shadow(
      remap = false
   )
   private Map modTags;

   @Inject(
      method = {"<init>(Ljava/util/List;)V"},
      at = {@At("RETURN")},
      remap = false
   )
   private void removeMod(List var1, CallbackInfo var2) {
      if (!XiaojiaAddons.mc.isSingleplayer()) {
         if (Configs.HideModID) {
            this.modTags.remove("xiaojiaaddons");
         }
      }
   }
}
