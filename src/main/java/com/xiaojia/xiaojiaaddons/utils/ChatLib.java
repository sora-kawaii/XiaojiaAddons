package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.event.HoverEvent;
import net.minecraft.event.HoverEvent.Action;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatLib {

    public static boolean toggleOff = false;

    public static String removeFormatting(String var0) {
        return var0 == null ? null : var0.replaceAll("[\\u00a7&][0-9a-zA-Z]", "");
    }

    public static void debug(String var0) {
        if (SessionUtils.isDev()) {
            if (XiaojiaAddons.isDebug()) {
                chat(var0);
            } else {
                System.err.println(var0);
            }

        }
    }

    public static void playerJoin(String var0) {
        if (Checker.enabled) {
            if (!toggleOff) {
                String var1 = "&bXJC > &r&8" + var0 + " &ejoined.";
                var1 = addColor(var1);
                ChatComponentText var2 = new ChatComponentText(var1);
                addComponent(var2);
            }
        }
    }

    public static void addComponent(IChatComponent var0, boolean var1) {
        if (MinecraftUtils.getPlayer() != null) {
            try {
                ClientChatReceivedEvent var2 = new ClientChatReceivedEvent((byte) 0, var0);
                if (var1 && MinecraftForge.EVENT_BUS.post(var2)) {
                    return;
                }

                MinecraftUtils.getPlayer().addChatMessage(var2.message);
            } catch (Exception var3) {
                var3.printStackTrace();
            }

        }
    }

    public static void addComponent(IChatComponent var0) {
        addComponent(var0, true);
    }

    public static String addColor(String var0) {
        if (var0 == null) {
            return "";
        } else {
            Pattern var1 = Pattern.compile("((?<!\\\\))&(?![^0-9a-fklmnor]|$)");
            Matcher var2 = var1.matcher(var0);
            return var2.replaceAll("§");
        }
    }

    public static void chat(String var0, boolean var1) {
        if (var0 == null) {
            var0 = "null";
        }

        if (!Configs.ShowXJAMessage) {
            System.err.println("Chat: " + var0);
        } else {
            String[] var2 = var0.split("\n");
            String[] var3 = var2;
            int var4 = var2.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String var6 = var3[var5];
                var6 = "&9[XJA] > &b" + var6;
                var6 = addColor(var6);
                System.out.println(var6);
                ChatComponentText var7 = new ChatComponentText(var6);
                addComponent(var7, var1);
            }

        }
    }

    public static void xjchat(int var0, String var1, String var2) {
        if (Checker.enabled) {
            if (var1.equals("&c[UPDATE AUTOCHECKER]") || !toggleOff) {
                if (var0 != 0 || !Configs.HideChatMessages) {
                    if (var0 != 1 || !Configs.HidePuzzleFailMessages) {
                        if (var0 != 2 || !Configs.HideDeathMessages) {
                            if (var0 == 0 || var0 == 1 || var0 == 2) {
                                if (var2 == null) {
                                    var2 = "null";
                                }

                                String[] var3 = var2.split("\n");
                                String[] var4 = var3;
                                int var5 = var3.length;

                                for (int var6 = 0; var6 < var5; ++var6) {
                                    String var7 = var4[var6];
                                    var7 = "&bXJC > &r&8" + var1 + "&r&f: &r" + var7;
                                    var7 = addColor(var7);
                                    System.out.println(var7);
                                    ChatComponentText var8 = new ChatComponentText(var7);
                                    addComponent(var8);
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    public static void say(String var0) {
        if (var0 == null) {
            var0 = "null";
        }

        EntityPlayerSP var1 = MinecraftUtils.getPlayer();
        String[] var2 = var0.split("\n");
        String[] var3 = var2;
        int var4 = var2.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (var1 != null) {
                var1.sendChatMessage(var6);
            }
        }

    }

    public static String removeColor(String var0) {
        if (var0 == null) {
            return "";
        } else {
            Pattern var1 = Pattern.compile("((?<!\\\\))§(?![^0-9a-fklmnor]|$)");
            Matcher var2 = var1.matcher(var0);
            return var2.replaceAll("&");
        }
    }

    public static void chat(String var0) {
        chat(var0, true);
    }

    public static void toggle() {
        toggleOff = !toggleOff;
        chat((toggleOff ? "&cDisabled" : "&aEnabled") + "&r&b xj chat!");
    }

    public static void showItem(int var0, String var1, String var2, String var3) {
        if (Checker.enabled) {
            if (!toggleOff) {
                if (var0 == 3) {
                    String var4 = "&bXJC > &r&8" + var1 + "&r&f: &rShowed item ";
                    var4 = addColor(var4);
                    ChatComponentText var5 = new ChatComponentText(var4);
                    ChatComponentText var6 = new ChatComponentText(var2);
                    ChatStyle var7 = new ChatStyle();
                    var7.setChatHoverEvent(new HoverEvent(Action.SHOW_ITEM, new ChatComponentText(var3)));
                    var6.setChatStyle(var7);
                    var5.appendSibling(var6);
                    addComponent(var5);
                }
            }
        }
    }

    public static void showXJCMessage(String var0) {
        if (Checker.enabled) {
            if (!toggleOff) {
                var0 = addColor(var0);
                ChatComponentText var1 = new ChatComponentText(var0);
                addComponent(var1);
            }
        }
    }

    public static void playerLeft(String var0) {
        if (Checker.enabled) {
            if (!toggleOff) {
                String var1 = "&bXJC > &r&8" + var0 + " &eleft.";
                var1 = addColor(var1);
                ChatComponentText var2 = new ChatComponentText(var1);
                addComponent(var2);
            }
        }
    }

    public static String getPrefix(String var0) {
        if (var0 == null) {
            return "";
        } else {
            Pattern var1 = Pattern.compile("^(&[0-9a-fklmnor])*");
            Matcher var2 = var1.matcher(var0);
            return var2.find() ? var2.group(0) : "&r";
        }
    }
}
