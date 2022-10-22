package com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Room;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Cube;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.Objects.Line;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import java.util.*;

public class AutoBlaze {

    private static final List blazes = new ArrayList();
    private static final HashMap transGraph = new HashMap();
    private static final List blocks = new ArrayList();
    private static final List places = new ArrayList();
    public static StringBuilder log = new StringBuilder();
    public static Room room = null;
    public static StringBuilder tempLog = new StringBuilder();
    private static boolean lowFirst = false;
    private final KeyBind keyBind;
    public boolean should;
    private double facingYaw;
    private boolean arrowShot;
    private int aotvSlot;
    private boolean tpPacketReceived;
    private Thread shootingThread;
    private double facingPitch;

    public AutoBlaze() {
        this.keyBind = AutoPuzzle.keyBind;
        this.should = false;
        this.shootingThread = null;
        this.tpPacketReceived = false;
        this.arrowShot = false;
        this.facingYaw = 10000.0;
        this.facingPitch = 10000.0;
    }

    private static boolean checkMiddle(Vector3d var0, int var1, Vector2d var2) {
        Vector2d[] var3 = ShortbowUtils.getLeftRightYawPitchByMiddle(var2);
        if (!canHit(var0.x, var0.y + 0.25 + 1.62, var0.z, var2, ((BlazeInfo) blazes.get(var1)).cube, true)) {
            log.append("middle: can't hit, ???").append("\ntemp log for this:\n");
            log.append(String.format("blaze is at %.2f %.2f %.2f\n", ((BlazeInfo) blazes.get(var1)).cube.x, ((BlazeInfo) blazes.get(var1)).cube.y, ((BlazeInfo) blazes.get(var1)).cube.z));
            log.append(String.format("vec is at %.2f %.2f %.2f\n", var0.x, var0.y + 0.25 + 1.62, var0.z));
            log.append(String.format("facing at %.2f %.2f\n", var2.x, var2.y));
            log.append(tempLog);
            return false;
        } else {
            for (int var4 = var1 + 1; var4 < blazes.size(); ++var4) {
                log.append("check middle trying to hit " + var4 + " th blaze\n");
                if (canHit(var0.x, var0.y + 0.25 + 1.62, var0.z, var3[0], ((BlazeInfo) blazes.get(var4)).cube, false)) {
                    return false;
                }

                if (canHit(var0.x, var0.y + 0.25 + 1.62, var0.z, var2, ((BlazeInfo) blazes.get(var4)).cube, true)) {
                    return false;
                }

                if (canHit(var0.x, var0.y + 0.25 + 1.62, var0.z, var3[1], ((BlazeInfo) blazes.get(var4)).cube, false)) {
                    return false;
                }
            }

            Iterator var6 = blocks.iterator();

            Cube var5;
            do {
                if (!var6.hasNext()) {
                    return true;
                }

                var5 = (Cube) var6.next();
            } while (!canHit(var0.x, var0.y + 0.25 + 1.62, var0.z, var2, var5, true));

            return false;
        }
    }

    private static boolean checkRight(Vector3d var0, int var1, Vector2d var2) {
        Vector2d[] var3 = ShortbowUtils.getLeftMiddleYawPitchByRight(var2);
        if (!canHit(var0.x, var0.y + 0.25 + 1.62, var0.z, var2, ((BlazeInfo) blazes.get(var1)).cube, false)) {
            log.append("right: can't hit, ???").append("\ntemp log for this:\n");
            log.append(String.format("blaze is at %.2f %.2f %.2f\n", ((BlazeInfo) blazes.get(var1)).cube.x, ((BlazeInfo) blazes.get(var1)).cube.y, ((BlazeInfo) blazes.get(var1)).cube.z));
            log.append(String.format("vec is at %.2f %.2f %.2f\n", var0.x, var0.y + 0.25 + 1.62, var0.z));
            log.append(String.format("facing at %.2f %.2f\n", var3[1].x, var3[1].y));
            log.append(tempLog);
            return false;
        } else {
            for (int var4 = var1 + 1; var4 < blazes.size(); ++var4) {
                log.append("check right trying to hit " + var4 + " th blaze\n");
                if (canHit(var0.x, var0.y + 0.25 + 1.62, var0.z, var3[0], ((BlazeInfo) blazes.get(var4)).cube, false)) {
                    return false;
                }

                if (canHit(var0.x, var0.y + 0.25 + 1.62, var0.z, var3[1], ((BlazeInfo) blazes.get(var4)).cube, true)) {
                    return false;
                }

                if (canHit(var0.x, var0.y + 0.25 + 1.62, var0.z, var2, ((BlazeInfo) blazes.get(var4)).cube, false)) {
                    return false;
                }
            }

            Iterator var6 = blocks.iterator();

            Cube var5;
            do {
                if (!var6.hasNext()) {
                    return true;
                }

                var5 = (Cube) var6.next();
            } while (!canHit(var0.x, var0.y + 0.25 + 1.62, var0.z, var2, var5, false));

            return false;
        }
    }

