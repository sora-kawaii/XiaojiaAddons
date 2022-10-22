package com.xiaojia.xiaojiaaddons.Features.Remote.API;

import com.xiaojia.xiaojiaaddons.Config.Config;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiKey {

    private static final Pattern API_KEY_PATTERN = Pattern.compile("^Your new API key is ([a-zA-Z0-9-]+)");

    public static void setApiKey(String var0) {
        ChatLib.chat("Api key set successfully.");
        Configs.APIKey = var0;
        Config.save();
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent var1) {
        if (Checker.enabled) {
            if (var1.type == 0) {
                String var2 = var1.message.getUnformattedText();
                if (var2 != null) {
                    Matcher var3 = API_KEY_PATTERN.matcher(var2);
                    if (var3.matches()) {
                        setApiKey(var3.group(1));
                    }

                }
            }
        }
    }
}
