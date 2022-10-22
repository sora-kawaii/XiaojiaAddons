package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Cube;
import com.xiaojia.xiaojiaaddons.Objects.TestCubeGUI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.vecmath.Vector2d;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class ShortbowUtils {

    private static final double k = 0.049;

    private static final double g = 1.0;

    public static boolean enabled = false;

    public static Vector2d getDirection(double var0, double var2, double var4, double var6, double var8, double var10) {
        return getDirection(var0, var2, var4, var6, var8, var10, true);
    }

    public static Vector2d getDirection(double var0, double var2, double var4, double var6, double var8, double var10, boolean var12) {
        double var13 = Math.PI;
        double var15 = var6 - var0;
        double var17 = var8 - var2;
        double var19 = var10 - var4;
        double var21 = Math.sqrt((var0 - var6) * (var0 - var6) + (var4 - var10) * (var4 - var10));
        double var23 = getV0(var12);
        double var25 = Math.acos(0.049 * var21 / var23);
        double var27 = -var25;
        double var29 = var25;

        double var33;
        double var35;
        double var37;
        double var31;
        while (var29 - var27 > 0.01) {
            var31 = var27 + (var29 - var27) / 3.0;
            var33 = var27 + (var29 - var27) / 3.0 * 2.0;
            var35 = getProjectileFunction(var21, var31);
            var37 = getProjectileFunction(var21, var33);
            if (var35 < var37) {
                var27 = var31;
            } else {
                var29 = var33;
            }
        }

        var31 = (var27 + var29) / 2.0;
        var27 = var31;
        var29 = var25;

        while (var29 - var27 > 0.01) {
            var33 = (var27 + var29) / 2.0;
            var35 = getProjectileFunction(var21, var33);
            if (var35 < var17) {
                var29 = var33;
            } else {
                var27 = var33;
            }
        }

        var33 = (var27 + var29) / 2.0;
        var27 = -var25;
        var29 = var31;

        while (var29 - var27 > 0.01) {
            var35 = (var27 + var29) / 2.0;
            var37 = getProjectileFunction(var21, var35);
            if (var37 > var17) {
                var29 = var35;
            } else {
                var27 = var35;
            }
        }

        var35 = (var27 + var29) / 2.0;

        for (var37 = Math.atan2(var19, var15) + 3.0 * var13 / 2.0; var37 < -var13; var37 += 2.0 * var13) {
        }

        while (var37 > var13) {
            var37 -= 2.0 * var13;
        }

        var37 = 180.0 / var13 * var37;
        var33 = -180.0 / var13 * var33;
        var35 = -180.0 / var13 * var35;
        double var39 = var33;
        if (Math.abs(var35) < Math.abs(var33)) {
            var39 = var35;
        }

        return new Vector2d(var37, var39);
    }

    public static Vector2d[] getLeftRightYawPitchByMiddle(Vector2d var0) {
        double var1 = var0.x;
        double var3 = var0.y;
        double var5 = getNewPitch(var3);
        double var7 = getDYaw(var3);
        return new Vector2d[]{new Vector2d(var1 - var7, var5), new Vector2d(var1 + var7, var5)};
    }

    public static double getProjectileFunction(double var0, double var2, boolean var4) {
        double var5 = getV0(var4);
        double var7 = -Math.log(1.0 - 0.049 * var0 / var5 / Math.cos(var2)) / 0.049;
        return var0 / (var5 * 0.049 * Math.cos(var2)) * (var5 * 0.049 * Math.sin(var2) + 1.0) - var7 / 0.049;
    }

    private static double getV0(boolean var0) {
        return var0 ? 13.75 : 13.8;
    }

    private static double y_t(double var0, double var2, boolean var4) {
        double var5 = getV0(var4);
        return 416.4931278633902 * (1.0 + 0.049 * var5 * Math.sin(var0)) * (1.0 - Math.exp(-0.049 * var2)) - 20.408163265306122 * var2;
    }

    public static void testTerminator() {
        (new Thread(() -> {
            try {
                double var0 = MathUtils.getX(MinecraftUtils.getPlayer());
                double var2 = MathUtils.getY(MinecraftUtils.getPlayer()) + MinecraftUtils.getPlayer().getEyeHeight();
                double var4 = MathUtils.getZ(MinecraftUtils.getPlayer());
                double var6 = (double) (MathUtils.getYaw() + 90.0F) * Math.PI / 180.0;
                double var8 = (double) (-MathUtils.getPitch()) * Math.PI / 180.0;
                double var10 = (1.5707963267948966 - var8) * 0.017453292519943295;
                if (var8 < 0.0) {
                    var10 = (var8 - -1.5707963267948966) * 0.017453292519943295;
                }

                double var12 = var8 + var10;
                double var14 = Math.sqrt(26.009999999999998 * Math.pow(0.017453292519943295, 2.0) + var10 * var10);
                ArrayList var16 = new ArrayList();
                TestCubeGUI.cubes.clear();

                for (int var17 = 0; var17 < 200; ++var17) {
                    double var18 = x_t(var8, (float) var17 / 10.0F, true);
                    double var20 = y_t(var8, (float) var17 / 10.0F, true);
                    double var22 = x_t(var12, (float) var17 / 10.0F, false);
                    double var24 = y_t(var12, (float) var17 / 10.0F, false);
                    Cube var26 = new Cube(var18 * Math.cos(var6) + var0, var2 + var20, var4 + var18 * Math.sin(var6), 0.1, 0.1);
                    Cube var27 = new Cube(var22 * Math.cos(var6 - var14) + var0, var2 + var24, var4 + var22 * Math.sin(var6 - var14), 0.1, 0.1);
                    Cube var28 = new Cube(var22 * Math.cos(var6 + var14) + var0, var2 + var24, var4 + var22 * Math.sin(var6 + var14), 0.1, 0.1);
                    var16.add(var27);
                    var16.add(var26);
                    var16.add(var28);
                }

                ControlUtils.leftClick();
                TestCubeGUI.cubes.addAll(var16);
            } catch (Exception var29) {
                var29.printStackTrace();
            }

        })).start();
    }

    public static void testEnderCrystals(double var0, double var2, double var4) {
        Vector2d var6 = calcYawPitchEnderCrystal(var0, var2, var4);
        double var7 = var6.x;
        double var9 = var6.y;
        ControlUtils.changeDirection((float) var7, (float) var9);
    }

    public static Vector2d calcYawPitchEnderCrystal(double var0, double var2, double var4) {
        double var6 = MathUtils.getX(MinecraftUtils.getPlayer());
        double var8 = MathUtils.getY(MinecraftUtils.getPlayer()) + MinecraftUtils.getPlayer().getEyeHeight();
        double var10 = MathUtils.getZ(MinecraftUtils.getPlayer());
        return getDirection(var6, var8, var10, var0, var2 + 1.0, var4);
    }

    public static double getProjectileFunction(double var0, double var2) {
        return getProjectileFunction(var0, var2, true);
    }

    public static Vector2d[] getLeftMiddleYawPitchByRight(Vector2d var0) {
        double var1 = var0.x;
        double var3 = var0.y;
        double var5 = getRawPitch(var3);
        double var7 = getDYaw(var5);
        double var9 = MathUtils.validYaw(var1 - var7);
        Vector2d[] var11 = getLeftRightYawPitchByMiddle(new Vector2d(var9, var5));
        double var12 = var11[1].x;
        double var14 = var11[1].y;

        assert Math.abs(var3 - var14) < 1.0E-4;

        assert Math.abs(var1 - var12) < 1.0E-4 || Math.abs(var1 - var12) > 359.9999;

        return new Vector2d[]{var11[0], new Vector2d(var9, var5)};
    }

    private static double getNewPitch(double var0) {
        assert var0 >= -90.0 && var0 <= 90.0;

        return -1.5707963267948966 + var0 + Math.abs(var0) * Math.PI / 180.0;
    }

    private static double x_t(double var0, double var2, boolean var4) {
        double var5 = getV0(var4);
        return var5 / 0.049 * Math.cos(var0) * (1.0 - Math.exp(-0.049 * var2));
    }

    private static double getDYaw(double var0) {
        return Math.sqrt(26.009999999999998 + Math.pow(var0 * Math.PI / 180.0 - 1.5707963267948966, 2.0));
    }

    private static double getRawPitch(double var0) {
        assert var0 >= -90.0 && var0 <= 90.0;

        return (1.5707963267948966 + var0) / (1.0 + 0.017453292519943295 * (double) (var0 < -1.5707963267948966 ? -1 : 1));
    }

    public static Vector2d[] getMiddleRightYawPitchByLeft(Vector2d var0) {
        double var1 = var0.x;
        double var3 = var0.y;
        double var5 = getRawPitch(var3);
        double var7 = getDYaw(var5);
        double var9 = MathUtils.validYaw(var1 + var7);
        Vector2d[] var11 = getLeftRightYawPitchByMiddle(new Vector2d(var9, var5));
        double var12 = var11[0].x;
        double var14 = var11[0].y;

        assert Math.abs(var3 - var14) < 1.0E-4;

        assert Math.abs(var1 - var12) < 1.0E-4 || Math.abs(var1 - var12) > 359.9999;

        return new Vector2d[]{new Vector2d(var9, var5), var11[1]};
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (enabled) {
            Iterator var2 = EntityUtils.getEntities().iterator();

            while (var2.hasNext()) {
                Entity var3 = (Entity) var2.next();
                if (var3 instanceof EntityArrow) {
                    Cube var4 = new Cube(MathUtils.getX(var3), MathUtils.getY(var3) - 0.1F, MathUtils.getZ(var3), 0.10000000149011612, 0.10000000149011612);
                    var4.color = new Color(255, 255, 255);
                    TestCubeGUI.cubes.add(var4);
                }
            }

        }
    }
}