    public static Vector3d calculateSidePosToEtherWarp(double var0, double var2, double var4) throws Exception {
        int[] var6 = new int[]{1, -1, 0, 0};
        int[] var7 = new int[]{0, 0, 1, -1};

        for (int var8 = 0; var8 < 4; ++var8) {
            float var9 = (float) var0 + (float) var6[var8];
            float var10 = (float) var4 + (float) var7[var8];
            if (BlockUtils.isBlockAir(var9, (float) var2, var10) && BlockUtils.isBlockAir(var9, (float) var2 - 1.0F, var10)) {
                return new Vector3d(var0 + (double) ((float) var6[var8] / 2.0F), var2, var4 + (double) ((float) var7[var8] / 2.0F));
            }
        }

        throw new Exception();
    }

    private static Vector2d blazeCanHit(Vector3d var0, int var1, boolean var2) {
        BlazeInfo var3 = (BlazeInfo) blazes.get(var1);
        double var4 = var3.cube.x;
        double var6 = var3.cube.y;
        double var8 = var3.cube.z;
        Vector2d var10 = new Vector2d(10000.0, 10000.0);
        Vector2d var11 = ShortbowUtils.getDirection(var0.x, var0.y + 0.25 + 1.62, var0.z, var4, var6, var8);
        log.append(String.format("trying blaze can hit: %.2f %.2f %.2f, %d", var0.x, var0.y, var0.z, var1));
        if (var2) {
            if (checkMiddle(var0, var1, var11)) {
                return var11;
            } else {
                var11 = ShortbowUtils.getDirection(var0.x, var0.y + 0.25 + 1.62, var0.z, var4, var6, var8, false);
                Vector2d[] var15 = ShortbowUtils.getMiddleRightYawPitchByLeft(var11);
                if (checkLeft(var0, var1, var11)) {
                    return var15[0];
                } else {
                    Vector2d[] var16 = ShortbowUtils.getLeftMiddleYawPitchByRight(var11);
                    return checkRight(var0, var1, var11) ? var16[1] : var10;
                }
            }
        } else if (!canHit(var0.x, var0.y + 0.25 + 1.62, var0.z, var11, var3.cube, true)) {
            log.append("can't hit, ????").append("\ntemp log for this:\n");
            log.append(String.format("blaze is at %.2f %.2f %.2f\n", var4, var6, var8));
            log.append(String.format("vec is at %.2f %.2f %.2f\n", var0.x, var0.y + 0.25 + 1.62, var0.z));
            log.append(String.format("facing at %.2f %.2f\n", var11.x, var11.y));
            log.append(tempLog);
            return var10;
        } else {
            for (int var12 = var1 + 1; var12 < blazes.size(); ++var12) {
                if (canHit(var0.x, var0.y + 0.25 + 1.62, var0.z, var11, ((BlazeInfo) blazes.get(var12)).cube, true)) {
                    log.append("can't hit, because it may shoot " + var12 + " th blaze!").append("\n");
                    return var10;
                }
            }

            Iterator var14 = blocks.iterator();

            Cube var13;
            do {
                if (!var14.hasNext()) {
                    return var11;
                }

                var13 = (Cube) var14.next();
            } while (!canHit(var0.x, var0.y + 0.25 + 1.62, var0.z, var11, var13, true));

            log.append(String.format("can't hit, because it may hit block at %.2f %.2f %.2f", var13.x, var13.y, var13.z)).append("\n");
            return var10;
        }
    }

