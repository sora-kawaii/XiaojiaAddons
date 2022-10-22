package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RelicESP {

   public static String fileName = "config/XiaojiaAddonsRelicFound.cfg";

   private static final HashSet got = new HashSet();

   private static final ArrayList locations = new ArrayList(Arrays.asList(new BlockPos(-236, 51, -239), new BlockPos(-254, 57, -279), new BlockPos(-272, 48, -291), new BlockPos(-217, 58, -304), new BlockPos(-206, 63, -301), new BlockPos(-274, 100, -178), new BlockPos(-284, 49, -234), new BlockPos(-275, 64, -272), new BlockPos(-225, 70, -316), new BlockPos(-300, 50, -218), new BlockPos(-300, 51, -254), new BlockPos(-296, 37, -270), new BlockPos(-303, 71, -318), new BlockPos(-313, 58, -250), new BlockPos(-311, 69, -251), new BlockPos(-317, 69, -273), new BlockPos(-328, 50, -238), new BlockPos(-348, 65, -202), new BlockPos(-342, 89, -221), new BlockPos(-342, 122, -253), new BlockPos(-355, 86, -213), new BlockPos(-354, 73, -285), new BlockPos(-384, 89, -225), new BlockPos(-372, 89, -242), new BlockPos(-183, 51, -252), new BlockPos(-178, 136, -297), new BlockPos(-188, 80, -346), new BlockPos(-147, 83, -335)));

   @SubscribeEvent
   public void onRelic(PlayerInteractEvent var1) {
      if (Checker.enabled) {
         if (Configs.RelicESP) {
            if (SkyblockUtils.isInSpiderDen()) {
               if (var1.action == Action.RIGHT_CLICK_BLOCK) {
                  if (locations.contains(var1.pos)) {
                     got.add(locations.indexOf(var1.pos));
                     save();
                  }

               }
            }
         }
      }
   }

   @SubscribeEvent
   public void onRender(RenderWorldLastEvent var1) {
      if (Checker.enabled) {
         if (Configs.RelicESP) {
            if (SkyblockUtils.isInSpiderDen()) {
               for(int var2 = 0; var2 < locations.size(); ++var2) {
                  if (!got.contains(var2)) {
                     GuiUtils.enableESP();
                     GuiUtils.drawBoxAtBlock((BlockPos)locations.get(var2), new Color(0, 255, 255), 1, 1, 0.002F);
                     GuiUtils.disableESP();
                  }
               }

            }
         }
      }
   }

   public static void save() {
      try {
         String var0 = (new Gson()).toJson(got);
         Files.write(Paths.get(fileName), var0.getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
      } catch (Exception var1) {
         System.out.println("Error while saving item rename config file");
         var1.printStackTrace();
      }

   }

   public static void load() {
      try {
         File var0 = new File(fileName);
         if (var0.exists()) {
            BufferedReader var1 = Files.newBufferedReader(Paths.get(fileName));
            Type var2 = (new TypeToken() {
            }).getType();
            HashSet var3 = (HashSet)(new Gson()).fromJson(var1, var2);
            got.addAll(var3);
         }
      } catch (Exception var4) {
         System.out.println("Error while loading item rename config file");
         var4.printStackTrace();
      }

   }
}
