package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Objects.Pair;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.*;

public class NBTUtils {
    public static boolean getBooleanFromExtraAttributes(ItemStack var0, String var1) {
        NBTTagCompound var2 = getExtraAttributes(var0);
        return var2 != null && var2.hasKey(var1) && var2.getBoolean(var1);
    }

    public static int getIntFromExtraAttributes(ItemStack var0, String var1) {
        NBTTagCompound var2 = getExtraAttributes(var0);
        return var2 != null && var2.hasKey(var1) ? var2.getInteger(var1) : 0;
    }

    public static NBTTagCompound getCompoundFromExtraAttributes(ItemStack var0, String var1) {
        NBTTagCompound var2 = getExtraAttributes(var0);
        return var2 != null && var2.hasKey(var1) ? var2.getCompoundTag(var1) : null;
    }

    public static Pair getFirstRune(ItemStack var0) {
        NBTTagCompound var1 = getExtraAttributes(var0);
        NBTTagCompound var2 = var1.getCompoundTag("runes");
        Set var3 = var2.getKeySet();
        Iterator var4 = var3.iterator();
        if (var4.hasNext()) {
            String var5 = (String) var4.next();
            return new Pair(var5, var2.getInteger(var5));
        } else {
            return null;
        }
    }

    public static ArrayList getBookNameAndLevel(ItemStack var0) {
        ArrayList var1 = new ArrayList();

        try {
            String var2 = ChatLib.removeFormatting((String) getLore(var0).get(1));
            var2 = var2.replaceAll(" ✖", "");
            if (var2.contains(",")) {
                var2 = var2.split(",")[0];
            }

            return getBookNameAndLevelFromString(var2);
        } catch (Exception var3) {
            return var1;
        }
    }

    private static NBTTagCompound getExtraAttributes(ItemStack var0) {
        return var0 == null ? null : var0.getSubCompound("ExtraAttributes", false);
    }

    public static boolean isBookUltimateFromName(String var0) {
        return var0.contains("§d§l");
    }

    public static boolean isItemRecombobulated(ItemStack var0) {
        return getIntFromExtraAttributes(var0, "rarity_upgrades") == 1;
    }

    public static ArrayList getBookNameAndLevelFromString(String var0) {
        ArrayList var1 = new ArrayList();

        try {
            ArrayList var2 = new ArrayList(Arrays.asList(var0.split(" ")));
            String var3 = (String) var2.get(var2.size() - 1);
            String var4 = var0.substring(0, var0.length() - var3.length() - 1);
            int var5 = StringUtils.getNumberFromRoman(var3);
            var3 = var5 + "";
            var1.add(var4);
            var1.add(var3);
            return var1;
        } catch (Exception var6) {
            return var1;
        }
    }

    public static Pair getFirstEnchantment(ItemStack var0) {
        NBTTagCompound var1 = getExtraAttributes(var0);
        NBTTagCompound var2 = var1.getCompoundTag("enchantments");
        Set var3 = var2.getKeySet();
        Iterator var4 = var3.iterator();
        if (var4.hasNext()) {
            String var5 = (String) var4.next();
            return new Pair(var5, var2.getInteger(var5));
        } else {
            return null;
        }
    }

    public static List getLore(ItemStack var0) {
        return var0 == null ? new ArrayList() : var0.getTooltip(XiaojiaAddons.mc.thePlayer, XiaojiaAddons.mc.gameSettings.advancedItemTooltips);
    }

    public static boolean isItemFullQuality(ItemStack var0) {
        return getIntFromExtraAttributes(var0, "baseStatBoostPercentage") == 50;
    }

    public static String getStringFromExtraAttributes(ItemStack var0, String var1) {
        NBTTagCompound var2 = getExtraAttributes(var0);
        return var2 != null && var2.hasKey(var1) ? var2.getString(var1) : "";
    }

    public static boolean isBookUltimate(ItemStack var0) {
        try {
            return isBookUltimateFromName((String) getLore(var0).get(1));
        } catch (Exception var2) {
            return false;
        }
    }

    public static String getUUID(ItemStack var0) {
        return getStringFromExtraAttributes(var0, "uuid");
    }

    public static String getSkyBlockID(ItemStack var0) {
        return getStringFromExtraAttributes(var0, "id");
    }

    public static boolean isItemStarred(ItemStack var0) {
        return getIntFromExtraAttributes(var0, "dungeon_item_level") != 0;
    }
}
