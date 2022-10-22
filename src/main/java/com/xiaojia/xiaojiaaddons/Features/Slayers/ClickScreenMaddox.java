package com.xiaojia.xiaojiaaddons.Features.Slayers;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

import java.util.Iterator;
import java.util.List;

public class ClickScreenMaddox {

    static long lastMaddoxTime = 0L;

    static String lastMaddoxCommand = "";

    @SubscribeEvent
    public void onMouseInputPost(GuiScreenEvent.MouseInputEvent.Post var1) {
        if (Mouse.getEventButton() == 0 && var1.gui instanceof GuiChat && Configs.ClickScreenMaddox && TimeUtils.curTime() - lastMaddoxTime < 10000L) {
            MinecraftUtils.getPlayer().sendChatMessage(lastMaddoxCommand);
        }

    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent var1) {
        String var2 = ChatLib.removeFormatting(var1.message.getUnformattedText());
        if (!var2.contains(":")) {
            if (var2.contains("[OPEN MENU]")) {
                List var3 = var1.message.getSiblings();
                Iterator var4 = var3.iterator();

                while (var4.hasNext()) {
                    IChatComponent var5 = (IChatComponent) var4.next();
                    if (var5.getUnformattedText().contains("[OPEN MENU]")) {
                        lastMaddoxCommand = var5.getChatStyle().getChatClickEvent().getValue();
                        lastMaddoxTime = TimeUtils.curTime();
                    }
                }

                if (Configs.ClickScreenMaddox) {
                    MinecraftUtils.getPlayer().addChatMessage(new ChatComponentText(ChatLib.addColor("&aOpen chat then click anywhere on-screen to open Maddox")));
                }
            }

        }
    }
}
