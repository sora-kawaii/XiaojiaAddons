package com.xiaojia.xiaojiaaddons.Objects;

import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Iterator;

public class TestCubeGUI {

    public static ArrayList lines = new ArrayList();

    public static ArrayList cubes = new ArrayList();

    @SubscribeEvent
    public void clear(WorldEvent.Load var1) {
        cubes.clear();
        lines.clear();
    }

    @SubscribeEvent
    public void test(RenderWorldLastEvent var1) {
        Iterator var2 = cubes.iterator();

        while (var2.hasNext()) {
            Cube var3 = (Cube) var2.next();
            GuiUtils.drawBoundingBoxAtPos((float) var3.x, (float) (var3.y - var3.h), (float) var3.z, var3.color, (float) var3.w, (float) var3.h * 2.0F);
        }

        var2 = lines.iterator();

        while (var2.hasNext()) {
            Line var4 = (Line) var2.next();
            GuiUtils.drawLine((float) var4.from.x, (float) var4.from.y, (float) var4.from.z, (float) var4.to.x, (float) var4.to.y, (float) var4.to.z, var4.color, 2);
        }

    }
}
