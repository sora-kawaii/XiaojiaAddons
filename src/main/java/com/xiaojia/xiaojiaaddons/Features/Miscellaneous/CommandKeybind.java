package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import java.io.BufferedReader;
import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

public class CommandKeybind {
   public static void loadKeyBinds() {
      try {
         File var0 = new File("config/XiaoJiaAddonsKeybinds.cfg");
         if (var0.exists()) {
            BufferedReader var1 = Files.newBufferedReader(Paths.get("config/XiaoJiaAddonsKeybinds.cfg"));
            Type var2 = (new TypeToken() {
            }).getType();
            HashMap var3 = (HashMap)(new Gson()).fromJson(var1, var2);
            Iterator var4 = var3.entrySet().iterator();

            while(var4.hasNext()) {
               Map.Entry var5 = (Map.Entry)var4.next();
               XiaojiaKeyBind.keyBinds.put(var5.getKey(), new XiaojiaKeyBind((String)var5.getKey(), (Integer)var5.getValue()));
            }
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      KeyBinding var7 = new KeyBinding("Use /xj keybind to add keybinds!", 0, "Addons - XiaojiaAddons KeyBind");
      ClientRegistry.registerKeyBinding(var7);
   }

   public static void saveKeyBinds() {
      try {
         HashMap var0 = new HashMap();
         Iterator var1 = XiaojiaKeyBind.keyBinds.values().iterator();

         while(var1.hasNext()) {
            XiaojiaKeyBind var2 = (XiaojiaKeyBind)var1.next();
            var0.put(var2.getCommand(), var2.getBind().getKeyCode());
         }

         String var4 = (new Gson()).toJson(var0);
         Path var5 = Paths.get("config/XiaoJiaAddonsKeybinds.cfg");
         Files.write(var5, var4.getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   @SubscribeEvent
   public void onKey(InputEvent.KeyInputEvent var1) {
      if (Checker.enabled) {
         Iterator var2 = XiaojiaKeyBind.keyBinds.values().iterator();

         while(true) {
            XiaojiaKeyBind var3;
            do {
               do {
                  if (!var2.hasNext()) {
                     return;
                  }

                  var3 = (XiaojiaKeyBind)var2.next();
               } while(!var3.getBind().isPressed());
            } while(var3.getCommand().startsWith("/") && ClientCommandHandler.instance.executeCommand(XiaojiaAddons.mc.thePlayer, var3.getCommand()) != 0);

            XiaojiaAddons.mc.thePlayer.sendChatMessage(var3.getCommand());
         }
      }
   }

   public static void list() {
      if (XiaojiaKeyBind.keyBinds.size() == 0) {
         ChatLib.chat("You have no keybind.");
      } else {
         Iterator var0 = XiaojiaKeyBind.keyBinds.values().iterator();

         while(var0.hasNext()) {
            XiaojiaKeyBind var1 = (XiaojiaKeyBind)var0.next();
            ChatComponentText var2 = new ChatComponentText(ChatLib.addColor("&9[XJA] > &e" + var1.getCommand() + " &b(" + Keyboard.getKeyName(var1.getBind().getKeyCode()) + ") &r&c&l[REMOVE]"));
            ChatStyle var3 = new ChatStyle();
            var3.setChatClickEvent(new ClickEvent(Action.RUN_COMMAND, "/xj keybind removeWithKey " + var1.getBind().getKeyCode() + " " + var1.getCommand()));
            var3.setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Click to remove this keybind")));
            var2.setChatStyle(var3);
            XiaojiaAddons.mc.thePlayer.addChatMessage(var2);
         }

      }
   }

   public static void remove(String var0, String var1) {
      int var2;
      try {
         var2 = Integer.parseInt(var1);
      } catch (Exception var4) {
         ChatLib.chat("Not a number!");
         return;
      }

      XiaojiaKeyBind var3 = XiaojiaKeyBind.getKeybind(var0, var2);
      if (var3 == null) {
         ChatLib.chat("No such keybind! Are the cases and keycode matching correctly?");
      } else {
         XiaojiaAddons.mc.gameSettings.keyBindings = (KeyBinding[])((KeyBinding[])ArrayUtils.removeElement((Object[])XiaojiaAddons.mc.gameSettings.keyBindings, var3.getBind()));
         XiaojiaKeyBind.keyBinds.remove(var0);
         saveKeyBinds();
         ChatLib.chat("&cRemoved&b keybind \"&e" + var0 + "&b\" (" + Keyboard.getKeyName(var3.getBind().getKeyCode()) + ") !");
      }
   }

   public static void add(String var0) {
      if (XiaojiaKeyBind.keyBinds.containsKey(var0)) {
         ChatLib.chat("Keybind already exists!");
      } else {
         XiaojiaKeyBind var1 = new XiaojiaKeyBind(var0, 0);
         XiaojiaKeyBind.keyBinds.put(var0, var1);
         saveKeyBinds();
         ChatLib.chat("&aAdded&b keybind \"&e" + var0 + "&b\"!");
      }
   }

   public static void remove(String var0) {
      XiaojiaKeyBind var1 = XiaojiaKeyBind.getKeybind(var0, -1);
      if (var1 == null) {
         ChatLib.chat("No such keybind! Are the cases matching correctly?");
      } else {
         XiaojiaAddons.mc.gameSettings.keyBindings = (KeyBinding[])((KeyBinding[])ArrayUtils.removeElement((Object[])XiaojiaAddons.mc.gameSettings.keyBindings, var1.getBind()));
         XiaojiaKeyBind.keyBinds.remove(var0);
         saveKeyBinds();
         ChatLib.chat("&cRemoved&b keybind \"&e" + var0 + "&b\"!");
      }
   }

   public static String getUsage() {
      return "&c/xj keybind add &ecommand&b to add keybind.\n&c/xj keybind remove &ecommand&b to remove keybind.\n&c/xj keybinds &bto list all keybinds.\n&eKeybinds are set in Options -> Controls.";
   }
}
