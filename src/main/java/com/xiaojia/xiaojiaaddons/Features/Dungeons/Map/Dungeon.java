package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.AutoBlaze;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water.WaterSolver;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.StonklessStonk;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Image;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec4b;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Dungeon {

    public static final HashMap floorSecrets = new HashMap() {
        {
            this.put("F1", 0.3);
            this.put("F2", 0.4);
            this.put("F3", 0.5);
            this.put("F4", 0.6);
            this.put("F5", 0.7);
            this.put("F6", 0.85);
            this.put("F7", 1.0);
        }
    };
    private static final String[] entryMessages = new String[]{"[BOSS] Bonzo: Gratz for making it this far, but I’m basically unbeatable.", "[BOSS] Scarf: This is where the journey ends for you, Adventurers.", "[BOSS] The Professor: I was burdened with terrible news recently...", "[BOSS] Thorn: Welcome Adventurers! I am Thorn, the Spirit! And host of the Vegan Trials!", "[BOSS] Livid: Welcome, you arrive right on time. I am Livid, the Master of Shadows.", "[BOSS] Sadan: So you made it all the way here... Now you wish to defy me? Sadan?!", "[BOSS] Necron: Finally, I heard so much about you. The Eye likes you very much.", "[BOSS] Maxor: I’VE BEEN TOLD I COULD HAVE A BIT OF FUN WITH YOU."};
    private static final KeyBind normalRoomNameKeyBind = new KeyBind("Display Normal Room Name Toggle", 0);
    public static Image newWhiteCheck = new Image("MapWhiteCheck.png");
    public static int floorInt;
    public static ArrayList rooms = new ArrayList();
    public static String trapType = "Unknown";
    public static int openedRooms;
    public static Image failedRoom = new Image("BloomMapFailedRoom.png");
    public static int endZ = 190;
    public static long bossEntry = 0L;
    public static Image whiteCheck = new Image("BloomMapWhiteCheck.png");
    public static int roomSize = 31;
    public static boolean bloodDone = false;
    public static int skillScore = 0;
    public static int deaths;
    public static int totalSecrets = 0;
    public static int openedWitherDoors = 0;
    public static Image newGreenCheck = new Image("MapGreenCheck.png");
    public static double secretsNeeded = 1.0;

    public static boolean isInDungeon = false;

    public static double secretsPercent = 0.0;

    public static Image questionMark = new Image("BloomMapQuestionMark.png");

    public static int secretsFound = 0;

    public static int score = 0;
    public static String currentRoom = "";
    public static int bonusScore = 0;
    public static int totalRooms = -1;
    public static boolean yellowDone = false;
    public static int witherDoors = 0;
    public static int puzzleCount = 0;
    public static long watcherDone = 0L;
    public static int completedRooms;
    public static boolean isFullyScanned = false;
    public static double secretsForMax = 0.0;
    public static boolean trapDone = false;
    public static ArrayList puzzles = new ArrayList();
    public static int startZ = -185;
    public static Image greenCheck = new Image("BloomMapGreenCheck.png");
    public static long runStarted = 0L;
    public static int puzzleDone = 0;
    public static boolean isMimicDead = false;
    public static boolean said270 = false;
    public static int startX = -185;
    public static int calculatedTotalSecrets = 0;
    public static boolean said300 = false;
    public static int exploreScore = 0;
    public static double overflowSecrets = 0.0;
    public static int endX = 190;
    public static ArrayList doors = new ArrayList();
    public static ArrayList unknownRooms = new ArrayList();
    public static long runEnded = 0L;
    public static long bloodOpen = 0L;
    public static int crypts;
    private static BufferedImage map = null;
    private static String scoreString2;
    private static Vector2i mapSize = new Vector2i(0, 0);
    private static long lastScan = 0L;
    private static ArrayList players = new ArrayList();
    private static String scoreString1;
    private static boolean isScanning = false;
    private boolean enableNormalRoomName = true;
    private HashSet scannedPuzzles = new HashSet();

    private static void setPixels(BufferedImage var0, int var1, int var2, int var3, int var4, Color var5) {
        for (int var6 = var1; var6 < var1 + var3; ++var6) {
            for (int var7 = var2; var7 < var2 + var4; ++var7) {
                var0.setRGB(var6, var7, var5.getRGB());
            }
        }

    }

    private static void checkRoom(String var0, String var1) {
        for (int var2 = 0; var2 < rooms.size(); ++var2) {
            Room var3 = (Room) rooms.get(var2);
            if (var3.name.equals(var0)) {
                var3.checkmark = var1;
            }
        }

    }

    public static void updateDoors() {
        ArrayList var0 = new ArrayList();

        Door var2;
        for (int var1 = 0; var1 < doors.size(); ++var1) {
            var2 = (Door) doors.get(var1);
            if (var2 != null) {
                int var3 = Block.getIdFromBlock(BlockUtils.getBlockAt(var2.x, 70, var2.z));
                if (var3 == 0 || var3 == 166) {
                    var2.type = "normal";
                }

                if (Configs.AlwaysHighlightWitherDoor && var2.type.equals("wither")) {
                    var2.explored = true;
                } else {
                    Room var4 = getRoomAt(Lookup.getRoomCenterCoords(new Vector2i(var2.x + 4, var2.z + 16)));
                    Room var5 = getRoomAt(Lookup.getRoomCenterCoords(new Vector2i(var2.x - 4, var2.z - 16)));
                    Room var6 = getRoomAt(Lookup.getRoomCenterCoords(new Vector2i(var2.x + 16, var2.z + 4)));
                    Room var7 = getRoomAt(Lookup.getRoomCenterCoords(new Vector2i(var2.x - 16, var2.z - 4)));
                    if (var4 != null && var5 != null && var4.x == var5.x) {
                        if (!var4.explored && !var5.explored) {
                            var2.explored = false;
                            continue;
                        }
                    } else if (var6 != null && var7 != null && var6.z == var7.z) {
                        if (!var6.explored && !var7.explored) {
                            var2.explored = false;
                            continue;
                        }
                    } else if (var2.type.equals("entrance")) {
                        var0.add(var2);
                    }

                    var2.explored = true;
                }
            }
        }

        Iterator var8 = var0.iterator();

        while (var8.hasNext()) {
            var2 = (Door) var8.next();
            doors.remove(var2);
        }

    }

    public static boolean isPlayerMage(String var0) {
        Iterator var1 = players.iterator();

        Player var2;
        do {
            if (!var1.hasNext()) {
                return false;
            }

            var2 = (Player) var1.next();
        } while (!var2.name.equals(var0));

        return var2.className.equals("MAGE");
    }

    public static void updateRooms() {
        byte[] var0 = Map.getMapColors();
        if (var0 != null) {
            int var1 = 0;
            int var2 = 0;

            for (int var3 = Map.startCorner.x + Map.roomSize / 2; var3 < 128; var3 += Map.roomSize / 2 + 2) {
                for (int var4 = Map.startCorner.y + Map.roomSize / 2; var4 < 128; var4 += Map.roomSize / 2 + 2) {
                    Vector2i var5 = Lookup.getRoomCenterCoords(new Vector2i(MathUtils.floor((float) (var3 - Map.startCorner.x) * 1.484375F) - 200, MathUtils.floor((float) (var4 - Map.startCorner.y) * 1.484375F) - 200));
                    if (var5 != null) {
                        byte var6 = var0[var3 + var4 * 128];
                        byte var7 = var0[var3 - 2 + var4 * 128];
                        Room var8 = getRoomAt(var5);
                        if (var8 != null) {
                            if (var1 % 2 == 0 && var2 % 2 == 0) {
                                if (var6 == 30 && var7 != 30) {
                                    checkRoom(var8.name, "green");
                                }

                                if (var6 == 34) {
                                    checkRoom(var8.name, "white");
                                }

                                if (var6 == 18 && var7 != 18) {
                                    checkRoom(var8.name, "failed");
                                }

                                if (var6 == 30 || var6 == 34) {
                                    if (var7 == 62) {
                                        trapDone = true;
                                    }

                                    if (var7 == 18) {
                                        bloodDone = true;
                                    }

                                    if (var7 == 74) {
                                        yellowDone = true;
                                    }
                                }

                                setExplored(var8.name, var6 != 0 && var6 != 85 && var6 != 119);
                            }

                            ++var2;
                        }
                    }
                }

                ++var1;
            }

        }
    }

    private static Color darker(Color var0) {
        return new Color(var0.getRed() * Configs.DarkenUnexploredPercent / 100, var0.getGreen() * Configs.DarkenUnexploredPercent / 100, var0.getBlue() * Configs.DarkenUnexploredPercent / 100);
    }

    public static Room getRoomAt(Vector2i var0) {
        var0 = Lookup.getRoomCenterCoords(var0);
        if (var0 == null) {
            return null;
        } else {
            for (int var1 = 0; var1 < rooms.size(); ++var1) {
                Room var2 = (Room) rooms.get(var1);
                if (var2.x == var0.x && var2.z == var0.y) {
                    return var2;
                }
            }

            return null;
        }
    }

    public static void showPlayers() {
        Iterator var0 = players.iterator();

        while (var0.hasNext()) {
            Player var1 = (Player) var0.next();
            System.err.println(var1.name + ": " + var1.className);
        }

    }

    public static void updatePlayers() {
        if (isInDungeon) {
            ArrayList var0 = TabUtils.getNames();
            if (var0.size() >= 18) {
                int var1 = 0;
                int[] var2 = new int[]{5, 9, 13, 17, 1};
                int var3 = var2.length;

                for (int var4 = 0; var4 < var3; ++var4) {
                    int var5 = var2[var4];
                    String var6 = ChatLib.removeFormatting((String) var0.get(var5)).trim();
                    boolean var7 = var6.contains("(DEAD)");
                    if (var6.contains(" ")) {
                        String var8 = var6.split(" ")[0];
                        if (var8.contains("[")) {
                            var8 = var6.split(" ")[1];
                        }

                        if (var8.length() != 0) {
                            String var9 = "";
                            if (var6.toUpperCase().contains("(MAGE")) {
                                var9 = "MAGE";
                            } else if (var6.toUpperCase().contains("(ARCHER")) {
                                var9 = "ARCHER";
                            } else if (var6.toUpperCase().contains("(TANK")) {
                                var9 = "TANK";
                            } else if (var6.toUpperCase().contains("(HEALER")) {
                                var9 = "HEALER";
                            } else if (var6.toUpperCase().contains("(BERSERK")) {
                                var9 = "BERSERK";
                            }

                            boolean var10 = false;

                            for (int var11 = 0; var11 < players.size(); ++var11) {
                                Player var12 = (Player) players.get(var11);
                                if (var12.name.equals(var8)) {
                                    var10 = true;
                                    var12.isDead = var7;
                                    var12.className = var9;
                                    var12.icon = var7 ? null : "icon-" + var1;
                                }
                            }

                            if (!var10) {
                                Player var20 = new Player();
                                var20.name = var8;
                                var20.isDead = var7;
                                var20.fetchHead();
                                var20.icon = var7 ? null : "icon-" + var1;
                                var20.className = var9;
                                players.add(var20);
                            }

                            if (!var7) {
                                ++var1;
                            }
                        }
                    }
                }

                java.util.Map var13 = Map.getMapDecorators();
                if (var13 != null) {
                    Iterator var14 = var13.entrySet().iterator();

                    while (var14.hasNext()) {
                        java.util.Map.Entry var15 = (java.util.Map.Entry) var14.next();
                        String var16 = (String) var15.getKey();
                        Vec4b var17 = (Vec4b) var15.getValue();

                        for (int var18 = 0; var18 < players.size(); ++var18) {
                            Player var19 = (Player) players.get(var18);
                            if (!var19.inRender && var16.equals(var19.icon) && !MinecraftUtils.getPlayer().getName().equals(var19.name)) {
                                var19.iconX = ((double) (var17.func_176112_b() + 128) - (double) Map.startCorner.x * 2.5) / 10.0 * (double) Configs.MapScale;
                                var19.iconY = ((double) (var17.func_176113_c() + 128) - (double) Map.startCorner.y * 2.5) / 10.0 * (double) Configs.MapScale;
                                var19.yaw = (float) (var17.func_176111_d() * 360) / 16.0F + 180.0F;
                                var19.realX = var19.iconX * 1.64;
                                var19.realZ = var19.iconY * 1.64;
                            }
                        }
                    }
                }

            }
        }
    }

    public static void makeMap() {
        BufferedImage var0 = new BufferedImage(25, 25, 6);

        int var1;
        Color var3;
        for (var1 = 0; var1 < rooms.size(); ++var1) {
            Room var2 = (Room) rooms.get(var1);
            var3 = var2.getColor();
            if (var2.name.equals("Unknown")) {
                var3 = new Color(255, 176, 31);
            } else if (!var2.explored && Configs.DarkenUnexplored) {
                var3 = darker(var3);
            }

            setPixels(var0, MathUtils.floor((float) ((var2.x + 200) / 16)) * 2 + 1, MathUtils.floor((float) ((var2.z + 200) / 16)) * 2 + 1, 3, 3, var3);
        }

        for (var1 = 0; var1 < doors.size(); ++var1) {
            Door var4 = (Door) doors.get(var1);
            var3 = var4.getColor();
            if (!var4.explored && Configs.DarkenUnexplored) {
                var3 = darker(var3);
            }

            setPixels(var0, MathUtils.floor((float) ((var4.x + 200) / 16)) * 2 + 2, MathUtils.floor((float) ((var4.z + 200) / 16)) * 2 + 2, 1, 1, var3);
        }

        map = var0;
    }

    public static void showDungeonInfo() {
        if (isInDungeon || !isFullyScanned) {
            System.err.println("Dungeon Log:");
            System.err.println("Dungeon floor: " + floorInt);
            System.err.println("Total Rooms: " + totalRooms);
            System.err.println("Start Corner: " + Map.startCorner);
            System.err.println("Room Size: " + Map.roomSize);
            System.err.println("Total Secrets: " + totalSecrets);
            System.err.println("Secrets Found: " + secretsFound);
            System.err.println("Skill Score: " + skillScore);
            System.err.println("Explore Score: " + exploreScore);
            System.err.println("Score: " + score);
            showMap();
            showPlayers();
            System.err.println();
        }
    }

    private static void addRoom(Room var0) {
        if (var0.name.equals("Unknown")) {
            unknownRooms.add(var0);
        } else {
            if (var0.name.equals("Blaze")) {
                AutoBlaze.setRoom(var0);
            }

            rooms.add(var0);
        }
    }

    public static void showMap() {
        byte[] var0 = Map.getMapColors();
        if (var0 != null) {
            int var1;
            for (var1 = 0; var1 < 128; ++var1) {
                StringBuilder var2 = new StringBuilder();

                for (int var3 = 0; var3 < 128; ++var3) {
                    var2.append(String.format("%3d", var0[var3 + var1 * 128])).append(" ");
                }

                System.err.println(var2);
            }

            for (var1 = Map.startCorner.x + Map.roomSize / 2; var1 < 128; var1 += Map.roomSize / 2 + 2) {
                for (int var5 = Map.startCorner.y + Map.roomSize / 2; var5 < 128; var5 += Map.roomSize / 2 + 2) {
                    byte var6 = var0[var1 + var5 * 128];
                    byte var4 = var0[var1 - 3 + var5 * 128];
                    System.err.println(var1 + ", " + var5 + ", " + var6 + ", " + var4);
                }
            }

        }
    }

    private static void setExplored(String var0, boolean var1) {
        for (int var2 = 0; var2 < rooms.size(); ++var2) {
            Room var3 = (Room) rooms.get(var2);
            if (var3.name.equals(var0)) {
                var3.explored = var1;
            }
        }

    }

    private void reset() {
        currentRoom = "";
        isInDungeon = false;
        floorInt = -1;
        puzzles = new ArrayList();
        secretsFound = 0;
        secretsNeeded = 1.0;
        secretsForMax = 0.0;
        crypts = 0;
        deaths = 0;
        openedRooms = 0;
        completedRooms = 0;
        openedWitherDoors = 0;
        runStarted = 0L;
        bloodOpen = 0L;
        watcherDone = 0L;
        bossEntry = 0L;
        runEnded = 0L;
        startX = -185;
        startZ = -185;
        endX = 190;
        endZ = 190;
        roomSize = 31;
        isScanning = false;
        isFullyScanned = false;
        players = new ArrayList();
        rooms = new ArrayList();
        unknownRooms = new ArrayList();
        doors = new ArrayList();
        totalRooms = 0;
        witherDoors = 0;
        trapType = "Unknown";
        map = null;
        mapSize = new Vector2i(0, 0);
        bloodDone = false;
        trapDone = false;
        yellowDone = false;
        scoreString1 = "";
        scoreString2 = "";
        said300 = false;
        said270 = false;
        skillScore = 0;
        exploreScore = 0;
        bonusScore = 0;
        score = 0;
        puzzleCount = 0;
        puzzleDone = 0;
        totalSecrets = 0;
        overflowSecrets = 0.0;
        secretsPercent = 0.0;
        isMimicDead = false;
    }

    @SubscribeEvent
    public void onTickKeyBind(TickEndEvent var1) {
        if (normalRoomNameKeyBind.isPressed()) {
            this.enableNormalRoomName = !this.enableNormalRoomName;
            ChatLib.chat(this.enableNormalRoomName ? "Normal Room Name &aactivated" : "Normal Room Name &cdeactivated");
        }

    }

    private void renderMap() {
        RenderUtils.drawImage(new Image(map), Configs.MapX, Configs.MapY, 25 * Configs.MapScale, 25 * Configs.MapScale);
    }

    private void drawScoreCalc() {
        if (Configs.ScoreCalculation) {
            RenderUtils.translate((float) Configs.MapX + (float) (25 * Configs.MapScale) / 2.0F, (float) Configs.MapY + 24.5F * (float) Configs.MapScale);
            RenderUtils.scale(0.1F * (float) Configs.MapScale, 0.1F * (float) Configs.MapScale);
            RenderUtils.drawStringWithShadow(scoreString1, (float) (-RenderUtils.getStringWidth(ChatLib.removeFormatting(scoreString1))) / 2.0F, 0.0F);
            RenderUtils.translate((float) Configs.MapX + (float) (25 * Configs.MapScale) / 2.0F, (float) Configs.MapY + 25.5F * (float) Configs.MapScale);
            RenderUtils.scale(0.1F * (float) Configs.MapScale, 0.1F * (float) Configs.MapScale);
            RenderUtils.drawStringWithShadow(scoreString2, (float) (-RenderUtils.getStringWidth(ChatLib.removeFormatting(scoreString2))) / 2.0F, 0.0F);
        }

    }

    @SubscribeEvent
    public void onTickScan(TickEndEvent var1) {
        if (isInDungeon && !isScanning && Configs.AutoScan && !isFullyScanned && TimeUtils.curTime() - lastScan >= 500L && Configs.MapEnabled) {
            lastScan = TimeUtils.curTime();
            (new Thread(this::scan)).start();
        }

        if (isFullyScanned && !Map.calibrated) {
            Map.calibrate();
            if (Map.calibrated && Configs.ChatInfo) {
                ChatLib.chat("&aCurrent Dungeon:\n" + String.format("&aPuzzles &c%d&a: \n &b- &d%s\n", puzzles.size(), String.join("\n &b- &d", this.scannedPuzzles)) + String.format("&6Trap: &a%s\n", trapType) + String.format("&8Wither Doors: &7%d\n", witherDoors - 1) + String.format("&7Total Secrets: &b%s\n", totalSecrets));
            }
        }

    }

    private void calcScore() {
        int var1 = deaths > 0 && Configs.AssumeSpirit ? deaths * 2 - 1 : deaths * 2;
        int var2 = bloodDone ? completedRooms : completedRooms + 1;
        var2 += bossEntry > runStarted ? 0 : 1;
        skillScore = MathUtils.floor((float) (100 - 10 * (puzzleCount - puzzleDone) - var1) - 80.0F * (float) (totalRooms - var2) / (float) totalRooms);
        skillScore = Double.isNaN(skillScore) ? 20 : Math.max(skillScore, 20);
        exploreScore = MathUtils.floor(60.0F * (float) Math.min(var2, totalRooms) / (float) totalRooms) + MathUtils.floor(40.0 * ((double) secretsFound - overflowSecrets) / secretsForMax);
        exploreScore = totalRooms != 0 && totalSecrets != 0 ? exploreScore : 0;
        byte var3 = 100;
        bonusScore = Math.min(crypts, 5) + (isMimicDead ? 2 : 0) + (Configs.AssumePaul ? 10 : 0);
        score = skillScore + exploreScore + var3 + bonusScore;
        int var4 = calculatedTotalSecrets > 0 ? calculatedTotalSecrets : totalSecrets;
        String var5 = "&7Secrets: &b" + secretsFound;
        String var6 = calculatedTotalSecrets == 0 ? "&8-&c" + totalSecrets : "&8-&e" + (var4 - secretsFound) + "&8-&c" + var4;
        String var7 = crypts == 0 ? "&7Crypts: &c0" : (crypts < 5 ? "&7Crypts: &e" + crypts : "&7Crypts: &a" + crypts);
        String var8 = floorInt < 6 ? "" : (isMimicDead ? "&7Mimic: &a✔" : "&7Mimic: &c✘");
        String var9 = floorInt <= 2 ? "" : (trapDone ? "&7Trap: &a✔" : "&7Trap: &c✘");
        String var10 = deaths == 0 ? "&7Deaths: &a0" : "&7Deaths: &c-" + var1;
        String var11 = score < 270 ? "&7Score: &c" + score : (score < 300 ? "&7Score: &e" + score : "&7Score: &a" + score);
        scoreString1 = (var5 + var6 + "     " + var7 + "     " + var8).trim();
        scoreString2 = (var9 + "     " + var10 + "     " + var11).trim();
        if (Configs.Announce270 && !said270 && score >= 270) {
            said270 = true;
            CommandsUtils.addCommand("/pc " + Configs.Announce270Message);
        }

        if (Configs.Announce300 && !said300 && score >= 300) {
            said300 = true;
            CommandsUtils.addCommand("/pc " + Configs.Announce300Message);
        }

    }

    @SubscribeEvent
    public void onMessageReceived(ClientChatReceivedEvent var1) {
        if (isInDungeon) {
            String var2 = ChatLib.removeFormatting(var1.message.getUnformattedText());
            if (var2.startsWith(" ☠ ")) {
            }

            String var3 = var2.toUpperCase();
            if (var3.contains("MIMIC DEAD!") || var3.contains("MIMIC KILLED!") || var3.contains("SKYTILS-DUNGEON-SCORE-MIMIC")) {
                isMimicDead = true;
            }

        }
    }

    private float getFloat(String var1, Pattern var2) {
        Matcher var3 = var2.matcher(var1);
        return var3.find() ? Float.parseFloat(var3.group(1)) : 0.0F;
    }

    @SubscribeEvent
    public void onTickUpdateDungeon(TickEndEvent var1) {
        if (Configs.MapEnabled && SkyblockUtils.isInDungeon()) {
            String var2 = SkyblockUtils.getDungeon();
            if (!isInDungeon) {
                if (var2.equals("E")) {
                    floorInt = 0;
                } else {
                    floorInt = Integer.parseInt(var2.substring(1));
                }
            }

            isInDungeon = true;
            if (floorInt != 0 && floorInt != 1 && floorInt != 2 && floorInt != 3) {
                if (floorInt == 4) {
                    endX = 190;
                    endZ = 158;
                } else {
                    endZ = 190;
                    endX = 190;
                }
            } else {
                endZ = 158;
                endX = 158;
            }

            try {
                ArrayList var3 = TabUtils.getNames();
                if (!isInDungeon || var3.size() < 52 || !((String) var3.get(0)).contains("Party (")) {
                    return;
                }

                IntStream var10002 = Arrays.stream(new int[]{48, 49, 50, 51, 52});
                var3.getClass();
                puzzles = new ArrayList(Arrays.asList(var10002.mapToObj(var3::get).filter((var0) -> {
                    return !var0.equals("");
                }).toArray((var0) -> {
                    return new String[var0];
                })));
                puzzleCount = this.getInt((String) var3.get(47), Pattern.compile("Puzzles: \\((\\d+)\\)"));
                puzzleDone = 0;

                for (int var4 = 1; var4 <= 5; ++var4) {
                    if (((String) var3.get(47 + var4)).contains("✔")) {
                        ++puzzleDone;
                    }
                }

                secretsFound = this.getInt((String) var3.get(31), Pattern.compile("Secrets Found: (\\d+)"));
                secretsPercent = this.getFloat((String) var3.get(44), Pattern.compile("Secrets Found: (.+)%"));
                secretsNeeded = floorSecrets.containsKey(var2) ? (Double) floorSecrets.get(var2) : 1.0;
                calculatedTotalSecrets = 0;
                if (secretsFound > 0) {
                    calculatedTotalSecrets = MathUtils.floor(100.0 / secretsPercent * (double) secretsFound + 0.5);
                }

                secretsForMax = calculatedTotalSecrets > 0 ? (double) calculatedTotalSecrets * secretsNeeded : (double) totalSecrets * secretsNeeded;
                overflowSecrets = (double) secretsFound > secretsForMax ? (double) secretsFound - secretsForMax : 0.0;
                crypts = this.getInt((String) var3.get(32), Pattern.compile("Crypts: (\\d+)"));
                deaths = this.getInt((String) var3.get(25), Pattern.compile("Deaths: \\((\\d+)\\)"));
                openedRooms = this.getInt((String) var3.get(42), Pattern.compile("Opened Rooms: (\\d+)"));
            } catch (Exception var5) {
                var5.printStackTrace();
            }

        }
    }

    @SubscribeEvent
    public void onTickUpdatePlayerIcon(TickEndEvent var1) {
        if (isInDungeon && MinecraftUtils.getWorld() != null) {
            try {
                for (int var2 = 0; var2 < players.size(); ++var2) {
                    Player var3 = (Player) players.get(var2);
                    EntityPlayer var4 = MinecraftUtils.getWorld().getPlayerEntityByName(var3.name);
                    if (var4 == null) {
                        var3.inRender = false;
                    } else if (MathUtils.isBetween((int) var4.posX + 200, 0, 190) && MathUtils.isBetween((int) var4.posZ + 200, 0, 190)) {
                        var3.inRender = true;
                        var3.realX = var4.posX + 200.0;
                        var3.realZ = var4.posZ + 200.0;
                        var3.iconX = (var3.realX * 0.6125 - 2.0) * 0.2 * (double) Configs.MapScale + (double) ((float) Configs.MapScale / 2.0F);
                        var3.iconY = (var3.realZ * 0.6125 - 2.0) * 0.2 * (double) Configs.MapScale + (double) ((float) Configs.MapScale / 2.0F);
                        var3.yaw = var4.rotationYaw + 180.0F;
                    }
                }
            } catch (Exception var5) {
                var5.printStackTrace();
            }

        }
    }

    private void drawBackground() {
        mapSize = Configs.ScoreCalculation ? new Vector2i(25, 27) : new Vector2i(25, 25);
        RenderUtils.drawRect((new Color(0.0F, 0.0F, 0.0F, (float) Configs.BackgroundAlpha / 255.0F)).getRGB(), Configs.MapX, Configs.MapY, mapSize.x * Configs.MapScale, mapSize.y * Configs.MapScale);
        Color var1 = ColorUtils.realColors[Configs.MapBoundingColor];
        RenderUtils.drawRect(var1.getRGB(), Configs.MapX - Configs.MapBoundingThickness, Configs.MapY - Configs.MapBoundingThickness, Configs.MapBoundingThickness, Configs.MapBoundingThickness + Configs.MapScale * mapSize.y);
        RenderUtils.drawRect(var1.getRGB(), Configs.MapX - Configs.MapBoundingThickness, Configs.MapY + Configs.MapScale * mapSize.y, Configs.MapBoundingThickness + Configs.MapScale * mapSize.x, Configs.MapBoundingThickness);
        RenderUtils.drawRect(var1.getRGB(), Configs.MapX + Configs.MapScale * mapSize.x, Configs.MapY, Configs.MapBoundingThickness, Configs.MapBoundingThickness + Configs.MapScale * mapSize.y);
        RenderUtils.drawRect(var1.getRGB(), Configs.MapX, Configs.MapY - Configs.MapBoundingThickness, Configs.MapBoundingThickness + Configs.MapScale * mapSize.x, Configs.MapBoundingThickness);
    }

    @SubscribeEvent
    public void onRenderMap(RenderGameOverlayEvent.Pre var1) {
        if (Checker.enabled) {
            if (var1.type == ElementType.TEXT) {
                if (Configs.MapDisplay) {
                    if (bossEntry <= runStarted || !Configs.DisableMapInBoss) {
                        if (isInDungeon && Configs.MapEnabled) {
                            RenderUtils.start();

                            try {
                                this.drawBackground();
                                if (map != null) {
                                    this.renderMap();
                                }

                                this.renderCheckmarks();
                                HashSet var2 = new HashSet();

                                int var3;
                                for (var3 = 0; var3 < rooms.size(); ++var3) {
                                    Room var4 = (Room) rooms.get(var3);
                                    if (!var2.contains(var4.name)) {
                                        var2.add(var4.name);
                                        if ((!var4.type.equals("puzzle") || !Configs.ShowPuzzleName) && (!var4.type.equals("trap") || !Configs.ShowTrapName)) {
                                            if ((var4.type.equals("normal") || var4.type.equals("rare")) && Configs.ShowNormalName && this.enableNormalRoomName) {
                                                var4.renderName();
                                            }
                                        } else {
                                            var4.renderName();
                                        }

                                        if (Configs.ShowSecrets != 0 && (var4.type.equals("normal") || var4.type.equals("rare"))) {
                                            var4.renderSecrets();
                                        }
                                    }
                                }

                                for (var3 = 0; var3 < players.size(); ++var3) {
                                    Player var11 = (Player) players.get(var3);
                                    if (!var11.isDead) {
                                        var11.render();
                                        ItemStack var5 = ControlUtils.getHeldItemStack();
                                        if ((var5 != null && (var5.getDisplayName().contains("Spirit Leap") || var5.getDisplayName().contains("Infinileap")) && Configs.ShowPlayerNames == 1 || Configs.ShowPlayerNames == 2) && !var11.name.equals(MinecraftUtils.getPlayer().getName())) {
                                            var11.renderName();
                                        }
                                    }
                                }

                                if (Configs.ScoreCalculation) {
                                    this.drawScoreCalc();
                                }
                            } catch (Exception var9) {
                                var9.printStackTrace();
                            } finally {
                                RenderUtils.end();
                            }

                        }
                    }
                }
            }
        }
    }

    private void getCompletedRooms() {
        HashMap var1 = new HashMap();
        Iterator var2 = rooms.iterator();

        while (true) {
            Room var3;
            do {
                if (!var2.hasNext()) {
                    int var6 = 0;
                    Iterator var7 = var1.keySet().iterator();

                    while (var7.hasNext()) {
                        String var4 = (String) var7.next();
                        int var5 = (Integer) var1.get(var4);
                        if (var5 == 1) {
                            ++var6;
                        } else if (var5 == 3) {
                            var6 += 2;
                        } else if (var5 == 5) {
                            var6 += 3;
                        } else {
                            var6 += 4;
                        }
                    }

                    completedRooms = var6;
                    return;
                }

                var3 = (Room) var2.next();
            } while (!var3.checkmark.equals("white") && !var3.checkmark.equals("green"));

            var1.putIfAbsent(var3.name, 0);
            var1.put(var3.name, (Integer) var1.get(var3.name) + 1);
        }
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent var1) {
        String var2 = ChatLib.removeFormatting(var1.message.getUnformattedText());
        if (var2.equals("[NPC] Mort: Here, I found this map when I first entered the dungeon.")) {
            runStarted = TimeUtils.curTime();
        }

        if (var2.startsWith("[BOSS] The Watcher") && bloodOpen == 0L) {
            bloodOpen = TimeUtils.curTime();
        }

        if (var2.equals("[BOSS] The Watcher: You have proven yourself. You may pass.")) {
            watcherDone = TimeUtils.curTime();
        }

        if (var2.endsWith("opened a WITHER door!")) {
            ++openedWitherDoors;
        }

        if (MapUtils.includes(entryMessages, var2)) {
            bossEntry = TimeUtils.curTime();
        }

        if (var2.equals("                             > EXTRA STATS <")) {
            runEnded = TimeUtils.curTime();
        }

    }

    @SubscribeEvent
    public void onTickCheckScanIsFull(TickEndEvent var1) {
        if (isInDungeon) {
            if (!isFullyScanned) {
                scoreString1 = "&cDungeon has not been";
                scoreString2 = "&cfully scanned.";
            } else {
                try {
                    this.getCompletedRooms();
                    this.calcScore();
                } catch (Exception var3) {
                    var3.printStackTrace();
                }
            }
        }

    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent var1) {
        if (Configs.DisplayAnnounce300 && score >= 300 && bossEntry <= runStarted) {
            GuiUtils.showTitle("&c&l300 Score!", "", 0, 5, 0);
        }

    }

    @SubscribeEvent
    public void onTickCheckCurrentRoom(TickEndEvent var1) {
        if (isInDungeon && isFullyScanned) {
            if (bossEntry > runStarted) {
                StonklessStonk.setInPuzzle(true);
            } else {
                String var2 = currentRoom;
                int var3 = MathUtils.floor(MathUtils.getX(MinecraftUtils.getPlayer()));
                int var4 = MathUtils.floor(MathUtils.getZ(MinecraftUtils.getPlayer()));
                Iterator var5 = rooms.iterator();

                Room var6;
                do {
                    if (!var5.hasNext()) {
                        if (!XiaojiaAddons.isDebug()) {
                            currentRoom = "";
                        }

                        StonklessStonk.setInPuzzle(false);
                        return;
                    }

                    var6 = (Room) var5.next();
                } while (!MathUtils.isBetween(var3, var6.x - 16, var6.x + 16) || !MathUtils.isBetween(var4, var6.z - 16, var6.z + 16));

                currentRoom = var6.name;
                StonklessStonk.setInPuzzle(var6.type.equals("puzzle") && (var6.name.equals("Water Board") || var6.name.equals("Three Weirdos")) || var6.name.equals("Unknown"));
                if (currentRoom.equals("Water Board") && !var2.equals(currentRoom)) {
                    WaterSolver.setRoom(var6);
                }

            }
        } else {
            StonklessStonk.setInPuzzle(false);
            currentRoom = "";
        }
    }

    private void scan() {
        if (!isScanning) {
            isScanning = true;
            ArrayList var1 = players;
            this.reset();
            players = var1;
            boolean var2 = true;
            HashSet var3 = new HashSet();
            this.scannedPuzzles = new HashSet();

            for (int var4 = startX; (double) var4 <= (double) startX + (double) (roomSize + 1) * Math.floor((float) endX / 31.0F - 1.0F); var4 = (int) ((double) var4 + Math.floor((float) (roomSize + 1) / 2.0F))) {
                for (int var5 = startZ; (double) var5 <= (double) startZ + (double) (roomSize + 1) * Math.floor((float) endZ / 31.0F - 1.0F); var5 = (int) ((double) var5 + Math.floor((float) (roomSize + 1) / 2.0F))) {
                    Room var6;
                    if (((double) var4 - (Math.floor((float) roomSize / 2.0F) + 24.0)) % (double) (roomSize + 1) == 0.0 && ((double) var5 - (Math.floor((float) roomSize / 2.0F) + 24.0)) % (double) (roomSize + 1) == 0) {
                        if (!MapUtils.chunkLoaded(new Vector3i(var4, 100, var5))) {
                            var2 = false;
                        }

                        if (!MapUtils.isColumnAir(var4, var5)) {
                            var6 = Lookup.getRoomFromCoords(new Vector2i(var4, var5));
                            if (var6 != null) {
                                if (!var3.contains(var6.name)) {
                                    var3.add(var6.name);
                                    totalSecrets += var6.secrets;
                                }

                                ++totalRooms;
                                addRoom(var6);
                                if (var6.type.equals("trap")) {
                                    trapType = var6.name.split(" ")[0];
                                }

                                if (var6.type.equals("puzzle")) {
                                    this.scannedPuzzles.add(var6.name);
                                }
                            }
                        }
                    } else {
                        int var7;
                        Room var8;
                        if (((var4 - (roomSize + 24)) % (roomSize + 1) == 0 && ((double) var5 - (Math.floor((float) roomSize / 2.0F) + 24.0)) % (double) (roomSize + 1) == 0 || ((double) var4 - (Math.floor((float) roomSize / 2.0F) + 24.0)) % (double) (roomSize + 1) == 0 && (var5 - (roomSize + 24)) % (roomSize + 1) == 0) && !MapUtils.isColumnAir(var4, var5)) {
                            if (MapUtils.isDoor(var4, var5)) {
                                Door var9 = new Door(var4, var5);
                                Block var11 = BlockUtils.getBlockAt(var4, 70, var5);
                                if (Block.getIdFromBlock(var11) == 173) {
                                    var9.type = "wither";
                                    ++witherDoors;
                                } else if (Block.getIdFromBlock(var11) == 159 && var11.getMetaFromState(MinecraftUtils.getWorld().getBlockState(new BlockPos(var4, 70, var5))) == 14) {
                                    var9.type = "blood";
                                } else if (var11.getRegistryName().equals("minecraft:monster_egg")) {
                                    var9.type = "entrance";
                                }

                                doors.add(var9);
                            } else {
                                var6 = new Room(var4, var5, Data.blankRoom);

                                for (var7 = 0; var7 < rooms.size(); ++var7) {
                                    var8 = (Room) rooms.get(var7);
                                    if (var8.x == var6.x - 16 && var8.z == var6.z) {
                                        var6 = new Room(var4, var5, var8.getJson());
                                    }

                                    if (var8.x == var6.x && var8.z == var6.z - 16) {
                                        var6 = new Room(var4, var5, var8.getJson());
                                    }
                                }

                                if (var6.type.equals("entrance")) {
                                    Door var10 = new Door(var6.x, var6.z);
                                    var10.type = "entrance";
                                    doors.add(var10);
                                } else {
                                    addRoom(var6);
                                }
                            }
                        } else if ((var4 - (roomSize + 24)) % (roomSize + 1) == 0 && (var5 - (roomSize + 24)) % (roomSize + 1) == 0 && !MapUtils.isColumnAir(var4, var5)) {
                            var6 = new Room(var4, var5, Data.blankRoom);

                            for (var7 = 0; var7 < rooms.size(); ++var7) {
                                var8 = (Room) rooms.get(var7);
                                if (var8.x == var6.x - 16 && var8.z == var6.z - 16) {
                                    var6 = new Room(var4, var5, var8.getJson());
                                }
                            }

                            addRoom(var6);
                        }
                    }
                }
            }

            makeMap();
            isFullyScanned = var2;
            isScanning = false;
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load var1) {
        this.reset();
    }

    private void renderCheckmarks() {
        HashSet var1 = new HashSet();
        RenderUtils.retainTransforms(true);
        RenderUtils.translate((float) Configs.MapX, (float) Configs.MapY);
        RenderUtils.scale(0.1F * (float) Configs.MapScale, 0.1F * (float) Configs.MapScale);
        int var2 = Configs.MapScale * 4;

        for (int var3 = 0; var3 < rooms.size(); ++var3) {
            Room var4 = (Room) rooms.get(var3);
            if (!var1.contains(var4.name) && !var4.type.equals("entrance")) {
                float var5 = (float) (var4.x + 200) * 1.25F + (float) Configs.MapScale * 1.25F - (float) var2 / 2.0F;
                float var6 = (float) (var4.z + 200) * 1.25F - (float) var2 / 4.0F;
                if (var4.checkmark.equals("green")) {
                    if (Configs.DrawCheckMode == 1) {
                        RenderUtils.drawImage(greenCheck, var5, var6, 25.0, 25.0);
                    } else if (Configs.DrawCheckMode == 2) {
                        RenderUtils.drawImage(newGreenCheck, var5, var6, 25.0, 25.0);
                    }

                    var1.add(var4.name);
                }

                if (var4.checkmark.equals("white")) {
                    if (Configs.DrawCheckMode == 1) {
                        RenderUtils.drawImage(whiteCheck, var5, var6, 25.0, 25.0);
                    } else if (Configs.DrawCheckMode == 2) {
                        RenderUtils.drawImage(newWhiteCheck, var5, var6, 25.0, 25.0);
                    }

                    var1.add(var4.name);
                }

                if (var4.checkmark.equals("failed")) {
                    if (Configs.DrawCheckMode != 0) {
                        RenderUtils.drawImage(failedRoom, var5, var6, 25.0, 25.0);
                    }

                    var1.add(var4.name);
                }
            }
        }

        RenderUtils.retainTransforms(false);
    }

    private int getInt(String var1, Pattern var2) {
        Matcher var3 = var2.matcher(var1);
        return var3.find() ? Integer.parseInt(var3.group(1)) : 0;
    }
}
