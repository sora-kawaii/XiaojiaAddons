package com.xiaojia.xiaojiaaddons.Commands;

import com.xiaojia.xiaojiaaddons.Features.Skills.AutoBuildFarmPumpkin;
import com.xiaojia.xiaojiaaddons.Features.Skills.AutoBuildFarmVertical;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class FarmingPoint extends CommandBase {

    private static EnumFarmingType type;

    static {
        type = EnumFarmingType.VERTICAL;
    }

    public static void setType(EnumFarmingType var0) {
        type = var0;
    }

    public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
        boolean var3 = false;
        int var4 = Integer.parseInt(var2[0]);
        int var5 = Integer.parseInt(var2[1]);
        ChatLib.chat("Current farming type: " + type);
        if (type == EnumFarmingType.VERTICAL) {
            AutoBuildFarmVertical.setFarmingPoint(var4, var5);
        } else if (type == EnumFarmingType.PUMPKIN) {
            AutoBuildFarmPumpkin.setFarmingPoint(var4, var5);
        }

    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public String getCommandUsage(ICommandSender var1) {
        return "&c/farmingpoint start end&b to set farming range.\nFor vertical farm, &c/farmingpoint startY endY&b;\nFor pumpkin farm, &c/farmingpoint startZ endZ&b.";
    }

    public String getCommandName() {
        return "farmingpoint";
    }
}
