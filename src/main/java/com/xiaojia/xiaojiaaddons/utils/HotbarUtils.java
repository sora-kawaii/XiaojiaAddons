package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class HotbarUtils {

    public static int soulwhipSlot = -1;

    public static int shortBowSlot = -1;

    public static int terminatorSlot = -1;

    public static int harvestSlot = -1;

    public static int enchantedBoneMealSlot = -1;

    public static int charmSlot = -1;

    public static int boneMealSlot = -1;

    public static int gyroSlot = -1;

    public static int healingwandSlot = -1;

    public static int emeraldBladeSlot = -1;

    public static int dirtWandSlot = -1;

    public static int aotvSlot = -1;

    public static int zombieswordSlot = -1;

    public static int treecapitatorSlot = -1;

    public static int gloomlockSlot = -1;

    public static int plasmaSlot = -1;

    public static int aotsSlot = -1;

    public static int rodSlot = -1;

    public static int saplingSlot = -1;

    public static boolean checkSoulwhip() {
        return ControlUtils.checkHotbarItem(soulwhipSlot, "Soul Whip");
    }

    public static boolean checkEmeraldBlade() {
        return ControlUtils.checkHotbarItem(emeraldBladeSlot, "Emerald Blade") || ControlUtils.checkHotbarItem(emeraldBladeSlot, "Giant's Sword");
    }

    public static int getIndex(String var0) {
        Inventory var1 = ControlUtils.getOpenedInventory();
        if (var1 != null && var1.getSize() >= 9) {
            int var2 = var1.getSize();
            List var3 = var1.getItemStacks().subList(var2 - 9, var2);

            for (int var4 = 0; var4 < 9; ++var4) {
                ItemStack var5 = (ItemStack) var3.get(var4);
                if (var5 != null) {
                    String var6 = NBTUtils.getSkyBlockID(var5);
                    if (var6.equals(var0)) {
                        return var4;
                    }
                }
            }

            return -1;
        } else {
            return -1;
        }
    }

    @SubscribeEvent(
            priority = EventPriority.HIGHEST
    )
    public void onTick(TickEndEvent event) {
        Inventory var2 = ControlUtils.getOpenedInventory();
        if (var2 != null && var2.getSize() == 45) {
            List var3 = var2.getItemStacks().subList(36, 45);
            boolean var4 = false;
            boolean var5 = false;
            boolean var6 = false;
            boolean var7 = false;
            boolean var8 = false;
            boolean var9 = false;
            boolean var10 = false;
            boolean var11 = false;
            boolean var12 = false;
            boolean var13 = false;
            boolean var14 = false;
            boolean var15 = false;
            boolean var16 = false;
            boolean var17 = false;
            boolean var18 = false;
            boolean var19 = false;
            boolean var20 = false;
            boolean var21 = false;
            boolean var22 = false;

            for (int var23 = 0; var23 < 9; ++var23) {
                ItemStack var24 = (ItemStack) var3.get(var23);
                if (var24 != null) {
                    String var25 = var24.hasDisplayName() ? var24.getDisplayName() : var24.getUnlocalizedName();
                    String var26 = var24.getItem().getRegistryName();
                    if (var25 != null && !var25.toLowerCase().contains("air")) {
                        if (var25.contains("Terminator")) {
                            terminatorSlot = var23;
                            var4 = true;
                        } else if (!var25.contains("Giant's Sword") && !var25.contains("Emerald Blade")) {
                            if (var25.contains("Axe of the Shredded")) {
                                aotsSlot = var23;
                                var6 = true;
                            } else if (var25.contains("Soul Whip")) {
                                soulwhipSlot = var23;
                                var7 = true;
                            } else if (var25.contains("Plasmaflux")) {
                                plasmaSlot = var23;
                                var8 = true;
                            } else if (var25.contains("Wand of")) {
                                healingwandSlot = var23;
                                var9 = true;
                            } else if (var25.contains("Gyrokinetic Wand")) {
                                gyroSlot = var23;
                                var10 = true;
                            } else if (var25.contains("Zombie Sword")) {
                                zombieswordSlot = var23;
                                var11 = true;
                            } else if (var25.contains("Gloomlock Grimoire")) {
                                gloomlockSlot = var23;
                                var12 = true;
                            } else if (var25.contains("Enchanted Bone Meal")) {
                                enchantedBoneMealSlot = var23;
                                var13 = true;
                            } else if (var25.contains("Bone Meal")) {
                                boneMealSlot = var23;
                                var14 = true;
                            } else if (var25.contains("Treecapitator")) {
                                treecapitatorSlot = var23;
                                var15 = true;
                            } else if (var25.contains("Sapling")) {
                                saplingSlot = var23;
                                var16 = true;
                            } else if (var26.toLowerCase().contains("rod")) {
                                rodSlot = var23;
                                var17 = true;
                            } else if (var25.contains("Aspect of the Void")) {
                                aotvSlot = var23;
                                var18 = true;
                            } else if (var25.contains("Shortbow")) {
                                shortBowSlot = var23;
                                var19 = true;
                            } else if (var25.contains("InfiniDirt")) {
                                dirtWandSlot = var23;
                                var20 = true;
                            } else if (var25.contains("Charminizer")) {
                                charmSlot = var23;
                                var21 = true;
                            } else if (var25.contains("Atominizer")) {
                                harvestSlot = var23;
                                var22 = true;
                            }
                        } else {
                            emeraldBladeSlot = var23;
                            var5 = true;
                        }
                    }
                }
            }

            if (!var4) {
                terminatorSlot = -1;
            }

            if (!var5) {
                emeraldBladeSlot = -1;
            }

            if (!var6) {
                aotsSlot = -1;
            }

            if (!var7) {
                soulwhipSlot = -1;
            }

            if (!var8) {
                plasmaSlot = -1;
            }

            if (!var9) {
                healingwandSlot = -1;
            }

            if (!var10) {
                gyroSlot = -1;
            }

            if (!var11) {
                zombieswordSlot = -1;
            }

            if (!var12) {
                gloomlockSlot = -1;
            }

            if (!var13) {
                enchantedBoneMealSlot = -1;
            }

            if (!var14) {
                boneMealSlot = -1;
            }

            if (!var15) {
                treecapitatorSlot = -1;
            }

            if (!var16) {
                saplingSlot = -1;
            }

            if (!var17) {
                rodSlot = -1;
            }

            if (!var18) {
                aotvSlot = -1;
            }

            if (!var19) {
                shortBowSlot = -1;
            }

            if (!var20) {
                dirtWandSlot = -1;
            }

            if (!var21) {
                charmSlot = -1;
            }

            if (!var22) {
                harvestSlot = -1;
            }

        }
    }
}
