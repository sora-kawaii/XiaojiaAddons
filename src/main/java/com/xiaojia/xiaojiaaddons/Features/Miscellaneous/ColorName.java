package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.PacketUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ColorName {

   private static final HashMap cachedColorName = new HashMap();

   public static HashMap colorMap = new HashMap();

   public static HashMap rankMap = new HashMap();

   public static void setColorMap(String var0, String var1) {
      Type var2 = (new TypeToken() {
      }).getType();
      rankMap = (HashMap)(new Gson()).fromJson(var0, var2);
      colorMap = (HashMap)(new Gson()).fromJson(var1, var2);
      cachedColorName.clear();
   }

   @SubscribeEvent
   public void onTitlePacketReceived(PacketReceivedEvent var1) {
      if (Checker.enabled) {
         if (Configs.ColorNameTitle) {
            if (var1.packet instanceof S45PacketTitle) {
               S45PacketTitle var2 = (S45PacketTitle)var1.packet;
               IChatComponent var3 = PacketUtils.getMessage(var2);
               if (var3 != null) {
                  PacketUtils.setMessage(var2, convert(var3));
               }
            }

         }
      }
   }

   public static String addColorName(String var0) {
      if (var0 == null) {
         return null;
      } else if (colorMap == null) {
         return var0;
      } else {
         String var1 = var0;
         if (cachedColorName.containsKey(var0)) {
            return (String)cachedColorName.get(var0);
         } else {
            String var6;
            for(Iterator var2 = colorMap.keySet().iterator(); var2.hasNext(); var0 = var0.replace("ᄅ", var6)) {
               String var3 = (String)var2.next();
               String var4 = (String)colorMap.get(var3);
               String var5 = "(§7|§.\\[(MVP|VIP)] |§.\\[(MVP|VIP)(§.)*\\++(§.)*] )(§.)*" + var3;
               var0 = var0.replaceAll(var5, "ᄅ");
               var0 = var0.replaceAll("(?<=§bXJC > §r§8)" + var3, "ᄅ");
               var0 = var0.replace(var3, var4 + var3 + "&r");
               var6 = (String)colorMap.get(var3) + var3 + "&r";
               if (!((String)rankMap.get(var3)).equals("")) {
                  var6 = (String)rankMap.get(var3) + " " + var6;
               }
            }

            String var7 = ChatLib.addColor(var0);
            addToCache(var1, var7);
            return var7;
         }
      }
   }

   private static Pair addColorNameToScoreboard(String var0, String var1) {
      String var2 = ChatLib.removeFormatting(var0 + var1);
      if (var2.startsWith("[") && var2.contains(" ")) {
         String var3 = var2.split(" ")[1];
         String var4 = ChatLib.removeFormatting(var0).split(" ")[1];
         boolean var5 = var4.length() < var3.length();
         Iterator var6 = colorMap.keySet().iterator();

         String var7;
         do {
            if (!var6.hasNext()) {
               return new Pair(var0, var1);
            }

            var7 = (String)var6.next();
         } while(!var7.contains(var3));

         String var8 = ChatLib.addColor((String)colorMap.get(var7));
         int var9 = var0.indexOf(var4);
         String var10 = var0.substring(0, var9) + var8 + var0.substring(var9);
         String var11 = var1;
         if (var5) {
            var11 = var8 + var1.substring(ChatLib.getPrefix(ChatLib.removeColor(var1)).length());
         }

         return new Pair(var10, var11);
      } else {
         return new Pair(var0, var1);
      }
   }

   public static IChatComponent convert(IChatComponent var0) {
      List var1 = var0.getSiblings();
      ChatStyle var2 = var0.getChatStyle();
      if (var1.isEmpty()) {
         if (!(var0 instanceof ChatComponentTranslation)) {
            ChatComponentText var8 = new ChatComponentText(addColorName(var0.getFormattedText()));
            var8.setChatStyle(var2);
            return var8;
         } else {
            System.out.println(var0.getFormattedText());
            return var0;
         }
      } else {
         int var3 = var0.getFormattedText().indexOf(((IChatComponent)var1.get(0)).getFormattedText());
         ChatComponentText var4 = new ChatComponentText(var0.getFormattedText().substring(0, var3));
         var4.setChatStyle(var2);
         var1.add(0, var4);
         var1 = compactSiblings(var1);
         IChatComponent var5 = null;
         Iterator var6 = var1.iterator();

         while(var6.hasNext()) {
            IChatComponent var7 = (IChatComponent)var6.next();
            if (var5 == null) {
               var5 = convert(var7);
            } else {
               var5.appendSibling(convert(var7));
            }
         }

         return var5;
      }
   }

   private static void addToCache(String var0, String var1) {
      if (cachedColorName.size() > 10000) {
         System.err.println("Color name cache too big! Clearing cache...");
         cachedColorName.clear();
      }

      cachedColorName.put(var0, var1);
   }

   @SubscribeEvent(
      priority = EventPriority.LOW
   )
   public void onMessageReceived(ClientChatReceivedEvent var1) {
      if (Checker.enabled) {
         if (var1.type != 2) {
            if (Configs.ColorNameChat) {
               IChatComponent var2 = var1.message.createCopy();
               var1.message = convert(var2);
            }
         }
      }
   }

   @SubscribeEvent
   public void onScoreBoardPacketReceived(PacketReceivedEvent var1) {
      if (Checker.enabled) {
         if (Configs.ColorNameScoreboard) {
            if (var1.packet instanceof S3EPacketTeams) {
               S3EPacketTeams var2 = (S3EPacketTeams)var1.packet;
               Field var3 = null;
               Field var4 = null;

               try {
                  var3 = S3EPacketTeams.class.getDeclaredField("field_149319_c");
                  var4 = S3EPacketTeams.class.getDeclaredField("field_149316_d");
               } catch (NoSuchFieldException var9) {
                  return;
               }

               var3.setAccessible(true);
               var4.setAccessible(true);

               try {
                  String var5 = (String)var3.get(var2);
                  String var6 = (String)var4.get(var2);
                  Pair var7 = addColorNameToScoreboard(var5, var6);
                  var3.set(var2, var7.getKey());
                  var4.set(var2, var7.getValue());
               } catch (Exception var8) {
               }
            }

         }
      }
   }

   @SubscribeEvent
   public void onTabPacketReceived(PacketReceivedEvent var1) {
      if (Checker.enabled) {
         if (Configs.ColorNameTab) {
            if (var1.packet instanceof S38PacketPlayerListItem) {
               S38PacketPlayerListItem var2 = (S38PacketPlayerListItem)var1.packet;
               List var3 = var2.getEntries();
               ArrayList var4 = new ArrayList();
               Iterator var5 = var3.iterator();

               while(var5.hasNext()) {
                  S38PacketPlayerListItem.AddPlayerData var6 = (S38PacketPlayerListItem.AddPlayerData)var5.next();
                  if (var6.getDisplayName() == null) {
                     var4.add(var6);
                  } else {
                     String var7 = var6.getDisplayName().getUnformattedText();
                     String[] var8 = var7.split(" ");
                     if (var8.length == 0) {
                        return;
                     }

                     byte var9 = 0;
                     if (var8[0].matches("^\\[.*]$")) {
                        var9 = 1;
                     }

                     if (var8.length <= var9) {
                        return;
                     }

                     var7 = var8[var9];
                     if (colorMap.containsKey(var7)) {
                        // TODO: fix
//                        String var10 = var6.getDisplayName().getFormattedText().replaceAll(var7, (String)colorMap.get(var7) + var7);
//                        ChatComponentText var11 = new ChatComponentText(ChatLib.addColor(var10));
//                        var2.getClass();
//                        S38PacketPlayerListItem.AddPlayerData var12 = new S38PacketPlayerListItem.AddPlayerData(var2, var6.getProfile(), var6.getPing(), var6.getGameMode(), var11);
//                        var4.add(var12);
                     } else {
                        var4.add(var6);
                     }
                  }
               }

               try {
                  var5 = null;

                  Field var15;
                  try {
                     var15 = S38PacketPlayerListItem.class.getDeclaredField("players");
                  } catch (NoSuchFieldException var13) {
                     var15 = S38PacketPlayerListItem.class.getDeclaredField("field_179769_b");
                  }

                  var15.setAccessible(true);
                  var15.set(var1.packet, var4);
               } catch (Exception var14) {
               }
            }

         }
      }
   }

   @SubscribeEvent
   public void onToolTip(ItemTooltipEvent var1) {
      if (Checker.enabled) {
         if (Configs.ColorNameItem) {
            String var2 = NBTUtils.getUUID(var1.itemStack);
            if (!var2.equals("")) {
               List var3 = (List)var1.toolTip.stream().map(ColorName::addColorName).collect(Collectors.toList());
               var1.toolTip.clear();
               var1.toolTip.addAll(var3);
            }
         }
      }
   }

   private static List compactSiblings(List var0) {
      StringBuilder var1 = new StringBuilder();
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var0.size(); ++var3) {
         IChatComponent var4 = (IChatComponent)var0.get(var3);
         ClickEvent var5 = var4.getChatStyle().getChatClickEvent();
         HoverEvent var6 = var4.getChatStyle().getChatHoverEvent();
         if ((var5 == null || var5.getAction() == Action.SUGGEST_COMMAND) && var6 == null) {
            var1.append(var4.getFormattedText());
         } else {
            var2.add(new ChatComponentText(var1.toString()));
            var2.add(var4);
            var1 = new StringBuilder();
         }
      }

      if (!var1.toString().equals("")) {
         var2.add(new ChatComponentText(var1.toString()));
      }

      return var2;
   }

   public static String addColorNameWithPrefix(String var0) {
      if (var0 == null) {
         return null;
      } else if (colorMap == null) {
         return var0;
      } else if (cachedColorName.containsKey(var0)) {
         return (String)cachedColorName.get(var0);
      } else if (!var0.contains("'s")) {
         return addColorName(var0);
      } else {
         String var1 = ChatLib.removeFormatting(var0);
         Iterator var2 = colorMap.keySet().iterator();

         String var3;
         String var4;
         do {
            if (!var2.hasNext()) {
               return addColorName(var0);
            }

            var3 = (String)var2.next();
            var4 = (String)colorMap.get(var3);
         } while(!var1.matches("\\[Lv[0-9]+] " + var3 + "'s.*"));

         String var5 = var0.replace(var3, var4 + var3 + ChatLib.getPrefix(ChatLib.removeColor(var0.replaceAll(".*\\[.*] ", ""))));
         var5 = ChatLib.addColor(var5);
         addToCache(var0, var5);
         return var5;
      }
   }

   @SubscribeEvent
   public void onPlayerJoin(EntityJoinWorldEvent var1) {
      if (Checker.enabled) {
         if (Configs.ColorNameNameTag) {
            if (var1.entity instanceof EntityPlayer) {
               EntityPlayer var2 = (EntityPlayer)var1.entity;
               Field var3 = null;

               try {
                  var3 = EntityPlayer.class.getDeclaredField("displayname");
               } catch (NoSuchFieldException var8) {
                  return;
               }

               var3.setAccessible(true);

               try {
                  String var4 = var2.getDisplayName().getFormattedText();
                  String var5 = addColorName(var4);
                  if (var5.equals(var4)) {
                     return;
                  }

                  var3.set(var2, var5);
                  ScorePlayerTeam var6 = XiaojiaAddons.mc.getNetHandler().getPlayerInfo(var2.getName()).getPlayerTeam();
                  var6.setNamePrefix("");
                  var6.setNameSuffix("");
               } catch (Exception var7) {
               }
            }

         }
      }
   }

   public static void showCache() {
      HashMap var0 = new HashMap(cachedColorName);
      Iterator var1 = var0.entrySet().iterator();

      Map.Entry var2;
      while(var1.hasNext()) {
         var2 = (Map.Entry)var1.next();
         System.err.println((String)var2.getKey() + " -> " + (String)var2.getValue());
      }

      System.err.println();
      var1 = rankMap.entrySet().iterator();

      while(var1.hasNext()) {
         var2 = (Map.Entry)var1.next();
         System.err.println((String)var2.getKey() + ": " + (String)var2.getValue());
      }

   }

   public static int getCacheSize() {
      return cachedColorName.size();
   }
}
