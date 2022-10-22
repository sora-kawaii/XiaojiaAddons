package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.vecmath.Vector3d;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ControlUtils {

    private static final KeyBind sprintKeyBind;

    private static final KeyBind moveRightKeyBind;

    private static final KeyBind sneakKeyBind;
    private static final KeyBind moveForwardKeyBind;
    private static final KeyBind moveBackwardKeyBind;
    private static final KeyBind attackKeyBind;
    private static final KeyBind jumpKeyBind;
    private static final KeyBind moveLeftKeyBind;
    private static final KeyBind useKeyBind;
    private static Method synHeldItem;
    private static Field pressTime;
    private static Method rightClickMethod;
    private static Inventory openedInventory;
    private static Method leftClickMethod;

    static {
        useKeyBind = new KeyBind(XiaojiaAddons.mc.gameSettings.keyBindUseItem);
        attackKeyBind = new KeyBind(XiaojiaAddons.mc.gameSettings.keyBindAttack);
        moveForwardKeyBind = new KeyBind(XiaojiaAddons.mc.gameSettings.keyBindForward);
        moveBackwardKeyBind = new KeyBind(XiaojiaAddons.mc.gameSettings.keyBindBack);
        moveLeftKeyBind = new KeyBind(XiaojiaAddons.mc.gameSettings.keyBindLeft);
        moveRightKeyBind = new KeyBind(XiaojiaAddons.mc.gameSettings.keyBindRight);
        sneakKeyBind = new KeyBind(XiaojiaAddons.mc.gameSettings.keyBindSneak);
        sprintKeyBind = new KeyBind(XiaojiaAddons.mc.gameSettings.keyBindSprint);
        jumpKeyBind = new KeyBind(XiaojiaAddons.mc.gameSettings.keyBindJump);
        openedInventory = null;
        synHeldItem = null;
        leftClickMethod = null;
        rightClickMethod = null;
        pressTime = null;

        try {
            synHeldItem = PlayerControllerMP.class.getDeclaredMethod("syncCurrentPlayItem");
        } catch (NoSuchMethodException var31) {
            try {
                synHeldItem = PlayerControllerMP.class.getDeclaredMethod("func_78750_j");
            } catch (NoSuchMethodException var30) {
                var30.printStackTrace();
            }
        } finally {
            if (synHeldItem != null) {
                synHeldItem.setAccessible(true);
            }

        }

        try {
            pressTime = KeyBinding.class.getDeclaredField("pressTime");
        } catch (NoSuchFieldException var29) {
            try {
                pressTime = KeyBinding.class.getDeclaredField("field_151474_i");
            } catch (NoSuchFieldException var28) {
                var28.printStackTrace();
            }
        } finally {
            if (pressTime != null) {
                pressTime.setAccessible(true);
            }

        }

        try {
            leftClickMethod = XiaojiaAddons.mc.getClass().getDeclaredMethod("clickMouse");
        } catch (NoSuchMethodException var27) {
            try {
                leftClickMethod = XiaojiaAddons.mc.getClass().getDeclaredMethod("func_147116_af");
            } catch (NoSuchMethodException var26) {
                var26.printStackTrace();
            }
        }

        leftClickMethod.setAccessible(true);

        try {
            rightClickMethod = XiaojiaAddons.mc.getClass().getDeclaredMethod("rightClickMouse");
        } catch (NoSuchMethodException var25) {
            try {
                rightClickMethod = XiaojiaAddons.mc.getClass().getDeclaredMethod("func_147121_ag");
            } catch (NoSuchMethodException var24) {
                var24.printStackTrace();
            }
        }

        rightClickMethod.setAccessible(true);
    }

    public static boolean checkHoldingItem(String var0) {
        ArrayList var1 = new ArrayList();
        var1.add(var0);
        return checkHoldingItem(var1);
    }

    public static ItemStack getItemStackInSlot(int var0, boolean var1) {
        Inventory var2 = getOpenedInventory();
        return var2 != null && (!var1 || var2.getSize() == 45) ? var2.getItemInSlot(var0) : null;
    }

    public static void releaseJump() {
        KeyBinding.setKeyBindState(jumpKeyBind.mcKeyBinding().getKeyCode(), false);
    }

    public static void faceSlowly(double var0, double var2) throws InterruptedException {
        faceSlowly((float) var0, (float) var2, true);
    }

    public static void etherWarp(double var0, double var2, double var4) throws Exception {
        sneak();
        faceSlowly(var0, var2, var4);
        Thread.sleep(SkyblockUtils.getPing() + 50);
        rightClick();
        Thread.sleep(SkyblockUtils.getPing() + 100);
        unSneak();
    }

    public static void faceSlowly(float var0, float var1, boolean var2) throws InterruptedException {
        float var3 = MathUtils.getYaw();
        float var4 = MathUtils.getPitch();
        if (var3 < 0.0F) {
            var3 += 360.0F;
        }

        if (var0 < 0.0F) {
            var0 += 360.0F;
        }

        if (var0 - var3 > 180.0F) {
            var0 -= 360.0F;
        }

        if (var3 - var0 > 180.0F) {
            var3 -= 360.0F;
        }

        int[] var5 = new int[]{50, 20, 7, 3};
        int var6 = var5[Configs.ChangeDirectionMode];
        int var7 = MathUtils.floor(Math.sqrt(Math.pow(var3 - var0, 2.0) + Math.pow(var4 - var1, 2.0)) / (double) var6) + 1;
        System.err.printf("curyaw %.2f, yaw %.2f%n", var3, var0);

        for (int var8 = 1; var8 <= var7; ++var8) {
            float var9;
            for (var9 = var3 + (var0 - var3) / (float) var7 * (float) var8; var9 > 180.0F; var9 -= 360.0F) {
            }

            while (var9 < -180.0F) {
                var9 += 360.0F;
            }

            float var10 = var4 + (var1 - var4) / (float) var7 * (float) var8;
            changeDirection(var9, var10);
            Thread.sleep((long) (10.0 + Math.random() * 20.0));
            checkDirection(var9, var10, var2);
        }

    }

    public static void rightClick() {
        try {
            if (Configs.CloseInvWhenClicking) {
                MinecraftUtils.getPlayer().closeScreen();
            }

            if (Configs.RawClick) {
                KeyBinding.onTick(-99);
            } else {
                rightClickMethod.invoke(XiaojiaAddons.mc);
            }
        } catch (Exception var1) {
        }

    }

    public static boolean checkHotbarItem(int var0, List var1) {
        Inventory var2 = getOpenedInventory();
        if (var0 != -1 && var2 != null) {
            Iterator var3 = var1.iterator();

            String var4;
            ItemStack var5;
            do {
                if (!var3.hasNext()) {
                    return false;
                }

                var4 = (String) var3.next();
                var5 = getItemStackInSlot(var0 + 36, true);
            } while (var5 == null || !var5.getDisplayName().contains(var4));

            return true;
        } else {
            return false;
        }
    }

    public static void faceSlowly(double var0, double var2, double var4) throws InterruptedException {
        faceSlowly((float) var0, (float) var2, (float) var4);
    }

    public static void faceSlowly(Vector3d var0) throws InterruptedException {
        faceSlowly((float) var0.x, (float) var0.y, (float) var0.z);
    }

    public static int getRightClickPressTime() throws IllegalAccessException {
        return (Integer) pressTime.get(useKeyBind.mcKeyBinding());
    }

    public static void setHeldItemIndex(int var0, boolean var1) {
        if (var0 >= 0 && var0 <= 8) {
            if (MinecraftUtils.getPlayer() != null) {
                InventoryPlayer var2 = MinecraftUtils.getPlayer().inventory;
                if (var2 != null) {
                    var2.currentItem = var0;
                    if (synHeldItem != null && var1) {
                        try {
                            synHeldItem.invoke(XiaojiaAddons.mc.playerController);
                        } catch (InvocationTargetException | IllegalAccessException var4) {
                            var4.printStackTrace();
                        }
                    }

                }
            }
        } else {
            System.err.println("WTF? NO");
        }
    }

    public static boolean checkHotbarItem(int var0, String var1) {
        Inventory var2 = getOpenedInventory();
        if (var0 != -1 && var2 != null) {
            ItemStack var3 = getItemStackInSlot(var0 + 36, true);
            if (var3 == null) {
                return false;
            } else {
                String var4 = var3.hasDisplayName() ? var3.getDisplayName() : var3.getItem().getRegistryName();
                return var4.contains(var1);
            }
        } else {
            return false;
        }
    }

    public static ItemStack getHeldItemStack() {
        if (MinecraftUtils.getPlayer() == null) {
            return null;
        } else {
            InventoryPlayer var0 = MinecraftUtils.getPlayer().inventory;
            return var0 == null ? null : var0.getCurrentItem();
        }
    }

    public static int getHeldItemIndex() {
        if (MinecraftUtils.getPlayer() == null) {
            return -1;
        } else {
            InventoryPlayer var0 = MinecraftUtils.getPlayer().inventory;
            return var0 == null ? -1 : var0.currentItem;
        }
    }

    public static void setHeldItemIndex(int var0) {
        setHeldItemIndex(var0, true);
    }

    public static String getOpenedInventoryName() {
        Inventory var0 = getOpenedInventory();
        return var0 != null ? var0.getName() : "";
    }

    public static void moveBackward(long var0) throws InterruptedException {
        KeyBinding.setKeyBindState(moveBackwardKeyBind.mcKeyBinding().getKeyCode(), true);
        Thread.sleep(var0);
        KeyBinding.setKeyBindState(moveBackwardKeyBind.mcKeyBinding().getKeyCode(), false);
    }

    public static void moveRight(long var0) throws InterruptedException {
        holdRight();
        Thread.sleep(var0);
        releaseRight();
    }

    public static void faceSlowly(float var0, float var1, float var2) throws InterruptedException {
        faceSlowly(var0, var1, var2, true);
    }

    public static void holdJump() {
        KeyBinding.setKeyBindState(jumpKeyBind.mcKeyBinding().getKeyCode(), true);
    }

    public static void stopMoving() {
        releaseForward();
        releaseBackward();
        releaseLeft();
        releaseRight();
        releaseJump();
        unSneak();
    }

    public static void moveForward(long var0) throws InterruptedException {
        KeyBinding.setKeyBindState(moveForwardKeyBind.mcKeyBinding().getKeyCode(), true);
        Thread.sleep(var0);
        KeyBinding.setKeyBindState(moveForwardKeyBind.mcKeyBinding().getKeyCode(), false);
    }

    public static boolean differentDirection(float var0, float var1) {
        return Math.abs(MathUtils.getYaw() - var0) > 0.01 && (double) Math.abs(MathUtils.getYaw() - var0) < 359.99 || Math.abs(MathUtils.getPitch() - var1) > 0.01;
    }

    public static Inventory getOpenedInventory() {
        return openedInventory;
    }

    public static void releaseForward() {
        KeyBinding.setKeyBindState(moveForwardKeyBind.mcKeyBinding().getKeyCode(), false);
    }

    public static void holdForward() {
        KeyBinding.setKeyBindState(moveForwardKeyBind.mcKeyBinding().getKeyCode(), true);
    }

    public static void releaseRightClick() {
        KeyBinding.setKeyBindState(useKeyBind.mcKeyBinding().getKeyCode(), false);
        if (MinecraftUtils.getPlayer().isUsingItem()) {
            XiaojiaAddons.mc.playerController.onStoppedUsingItem(MinecraftUtils.getPlayer());
        }

    }

    public static Tuple getFaceYawAndPitch(float var0, float var1, float var2) {
        System.err.printf("facing %.2f %.2f %.2f%n", var0, var1, var2);
        float var3 = 3.1415927F;
        float var4 = MathUtils.getX(MinecraftUtils.getPlayer());
        float var5 = MathUtils.getY(MinecraftUtils.getPlayer()) + MinecraftUtils.getPlayer().getEyeHeight();
        float var6 = MathUtils.getZ(MinecraftUtils.getPlayer());
        float var7 = var0 - var4;
        float var8 = var1 - var5;
        float var9 = var2 - var6;
        float var10 = (float) Math.sqrt((var4 - var0) * (var4 - var0) + (var6 - var2) * (var6 - var2));
        float var11 = (float) Math.atan2(var8, var10);
        float var12 = -180.0F / var3 * var11;

        float var13;
        for (var13 = (float) (Math.atan2(var9, var7) + (double) (3.0F * var3 / 2.0F)); var13 < -var3; var13 += 2.0F * var3) {
        }

        while (var13 > var3) {
            var13 -= 2.0F * var3;
        }

        var13 = 180.0F / var3 * var13;
        return new Tuple(var13, var12);
    }

    public static void releaseRight() {
        KeyBinding.setKeyBindState(moveRightKeyBind.mcKeyBinding().getKeyCode(), false);
    }

    public static void jump() throws InterruptedException {
        KeyBinding.setKeyBindState(jumpKeyBind.mcKeyBinding().getKeyCode(), true);
        Thread.sleep(100L);
        KeyBinding.setKeyBindState(jumpKeyBind.mcKeyBinding().getKeyCode(), false);
        Thread.sleep(100L);
    }

    public static boolean checkHotbarItemRegistryName(int var0, String var1) {
        Inventory var2 = getOpenedInventory();
        if (var0 != -1 && var2 != null) {
            ItemStack var3 = getItemStackInSlot(var0 + 36, true);
            return var3 != null && var3.getItem().getRegistryName().toLowerCase().contains(var1);
        } else {
            return false;
        }
    }

    public static void changeDirection(float var0, float var1) {
        if (!(var1 > 90.0F) && var1 >= -90.0F && var0 >= -180.0F && !(var0 > 180.0F)) {
            System.err.println("changed dir! " + var0 + ", " + var1);
            if (Configs.CloseInvWhenChangingDirection) {
                MinecraftUtils.getPlayer().closeScreen();
            }

            EntityPlayerSP var10000 = MinecraftUtils.getPlayer();
            var10000.rotationYaw += MathHelper.wrapAngleTo180_float(var0 - MinecraftUtils.getPlayer().rotationYaw);
            var10000 = MinecraftUtils.getPlayer();
            var10000.rotationPitch += MathHelper.wrapAngleTo180_float(var1 - MinecraftUtils.getPlayer().rotationPitch);
            XiaojiaAddons.mc.entityRenderer.getMouseOver(MathUtils.partialTicks);
        } else {
            System.err.println("wrong dir! " + var0 + ", " + var1);
        }
    }

    public static void releaseBackward() {
        KeyBinding.setKeyBindState(moveBackwardKeyBind.mcKeyBinding().getKeyCode(), false);
    }

    public static String getInventoryName() {
        Inventory var0 = getOpenedInventory();
        return var0 != null && var0.getName() != null ? ChatLib.removeFormatting(var0.getName()) : "";
    }

    public static void face(double var0, double var2, double var4) {
        face((float) var0, (float) var2, (float) var4);
    }

    public static void releaseLeftClick() {
        KeyBinding.setKeyBindState(attackKeyBind.mcKeyBinding().getKeyCode(), false);
    }

    public static void unSneak() {
        KeyBinding.setKeyBindState(sneakKeyBind.mcKeyBinding().getKeyCode(), false);
    }

    public static void faceSlowly(BlockPos var0) throws InterruptedException {
        faceSlowly((float) var0.getX(), (float) var0.getY(), (float) var0.getZ());
    }

    public static void face(Entity var0) {
        face((float) var0.posX, (float) var0.posY, (float) var0.posZ);
    }

    public static void leftClick() {
        try {
            if (Configs.CloseInvWhenClicking) {
                MinecraftUtils.getPlayer().closeScreen();
            }

            if (Configs.RawClick) {
                KeyBinding.onTick(-100);
            } else {
                leftClickMethod.invoke(XiaojiaAddons.mc);
            }
        } catch (Exception var1) {
        }

    }

    public static synchronized void faceSlowly(float var0, float var1, float var2, boolean var3) throws InterruptedException {
        Tuple var4 = getFaceYawAndPitch(var0, var1, var2);
        System.err.printf("facing slowly to %.2f %.2f %.2f%n", var0, var1, var2);
        float var5 = (Float) var4.getFirst();
        float var6 = (Float) var4.getSecond();
        faceSlowly(var5, var6, var3);
    }

    public static void checkDirection(float var0, float var1, boolean var2) throws InterruptedException {
        if (differentDirection(var0, var1) && var2) {
            ChatLib.chat("Detected yaw/pitch move, interrupted.");
            throw new InterruptedException();
        }
    }

    public static void holdRightClick() {
        KeyBinding.setKeyBindState(useKeyBind.mcKeyBinding().getKeyCode(), true);
    }

    public static void holdSprint() {
        KeyBinding.setKeyBindState(sprintKeyBind.mcKeyBinding().getKeyCode(), true);
    }

    public static void etherWarp(Vector3d var0) throws Exception {
        etherWarp(var0.x, var0.y, var0.z);
    }

    public static void randomChangeDirection(double var0) {
        float var2 = (float) MathUtils.validYaw((double) MinecraftUtils.getPlayer().rotationYaw + var0 - 2.0 * var0 * Math.random());
        float var3 = (float) MathUtils.validPitch((double) MinecraftUtils.getPlayer().rotationPitch + var0 - 2.0 * var0 * Math.random());
        changeDirection(var2, var3);
    }

    public static void face(float var0, float var1, float var2) {
        Tuple var3 = getFaceYawAndPitch(var0, var1, var2);
        float var4 = (Float) var3.getFirst();
        float var5 = (Float) var3.getSecond();
        changeDirection(var4, var5);
    }

    public static void sneak() {
        KeyBinding.setKeyBindState(sneakKeyBind.mcKeyBinding().getKeyCode(), true);
    }

    public static void releaseLeft() {
        KeyBinding.setKeyBindState(moveLeftKeyBind.mcKeyBinding().getKeyCode(), false);
    }

    public static void moveLeft(long var0) throws InterruptedException {
        holdLeft();
        Thread.sleep(var0);
        releaseLeft();
    }

    public static boolean reportedFacing(double var0, double var2) {
        try {
            Field var4;
            try {
                var4 = EntityPlayerSP.class.getDeclaredField("lastReportedYaw");
            } catch (NoSuchFieldException var11) {
                var4 = EntityPlayerSP.class.getDeclaredField("field_175164_bL");
            }

            Field var5;
            try {
                var5 = EntityPlayerSP.class.getDeclaredField("lastReportedPitch");
            } catch (NoSuchFieldException var10) {
                var5 = EntityPlayerSP.class.getDeclaredField("field_175165_bM");
            }

            var5.setAccessible(true);
            var4.setAccessible(true);
            double var6 = var4.getFloat(MinecraftUtils.getPlayer());
            double var8 = var5.getFloat(MinecraftUtils.getPlayer());
            return MathUtils.sameYaw(var0, var6) && MathUtils.samePitch(var2, var8);
        } catch (Exception var12) {
            var12.printStackTrace();
            return false;
        }
    }

    public static void forceFace() {
        MinecraftUtils.getPlayer().prevRotationPitch = MinecraftUtils.getPlayer().rotationPitch;
        MinecraftUtils.getPlayer().prevRotationYaw = MinecraftUtils.getPlayer().rotationYaw;
    }

    public static void holdLeftClick() {
        KeyBinding.setKeyBindState(attackKeyBind.mcKeyBinding().getKeyCode(), true);
    }

    public static boolean checkHoldingItem(List var0) {
        return getOpenedInventory() != null && getOpenedInventory().getSize() == 45 && checkHotbarItem(getHeldItemIndex(), var0);
    }

    public static void holdRight() {
        KeyBinding.setKeyBindState(moveRightKeyBind.mcKeyBinding().getKeyCode(), true);
    }

    public static void face(BlockPos var0) {
        face((float) var0.getX(), (float) var0.getY(), (float) var0.getZ());
    }

    public static void moveRandomly(long var0) throws InterruptedException {
        int var2 = (int) (Math.random() * 4.0);
        switch (var2) {
            case 0:
            case 1:
                moveForward(var0);
                break;
            case 2:
                moveLeft(var0);
                break;
            case 3:
                moveRight(var0);
        }

    }

    public static void holdLeft() {
        KeyBinding.setKeyBindState(moveLeftKeyBind.mcKeyBinding().getKeyCode(), true);
    }

    public static ItemStack getBoots() {
        EntityPlayerSP var0 = MinecraftUtils.getPlayer();
        return var0.getEquipmentInSlot(1);
    }

    @SubscribeEvent
    public void onTickUpdateInventory(TickEndEvent var1) {
        EntityPlayerSP var2 = MinecraftUtils.getPlayer();
        if (var2 != null && var2.openContainer != null) {
            openedInventory = new Inventory(var2.openContainer);
        } else {
            openedInventory = null;
        }

    }
}
