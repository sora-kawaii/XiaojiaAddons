package com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Features.Remote.RemoteUtils;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class Quiz {

    private static final HashMap solutions = new HashMap();
    private static final String url = "https://cdn.jsdelivr.net/gh/Skytils/SkytilsMod-Data@main/solvers/oruotrivia.json";
    public static String answer = null;
    private static long lastFetch = 0L;
    private static ArrayList answers = null;

    public static void fetch() {
        (new Thread(() -> {
            try {
                String var0 = RemoteUtils.get("https://cdn.jsdelivr.net/gh/Skytils/SkytilsMod-Data@main/solvers/oruotrivia.json", new ArrayList(), false);
                JsonParser var1 = new JsonParser();
                JsonObject var2 = var1.parse(var0).getAsJsonObject();
                Iterator var3 = var2.entrySet().iterator();

                while (var3.hasNext()) {
                    Map.Entry var4 = (Map.Entry) var3.next();
                    String var5 = (String) var4.getKey();
                    JsonArray var6 = ((JsonElement) var4.getValue()).getAsJsonArray();
                    solutions.put(var5, new ArrayList());
                    Iterator var7 = var6.iterator();

                    while (var7.hasNext()) {
                        JsonElement var8 = (JsonElement) var7.next();
                        ((ArrayList) solutions.get(var5)).add(var8.getAsString());
                    }
                }
            } catch (Exception var9) {
                var9.printStackTrace();
            }

        })).start();
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load var1) {
        answer = null;
        answers = null;
    }

    @SubscribeEvent
    public void onTickRemove(TickEndEvent var1) {
        if (true) {
            if (Configs.QuizSolver) {
                if (SkyblockUtils.isInDungeon()) {
                    if (MinecraftUtils.getWorld() != null) {
                        List var2 = EntityUtils.getEntities();
                        Iterator var3 = var2.iterator();

                        while (true) {
                            Entity var4;
                            String var5;
                            do {
                                do {
                                    do {
                                        if (!var3.hasNext()) {
                                            return;
                                        }

                                        var4 = (Entity) var3.next();
                                    } while (!(var4 instanceof EntityArmorStand));
                                } while (!var4.hasCustomName());

                                var5 = var4.getCustomNameTag();
                            } while (!var5.contains("ⓐ") && !var5.contains("ⓑ") && !var5.contains("ⓒ"));

                            if (answer != null && !var5.contains(answer)) {
                                var4.posY = var4.lastTickPosY = 9999.0;
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onReceive(ClientChatReceivedEvent var1) {
        if (true) {
            if (var1.type == 0) {
                if (Configs.QuizSolver) {
                    if (SkyblockUtils.isInDungeon()) {
                        String var2 = ChatLib.removeFormatting(var1.message.getUnformattedText()).trim();
                        if (var2.startsWith("[STATUE] Oruo the Omniscient: ") && var2.contains("answered Question #") && var2.endsWith("correctly!")) {
                            answer = null;
                        }

                        String var4;
                        Iterator var10;
                        if (var2.equals("What SkyBlock year is it?")) {
                            long var3 = Dungeon.runStarted;
                            long var5 = TimeUtils.curTime();
                            long var7 = var3 < 5L ? var5 : var3;
                            int var9 = MathUtils.floor(((double) var7 / 1000.0 - 1.560276E9) / 446400.0 + 1.0);
                            answer = "Year " + var9;
                        } else {
                            var10 = solutions.keySet().iterator();

                            while (var10.hasNext()) {
                                var4 = (String) var10.next();
                                if (var2.contains(var4)) {
                                    answers = (ArrayList) solutions.get(var4);
                                }
                            }
                        }

                        if (answers != null && (var2.startsWith("ⓐ") || var2.startsWith("ⓑ") || var2.startsWith("ⓒ"))) {
                            var10 = answers.iterator();

                            while (var10.hasNext()) {
                                var4 = (String) var10.next();
                                if (var2.contains(var4)) {
                                    answer = var4;
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    @SubscribeEvent(
            priority = EventPriority.HIGH
    )
    public void onPlayerInteract(PlayerInteractEvent var1) {
        if (true) {
            if (Configs.QuizSolver) {
                if (var1.action == Action.RIGHT_CLICK_BLOCK || var1.action == Action.LEFT_CLICK_BLOCK) {
                    Block var2 = BlockUtils.getBlockAt(var1.pos);
                    if ((var2 == Blocks.stone_button || var2 == Blocks.double_stone_slab) && answer != null) {
                        if (Dungeon.currentRoom.equals("Quiz")) {
                            EntityArmorStand var3 = null;
                            Iterator var4 = EntityUtils.getEntities().iterator();

                            while (var4.hasNext()) {
                                Entity var5 = (Entity) var4.next();
                                if (var5 instanceof EntityArmorStand && var5.hasCustomName()) {
                                    String var6 = var5.getCustomNameTag();
                                    if (var6.contains(answer) && (var6.contains("ⓐ") || var6.contains("ⓑ") || var6.contains("ⓒ"))) {
                                        var3 = (EntityArmorStand) var5;
                                        break;
                                    }
                                }
                            }

                            if (var3 != null) {
                                if (var3.getDistanceSq(var1.pos) > 4.0) {
                                    ChatLib.chat("Blocked wrong click in quiz!");
                                    var1.setCanceled(true);
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Configs.QuizSolver) {
            if (TimeUtils.curTime() - lastFetch > 600000L) {
                lastFetch = TimeUtils.curTime();
                fetch();
            }

        }
    }
}
