package com.xiaojia.xiaojiaaddons.Features.Remote;


import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DungeonLoot {

    private final List loots = new ArrayList();
    private int score = -1;
    private String floor;

    private chestType type;

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent var1) {
        if (true) {
            if (var1.type == 0) {
                if (SkyblockUtils.isInDungeon()) {
                    String var2 = ChatLib.removeFormatting(var1.message.getUnformattedText());
                    Pattern var3 = Pattern.compile("^ *The Catacombs - Floor (.*)");
                    Matcher var4 = var3.matcher(var2);
                    if (var4.find()) {
                        this.floor = "F" + StringUtils.getNumberFromRoman(var4.group(1));
                    } else {
                        var3 = Pattern.compile("^ *Master Mode Catacombs - Floor (.*)");
                        var4 = var3.matcher(var2);
                        if (var4.find()) {
                            this.floor = "M" + StringUtils.getNumberFromRoman(var4.group(1));
                        } else {
                            var3 = Pattern.compile("^ *Team Score: (\\d+)");
                            var4 = var3.matcher(var2);
                            if (var4.find()) {
                                this.score = Integer.parseInt(var4.group(1));
                                this.loots.clear();
                            } else {
                                var3 = Pattern.compile("^  (WOOD|DIAMOND|EMERALD|OBSIDIAN|BEDROCK) CHEST REWARDS$");
                                var4 = var3.matcher(var2);
                                if (var4.find()) {
                                    this.type = DungeonLoot.chestType.valueOf(var4.group(1));
                                    this.loots.clear();
                                } else {
                                    var3 = Pattern.compile("^    ([^ ].*)$");
                                    var4 = var3.matcher(var2);
                                    if (var4.find()) {
                                        String var5 = var4.group(1).replace("RARE REWARD! ", "");
                                        if (this.score != -1) {
                                            this.loots.add(var5);
                                        }

                                    } else {
                                        if (var2.equals("") && this.loots.size() > 0) {
//                                            XiaojiaChat.uploadLoot(this.floor, this.score, this.type.toString(), this.loots);
                                            this.score = -1;
                                            this.loots.clear();
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    enum chestType {

        WOOD,

        DIAMOND,

        EMERALD,

        OBSIDIAN,

        BEDROCK
    }
}
