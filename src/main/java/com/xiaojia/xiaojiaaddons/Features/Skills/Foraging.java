package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.vecmath.Vector3f;
import java.util.ArrayList;

public class Foraging {

    private static final ArrayList clearBlocks = new ArrayList();
    private static final KeyBind keyBind = new KeyBind("Auto Foraging", 0);
    private static final ArrayList foragingBlocks = new ArrayList();
    private boolean isAutoForaging = false;
    private boolean autoForagingThreadLock = false;
    private boolean haveEnchantedBoneMeal = false;

    public static void setForagingPoint(int var0) {
        float var1 = MathUtils.getX(MinecraftUtils.getPlayer());
        float var2 = MathUtils.getY(MinecraftUtils.getPlayer());
        float var3 = MathUtils.getZ(MinecraftUtils.getPlayer());
        foragingBlocks.clear();
        clearBlocks.clear();
        if (var0 == 0) {
            foragingBlocks.add(new Vector3f(var1, var2, var3 - 3.0F));
            foragingBlocks.add(new Vector3f(var1 + 1.0F, var2, var3 - 3.0F));
            foragingBlocks.add(new Vector3f(var1 + 1.0F, var2, var3 - 2.0F));
            foragingBlocks.add(new Vector3f(var1, var2, var3 - 2.0F));
            clearBlocks.add(new Vector3f(var1, var2, var3 - 1.0F));
            clearBlocks.add(new Vector3f(var1 + 1.0F, var2, var3 - 1.0F));
            clearBlocks.add(new Vector3f(var1, var2, var3 - 2.0F));
            clearBlocks.add(new Vector3f(var1 + 1.0F, var2, var3 - 2.0F));
            clearBlocks.add(new Vector3f(var1, var2, var3 - 3.0F));
            clearBlocks.add(new Vector3f(var1 + 1.0F, var2, var3 - 3.0F));
            ChatLib.chat("Direction: north");
        } else if (var0 == 1) {
            foragingBlocks.add(new Vector3f(var1, var2, var3 + 3.0F));
            foragingBlocks.add(new Vector3f(var1 - 1.0F, var2, var3 + 3.0F));
            foragingBlocks.add(new Vector3f(var1 - 1.0F, var2, var3 + 2.0F));
            foragingBlocks.add(new Vector3f(var1, var2, var3 + 2.0F));
            clearBlocks.add(new Vector3f(var1, var2, var3 + 1.0F));
            clearBlocks.add(new Vector3f(var1 + 1.0F, var2, var3 + 1.0F));
            clearBlocks.add(new Vector3f(var1, var2, var3 + 2.0F));
            clearBlocks.add(new Vector3f(var1 + 1.0F, var2, var3 + 2.0F));
            clearBlocks.add(new Vector3f(var1, var2, var3 + 3.0F));
            clearBlocks.add(new Vector3f(var1 + 1.0F, var2, var3 + 3.0F));
            ChatLib.chat("Direction: south");
        } else if (var0 == 2) {
            foragingBlocks.add(new Vector3f(var1 - 3.0F, var2, var3));
            foragingBlocks.add(new Vector3f(var1 - 3.0F, var2, var3 - 1.0F));
            foragingBlocks.add(new Vector3f(var1 - 2.0F, var2, var3 - 1.0F));
            foragingBlocks.add(new Vector3f(var1 - 2.0F, var2, var3));
            clearBlocks.add(new Vector3f(var1 - 1.0F, var2, var3));
            clearBlocks.add(new Vector3f(var1 - 1.0F, var2, var3 - 1.0F));
            clearBlocks.add(new Vector3f(var1 - 2.0F, var2, var3));
            clearBlocks.add(new Vector3f(var1 - 2.0F, var2, var3 - 1.0F));
            clearBlocks.add(new Vector3f(var1 - 3.0F, var2, var3));
            clearBlocks.add(new Vector3f(var1 - 3.0F, var2, var3 - 1.0F));
            ChatLib.chat("Direction: west");
        } else {
            foragingBlocks.add(new Vector3f(var1 + 3.0F, var2, var3));
            foragingBlocks.add(new Vector3f(var1 + 3.0F, var2, var3 + 1.0F));
            foragingBlocks.add(new Vector3f(var1 + 2.0F, var2, var3 + 1.0F));
            foragingBlocks.add(new Vector3f(var1 + 2.0F, var2, var3));
            clearBlocks.add(new Vector3f(var1 + 1.0F, var2, var3));
            clearBlocks.add(new Vector3f(var1 + 1.0F, var2, var3 - 1.0F));
            clearBlocks.add(new Vector3f(var1 + 2.0F, var2, var3));
            clearBlocks.add(new Vector3f(var1 + 2.0F, var2, var3 - 1.0F));
            clearBlocks.add(new Vector3f(var1 + 3.0F, var2, var3));
            clearBlocks.add(new Vector3f(var1 + 3.0F, var2, var3 - 1.0F));
            ChatLib.chat("Direction: east");
        }

        ChatLib.chat(String.format("Successfully set foraging point at (%.2f %.2f %.2f)", var1, var2, var3));
    }

