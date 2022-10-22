package com.xiaojia.xiaojiaaddons.Commands;

import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class PP extends CommandBase {
    public String getCommandName() {
        return "pp";
    }

    public String getCommandUsage(ICommandSender var1) {
        return "";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
        CommandsUtils.addCommand("/p PotassiumWings");
    }
}
