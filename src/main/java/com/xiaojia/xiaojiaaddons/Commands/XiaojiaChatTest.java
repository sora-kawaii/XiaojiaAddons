package com.xiaojia.xiaojiaaddons.Commands;

import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import java.util.ArrayList;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class XiaojiaChatTest extends CommandBase {
   public String getCommandName() {
      return "xt";
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (Checker.enabled) {
         if (var2 != null) {
            if (var2.length == 0) {
               ChatLib.chat(this.getUsage());
            } else {
               String var3 = String.join(" ", var2);
               com.xiaojia.xiaojiaaddons.Features.Remote.XiaojiaChat.chat("@t " + var3, 0);
            }
         }
      }
   }

   public String getCommandUsage(ICommandSender var1) {
      return this.getUsage();
   }

   public ArrayList getCommandAliases() {
      return new ArrayList();
   }

   public int getRequiredPermissionLevel() {
      return 0;
   }

   private String getUsage() {
      return "&c/xt message&b to send tester mode chat";
   }
}
