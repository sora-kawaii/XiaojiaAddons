package com.xiaojia.xiaojiaaddons.Features.Remote;

import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.SessionUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class XiaojiaChat {
   @SubscribeEvent
   public void onChatReceive(ClientChatReceivedEvent var1) {
      if (Checker.enabled) {
         String var2 = var1.message.getFormattedText();
         String var3 = ChatLib.removeFormatting(var1.message.getUnformattedText());
         if (var3.startsWith("PUZZLE FAIL! " + MinecraftUtils.getPlayer().getName())) {
            chat(var2, 1);
         }

         if (var3.startsWith(" ☠ You ")) {
            chat(var2, 2);
         }

      }
   }

   public static void ping() {
      (new Thread(() -> {
         System.out.println("ping: " + RemoteUtils.get("xjaddons_ping"));
      })).start();
   }

   public static void uploadLoot(String var0, int var1, String var2, List var3) {
      String var4 = (String)var3.stream().reduce("", (var0x, var1x) -> {
         return var0x.equals("") ? var1x : var0x + ", " + var1x;
      });
      String var5 = String.format("{\"uuid\": \"%s\", \"name\": \"%s\", \"floor\": \"%s\", \"score\": \"%d\", \"chest\": \"%s\", \"loots\": \"%s\", \"type\": \"%d\"}", getUUID(), MinecraftUtils.getPlayer().getName(), var0, var1, var2, var4, 9);
      (new Thread(() -> {
         ClientSocket.chat(var5);
      })).start();
   }

   public static void chat(String var0, int var1) {
      String var2 = String.format("{\"uuid\": \"%s\", \"name\": \"%s\", \"msg\": \"%s\", \"type\": \"%d\"}", getUUID(), MinecraftUtils.getPlayer().getName(), var0, var1);
      (new Thread(() -> {
         ClientSocket.chat(var2);
      })).start();
   }

   public static void queryOnline() {
      String var0 = "{\"type\": \"7\"}";
      (new Thread(() -> {
         ClientSocket.chat(var0);
      })).start();
   }

   public static String getUUID() {
      return SessionUtils.getUUID();
   }

   public static void bugReport(String var0) {
      StringBuilder var1 = new StringBuilder();

      try {
         File var2 = new File("logs/fml-client-latest.log");
         BufferedReader var3 = new BufferedReader(new InputStreamReader(new FileInputStream(var2), "GBK"));

         String var4;
         while((var4 = var3.readLine()) != null) {
            var1.append(var4);
            var1.append("\n");
         }

         String var5 = String.format("{\"uuid\": \"%s\", \"name\": \"%s\", \"introduction\": \"%s\", \"type\": \"%d\"}", getUUID(), MinecraftUtils.getPlayer().getName(), var0, 10);
         (new Thread(() -> {
            ClientSocket.chat(var5);
            ClientSocket.chat(var1.toString());
            ChatLib.chat("Bug report has been successfully sent. Thank you for reporting!");
         })).start();
      } catch (Exception var6) {
         var6.printStackTrace();
         ChatLib.chat("Failed to send bug report. Please ensure you have logs/fml-client-latest.log!");
      }

   }

   public static void chat(String var0, String var1, int var2) {
      String var3 = String.format("{\"uuid\": \"%s\", \"name\": \"%s\", \"dis\": \"%s\", \"type\": \"%d\", \"nbt\": \"%s\"}", getUUID(), MinecraftUtils.getPlayer().getName(), var1, var2, var0);
      (new Thread(() -> {
         ClientSocket.chat(var3);
      })).start();
   }
}