    private static boolean checkLeft(Vector3d var0, int var1, Vector2d var2) {
        Vector2d[] var3 = ShortbowUtils.getMiddleRightYawPitchByLeft(var2);
        if (!canHit(var0.x, var0.y + 0.25 + 1.62, var0.z, var2, ((BlazeInfo) blazes.get(var1)).cube, false)) {
            log.append("left: can't hit, ???").append("\ntemp log for this:\n");
            log.append(String.format("blaze is at %.2f %.2f %.2f\n", ((BlazeInfo) blazes.get(var1)).cube.x, ((BlazeInfo) blazes.get(var1)).cube.y, ((BlazeInfo) blazes.get(var1)).cube.z));
            log.append(String.format("vec is at %.2f %.2f %.2f\n", var0.x, var0.y + 0.25 + 1.62, var0.z));
            log.append(String.format("facing at %.2f %.2f\n", var3[0].x, var3[0].y));
            log.append(tempLog);
            return false;
        } else {
            for (int var4 = var1 + 1; var4 < blazes.size(); ++var4) {
                log.append("check left trying to hit " + var4 + " th blaze\n");
                if (canHit(var0.x, var0.y + 0.25 + 1.62, var0.z, var2, ((BlazeInfo) blazes.get(var4)).cube, false)) {
                    return false;
                }

                if (canHit(var0.x, var0.y + 0.25 + 1.62, var0.z, var3[0], ((BlazeInfo) blazes.get(var4)).cube, true)) {
                    return false;
                }

                if (canHit(var0.x, var0.y + 0.25 + 1.62, var0.z, var3[1], ((BlazeInfo) blazes.get(var4)).cube, false)) {
                    return false;
                }
            }

            Iterator var6 = blocks.iterator();

            Cube var5;
            do {
                if (!var6.hasNext()) {
                    return true;
                }

                var5 = (Cube) var6.next();
            } while (!canHit(var0.x, var0.y + 0.25 + 1.62, var0.z, var2, var5, false));

            return false;
        }
    }

    public static boolean isInXZSquare(double var0, double var2, double var4, double var6, double var8, double var10) {
        return MathUtils.isBetween(var0, var4, var8) && MathUtils.isBetween(var2, var6, var10);
    }

    public static void test() {
        blazes.clear();
        lowFirst = true;
        blazes.add(new BlazeInfo(45.0, 51.0, 143.0, 10, null));
        blazes.add(new BlazeInfo(49.0, 59.0, 143.0, 100, null));
        System.out.println(checkMiddle(new Vector3d(40.5, 42.75, 143.5), 0, new Vector2d(-96.34, -59.24)));
        System.out.println(log);
    }

    public static boolean canHit(double var0, double var2, double var4, Vector2d var6, Cube var7, boolean var8) {
        double var9 = var6.x;
        double var11 = var6.y;
        tempLog = new StringBuilder();
        log.append(String.format("trying canHit with yaw: %.2f, pitch: %.2f", var9, var11)).append("\n");
        double var13 = Math.PI;
        double var15 = var7.x - var7.w;
        double var17 = var7.z - var7.w;
        double var19 = var7.x + var7.w;
        double var21 = var7.z + var7.w;
        if (isInXZSquare(var0, var4, var15, var17, var19, var21)) {
            return true;
        } else {
            double var23 = (var9 + 90.0) * var13 / 180.0;
            double var25 = Math.tan(var23);
            double var27 = var4 + (var15 - var0) * var25;
            double var29 = var0 + (var17 - var4) * (1.0 / var25);
            double var31 = var4 + (var19 - var0) * var25;
            double var33 = var0 + (var21 - var4) * (1.0 / var25);
            ArrayList var35 = new ArrayList();
            if (var27 >= var17 && var27 < var21) {
                var35.add(new Vector2d(var15, var27));
            }

            if (var31 > var17 && var31 <= var21) {
                var35.add(new Vector2d(var19, var31));
            }

            if (var29 > var15 && var29 <= var19) {
                var35.add(new Vector2d(var29, var17));
            }

            if (var33 >= var15 && var33 < var19) {
                var35.add(new Vector2d(var33, var21));
            }

            if (var35.size() <= 1) {
                log.append("canHit false: intersect size too small").append("\n");
                return false;
            } else if (var35.size() != 2) {
                ChatLib.chat("NOOOOOOOOO");
                return false;
            } else {
                var15 = ((Vector2d) var35.get(0)).x;
                var19 = ((Vector2d) var35.get(1)).x;
                var17 = ((Vector2d) var35.get(0)).y;
                var21 = ((Vector2d) var35.get(1)).y;
                double var36 = Math.sqrt((var0 - var15) * (var0 - var15) + (var4 - var17) * (var4 - var17));
                double var38 = Math.sqrt((var0 - var19) * (var0 - var19) + (var4 - var21) * (var4 - var21));
                var11 = -var13 / 180.0 * var11;
                double var40 = var7.y - var7.h - var2;
                double var42 = var7.y + var7.h - var2;
                double var44 = ShortbowUtils.getProjectileFunction(var36, var11, var8);
                double var46 = ShortbowUtils.getProjectileFunction(var38, var11, var8);
                log.append(String.format("sx: %.2f, tx: %.2f, sz: %.2f, tz: %.2f", var15, var19, var17, var21)).append("\n");
                log.append(String.format("sX: %.2f, tX: %.2f, pitch: %.2f", var36, var38, var11)).append("\n");
                log.append(String.format("sy: %.2f, ty: %.2f, sY: %.2f, tY: %.2f", var40, var42, var44, var46)).append("\n");
                boolean var48 = MathUtils.isBetween(var44, var40, var42) || MathUtils.isBetween(var46, var40, var42) || MathUtils.isBetween(var40, var44, var46) && MathUtils.isBetween(var42, var44, var46);
                log.append("res: " + var48 + "\n");
                return var48;
            }
        }
    }

