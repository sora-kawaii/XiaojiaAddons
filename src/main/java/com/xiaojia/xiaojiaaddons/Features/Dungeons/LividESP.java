package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

public class LividESP extends RenderEntityESP {

    public BlockPos pos = new BlockPos(5, 109, 43);

    private EnumChatFormatting colorChat = null;

    public boolean shouldRenderESP(EntityInfo var1) {
        return true;
    }

    @SubscribeEvent
    public void onTickCheck(TickEndEvent var1) {
        this.colorChat = null;
        if (true) {
            if (Configs.ShowCorrectLivid) {
                if (Dungeon.bossEntry > Dungeon.runStarted) {
                    if (Dungeon.floorInt == 5) {
                        IBlockState var2 = BlockUtils.getBlockStateAt(this.pos);
                        if (var2 != null) {
                            if (var2.getBlock() == Blocks.stained_glass) {
                                EnumDyeColor var3 = var2.getValue(BlockStainedGlass.COLOR);
                                if (var3 == EnumDyeColor.WHITE) {
                                    this.colorChat = EnumChatFormatting.WHITE;
                                } else if (var3 == EnumDyeColor.MAGENTA) {
                                    this.colorChat = EnumChatFormatting.LIGHT_PURPLE;
                                } else if (var3 == EnumDyeColor.RED) {
                                    this.colorChat = EnumChatFormatting.RED;
                                } else if (var3 == EnumDyeColor.SILVER) {
                                    this.colorChat = EnumChatFormatting.GRAY;
                                } else if (var3 == EnumDyeColor.GRAY) {
                                    this.colorChat = EnumChatFormatting.GRAY;
                                } else if (var3 == EnumDyeColor.GREEN) {
                                    this.colorChat = EnumChatFormatting.DARK_GREEN;
                                } else if (var3 == EnumDyeColor.LIME) {
                                    this.colorChat = EnumChatFormatting.GREEN;
                                } else if (var3 == EnumDyeColor.BLUE) {
                                    this.colorChat = EnumChatFormatting.BLUE;
                                } else if (var3 == EnumDyeColor.PURPLE) {
                                    this.colorChat = EnumChatFormatting.DARK_PURPLE;
                                } else if (var3 == EnumDyeColor.YELLOW) {
                                    this.colorChat = EnumChatFormatting.YELLOW;
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    public boolean shouldDrawString(EntityInfo var1) {
        return false;
    }

    public boolean enabled() {
        return Dungeon.bossEntry > Dungeon.runStarted && Dungeon.floorInt == 5;
    }

    public EntityInfo getEntityInfo(Entity var1) {
        if (var1 instanceof EntityArmorStand && this.colorChat != null && var1.getName().contains("Livid") && var1.getName().contains(this.colorChat.toString() + "§lLivid")) {
            HashMap var2 = new HashMap();
            var2.put("entity", var1);
            var2.put("yOffset", 1.0F);
            var2.put("drawString", EntityInfo.EnumDraw.DONT_DRAW_STRING);
            var2.put("width", 0.5F);
            var2.put("height", 2.0F);
            var2.put("fontColor", 3407667);
            var2.put("isFilled", Configs.ShowCorrectLividWithFilledBox);
            var2.put("isESP", Configs.ShowCorrectLividWithESP);
            var2.put("kind", "Livid");
            return new EntityInfo(var2);
        } else {
            return null;
        }
    }
}
