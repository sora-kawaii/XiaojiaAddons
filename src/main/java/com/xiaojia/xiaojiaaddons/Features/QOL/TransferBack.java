package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransferBack {

    private static final String namePattern = "[a-zA-Z0-9_]+";

    private static final String promotePattern = "^(?:\\[VIP\\+*] |\\[MVP\\+*] |)(?<name>[a-zA-Z0-9_]+) has promoted";

    private static final String transferPattern = "^The party was transferred to (?:\\[VIP\\+*] |\\[MVP\\+*] |)[a-zA-Z0-9_]+ by (?:\\[VIP\\+*] |\\[MVP\\+*] |)(?<name>[a-zA-Z0-9_]+)";
    private static final String rankPattern = "(?:\\[VIP\\+*] |\\[MVP\\+*] |)";
    private static String fromPerson = null;

    public static void transferBack() {
        if (fromPerson != null) {
            CommandsUtils.addCommand("/p transfer " + fromPerson);
        }

    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent var1) {
        String var2 = var1.message.getUnformattedText();
        Pattern var3 = Pattern.compile("^The party was transferred to (?:\\[VIP\\+*] |\\[MVP\\+*] |)[a-zA-Z0-9_]+ by (?:\\[VIP\\+*] |\\[MVP\\+*] |)(?<name>[a-zA-Z0-9_]+)");
        Matcher var4 = var3.matcher(var2);
        if (var4.find()) {
            fromPerson = var4.group("name");
        }

        var3 = Pattern.compile("^(?:\\[VIP\\+*] |\\[MVP\\+*] |)(?<name>[a-zA-Z0-9_]+) has promoted");
        var4 = var3.matcher(var2);
        if (var4.find()) {
            fromPerson = var4.group("name");
        }

    }
}
