package com.xiaojia.xiaojiaaddons.Features.Remote;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LowestBin {

    private static final HashMap prices = new HashMap();

    private static final String url = "https://skytils.gg/api/auctions/lowestbins";

    private static long lastSuccessTime = 0L;

    private static long lastCheckedTime = 0L;

    public static int getLastUpdate() {
        return (int) ((TimeUtils.curTime() - lastSuccessTime) / 1000L);
    }

    public static double getItemValue(ItemStack var0) throws Exception {
        String var1 = NBTUtils.getSkyBlockID(var0);
        if (var1.equals("")) {
            throw new Exception();
        } else {
            String var2 = var1;
            String var3;
            if (var1.equals("PET")) {
                var3 = NBTUtils.getStringFromExtraAttributes(var0, "petInfo");
                JsonObject var4 = (new Gson()).fromJson(var3, JsonObject.class);
                if (var4.has("type") && var4.has("tier")) {
                    var2 = "PET-" + var4.get("type").getAsString() + "-" + var4.get("tier").getAsString();
                }
            } else {
                Pair var5;
                if (var1.equals("ENCHANTED_BOOK")) {
                    var5 = NBTUtils.getFirstEnchantment(var0);
                    if (var5 != null) {
                        var2 = "ENCHANTED_BOOK-" + ((String) var5.getKey()).toUpperCase() + "-" + var5.getValue();
                    }
                } else if (var1.equals("POTION")) {
                    var3 = NBTUtils.getStringFromExtraAttributes(var0, "potion");
                    int var6 = NBTUtils.getIntFromExtraAttributes(var0, "potion_level");
                    var2 = "POTION-" + var3.toUpperCase() + "-" + var6;
                    if (NBTUtils.getBooleanFromExtraAttributes(var0, "enhanced")) {
                        var2 = var2 + "-ENHANCED";
                    }

                    if (NBTUtils.getBooleanFromExtraAttributes(var0, "extended")) {
                        var2 = var2 + "-EXTENDED";
                    }

                    if (NBTUtils.getBooleanFromExtraAttributes(var0, "splash")) {
                        var2 = var2 + "-SPLASH";
                    }
                } else if (var1.equals("RUNE")) {
                    var5 = NBTUtils.getFirstRune(var0);
                    if (var5 != null) {
                        var2 = "RUNE-" + ((String) var5.getKey()).toUpperCase() + "-" + var5.getValue();
                    }
                }
            }

            if (prices.containsKey(var2)) {
                return (Double) prices.get(var2);
            } else {
                throw new Exception();
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (true) {
            long var2 = TimeUtils.curTime();
            if (var2 - lastCheckedTime >= 60000L) {
                lastCheckedTime = var2;
                (new Thread(() -> {
                    try {
                        if (Configs.FetchLowestBin) {
                            String var1 = RemoteUtils.get("https://skytils.gg/api/auctions/lowestbins", new ArrayList(), false);

                            for (Map.Entry<String, JsonElement> entry : new JsonParser().parse(var1).getAsJsonObject().entrySet()) {
                                prices.put(entry.getKey(), entry.getValue().getAsDouble());
                            }

                            lastSuccessTime = TimeUtils.curTime();
                        }
                    } catch (Exception var6) {
                        var6.printStackTrace();
                    }

                })).start();
            }

        }
    }
}
