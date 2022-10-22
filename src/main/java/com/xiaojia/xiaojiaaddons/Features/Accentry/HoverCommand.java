package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HoverCommand {
    public void convert(IChatComponent var1) {
        ChatStyle var2 = var1.getChatStyle();
        if (var2.getChatClickEvent() != null && var2.getChatHoverEvent() == null && var2.getChatClickEvent().getAction() == Action.RUN_COMMAND) {
            String var3 = var2.getChatClickEvent().getValue();
            var2.setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, new ChatComponentText(var3)));
        }

        var1.getSiblings().forEach(this::convert);
    }

    @SubscribeEvent
    public void onMessageReceived(ClientChatReceivedEvent var1) {
        if (Configs.ChatHoverCommand) {
            if (var1.type != 2) {
                IChatComponent var2 = var1.message;
                this.convert(var2);
            }
        }
    }
}
