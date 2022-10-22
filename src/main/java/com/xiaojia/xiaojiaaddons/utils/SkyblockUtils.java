package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.QOL.AutoLobby;
import com.xiaojia.xiaojiaaddons.Objects.ScoreBoard;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SkyblockUtils {

   private long lastPing;

   private static String dungeon = "F6";

   private static boolean isInCrystalHollows = false;

   private static final ArrayList crystalHollowsMaps = new ArrayList(Arrays.asList("Fairy Grotto", "Goblin Holdout", "Goblin Queen's Den", "Jungle", "Jungle Temple", "Mines of Divan", "Mithril Deposits", "Lost Precursor City", "Precursor Remnants", "Magma Fields", "Crystal Nucleus", "Khazad", "Dragon's Lair"));

   public static int pingsIndex = 0;

   private static boolean set = false;

   private static final ArrayList dwarvenMaps = new ArrayList(Arrays.asList("The Forge", "Forge Basin", "Palace Bridge", "Royal Palace", "Aristocrat Passage", "Hanging Court", "Divan's Gateway", "Far Reserve", "Goblin Burrows", "Miner's Guild", "Great Ice Wall", "The Mist", "C&", "Grand Library", "Barracks of Heroes", "Dwarven Village", "The Lift", "Royal Quarters", "Lava Springs", "Cliffside Veins", "Rampart's Quarry", "Upper Mines", "Royal Mines", "Dwarven Mines", "Gates to the Mines"));

   private static final ArrayList maps = new ArrayList(Arrays.asList("Ruins", "Forest", "Mountain", "High Level", "Wilderness", "Dungeon Hub", "Kuudra's End", "Deep Caverns", "Hub", "Spider's Den", "Gunpowder Mines", "Void Sepulture", "Dragon's Nest", "The End", "The Mist", "Blazing Fortress", "The Catacombs", "Howling Cave", "Your Island", "Dojo Arena", "None"));

   private static final ArrayList netherMaps = new ArrayList(Arrays.asList("Barbarian Outpost", "The Bastion", "Blazing Volcano", "Burning Desert", "Cathedral", "Crimson Fields", "Dojo", "Dragontail", "Forgotten Skull", "Kuudra's End", "Mage Outpost", "Magma Chamber", "Mystic Marsh", "Odger's Hut", "Ruins of Ashfang", "Stronghold", "The Wasteland"));

   public static int calculatedPing = -1;

   private Thread pingThread;

   private static final ArrayList m7ScoreBoards = new ArrayList(Arrays.asList("Soul Dragon", "Ice Dragon", "Apex Dragon", "Flame Dragon", "Power Dragon", "No Alive Dragons"));

   private static String currentMap = "";

   private static String currentServer = "";

   public static long[] pings = new long[]{-1L, -1L, -1L, -1L, -1L};

   private static boolean isInDwarven = false;

   private static boolean isInNether = false;

   public static boolean isInKuudra() {
      return currentMap.equals("Kuudra's End");
   }

   public static boolean isInMysticMarsh() {
      return currentMap.equals("Mystic Marsh");
   }

   public static int getPing() {
      return calculatedPing;
   }

   public static boolean isInGunpowderMines() {
      return currentMap.equals("Gunpowder Mines") || currentMap.equals("Deep Caverns");
   }

   public static String getCurrentMap() {
      return currentMap;
   }

   @SubscribeEvent
   public void onTick(TickEndEvent event) {
      if (!set || !XiaojiaAddons.isDebug()) {
         currentMap = updateCurrentMap();
      }

      if (isInSkyblock() && (this.pingThread == null || !this.pingThread.isAlive())) {
         long var2 = TimeUtils.curTime();
         if (var2 - this.lastPing > 1500L) {
            this.lastPing = var2;
            this.pingThread = new Thread(() -> {
               ChatLib.say("/whereami");

               try {
                  Thread.sleep(60000L);
               } catch (InterruptedException var4) {
                  long var2_ = TimeUtils.curTime() - this.lastPing;
                  pings[pingsIndex] = var2_;
                  calculatedPing = calcPing();
                  pingsIndex = (pingsIndex + 1) % 5;
               }

            });
            this.pingThread.start();
         }
      }

   }

   public static boolean isInSpiderDen() {
      return currentMap.equals("Spider's Den");
   }

   public static boolean isInDwarven() {
      return isInDwarven;
   }

   public static boolean isInRuin() {
      return currentMap.equals("Ruins");
   }

   public static double getVelocity() {
      return AutoLobby.getVelocity();
   }

   public static boolean isInDragon() {
      return currentMap.equals("Dragon's Nest");
   }

   public static double getMotionY() {
      return MinecraftUtils.getPlayer().motionY;
   }

   public static boolean isInMountain() {
      return currentMap.equals("Mountain");
   }

   public static String getCurrentServer() {
      return currentServer;
   }

   public static boolean isInDojo() {
      return currentMap.equals("Dojo Arena");
   }

   public static int calcPing() {
      int var0 = 0;
      int var1 = 0;
      long[] var2 = pings;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         long var5 = var2[var4];
         if (var5 != -1L) {
            var1 = (int)((long)var1 + var5);
            ++var0;
         }
      }

      if (var0 == 0) {
         return -1;
      } else {
         return var1 / var0;
      }
   }

   public static boolean isInAshFang() {
      return currentMap.equals("Ruins of Ashfang");
   }

   public static boolean isInSkyblock() {
      return ChatLib.removeFormatting(ScoreBoard.title).contains("SKYBLOCK");
   }

   private static String updateCurrentMap() {
      ArrayList var0 = ScoreBoard.getLines();
      if (var0.size() < 5) {
         return "";
      } else {
         String var1 = (String)var0.get(var0.size() - 3) + (String)var0.get(var0.size() - 4) + (String)var0.get(var0.size() - 5);
         StringBuilder var2 = new StringBuilder();
         var1 = ChatLib.removeFormatting(var1);

         for(int var3 = 0; var3 < var1.length(); ++var3) {
            char var4 = var1.charAt(var3);
            if (var4 >= 'A' && var4 <= 'Z' || var4 >= 'a' && var4 <= 'z' || var4 >= '0' && var4 <= '9' || var4 == '\'' || var4 == ' ' || var4 == '(' || var4 == ')' || var4 == 251 || var4 == '&') {
               var2.append(var4);
            }
         }

         var1 = var2.toString();
         Iterator var6 = m7ScoreBoards.iterator();

         while(var6.hasNext()) {
            String var8 = (String)var6.next();
            if (var1.contains(var8)) {
               dungeon = "M7";
               return "The Catacombs";
            }
         }

         String var7 = "Others";
         Iterator var9 = maps.iterator();

         String var5;
         while(var9.hasNext()) {
            var5 = (String)var9.next();
            if (var1.contains(var5)) {
               var7 = var5;
               break;
            }
         }

         isInCrystalHollows = false;
         if (var7.equals("Others")) {
            var9 = crystalHollowsMaps.iterator();

            while(var9.hasNext()) {
               var5 = (String)var9.next();
               if (var1.contains(var5)) {
                  var7 = var5;
                  isInCrystalHollows = true;
               }
            }
         }

         isInDwarven = var7.equals("The Mist");
         if (var7.equals("Others")) {
            var9 = dwarvenMaps.iterator();

            while(var9.hasNext()) {
               var5 = (String)var9.next();
               if (var1.contains(var5)) {
                  var7 = var5;
                  isInDwarven = true;
               }
            }
         }

         if (var7.equals("Others") || var7.equals("Ruins")) {
            var9 = netherMaps.iterator();

            while(var9.hasNext()) {
               var5 = (String)var9.next();
               if (var1.contains(var5)) {
                  var7 = var5;
                  isInNether = true;
               }
            }
         }

         if (var7.equals("The Catacombs")) {
            Pattern var10 = Pattern.compile("The Catacombs \\((.*)\\)");
            Matcher var11 = var10.matcher(var1);
            if (var11.find()) {
               dungeon = var11.group(1);
            }
         }

         return var7;
      }
   }

   public static boolean isInHowlingCave() {
      return currentMap.equals("Howling Cave");
   }

   public static boolean isInVoidSepulture() {
      return currentMap.equals("Void Sepulture");
   }

   public static String getDungeon() {
      return dungeon;
   }

   @SubscribeEvent
   public void onChatReceived(ClientChatReceivedEvent var1) {
      String var2 = var1.message.getUnformattedText();
      Pattern var3 = Pattern.compile("^Sending to server (.*)\\.\\.\\..*");
      Matcher var4 = var3.matcher(var2);
      if (var4.find()) {
         currentServer = var4.group(1);
         set = false;
      }

   }

   public static boolean isInNether() {
      return isInNether;
   }

   public static boolean isInDungeon() {
      return currentMap.equals("The Catacombs");
   }

   public static boolean isInMist() {
      return currentMap.equals("The Mist");
   }

   public static void setDungeon(String var0) {
      dungeon = var0;
   }

   public static boolean isInForest() {
      return currentMap.equals("Forest");
   }

   public static boolean isInCrystalHollows() {
      return isInCrystalHollows;
   }

   public static boolean isInEndIsland() {
      return isInDragon() || isInTheEnd() || isInVoidSepulture();
   }

   public static void setCurrentMap(String var0) {
      currentMap = var0;
      set = true;
   }

   public static double getAccelerate() {
      return AutoLobby.getAccelerate();
   }

   public static double getCurrentY() {
      return AutoLobby.getCurrentY();
   }

   public static boolean isInTheEnd() {
      return currentMap.equals("The End");
   }

   @SubscribeEvent
   public void pingChatReceived(ClientChatReceivedEvent var1) {
      if (var1.type == 0) {
         if (ChatLib.removeFormatting(var1.message.getUnformattedText()).startsWith("You are currently connected to server") && this.pingThread != null && this.pingThread.isAlive()) {
            this.pingThread.interrupt();
            var1.setCanceled(true);
         }

      }
   }
}
