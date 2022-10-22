package com.xiaojia.xiaojiaaddons.Commands;

import com.xiaojia.xiaojiaaddons.Features.Skills.Foraging;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class AutoForagingCommand extends CommandBase {
   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      byte var3 = 0;
      if (var2.length > 0) {
         if (var2[0].toLowerCase().equals("south")) {
            var3 = 1;
         } else if (var2[0].toLowerCase().equals("west")) {
            var3 = 2;
         } else if (var2[0].toLowerCase().equals("east")) {
            var3 = 3;
         }
      }

      Foraging.setForagingPoint(var3);
   }

   public String getCommandUsage(ICommandSender var1) {
      return "";
   }

   public int getRequiredPermissionLevel() {
      return 0;
   }

   public String getCommandName() {
      return "foragingpoint";
   }
}
