package com.xiaojia.xiaojiaaddons.Commands;

import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;

public class XiaojiaChat extends CommandBase {
    public ArrayList getCommandAliases() {
        return new ArrayList();
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public String getCommandUsage(ICommandSender var1) {
        return this.getUsage();
    }

    public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
        if (Checker.enabled) {
            if (var2 != null) {
                if (var2.length == 0) {
                    ChatLib.chat(this.getUsage());
                } else {
                    String var3 = String.join(" ", var2);
                    if (var3.equals("toggle")) {
                        ChatLib.toggle();
                    } else if (var3.equals("online")) {
                        com.xiaojia.xiaojiaaddons.Features.Remote.XiaojiaChat.queryOnline();
                    } else {
                        com.xiaojia.xiaojiaaddons.Features.Remote.XiaojiaChat.chat(var3, 0);
                    }
                }
            }
        }
    }

    public String getCommandName() {
        return "xc";
    }

    private String getUsage() {
        return "&c/xc message&b to send chat\n&c/xc toggle&b to toggle\n&c/xc online&b to see online members";
    }
}