    private boolean checkForagingRequirements() {
        this.haveEnchantedBoneMeal = ControlUtils.checkHotbarItem(HotbarUtils.enchantedBoneMealSlot, "Enchanted Bone Meal");
        if (!ControlUtils.checkHotbarItem(HotbarUtils.treecapitatorSlot, "Treecapitator")) {
            ChatLib.chat("Treecapitator needed!");
            return false;
        } else if (!ControlUtils.checkHotbarItemRegistryName(HotbarUtils.rodSlot, "rod")) {
            ChatLib.chat("Rod needed!");
            return false;
        } else if (!ControlUtils.checkHotbarItem(HotbarUtils.boneMealSlot, "Bone Meal") && !ControlUtils.checkHotbarItem(HotbarUtils.enchantedBoneMealSlot, "Enchanted Bone Meal")) {
            ChatLib.chat("(Enchanted) Bone Meal needed!");
            return false;
        } else if (!ControlUtils.checkHotbarItem(HotbarUtils.saplingSlot, "Sapling")) {
            ChatLib.chat("Sapling needed!");
            return false;
        } else if (foragingBlocks.size() != 4) {
            ChatLib.chat("/foragingpoint first!");
            return false;
        } else {
            return true;
        }
    }

    private void stopAutoForaging() {
        if (this.isAutoForaging) {
            if (MinecraftUtils.getPlayer() != null) {
                MinecraftUtils.getPlayer().playSound("random.successful_hit", 1000.0F, 1.0F);
            }

            this.isAutoForaging = false;
            ChatLib.chat("Auto Foraging &cdeactivated");
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (true) {
            if (keyBind.isPressed()) {
                this.isAutoForaging = !this.isAutoForaging;
                ChatLib.chat(this.isAutoForaging ? "Auto Foraging &aactivated" : "Auto Foraging &cdeactivated");
            }

            if (Configs.AutoForaging && this.isAutoForaging) {
                if (!this.checkForagingRequirements()) {
                    this.stopAutoForaging();
                } else if (!this.autoForagingThreadLock) {
                    this.autoForagingThreadLock = true;
                    (new Thread(() -> {
                        long var1 = 0L;

                        try {
                            while (this.isAutoForaging) {
                                ControlUtils.setHeldItemIndex(HotbarUtils.rodSlot);
                                Thread.sleep((long) (20.0 + Math.random() * 50.0));
                                ControlUtils.rightClick();

                                int var3;
                                Vector3f var4;
                                for (var3 = 0; var3 < 6; ++var3) {
                                    var4 = (Vector3f) clearBlocks.get(var3);
                                    if (!BlockUtils.isBlockAir(var4.x, var4.y, var4.z)) {
                                        ControlUtils.face(var4.x, var4.y + 0.5F, var4.z);
                                        ControlUtils.holdLeftClick();

                                        while (!BlockUtils.isBlockAir(var4.x, var4.y, var4.z)) {
                                            if (!this.isAutoForaging) {
                                                ControlUtils.releaseLeftClick();
                                                return;
                                            }

                                            Thread.sleep(20L);
                                        }

                                        ControlUtils.releaseLeftClick();
                                    }
                                }

                                ControlUtils.setHeldItemIndex(HotbarUtils.saplingSlot);

                                for (var3 = 0; var3 < 4; ++var3) {
                                    if (!this.isAutoForaging) {
                                        return;
                                    }

                                    var4 = (Vector3f) foragingBlocks.get(var3);
                                    float var5 = var4.x;
                                    float var6 = var4.y;
                                    float var7 = var4.z;
                                    float var8 = 0.1F;
                                    ControlUtils.faceSlowly((float) ((double) var5 + Math.random() * 2.0 * (double) var8 - (double) var8), var6, (float) ((double) var7 + Math.random() * 2.0 * (double) var8 - (double) var8));

                                    while (BlockUtils.isBlockAir(var5, var6, var7)) {
                                        Thread.sleep((long) (20.0 + Math.random() * 40.0));
                                        if (!this.isAutoForaging) {
                                            return;
                                        }

                                        ControlUtils.rightClick();
                                    }
                                }

                                if (this.haveEnchantedBoneMeal) {
                                    ControlUtils.setHeldItemIndex(HotbarUtils.enchantedBoneMealSlot);
                                } else {
                                    ControlUtils.setHeldItemIndex(HotbarUtils.boneMealSlot);
                                }

                                Vector3f var14 = (Vector3f) foragingBlocks.get(3);

                                while (BlockUtils.isBlockSapling(var14.x, var14.y, var14.z)) {
                                    if (!this.isAutoForaging) {
                                        return;
                                    }

                                    ControlUtils.rightClick();
                                    Thread.sleep((long) (20.0 + Math.random() * 150.0));
                                }

                                ControlUtils.setHeldItemIndex(HotbarUtils.treecapitatorSlot);
                                Thread.sleep(40L);
                                System.out.println(TimeUtils.curTime() - var1);

                                while (TimeUtils.curTime() - var1 < 1000L) {
                                    Thread.sleep(20L);
                                }

                                var1 = TimeUtils.curTime();
                                ControlUtils.holdLeftClick();

                                while (!BlockUtils.isBlockAir(var14.x, var14.y, var14.z)) {
                                    if (!this.isAutoForaging) {
                                        ControlUtils.releaseLeftClick();
                                        return;
                                    }

                                    Thread.sleep(20L);
                                }

                                ControlUtils.releaseLeftClick();
                                Thread.sleep(500L);

                                for (int var15 = 0; var15 < 6; ++var15) {
                                    Vector3f var16 = (Vector3f) clearBlocks.get(var15);
                                    if (!BlockUtils.isBlockAir(var16.x, var16.y, var16.z)) {
                                        ControlUtils.face(var16.x, var16.y + 0.5F, var16.z);
                                        ControlUtils.holdLeftClick();

                                        while (!BlockUtils.isBlockAir(var16.x, var16.y, var16.z)) {
                                            if (!this.isAutoForaging) {
                                                ControlUtils.releaseLeftClick();
                                                return;
                                            }

                                            Thread.sleep(20L);
                                        }

                                        ControlUtils.releaseLeftClick();
                                        var1 = TimeUtils.curTime();
                                    }
                                }
                            }

                        } catch (Exception var12) {
                            this.stopAutoForaging();
                            var12.printStackTrace();
                        } finally {
                            this.autoForagingThreadLock = false;
                        }
                    })).start();
                }
            }
        }
    }
}