    public static void setRoom(Room var0) {
        room = var0;
    }

    public static Vector3d whereShouldIEtherWarpTo(double var0, double var2, double var4, double var6) throws Exception {
        double var8 = Math.round(var4 * 4.0) % 4L == 1L ? 0.25 : 0.5;
        return var0 >= var4 + 20.0 ? new Vector3d(var2, var4 + var8, var6) : calculateSidePosToEtherWarp(var2, var4, var6);
    }

    public void calculateBlazes() {
        ArrayList var1 = new ArrayList();
        Iterator var2 = EntityUtils.getEntities().iterator();

        while (var2.hasNext()) {
            Entity var3 = (Entity) var2.next();
            String var4 = var3.getName();
            if (var4.contains("Blaze") && var4.contains("/")) {
                String var5 = ChatLib.removeFormatting(var3.getName());

                try {
                    int var6 = Integer.parseInt(var5.substring(var5.indexOf("/") + 1, var5.length() - 1));
                    var1.add(new BlazeInfo(MathUtils.getX(var3), MathUtils.getY(var3) - 1.0F, MathUtils.getZ(var3), var6, var3));
                } catch (NumberFormatException var7) {
                    var7.printStackTrace();
                }
            }
        }

        blazes.clear();
        blazes.addAll(var1);
        blazes.sort((var0_, var1x_) -> {
            BlazeInfo var0 = (BlazeInfo) var0_, var1x = (BlazeInfo) var1x_;
            return lowFirst ? var0.hp - var1x.hp : var1x.hp - var0.hp;
        });
        var2 = blazes.iterator();

        while (var2.hasNext()) {
            BlazeInfo var8 = (BlazeInfo) var2.next();
            log.append(String.format("blaze: %.2f %.2f %.2f %d", var8.cube.x, var8.cube.y, var8.cube.z, var8.hp)).append("\n");
        }

    }

    @SubscribeEvent
    public void onPacketReceived(PacketReceivedEvent var1) {
        if (var1.packet instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook var2 = (S08PacketPlayerPosLook) var1.packet;
            this.tpPacketReceived = true;
            log.append(PacketUtils.getPosLookPacket(var2));
        }

    }

    private List getPossibleEtherwarpPointsFromPlayer() throws Exception {
        ArrayList var1 = new ArrayList();
        Iterator var2 = places.iterator();

        while (var2.hasNext()) {
            Vector3d var3 = (Vector3d) var2.next();
            if (this.canEtherwarpFromCur(var3)) {
                log.append("can go to " + var3).append("\n");
                var1.add(var3);
            }
        }

        return var1;
    }

