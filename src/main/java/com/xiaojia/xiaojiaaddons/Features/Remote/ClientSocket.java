package com.xiaojia.xiaojiaaddons.Features.Remote;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water.Patterns;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.ColorName;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.lwjgl.opengl.Display;

public class ClientSocket {

   private static int socketId = 0;

   private static Socket socket = null;

   private static PrintWriter out;

   private static boolean connected = true;

   private static BufferedReader in;

   private static Thread socketThread = null;

   public static void connect() {
      Display.setTitle("XiaojiaAddons 2.4.8.3 [CRACKED/DEOBFUSCATED BY ソラ]");
   }

   public static synchronized void chat(String var0) {
   }

   public static void disconnect() {
      ++socketId;

      try {
         socket.close();
      } catch (Exception var1) {
         var1.printStackTrace();
      }

      connected = false;
   }

   public static void authenticate() {
   }

   public static void reconnect() {
      disconnect();
      connect();
   }

   // $FF: synthetic method
   private static void lambda$connect$3() {
      int var0 = socketId;

      try {
         authenticate();
         boolean var1 = true;

         while(true) {
            StringBuilder var2 = new StringBuilder();
            int var3 = in.read();
            if (var3 == -1) {
               break;
            }

            if (var1) {
               var1 = false;
            }

            while((char)var3 != '\n') {
               var2.append((char)var3);
               var3 = in.read();
               if (var3 == -1) {
                  return;
               }
            }

            String var4 = var2.toString();
            var4 = XiaojiaAddons.cipherUtils.decrypt(var4);
            Pattern var5 = Pattern.compile("^\\{\"uuid\": \"(.*)\", \"name\": \"(.*)\", \"msg\": \"(.*)\", \"type\": \"(.*)\"}$");
            Matcher var6 = var5.matcher(var4);
            String var7;
            String var8;
            String var9;
            int var10;
            if (var6.find()) {
               var7 = var6.group(1);
               var8 = var6.group(2);
               var9 = var6.group(3);
               var10 = Integer.parseInt(var6.group(4));

               assert var10 == 0 || var10 == 1 || var10 == 2;

               ChatLib.xjchat(var10, var8, var9);
            } else {
               var5 = Pattern.compile("^\\{\"uuid\": \"(.*)\", \"name\": \"(.*)\", \"dis\": \"(.*)\", \"type\": \"(.*)\", \"nbt\": \"(.*)\"}$");
               var6 = var5.matcher(var4);
               if (var6.find()) {
                  var7 = var6.group(1);
                  var8 = var6.group(2);
                  var9 = var6.group(3);
                  var10 = Integer.parseInt(var6.group(4));
                  String var11 = var6.group(5);

                  assert var10 == 3;

                  ChatLib.showItem(var10, var8, var9, var11);
               } else {
                  var5 = Pattern.compile("^\\{\"uuid\": \"(.*)\", \"name\": \"(.*)\", \"type\": \"(.*)\", \"ver\": \"(.*)\"}$");
                  var6 = var5.matcher(var4);
                  int var18;
                  if (var6.find()) {
                     var7 = var6.group(1);
                     var8 = var6.group(2);
                     var18 = Integer.parseInt(var6.group(3));
                     String var20 = var6.group(4);

                     assert var18 == 4;

                     Checker.onAuth();
                  } else {
                     var5 = Pattern.compile("^\\{\"uuid\": \"(.*)\", \"name\": \"(.*)\", \"type\": \"(.*)\"}$");
                     var6 = var5.matcher(var4);
                     if (var6.find()) {
                        var7 = var6.group(1);
                        var8 = var6.group(2);
                        var18 = Integer.parseInt(var6.group(3));

                        assert var18 == 5 || var18 == 6;

                        if (var18 == 5) {
                           ChatLib.playerJoin(var8);
                        } else {
                           ChatLib.playerLeft(var8);
                        }
                     } else {
                        var5 = Pattern.compile("^\\{\"type\": \"(.*)\", \"msg\": \"((.*\\n*)*)\"}$");
                        var6 = var5.matcher(var4);
                        int var17;
                        if (var6.find()) {
                           var17 = Integer.parseInt(var6.group(1));
                           var8 = var6.group(2);

                           assert var17 == 7;

                           ChatLib.showXJCMessage(var8);
                        } else {
                           var5 = Pattern.compile("^\\{\"type\": \"(.*)\", \"rank\": \"(.*)\", \"color\": \"(.*)\"}$");
                           var6 = var5.matcher(var4);
                           if (var6.find()) {
                              var17 = Integer.parseInt(var6.group(1));
                              var8 = var6.group(2);
                              var9 = var6.group(3);

                              assert var17 == 8;

                              (new Thread(() -> {
                                 ColorName.setColorMap(var8, var9);
                              })).start();
                           } else {
                              var5 = Pattern.compile("^\\{\"type\": \"(.*)\", \"water\": \"(.*)\"}$");
                              var6 = var5.matcher(var4);
                              if (var6.find()) {
                                 var17 = Integer.parseInt(var6.group(1));
                                 var8 = var6.group(2);

                                 assert var17 == 11;

                                 (new Thread(() -> {
                                    Patterns.load(var8);
                                 })).start();
                              } else {
                                 var5 = Pattern.compile("^\\{\"type\": \"(.*)\", \"timestamp\": \"(.*)\"}$");
                                 var6 = var5.matcher(var4);
                                 if (var6.find()) {
                                    var17 = Integer.parseInt(var6.group(1));
                                    long var19 = Long.parseLong(var6.group(2));

                                    assert var17 == 12;

                                    authenticate();
                                 } else {
                                    var5 = Pattern.compile("^\\{\"type\": \"(.*)\"}$");
                                    var6 = var5.matcher(var4);
                                    if (var6.find()) {
                                       var17 = Integer.parseInt(var6.group(1));
                                       if (var17 == 69) {
                                          (new Thread(() -> {
                                          })).start();
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      } catch (Exception var15) {
         var15.printStackTrace();
      } finally {
         if (socketId == var0) {
            reconnect();
         }

      }

   }
}
