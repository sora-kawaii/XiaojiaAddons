package com.xiaojia.xiaojiaaddons.Commands;

import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class TestControl extends CommandBase {

    private final boolean show = false;

    public String getCommandName() {
        return "control";
    }

    public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
        if (var2.length == 0) {
            ChatLib.chat(this.getUsage());
        } else {
            if (var2[0].equals("left")) {
                (new Thread(() -> {
                    try {
                        for (int var0 = 0; var0 < 100; ++var0) {
                            Thread.sleep(100L);
                            ControlUtils.leftClick();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                })).start();
            } else if (var2[0].equals("right")) {
                (new Thread(() -> {
                    try {
                        for (int var0 = 0; var0 < 100; ++var0) {
                            Thread.sleep(100L);
                            ControlUtils.rightClick();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                })).start();
            } else {
                (new Thread(() -> {
                    try {
                        for (int var0 = -10; var0 < 10; ++var0) {
                            for (int i = -10; i < 10; ++i) {
                                Thread.sleep(100L);
                                ControlUtils.face((float) var0, (float) (Math.random() * 50.0 + 50.0), (float) i);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                })).start();
            }

        }
    }

    public String getCommandUsage(ICommandSender var1) {
        return this.getUsage();
    }

    private String getUsage() {
        return "/control right to test rightClick\n/control left to test leftClick\n/control face to test face";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