    public void calculatePlaces() throws Exception {
        int var1 = room.x;
        int var2 = room.z;
        int[] var3 = new int[]{7, -7, 0, 0};
        int[] var4 = new int[]{0, 0, 7, -7};
        short var5 = 159;
        short var6 = 231;
        if (!lowFirst) {
            var5 = 59;
            var6 = 131;
        }

        for (int var7 = var5; var7 <= var6; var7 += 9) {
            float var8 = (float) var7 / 2.0F - 0.25F;

            for (int var9 = 0; var9 < 4; ++var9) {
                float var10 = (float) (var1 + var3[var9]) + 0.5F;
                float var11 = (float) (var2 + var4[var9]) + 0.5F;
                if (!BlockUtils.isBlockAir(var10, var8, var11) && BlockUtils.isBlockAir(var10, var8 + 1.0F, var11)) {
                    places.add(new Vector3d(var10, var8, var11));
                    log.append(String.format("add new place: %.2f %.2f %.2f", var10, var8, var11)).append("\n");
                    Cube var12;
                    if (var7 % 2 == 0) {
                        var12 = new Cube(var10, (double) ((float) var7 / 2.0F) - 0.5, var11, 0.5, 0.5);
                    } else {
                        var12 = new Cube(var10, var8, var11, 0.5, 0.25);
                    }

                    blocks.add(var12);
                    log.append(String.format("block: %.2f %.2f %.2f", var12.x, var12.y, var12.z)).append("\n");
                }
            }
        }

        transGraph.clear();
        ArrayList var15 = new ArrayList();
        Iterator var16 = places.iterator();

        while (var16.hasNext()) {
            Vector3d var17 = (Vector3d) var16.next();
            transGraph.put(var17, new ArrayList());
            Iterator var18 = places.iterator();

            while (var18.hasNext()) {
                Vector3d var19 = (Vector3d) var18.next();
                Vector3d var20 = new Vector3d(var17.x, var17.y + 1.54 + 0.25, var17.z);
                Vector3d var13 = whereShouldIEtherWarpTo(var17.y + 0.25 + 1.54, var19.x, var19.y, var19.z);
                boolean var14 = this.noBlocksBetween(var20, var13);
                var15.add(new Line(var20, var13, var14));
                if (var14) {
                    ((List) transGraph.get(var17)).add(var19);
                }
            }
        }

    }

    private ArrayList getOrderSequence(boolean var1) throws Exception {
        ArrayList var2 = new ArrayList();
        int var3 = 0;

        Vector3d var5;
        for (Vector3d var4 = null; var3 < blazes.size(); var4 = var5) {
            var5 = null;
            int var6 = var3;
            Object var7 = new ArrayList();
            if (var4 == null) {
                var7 = this.getPossibleEtherwarpPointsFromPlayer();
            } else {
                ((List) var7).addAll((Collection) transGraph.get(var4));
            }

            ArrayList var8 = new ArrayList();
            Iterator var9 = ((List) var7).iterator();

            while (var9.hasNext()) {
                Vector3d var10 = (Vector3d) var9.next();
                int var11 = var3;
                ArrayList var12 = new ArrayList();
                var12.add(new BlazeOrder(var10.x, var10.y, var10.z));

                while (var11 < blazes.size()) {
                    Vector2d var13 = blazeCanHit(var10, var11, var1);
                    if (var13.x > 1000.0 || var13.y > 70.0) {
                        break;
                    }

                    var12.add(new BlazeOrder(var13.x, var13.y, ((BlazeInfo) blazes.get(var11)).hpEntity));
                    log.append("Can hit: " + var10 + ", " + var11).append("\n");
                    ++var11;
                }

                if (var11 > var6) {
                    var6 = var11;
                    var5 = var10;
                    var8 = var12;
                }
            }

            if (var5 == null) {
                ChatLib.chat("CANT FIND GOOD POSITION!");
                throw new Exception();
            }

            var2.addAll(var8);
            log.append(String.format("decided to tp to: %.2f %.2f %.2f\n\n", ((BlazeOrder) var8.get(0)).x, ((BlazeOrder) var8.get(0)).y, ((BlazeOrder) var8.get(0)).z));
            var3 = var6;
        }

        return var2;
    }

