package com.xiaojia.xiaojiaaddons.Features.Nether;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.awt.*;

public class SpongeESP extends BlockESP {
    public boolean isEnabled() {
        return Configs.SpongeESP;
    }

    public Color getColor() {
        return new Color(255, 242, 82, 120);
    }

    public Block getBlock() {
        return Blocks.sponge;
    }
}
