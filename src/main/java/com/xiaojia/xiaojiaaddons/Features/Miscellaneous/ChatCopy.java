package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatCopy {
    @SubscribeEvent(
            priority = EventPriority.LOW
    )
    public void onMessageReceived(ClientChatReceivedEvent var1) {
        if (true) {
            if (Configs.ChatCopy) {
                if (var1.type != 2) {
                    String var2 = var1.message.getFormattedText();
                    String var3 = ChatLib.removeFormatting(var1.message.getUnformattedText());
                    if (!var3.matches("^[-▬=]+$") || var3.length() < 4) {
                        ChatComponentText var4 = new ChatComponentText(ChatLib.addColor("&r&8 [&0C"));
                        ChatStyle var5 = new ChatStyle();
                        var5.setChatClickEvent(new ClickEvent(Action.RUN_COMMAND, "/xj copy " + var3));
                        var5.setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Click to copy this message")));
                        var4.setChatStyle(var5);
                        ChatComponentText var6 = new ChatComponentText(ChatLib.addColor("&8C]"));
                        ChatStyle var7 = new ChatStyle();
                        var7.setChatClickEvent(new ClickEvent(Action.RUN_COMMAND, "/xj copy " + var2));
                        var7.setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, new ChatComponentText(ChatLib.addColor("Click to copy this &1m&2e&3s&4s&5a&6g&ae"))));
                        var6.setChatStyle(var7);
                        var1.message.appendSibling(var4);
                        var1.message.appendSibling(var6);
                    }
                }
            }
        }
    }
}
