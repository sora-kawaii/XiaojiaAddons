package com.xiaojia.xiaojiaaddons.Features.Nether;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Display.Display;
import com.xiaojia.xiaojiaaddons.Objects.Display.DisplayLine;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Kuudra {

    private static final ArrayList infos = new ArrayList();
    private static final ArrayList secondaryInfos = new ArrayList();
    private static final ArrayList primaryInfos = new ArrayList();
    private static final ArrayList personalInfos = new ArrayList();
    private static final HashMap tiers = new HashMap();
    public static Display display2 = new Display();
    public static Display display1 = new Display();
    public static Display display3 = new Display();

    static {
        primaryInfos.add(new KuudraInfo("Rapid Fire", new HashMap() {
            {
                this.put(1, 40);
                this.put(2, 80);
                this.put(3, 120);
                this.put(4, 160);
                this.put(5, 200);
            }
        }));
        primaryInfos.add(new KuudraInfo("Bonus Damage", new HashMap() {
            {
                this.put(1, 50);
                this.put(2, 80);
                this.put(3, 110);
                this.put(4, 140);
                this.put(5, 170);
                this.put(6, 200);
            }
        }));
        primaryInfos.add(new KuudraInfo("Accelerated", new HashMap() {
            {
                this.put(1, 10);
                this.put(2, 20);
                this.put(3, 30);
                this.put(4, 40);
                this.put(5, 50);
                this.put(6, 60);
            }
        }));
        primaryInfos.add(new KuudraInfo("Blast Radius", new HashMap() {
            {
                this.put(1, 20);
                this.put(2, 40);
                this.put(3, 60);
                this.put(4, 80);
                this.put(5, 100);
                this.put(6, 120);
            }
        }));
        primaryInfos.add(new KuudraInfo("Multi Shot", new HashMap() {
            {
                this.put(1, 50);
                this.put(2, 80);
                this.put(3, 110);
                this.put(4, 140);
                this.put(5, 170);
            }
        }));
        secondaryInfos.add(new KuudraInfo("Confusion", new HashMap() {
            {
                this.put(1, 100);
                this.put(2, 200);
                this.put(3, 300);
                this.put(4, 400);
                this.put(5, 500);
            }
        }));
        secondaryInfos.add(new KuudraInfo("Wounded", new HashMap() {
            {
                this.put(1, 100);
                this.put(2, 200);
                this.put(3, 300);
                this.put(4, 400);
                this.put(5, 500);
            }
        }));
        secondaryInfos.add(new KuudraInfo("Sweet Spot", new HashMap() {
            {
                this.put(1, 100);
                this.put(2, 200);
                this.put(3, 300);
                this.put(4, 400);
                this.put(5, 500);
            }
        }));
        secondaryInfos.add(new KuudraInfo("Hinder", new HashMap() {
            {
                this.put(1, 100);
                this.put(2, 200);
                this.put(3, 300);
                this.put(4, 400);
                this.put(5, 500);
            }
        }));
        personalInfos.add(new KuudraInfo("Detonate", new HashMap() {
            {
                this.put(1, 50);
                this.put(2, 80);
                this.put(3, 110);
                this.put(4, 140);
            }
        }));
        personalInfos.add(new KuudraInfo("Shell", new HashMap() {
            {
                this.put(1, 50);
                this.put(2, 80);
                this.put(3, 110);
                this.put(4, 140);
            }
        }));
        personalInfos.add(new KuudraInfo("Magical Arrows", new HashMap() {
            {
                this.put(1, 50);
                this.put(2, 80);
                this.put(3, 110);
                this.put(4, 140);
            }
        }));
        infos.addAll(primaryInfos);
        infos.addAll(secondaryInfos);
        infos.addAll(personalInfos);
    }

    private static String getColorStringFromTier(int var0, int var1) {
        if (var0 == var1) {
            return "&b&l";
        } else if (var0 == 1) {
            return "&9&l";
        } else if (var0 == 2) {
            return "&6&l";
        } else {
            return var0 < 5 ? "&e&l" : "&c&l";
        }
    }

    private static String getStringFromInfo(KuudraInfo var0) {
        String var1 = var0.name;
        int var2 = (Integer) tiers.getOrDefault(var1, 1);
        int var3 = (Integer) var0.cost.getOrDefault(var2, -1);
        String var4 = getColorStringFromTier(var2, var0.cost.size() + 1);
        String var5 = "T" + var2;
        String var6 = var3 == -1 ? "MAXED" : var3 + "";
        return "&e" + var1 + ": " + var4 + var5 + " " + var6;
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent var1) {
        if (Checker.enabled) {
            if (var1.type == 0) {
                if (Configs.KuudraDisplay) {
                    String var2 = ChatLib.removeFormatting(var1.message.getUnformattedText());
                    Pattern var3 = Pattern.compile("^([a-zA-Z ]+)( was upgraded to tier: | has been upgraded to tier: )(\\d+)$");
                    Matcher var4 = var3.matcher(var2);
                    if (var4.find()) {
                        String var5 = var4.group(1);
                        int var6 = Integer.parseInt(var4.group(3));
                        tiers.put(var5, var6);
                    }

                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Checker.enabled) {
            display1.clearLines();
            display2.clearLines();
            display3.clearLines();
            display1.setRenderLoc(Configs.KuudraX, Configs.KuudraY);
            display2.setRenderLoc(Configs.KuudraX + 9 * Configs.KuudraScale, Configs.KuudraY);
            display3.setRenderLoc(Configs.KuudraX + 18 * Configs.KuudraScale, Configs.KuudraY);
            display1.setShouldRender(true);
            display2.setShouldRender(true);
            display3.setShouldRender(true);
            if (Configs.KuudraTest || SkyblockUtils.isInKuudra()) {
                if (Configs.KuudraTest) {
                    tiers.put("Rapid Fire", 1);
                    tiers.put("Bonus Damage", 2);
                    tiers.put("Accelerated", 3);
                    tiers.put("Blast Radius", 4);
                    tiers.put("Multi Shot", 5);
                    tiers.put("Confusion", 6);
                    tiers.put("Hinder", 6);
                    tiers.put("Shell", 4);
                }

                DisplayLine var2 = new DisplayLine("&c&lPrimary");
                var2.setScale(1.51F * (float) Configs.KuudraScale / 15.0F);
                display1.addLine(var2);
                var2 = new DisplayLine("&d&lSecondary");
                var2.setScale(1.51F * (float) Configs.KuudraScale / 15.0F);
                display2.addLine(var2);
                var2 = new DisplayLine("&4&lPersonal");
                var2.setScale(1.51F * (float) Configs.KuudraScale / 15.0F);
                display3.addLine(var2);
                Iterator var3 = primaryInfos.iterator();

                KuudraInfo var4;
                DisplayLine var5;
                while (var3.hasNext()) {
                    var4 = (KuudraInfo) var3.next();
                    var5 = new DisplayLine(getStringFromInfo(var4));
                    var5.setScale(1.51F * (float) Configs.KuudraScale / 20.0F);
                    display1.addLine(var5);
                }

                var3 = secondaryInfos.iterator();

                while (var3.hasNext()) {
                    var4 = (KuudraInfo) var3.next();
                    var5 = new DisplayLine(getStringFromInfo(var4));
                    var5.setScale(1.51F * (float) Configs.KuudraScale / 20.0F);
                    display2.addLine(var5);
                }

                var3 = personalInfos.iterator();

                while (var3.hasNext()) {
                    var4 = (KuudraInfo) var3.next();
                    var5 = new DisplayLine(getStringFromInfo(var4));
                    var5.setScale(1.51F * (float) Configs.KuudraScale / 20.0F);
                    display3.addLine(var5);
                }

            }
        }
    }

    @SubscribeEvent
    public void onLoad(WorldEvent.Load var1) {
        tiers.clear();
    }

    static class KuudraInfo {

        String name;

        HashMap cost;

        public KuudraInfo(String var1, HashMap var2) {
            this.name = var1;
            this.cost = var2;
        }
    }
}
