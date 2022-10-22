package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import java.util.HashMap;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class XiaojiaKeyBind {

   private final String command;

   public static HashMap keyBinds = new HashMap();

   private final KeyBinding bind;

   public XiaojiaKeyBind(String var1, int var2) {
      this.command = var1;
      this.bind = new KeyBinding(var1, var2, "Addons - XiaojiaAddons KeyBind");
      ClientRegistry.registerKeyBinding(this.bind);
   }

   public static XiaojiaKeyBind getKeybind(String var0, int var1) {
      XiaojiaKeyBind var2 = (XiaojiaKeyBind)keyBinds.get(var0);
      return var2 == null || var1 != -1 && var2.getBind().getKeyCode() != var1 ? null : var2;
   }

   public KeyBinding getBind() {
      return this.bind;
   }

   public int hashCode() {
      return this.command.hashCode();
   }

   public String getCommand() {
      return this.command;
   }

   public boolean equals(Object var1) {
      return var1 instanceof XiaojiaKeyBind ? ((XiaojiaKeyBind)var1).command.equals(this.command) : false;
   }
}