    private void executeOrder(int var1, ArrayList var2) throws Exception {
        long var3 = 0L;
        Iterator var5 = var2.iterator();

        while (var5.hasNext()) {
            BlazeOrder var6 = (BlazeOrder) var5.next();
            if (!this.should) {
                break;
            }

            if (var6.type == AutoBlaze.BlazeOrder.Type.WARP) {
                this.tpPacketReceived = false;
                this.etherWarpTo(new Vector3d(var6.x, var6.y, var6.z));
            } else {
                this.arrowShot = false;
                log.append("shooting to " + var6.yaw + ", " + var6.pitch).append("\n");
                ControlUtils.setHeldItemIndex(var1);
                ControlUtils.faceSlowly(var6.yaw, var6.pitch);
                this.facingYaw = var6.yaw;
                this.facingPitch = var6.pitch;
                double var7 = Math.sqrt(MathUtils.distanceSquareFromPlayer(var6.entity));
                long var9 = MathUtils.floor(var7 * 50.0 / 1.7);
                log.append(String.format("distance: %.2f, estimate: %d\n", var7, var9));
                Thread.sleep(Configs.TurnShootDelay);

                while (TimeUtils.curTime() + var9 < var3 + 50L && this.should) {
                    Thread.sleep(20L);
                }

                if (!this.should) {
                    break;
                }

                ControlUtils.leftClick();

                for (int var11 = 0; !this.arrowShot && this.should; ++var11) {
                    if (var11 > 20) {
                        ControlUtils.leftClick();
                        var11 = 0;
                    }

                    Thread.sleep(20L);
                    ControlUtils.randomChangeDirection(1.0E-5);
                }

                var3 = var9 + TimeUtils.curTime();
                Thread.sleep(100L);
                log.append("lastHitime: " + var3 + "\n");
            }
        }

    }

    private void grabSecret() throws Exception {
        Vector3d var1 = this.getSecretPoint();
        log.append(String.format("\nsecret is at: %.2f %.2f %.2f\n", var1.x, var1.y, var1.z));
        if (this.canEtherwarpFromCur(var1)) {
            this.etherWarpTo(var1);
        } else {
            Vector3d var2 = new Vector3d(-1.0, -1.0, -1.0);
            transGraph.put(var2, new ArrayList());
            List var3 = this.getPossibleEtherwarpPointsToSecret(var1);
            Iterator var4 = var3.iterator();

            while (var4.hasNext()) {
                Vector3d var5 = (Vector3d) var4.next();
                ((List) transGraph.get(var5)).add(var1);
            }

            List var13 = this.getPossibleEtherwarpPointsFromPlayer();
            Iterator var14 = var13.iterator();

            while (var14.hasNext()) {
                Vector3d var6 = (Vector3d) var14.next();
                ((List) transGraph.get(var2)).add(var6);
            }

            HashMap var15 = new HashMap();
            HashMap var16 = new HashMap();
            ArrayDeque var7 = new ArrayDeque();
            var7.add(var2);
            var15.put(var2, 0);
            boolean var8 = false;

            Vector3d var9;
            Vector3d var12;
            while (!var7.isEmpty()) {
                var9 = (Vector3d) var7.pollFirst();
                int var10 = (Integer) var15.get(var9);
                if (var8) {
                    break;
                }

                Iterator var11 = ((List) transGraph.get(var9)).iterator();

                while (var11.hasNext()) {
                    var12 = (Vector3d) var11.next();
                    if ((Integer) var15.getOrDefault(var12, 10000) > var10 + 1) {
                        var15.put(var12, var10 + 1);
                        var16.put(var12, var9);
                        var7.offerLast(var12);
                        if (var12 == var1) {
                            var8 = true;
                            break;
                        }
                    }
                }
            }

            if (!var8) {
                ChatLib.chat("Can't find a path to get the secret!");
                throw new Exception();
            } else {
                var9 = var1;

                ArrayList var17;
                for (var17 = new ArrayList(); var9 != var2; var9 = (Vector3d) var16.get(var9)) {
                    var17.add(var9);
                }

                for (int var18 = var17.size() - 1; var18 >= 0; --var18) {
                    var12 = (Vector3d) var17.get(var18);
                    this.etherWarpTo(var12);
                }

            }
        }
    }

    private List getPossibleEtherwarpPointsToSecret(Vector3d var1) throws Exception {
        ArrayList var2 = new ArrayList();
        Iterator var3 = places.iterator();

        while (var3.hasNext()) {
            Vector3d var4 = (Vector3d) var3.next();
            if (this.canEtherwarp(var4, var1)) {
                log.append("can go from " + var4).append("\n");
                var2.add(var4);
            }
        }

        return var2;
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load var1) {
        blazes.clear();
        blocks.clear();
        places.clear();
        setRoom(null);
        log = new StringBuilder();
    }

