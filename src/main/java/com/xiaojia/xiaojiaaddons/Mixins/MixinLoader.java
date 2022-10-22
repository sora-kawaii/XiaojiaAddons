package com.xiaojia.xiaojiaaddons.Mixins;

import java.util.Map;
import javax.annotation.Nullable;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

public class MixinLoader implements IFMLLoadingPlugin {
   public String getModContainerClass() {
      return null;
   }

   public void injectData(Map var1) {
   }

   public MixinLoader() {
      System.out.println("mixins initialized");
      MixinBootstrap.init();
      Mixins.addConfiguration("mixins.xiaojiaaddons.json");
   }

   public String getAccessTransformerClass() {
      return null;
   }

   public String[] getASMTransformerClass() {
      return new String[0];
   }

   @Nullable
   public String getSetupClass() {
      return null;
   }
}
