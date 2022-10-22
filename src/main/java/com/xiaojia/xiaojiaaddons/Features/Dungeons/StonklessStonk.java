package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.BlockChangeEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StonklessStonk {

    private static final KeyBind keyBind = new KeyBind("Stonkless Stonk", 0);
    private static final int witherEssenceHash = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzRkYjRhZGZhOWJmNDhmZjVkNDE3MDdhZTM0ZWE3OGJkMjM3MTY1OWZjZDhjZDg5MzQ3NDlhZjRjY2U5YiJ9fX0=".hashCode();
    private static final int redstoneKeyHash = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2I4NTJiYTE1ODRkYTllNTcxNDg1OTk5NTQ1MWU0Yjk0NzQ4YzRkZDYzYWU0NTQzYzE1ZjlmOGFlYzY1YzgifX19".hashCode();
    private static final HashMap doneSecretsPos = new HashMap();
    private static boolean should = true;
    private static boolean isInPuzzle = false;
    private static long lastClickTime = 0L;
    private final HashMap blockHashMap = new HashMap();
    private HashMap currentBlockMap = new HashMap();
    private BlockPos facingPos;
    private BlockPos lastPlayerPos;

    public static void setInPuzzle(boolean var0) {
        isInPuzzle = var0;
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        this.facingPos = null;
        if (Checker.enabled) {
            if (keyBind.isPressed()) {
                should = !should;
                ChatLib.chat(should ? "Stonkless Stonk &aactivated" : "Stonkless Stonk &cdeactivated");
            }

            if (SkyblockUtils.isInDungeon()) {
                if (Configs.StonklessStonkEnable) {
                    if (should) {
                        if (!isInPuzzle) {
                            if (MinecraftUtils.getPlayer() != null) {
                                float var2 = 3.1415927F;
                                float var3 = MathUtils.getX(MinecraftUtils.getPlayer());
                                float var4 = MathUtils.getY(MinecraftUtils.getPlayer()) + MinecraftUtils.getPlayer().getEyeHeight();
                                float var5 = MathUtils.getZ(MinecraftUtils.getPlayer());
                                float var6 = MathUtils.getYaw() * var2 / 180.0F;
                                float var7 = MathUtils.getPitch() * var2 / 180.0F;
                                float var8 = 5.0F;
                                int var9 = -1;
                                int var10 = -1;
                                int var11 = -1;
                                HashMap var12 = new HashMap();
                                boolean var13 = TimeUtils.curTime() - lastClickTime > 200L;

                                try {
                                    for (int var14 = 1; var14 <= 300; ++var14) {
                                        float var15 = var8 * (float) var14 / 300.0F;
                                        float var16 = (float) ((double) var3 - Math.sin(var6) * Math.cos(var7) * (double) var15);
                                        float var17 = (float) ((double) var4 - Math.sin(var7) * (double) var15);
                                        float var18 = (float) ((double) var5 + Math.cos(var6) * Math.cos(var7) * (double) var15);
                                        int var19 = MathUtils.floor(var16);
                                        int var20 = MathUtils.floor(var17);
                                        int var21 = MathUtils.floor(var18);
                                        if (var19 != var9 || var20 != var10 || var21 != var11) {
                                            var9 = var19;
                                            var10 = var20;
                                            var11 = var21;
                                            BlockPos var22 = new BlockPos(var19, var20, var21);
                                            Block var23 = BlockUtils.getBlockAt(var22);
                                            var12.put(var22, BlockUtils.getBlockStateAt(var22));
                                            if ((!doneSecretsPos.containsKey(var22) || TimeUtils.curTime() - (Long) doneSecretsPos.get(var22) >= 1000L) && this.isSecret(var23, var22)) {
                                                Inventory var24 = ControlUtils.getOpenedInventory();
                                                if (var24 == null || var24.getSize() != 45) {
                                                    var13 = false;
                                                }

                                                ItemStack var25 = ControlUtils.getHeldItemStack();
                                                String var26 = "";
                                                if (var25 != null && var25.hasDisplayName()) {
                                                    var26 = ControlUtils.getHeldItemStack().getDisplayName();
                                                }

                                                if (!Configs.StonklessStonkWithoutPickaxe && !var26.contains("Stonk") && !var26.contains("Pickaxe")) {
                                                    var13 = false;
                                                }

                                                if (var13) {
                                                    this.facingPos = var22;
                                                }

                                                var13 &= Configs.StonklessStonkAutoClickSecret;
                                                return;
                                            }
                                        }
                                    }

                                } finally {
                                    this.currentBlockMap = var12;
                                    if (var13) {
                                        lastClickTime = TimeUtils.curTime();
                                        this.clickSecret(this.facingPos);
                                        doneSecretsPos.put(this.facingPos, TimeUtils.curTime());
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent var1) {
        if (Checker.enabled) {
            if (Configs.StonklessStonkEnable && should) {
                if (SkyblockUtils.isInDungeon()) {
                    if (!isInPuzzle) {
                        Iterator var2 = this.blockHashMap.entrySet().iterator();

                        while (var2.hasNext()) {
                            Map.Entry var3 = (Map.Entry) var2.next();
                            BlockPos var4 = (BlockPos) var3.getKey();
                            if (!doneSecretsPos.containsKey(var4)) {
                                int var5 = var4.getX();
                                int var6 = var4.getY();
                                int var7 = var4.getZ();
                                GuiUtils.enableESP();
                                if (var4.equals(this.facingPos)) {
                                    GuiUtils.drawBoxAtBlock(var5, var6, var7, 0, 255, 0, 100, 1, 1, 0.0F);
                                } else {
                                    GuiUtils.drawBoxAtBlock(var5, var6, var7, 211, 211, 211, 100, 1, 1, 0.0F);
                                }

                                GuiUtils.disableESP();
                            }
                        }

                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load var1) {
        if (Checker.enabled) {
            this.blockHashMap.clear();
            doneSecretsPos.clear();
            this.facingPos = null;
            this.lastPlayerPos = null;
        }
    }

    @SubscribeEvent(
            priority = EventPriority.HIGH
    )
    public void onRightClick(PlayerInteractEvent var1) throws Exception {
        if (Checker.enabled) {
            if (this.facingPos != null) {
                if (var1.action == Action.RIGHT_CLICK_BLOCK) {
                    if (!this.facingPos.equals(XiaojiaAddons.mc.objectMouseOver.getBlockPos())) {
                        var1.setCanceled(true);
                        this.clickSecret(this.facingPos);
                    }

                    doneSecretsPos.put(this.facingPos, TimeUtils.curTime());
                }
            }
        }
    }

    @SubscribeEvent
    public void getSecretsonTick(TickEndEvent event) {
        if (Checker.enabled) {
            if (Configs.StonklessStonkEnable) {
                if (SkyblockUtils.isInDungeon()) {
                    if (!isInPuzzle) {
                        int var2 = MathUtils.getBlockX(MinecraftUtils.getPlayer());
                        int var3 = MathUtils.getBlockY(MinecraftUtils.getPlayer());
                        int var4 = MathUtils.getBlockZ(MinecraftUtils.getPlayer());
                        BlockPos var5 = new BlockPos(MathUtils.floor((float) var2), MathUtils.floor((float) var3), MathUtils.floor((float) var4));
                        if (!var5.equals(this.lastPlayerPos)) {
                            this.lastPlayerPos = var5;
                            this.blockHashMap.clear();

                            for (int var6 = var2 - 6; var6 <= var2 + 6; ++var6) {
                                for (int var7 = var3 - 6; var7 <= var3 + 6; ++var7) {
                                    for (int var8 = var4 - 6; var8 <= var4 + 6; ++var8) {
                                        BlockPos var9 = new BlockPos(var6, var7, var8);
                                        Block var10 = BlockUtils.getBlockAt(var9);
                                        if (this.isSecret(var10, var9)) {
                                            this.blockHashMap.put(var9, var10);
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlockChange(BlockChangeEvent var1) {
        if (Checker.enabled) {
            if (SkyblockUtils.isInDungeon()) {
                if (MinecraftUtils.getPlayer() != null && MinecraftUtils.getWorld() != null) {
                    if (var1.position.distanceSq(MinecraftUtils.getPlayer().getPosition()) <= 20.0) {
                        if (!doneSecretsPos.containsKey(var1.position)) {
                            if (!this.isSecret(var1.oldBlock.getBlock(), var1.position) && this.isSecret(var1.newBlock.getBlock(), var1.position)) {
                                this.blockHashMap.put(var1.position, var1.newBlock.getBlock());
                            }

                            if (this.isSecret(var1.oldBlock.getBlock(), var1.position) && !this.isSecret(var1.newBlock.getBlock(), var1.position)) {
                                this.blockHashMap.remove(var1.position);
                            }

                        }
                    }
                }
            }
        }
    }

    private void clickSecret(BlockPos var1) {
        if (var1 != null) {
            try {
                Iterator var2 = this.currentBlockMap.keySet().iterator();

                BlockPos var3;
                while (var2.hasNext()) {
                    var3 = (BlockPos) var2.next();
                    if (!this.isSecret(((IBlockState) this.currentBlockMap.get(var3)).getBlock(), var3)) {
                        MinecraftUtils.getWorld().setBlockToAir(var3);
                    }
                }

                XiaojiaAddons.mc.playerController.onPlayerRightClick(MinecraftUtils.getPlayer(), XiaojiaAddons.mc.theWorld, MinecraftUtils.getPlayer().inventory.getCurrentItem(), this.facingPos, XiaojiaAddons.mc.objectMouseOver.sideHit, XiaojiaAddons.mc.objectMouseOver.hitVec);
                var2 = this.currentBlockMap.keySet().iterator();

                while (var2.hasNext()) {
                    var3 = (BlockPos) var2.next();
                    if (!this.isSecret(((IBlockState) this.currentBlockMap.get(var3)).getBlock(), var3)) {
                        MinecraftUtils.getWorld().setBlockState(var3, (IBlockState) this.currentBlockMap.get(var3));
                    }
                }
            } catch (Exception var4) {
            }

        }
    }

    private boolean isSecret(Block var1, BlockPos var2) {
        if (!(var1 instanceof BlockChest) && !(var1 instanceof BlockLever)) {
            if (!(var1 instanceof BlockSkull)) {
                return false;
            } else {
                String var3 = BlockUtils.getTileProperty((BlockSkull) var1, var2);
                return var3.hashCode() == witherEssenceHash || var3.hashCode() == redstoneKeyHash && (Dungeon.currentRoom.equals("Golden Oasis") || Dungeon.currentRoom.equals("Redstone Key"));
            }
        } else {
            return true;
        }
    }
}
