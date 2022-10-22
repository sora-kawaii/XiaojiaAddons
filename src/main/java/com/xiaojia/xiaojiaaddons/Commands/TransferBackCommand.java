package com.xiaojia.xiaojiaaddons.Commands;

import com.xiaojia.xiaojiaaddons.Features.QOL.TransferBack;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class TransferBackCommand extends CommandBase {
    public int getRequiredPermissionLevel() {
        return 0;
    }

    public String getCommandName() {
        return "ptb";
    }

    public String getCommandUsage(ICommandSender var1) {
        return "";
    }

    public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
        TransferBack.transferBack();
    }
}
