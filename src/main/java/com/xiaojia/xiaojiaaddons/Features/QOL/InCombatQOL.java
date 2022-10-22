package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InCombatQOL {

    public static final String[] dungTrash = new String[]{"Training Weight", "Health VIII Potion", "Health 8 Potion", "Beating Heart", "Premium Flesh", "Mimic Fragment", "Defuse Kit", "Tripwire Hook", "Button", "Carpet", "Lever", "Sign", "Enchanted Rotten Flesh", "Enchanted Bone", "Enchanted Ice", "Optic Lens", "Diamond Atom"};
    public static final String[] runes = new String[]{"Snow Rune", "Blood Rune", "Zap Rune", "Gem Rune", "Lava Rune", "Hot Rune", "White Spiral Rune", "Hearts Rune", "Ice Rune", "Redstone Rune", "Sparkling Rune", "Clouds Rune", "Golden Rune"};
    public static final String[] dungArmor = new String[]{"Rotten", "Skeleton Master", "Skeleton Grunt", "Skeleton Lord", "Zombie Soldier", "Skeleton Soldier", "Zombie Knight", "Zombie Commander", "Zombie Lord", "Skeletor", "Super Heavy", "Heavy", "Sniper Helmet", "Dreadlord", "Earth Shard", "Zombie Commander Whip", "Machine Gun", "Sniper Bow", "Soulstealer Bow", "Cutlass", "Silent Death", "Conjuring"};
    private final KeyBind tradeKeyBind = new KeyBind("Trade Menu", 0);
    private final KeyBind autoSellKeyBind = new KeyBind("Auto Sell", 0);
    private final ArrayList armorKeyBinds = new ArrayList() {
        {
            for (int var2 = 0; var2 < 9; ++var2) {
                this.add(new KeyBind(String.format("Wardrobe Slot %d", var2), 0));
            }

        }
    };
    private boolean autoSelling = false;
    private boolean openWardrobe = false;
    private boolean openTrade = false;
    private int wardrobeSlot = -1;
    private int currentStep = 0;

    private static boolean canSell(ItemStack var0) {
        String var1 = DisplayUtils.getDisplayString(var0).toLowerCase();
        boolean var2 = NBTUtils.isItemRecombobulated(var0);
        boolean var3 = NBTUtils.isItemFullQuality(var0);
        boolean var4 = NBTUtils.isItemStarred(var0);
        int var5 = NBTUtils.getIntFromExtraAttributes(var0, "trainingWeightsHeldTime");
        String[] var6;
        int var7;
        int var8;
        String var9;
        if (Configs.AutoSellDungeonArmor) {
            var6 = dungArmor;
            var7 = var6.length;

            for (var8 = 0; var8 < var7; ++var8) {
                var9 = var6[var8];
                if (var1.contains(var9.toLowerCase())) {
                    if (var2 && !Configs.CanAutoSellRecomed) {
                        return false;
                    }

                    if (var3 && !Configs.CanAutoSellFullQuality) {
                        return false;
                    }

                    if (var4 && !Configs.CanAutoSellStarred) {
                        return false;
                    }

                    return !var2 || !var3 || Configs.CanAutoSellFullQualityRecomed;
                }
            }
        }

        if (Configs.AutoSellDungeonTrash) {
            if (var5 > 10000 && !Configs.CanSellTrainingWeightLong) {
                return false;
            }

            var6 = dungTrash;
            var7 = var6.length;

            for (var8 = 0; var8 < var7; ++var8) {
                var9 = var6[var8];
                if (var1.contains(var9.toLowerCase())) {
                    return !var2 || Configs.CanSellRecomedDungeonTrash;
                }
            }
        }

        if (Configs.AutoSellSuperboom && var1.contains("superboom tnt")) {
            return true;
        } else {
            if (Configs.AutoSellRunes) {
                var6 = runes;
                var7 = var6.length;

                for (var8 = 0; var8 < var7; ++var8) {
                    var9 = var6[var8];
                    if (var1.contains(var9.toLowerCase())) {
                        return true;
                    }
                }
            }

            if (Configs.AutoSellBlazeHat && var1.equals("blaze hat")) {
                return true;
            } else if (Configs.AutoSellIceRod && var1.contains("ice rod")) {
                return true;
            } else if (Configs.AutoSellMusicDisc && var1.contains("music disc")) {
                return true;
            } else if (Configs.AutoSellFairySet && !var2 && (var1.equals("fairy's fedora") || var1.equals("fairy's polo") || var1.equals("fairy's trousers") || var1.equals("fairy's galoshes"))) {
                return true;
            } else if (Configs.AutoSellEnchantedFeather && var1.equals("enchanted feather")) {
                return true;
            } else if (Configs.AutoSellEnchantedGoldenApple && var1.equals("enchanted golden apple")) {
                return true;
            } else if (Configs.AutoSellGoldenApple && var1.equals("golden apple")) {
                return true;
            } else if (Configs.AutoSellSeaLantern && var1.equals("sea lantern")) {
                return true;
            } else if (Configs.AutoSellBait && var1.endsWith(" bait")) {
                return true;
            } else if (Configs.AutoSellAscensionRope && var1.contains("ascension rope")) {
                return true;
            } else if (Configs.AutoSellWishingCompass && var1.contains("wishing compass")) {
                return true;
            } else {
                if (Configs.AutoSellFineGem) {
                    Pattern var10 = Pattern.compile("fine \\w+ gemstone");
                    Matcher var12 = var10.matcher(var1);
                    if (var12.find()) {
                        return true;
                    }
                }

                if (var1.startsWith("enchanted book")) {
                    ArrayList var11 = NBTUtils.getBookNameAndLevel(var0);
                    if (var11.size() != 2) {
                        return false;
                    } else {
                        String var13 = (String) var11.get(0);
                        String var14 = (String) var11.get(1);
                        if (var13.equals("Feather Falling") && Configs.AutoSellFeatherFalling && (var14.equals("6") || var14.equals("7"))) {
                            return true;
                        } else if (!var13.equals("Infinite Quiver") || !Configs.AutoSellInfiniteQuiver || !var14.equals("6") && !var14.equals("7")) {
                            if (var13.equals("Bank") && Configs.AutoSellBank) {
                                return true;
                            } else if (var13.equals("No Pain No Gain") && Configs.AutoSellNoPainNoGain) {
                                return true;
                            } else if (var13.equals("Magnet") && Configs.AutoSellMagnet && var14.equals("6")) {
                                return true;
                            } else {
                                return var13.equals("Ultimate Jerry") && Configs.AutoSellUltimateJerry;
                            }
                        } else {
                            return true;
                        }
                    }
                } else {
                    return Configs.AutoSellConfig.length() > 0 && var1.contains(Configs.AutoSellConfig.toLowerCase());
                }
            }
        }
    }

    @SubscribeEvent
    public void onPostGuiRender(GuiScreenEvent.DrawScreenEvent.Post var1) {
        if (true) {
            if (this.openWardrobe || this.openTrade || this.autoSelling) {
                Inventory var2 = ControlUtils.getOpenedInventory();
                if (var2 != null && var2.getName() != null) {
                    String var3 = ChatLib.removeFormatting(var2.getName());
                    if (var3.contains("Pets")) {
                        if (this.currentStep == 0 && (this.openTrade || this.openWardrobe || this.autoSelling)) {
                            var2.click(48, false, "MIDDLE");
                            ++this.currentStep;
                            if (!Configs.InCombatFastMode) {
                                return;
                            }

                            if (!this.openTrade && !this.autoSelling) {
                                if (this.openWardrobe) {
                                    var2.click(32, false, "MIDDLE", 1);
                                    ++this.currentStep;
                                    if (!Configs.NakePrevention) {
                                        var2.click(this.wardrobeSlot + 36, false, "LEFT", 2);
                                        MinecraftUtils.getPlayer().closeScreen();
                                        this.openWardrobe = false;
                                    }
                                }
                            } else {
                                var2.click(22, false, "MIDDLE", 1);
                                if (this.openTrade) {
                                    this.openTrade = false;
                                }

                                ++this.currentStep;
                            }
                        }
                    } else if (var3.contains("SkyBlock Menu")) {
                        if (this.currentStep == 1) {
                            if (!this.openTrade && !this.autoSelling) {
                                if (this.openWardrobe) {
                                    var2.click(32, false, "MIDDLE");
                                    ++this.currentStep;
                                }
                            } else {
                                var2.click(22, false, "MIDDLE");
                                this.openTrade = false;
                                ++this.currentStep;
                            }
                        }
                    } else if (var3.contains("Wardrobe")) {
                        if (this.openWardrobe && this.currentStep == 2) {
                            (new Thread(() -> {
                                ++this.currentStep;

                                try {
                                    ItemStack var2x;
                                    for (var2x = var2.getItemInSlot(this.wardrobeSlot + 36); var2x == null || var2x.getItemDamage() != 9 && var2x.getItemDamage() != 10; var2x = ControlUtils.getOpenedInventory().getItemInSlot(this.wardrobeSlot + 36)) {
                                        Thread.sleep(20L);
                                    }

                                    if (var2x.getItemDamage() == 10 && Configs.NakePrevention) {
                                        ChatLib.chat("Detected Taking off Armor, stopped.");
                                    } else {
                                        var2.click(this.wardrobeSlot + 36, false, "LEFT");
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                MinecraftUtils.getPlayer().closeScreen();
                                this.openWardrobe = false;
                            })).start();
                        }
                    } else if (var3.contains("Trade") && this.autoSelling && this.currentStep == 2) {
                        (new Thread(() -> {
                            ++this.currentStep;

                            for (int i = 53; i < 81; ++i) {
                                Inventory inventory = ControlUtils.getOpenedInventory();
                                if (inventory == null || inventory.getSize() != 90) {
                                    return;
                                }

                                ItemStack stack = inventory.getItemInSlot(i);
                                if (stack != null) {
                                    try {
                                        if (canSell(stack)) {
                                            inventory.click(i, false, "MIDDLE");
                                            Thread.sleep(Configs.AutoSellCD);
                                        }
                                    } catch (Exception var5) {
                                        var5.printStackTrace();
                                    }
                                }
                            }

                            MinecraftUtils.getPlayer().closeScreen();
                            this.autoSelling = false;
                        })).start();
                    }

                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (true) {
            for (int var2 = 0; var2 < 9; ++var2) {
                if (((KeyBind) this.armorKeyBinds.get(var2)).isPressed()) {
                    ChatLib.chat(String.format("Swapping to %d", var2));
                    CommandsUtils.addCommand("/pets");
                    this.openWardrobe = true;
                    this.currentStep = 0;
                    this.wardrobeSlot = var2;
                    return;
                }
            }

            if (this.tradeKeyBind.isPressed()) {
                ChatLib.chat("Opening trade");
                CommandsUtils.addCommand("/pets");
                this.openTrade = true;
                this.currentStep = 0;
            }

            if (this.autoSellKeyBind.isPressed()) {
                ChatLib.chat("Auto Selling");
                CommandsUtils.addCommand("/pets");
                this.autoSelling = true;
                this.currentStep = 0;
            }

        }
    }
}