    private boolean canEtherwarp(Vector3d var1, Vector3d var2) throws Exception {
        return this.noBlocksBetween(new Vector3d(var1.x, var1.y + 0.25 + 1.54, var1.z), whereShouldIEtherWarpTo(var1.y + 0.25 + 1.54, var2.x, var2.y, var2.z));
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent var1) {
        Entity var2 = var1.entity;
        if (var2 instanceof EntityArrow) {
            double var3 = MathUtils.distanceSquareFromPlayer(var2.posX, var2.posY, var2.posZ);
            if (var3 < 25.0) {
                log.append(String.format("entity join: " + var2 + ", %.2f", var3)).append("\n");
                Vec3 var5 = new Vec3(var2.posX - (double) MathUtils.getX(MinecraftUtils.getPlayer()), var2.posY - (double) MathUtils.getY(MinecraftUtils.getPlayer()), var2.posZ - (double) MathUtils.getZ(MinecraftUtils.getPlayer()));
                double var6 = (this.facingYaw + 90.0) * Math.PI / 180.0;
                double var8 = -this.facingPitch * Math.PI / 180.0;
                Vec3 var10 = new Vec3(Math.cos(var8) * Math.cos(var6), Math.sin(var8), Math.cos(var8) * Math.sin(var6));
                double var11 = Math.acos(var5.dotProduct(var10) / var5.lengthVector() / var10.lengthVector());
                double var13 = var2.posZ - (double) MathUtils.getZ(MinecraftUtils.getPlayer());
                double var15 = var2.posX - (double) MathUtils.getX(MinecraftUtils.getPlayer());
                double var17 = Math.atan2(var13, var15);
                double var19 = Math.abs(var17 - var6);
                var19 = Math.min(var19, 6.283185307179586 - var19);
                log.append(String.format("diff: %.2f, diffAlpha: %.2f\n", var11, var19));
                if (var11 < 0.5 && var19 < 0.1) {
                    this.arrowShot = true;
                }
            }
        }

    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Checker.enabled && Configs.AutoBlaze && Dungeon.currentRoom.equals("Blaze")) {
            if (this.keyBind.isPressed()) {
                this.should = !this.should;
                if (this.should) {
                    ChatLib.chat("Auto Blaze &aactivated");
                } else {
                    ChatLib.chat("Auto Blaze &cdeactivated");
                }
            }

            if (this.should && Dungeon.isFullyScanned && room != null) {
                lowFirst = room.core == 1103121487;
                if (this.shootingThread == null || !this.shootingThread.isAlive()) {
                    this.shootingThread = new Thread(() -> {
                        try {
                            log.append("lowFirst: " + lowFirst).append("\n");
                            this.calculatePlaces();
                            this.calculateBlazes();
                            this.calculateBlocks();
                            boolean var1 = true;
                            this.aotvSlot = HotbarUtils.aotvSlot;
                            int var2 = HotbarUtils.terminatorSlot;
                            int var3 = HotbarUtils.shortBowSlot;
                            log.append("starting!").append("\n");
                            if (this.aotvSlot == -1) {
                                ChatLib.chat("Requires aotv in hotbar.");
                                throw new Exception();
                            }

                            int var4;
                            if (var2 != -1) {
                                var4 = var2;
                            } else {
                                if (var3 == -1) {
                                    ChatLib.chat("Requires terminator / shortbow in hotbar.");
                                    throw new Exception();
                                }

                                var4 = var3;
                                var1 = false;
                            }

                            ArrayList var5 = this.getOrderSequence(var1);
                            this.executeOrder(var4, var5);
                            if (Configs.AutoBlazeSecret && this.should) {
                                this.grabSecret();
                            }
                        } catch (Exception var9) {
                            var9.printStackTrace();
                        } finally {
                            this.deactivate();
                        }

                    });
                    this.shootingThread.start();
                }
            } else {
                this.deactivate();
            }
        } else {
            this.deactivate();
        }
    }

    private void etherWarpTo(Vector3d var1) throws Exception {
        this.tpPacketReceived = false;
        Vector3d var2 = whereShouldIEtherWarpTo(MathUtils.getY(MinecraftUtils.getPlayer()), var1.x, var1.y, var1.z);
        log.append(String.format("etherwarp to %.2f %.2f %.2f", var1.x, var1.y, var1.z)).append("\n");
        log.append(String.format("aim at %.2f %.2f %.2f", var2.x, var2.y, var2.z)).append("\n");
        ControlUtils.setHeldItemIndex(this.aotvSlot);
        ControlUtils.etherWarp(var2.x, var2.y, var2.z);
        int var3 = 0;

        while (!this.tpPacketReceived && this.should) {
            Thread.sleep(20L);
            ++var3;
            if (var3 >= 50) {
                ChatLib.chat("Too long no packet, please try again.");
                throw new Exception();
            }
        }

        Thread.sleep(Configs.EtherWarpDelayAfter);
        if (MathUtils.distanceSquareFromPlayer(var1.x, var1.y + 0.25 + (double) MathUtils.getEyeHeight(MinecraftUtils.getPlayer()), var1.z) > 1.0) {
            ChatLib.chat("Failed to etherwarp!");
            log.append(String.format("Player is at %.2f %.2f %.2f", MathUtils.getX(MinecraftUtils.getPlayer()), MathUtils.getY(MinecraftUtils.getPlayer()), MathUtils.getZ(MinecraftUtils.getPlayer()))).append("\n");
            throw new Exception();
        }
    }

    private Vector3d getSecretPoint() throws Exception {
        int var1 = lowFirst ? 118 : 68;
        int[] var2 = new int[]{1, -1, -1, 1};
        int[] var3 = new int[]{1, 1, -1, -1};

        for (int var4 = 0; var4 < 4; ++var4) {
            int var5 = room.x + var2[var4] * 5;
            int var6 = room.z + var3[var4] * 8;
            if (BlockUtils.isBlockBedRock(var5, var1, var6) && BlockUtils.isBlockWater(var5, var1, var6 - var3[var4])) {
                return new Vector3d((double) var5 + 0.5, (double) var1 + 0.75, (double) var6 + 0.5);
            }

            var5 = room.x + var2[var4] * 8;
            var6 = room.z + var3[var4] * 5;
            if (BlockUtils.isBlockBedRock(var5, var1, var6) && BlockUtils.isBlockWater(var5 - var2[var4], var1, var6)) {
                return new Vector3d((double) var5 + 0.5, (double) var1 + 0.75, (double) var6 + 0.5);
            }
        }

        throw new Exception();
    }

    private boolean canEtherwarpFromCur(Vector3d var1) throws Exception {
        return this.noBlocksBetween(new Vector3d(MathUtils.getX(MinecraftUtils.getPlayer()), (double) MathUtils.getY(MinecraftUtils.getPlayer()) + 1.54, MathUtils.getZ(MinecraftUtils.getPlayer())), whereShouldIEtherWarpTo(MathUtils.getY(MinecraftUtils.getPlayer()), var1.x, var1.y, var1.z));
    }

    private boolean noBlocksBetween(Vector3d var1, Vector3d var2) {
        return BlockUtils.getNearestBlock(var1, var2) == null;
    }

    private void deactivate() {
        if (this.should) {
            this.should = false;
            ChatLib.chat("Auto Blaze &cdeactivated");
        }

    }

    @SubscribeEvent
    public void onBlazeDeath(LivingDeathEvent var1) {
        if (var1.entity instanceof EntityBlaze) {
            log.append("blaze " + var1.entity + " died!\n");
        }

    }

    public void calculateBlocks() {
        ArrayList var1 = new ArrayList();
        byte var2 = 71;
        byte var3 = 118;
        if (!lowFirst) {
            var2 = 20;
            var3 = 69;
        }

        for (int var4 = var2; var4 < var3; ++var4) {
            var1.add(new Cube((double) room.x + 0.5, (double) var4 + 0.5, (double) room.z + 0.5, 0.5, 0.5));
            log.append("block: " + room.x + ", " + var4 + ", " + room.z).append("\n");
        }

        blocks.clear();
        blocks.addAll(var1);
    }

    static class BlazeInfo {

        Cube cube;

        int hp;

        Entity hpEntity;

        public BlazeInfo(double var1, double var3, double var5, int var7, Entity var8) {
            this.cube = new Cube(var1, var3, var5, (float) (Configs.BlazeScale) / 100.0F, 0.3 * (double) Configs.BlazeScale / 100.0);
            this.hp = var7;
            this.hpEntity = var8;
        }
    }

    static class BlazeOrder {

        double z;

        double yaw;

        double pitch;

        double x;

        double y;

        double w;

        double h;

        Type type;

        Entity entity;

        public BlazeOrder(double var1, double var3, double var5) {
            this.type = AutoBlaze.BlazeOrder.Type.WARP;
            this.x = var1;
            this.y = var3;
            this.z = var5;
        }

        public BlazeOrder(double var1, double var3, Entity var5) {
            this.type = AutoBlaze.BlazeOrder.Type.SHOOT;
            this.yaw = var1;
            this.pitch = var3;
            this.entity = var5;
        }

        enum Type {

            WARP,

            SHOOT
        }
    }
}
