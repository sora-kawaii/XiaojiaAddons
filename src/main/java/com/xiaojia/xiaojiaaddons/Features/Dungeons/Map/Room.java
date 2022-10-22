package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ColorUtils;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;
import net.minecraft.block.Block;

import java.awt.*;
import java.util.ArrayList;

public class Room {

    public int[] size;

    public int x;

    public int secrets;

    public int z;

    public String checkmark;

    public ArrayList cores;

    public int core;

    public boolean explored;

    public String type;

    public String name;

    public Room(int var1, int var2, Data var3) {
        this.x = var1;
        this.z = var2;
        this.secrets = var3.secrets;
        this.name = var3.name;
        this.type = var3.type;
        this.cores = var3.cores;
        this.core = this.getCore();
        this.size = new int[]{3, 3};
        this.checkmark = "";
        this.explored = true;
    }

    public void renderSecrets() {
        RenderUtils.retainTransforms(true);
        RenderUtils.translate((float) Configs.MapX, (float) Configs.MapY, 0.0F);
        if (Configs.ShowSecrets == 0 || Configs.ShowSecrets == 1) {
            RenderUtils.scale(0.1F * (float) Configs.MapScale, 0.1F * (float) Configs.MapScale);
            RenderUtils.drawStringWithShadow("&7" + this.secrets, (float) (this.x + 200) * 1.25F - (float) Configs.MapScale * 1.25F, (float) (this.z + 200) * 1.25F - (float) Configs.MapScale * 1.25F);
        }

        RenderUtils.retainTransforms(false);
    }

    private int getCore() {
        StringBuilder var1 = new StringBuilder();

        for (int var2 = 140; var2 > 11; --var2) {
            int var3 = Block.getIdFromBlock(BlockUtils.getBlockAt(this.x, var2, this.z));
            if (var3 != 5 && var3 != 54) {
                var1.append(var3);
            }
        }

        return var1.toString().hashCode();
    }

    public void renderName() {
        String[] var1 = this.name.split(" ");
        RenderUtils.retainTransforms(true);
        RenderUtils.translate((float) Configs.MapX, (float) Configs.MapY, 0.0F);
        RenderUtils.scale(0.1F * (float) Configs.MapScale, 0.1F * (float) Configs.MapScale);

        for (int var2 = 0; var2 < var1.length; ++var2) {
            RenderUtils.drawStringWithShadow(ColorUtils.colors[Configs.RoomNameColor] + var1[var2], (float) (this.x + 200) * 1.25F + (float) Configs.MapScale - (float) RenderUtils.getStringWidth(var1[var2]) / 2.0F, (float) (this.z + 200) * 1.25F - (float) (Math.abs(var1.length - 1) * 3) + (float) (var2 * 8));
        }

        RenderUtils.retainTransforms(false);
    }

    public Color getColor() {
        switch (this.type) {
            case "puzzle":
                return new Color(117, 0, 133, 255);
            case "blood":
                return new Color(255, 0, 0, 255);
            case "trap":
                return new Color(216, 127, 51, 255);
            case "yellow":
                return new Color(254, 223, 0, 255);
            case "fairy":
                return new Color(224, 0, 255, 255);
            case "entrance":
                return new Color(20, 133, 0, 255);
            case "rare":
            default:
                return new Color(107, 58, 17, 255);
        }
    }

    public Data getJson() {
        if (this.cores.size() == 0) {
            this.cores = new ArrayList();
            this.cores.add(this.core);
        }

        return new Data(this.name, this.type, this.secrets, this.cores);
    }
}
