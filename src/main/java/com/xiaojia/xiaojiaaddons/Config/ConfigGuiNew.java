package com.xiaojia.xiaojiaaddons.Config;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.Config.ButtonsNew.Button;
import com.xiaojia.xiaojiaaddons.Config.ButtonsNew.ClearTextButton;
import com.xiaojia.xiaojiaaddons.Config.ButtonsNew.CloseButton;
import com.xiaojia.xiaojiaaddons.Config.ButtonsNew.TextInput;
import com.xiaojia.xiaojiaaddons.Config.Setting.BooleanSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.FolderSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.Setting;
import com.xiaojia.xiaojiaaddons.Config.Setting.TextSetting;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class ConfigGuiNew extends GuiScreen {

   private static final int guiHeight = 260;

   private final ConcurrentLinkedDeque textInputs = new ConcurrentLinkedDeque();

   private static Setting selectedFirstCategory = null;

   private static final int titleHeight = 30;

   public static String searchText = "";

   private final int gapBetween = 3;

   private static final int firstCategoryHeight = 23;

   private static final int secondCategoryWidth = 150;

   private final int thirdGap = 10;

   private final int iconHeight = 14;

   private final int insideGap = 3;

   private final int closeIconSize = 12;

   private final int secondGap = 4;

   private int lastHeight;

   private static ArrayList settings;

   private static Setting selectedSecondCategory = null;

   private static ArrayList secondCategory;

   private static ArrayList firstCategory;

   private final int lineHeight = 20;

   private final Color descriptionColor = new Color(170, 170, 170, 240);

   private int secondScroll = 0;

   private static GuiTextField searchBar = null;

   private int maxSecondScroll = 0;

   private final int searchHeight = 14;

   private static final int guiWidth = 500;

   private final int thirdGapBetween = 5;

   private int thirdScroll = 0;

   private int maxThirdScroll = 0;

   private int lastWidth;

   private final int startScale;

   private final int searchWidth = 120;

   private void drawSettings() {
      int var4;
      int var5;
      if (selectedSecondCategory != null) {
         int var1 = this.getStartX() + 325;
         int var2 = this.getStartY() + 30 + 23 + 10 + this.thirdScroll;
         String[] var3 = selectedSecondCategory.description.split("\n");
         var4 = var3.length;

         for(var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (this.isInThirdCategory(var2, var2 + 9)) {
               this.drawCenteredString(this.fontRendererObj, ChatLib.addColor(var6), var1, var2, this.descriptionColor.getRGB());
            }

            var2 += 10;
         }
      }

      Iterator var10 = settings.iterator();

      while(var10.hasNext()) {
         Setting var11 = (Setting)var10.next();
         if (!(var11 instanceof FolderSetting) && (this.isInThirdCategory(var11.y) || this.isInThirdCategory(var11.y + var11.height))) {
            this.drawGradientRect(var11.x, Math.max(var11.y, this.getStartY() + 30 + 23 + 5), var11.x + var11.width, Math.min(var11.y + var11.height, this.getStartY() + 260), (new Color(25, 25, 25, 95)).getRGB(), (new Color(25, 25, 25, 85)).getRGB());
         }

         String var12 = var11.name;
         if (var11 instanceof BooleanSetting && (Boolean)var11.get(Boolean.class)) {
            var12 = "§a" + var12;
         }

         if (this.isInThirdCategory(var11.y + 4, var11.y + 4 + 10)) {
            if (var11 instanceof FolderSetting) {
               this.drawCenteredString(this.fontRendererObj, var12, this.getStartX() + 325, var11.y + 4, (new Color(255, 255, 255, 255)).getRGB());
            } else {
               this.drawString(this.fontRendererObj, var12, var11.x + 5, var11.y + 4, (new Color(255, 255, 255, 255)).getRGB());
            }
         }

         var4 = var11.x + 4;
         var5 = var11.y + 14 + 4;
         String[] var13 = var11.description.split("\n");
         int var7 = var13.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String var9 = var13[var8];
            if (this.isInThirdCategory(var5, var5 + 10)) {
               if (var11 instanceof FolderSetting) {
                  this.drawCenteredString(this.fontRendererObj, ChatLib.addColor(var9), this.getStartX() + 325, var5, this.descriptionColor.getRGB());
               } else {
                  this.drawString(this.fontRendererObj, ChatLib.addColor(var9), var4, var5, this.descriptionColor.getRGB());
               }
            }

            var5 += 10;
         }
      }

   }

   private static void getSettings() {
      ArrayList var0 = new ArrayList();
      ArrayList var1 = new ArrayList();
      Iterator var2 = XiaojiaAddons.settings.iterator();

      while(var2.hasNext()) {
         Setting var3 = (Setting)var2.next();
         var3.x = var3.y = var3.width = var3.height = 0;
         if (isFirstCategory(var3)) {
            var0.add(var3);
            var3.set(false);
         } else if (isSecondCategory(var3)) {
            var1.add(var3);
            if (var3 instanceof FolderSetting) {
               var3.set(false);
            }
         }
      }

      if (selectedFirstCategory != null) {
         selectedFirstCategory.set(true);
      }

      if (selectedSecondCategory != null) {
         selectedSecondCategory.set(true);
      }

      settings = selectedSecondCategory == null ? new ArrayList() : selectedSecondCategory.getSons();
      if (searchBar != null && !searchBar.getText().equals("")) {
         String var6 = searchBar.getText().toLowerCase();
         settings = new ArrayList();
         HashSet var7 = new HashSet();
         Iterator var4 = XiaojiaAddons.settings.iterator();

         label56:
         while(true) {
            Setting var5;
            do {
               do {
                  if (!var4.hasNext()) {
                     break label56;
                  }

                  var5 = (Setting)var4.next();
               } while(isFirstCategory(var5));
            } while(!var5.name.toLowerCase().contains(var6) && !var5.description.toLowerCase().contains(var6) && !var7.contains(var5.parent.name));

            settings.add(var5);
            var7.add(var5.name);
         }
      }

      firstCategory = var0;
      secondCategory = var1;
   }

   private int getStartX() {
      return (this.width - 500) / 2;
   }

   private void drawButtons(Minecraft var1, int var2, int var3) {
      Iterator var4 = this.buttonList.iterator();

      while(var4.hasNext()) {
         GuiButton var5 = (GuiButton)var4.next();
         if (var5 instanceof Button) {
            ((Button)var5).draw(var1, var2, var3);
         }
      }

   }

   public void initGui() {
      this.textInputs.clear();
      this.buttonList.clear();
      byte var1 = 9;
      this.buttonList.add(new CloseButton(this, 0, this.getStartX() + 500 - var1 - 12, this.getStartY() + var1, 12, 12, ""));
      var1 = 4;
      int var2 = this.getStartX() + var1;
      int var3 = this.getStartY() + 30 + var1;
      int var4 = (14 - this.fontRendererObj.FONT_HEIGHT) / 2;

      int var7;
      for(Iterator var5 = firstCategory.iterator(); var5.hasNext(); var2 += var7 + 2 * var4 + 3) {
         Setting var6 = (Setting)var5.next();
         var7 = this.mc.fontRendererObj.getStringWidth(var6.name);
         var6.x = var2;
         var6.y = var3;
         var6.width = var7 + 2 * var4;
         var6.height = 14;
         this.buttonList.add(Button.buttonFromSetting(this, var6, var6.x, var6.y));
      }

      int var11 = this.getStartY() + 30 + 23;
      int var12 = this.getStartY() + 260;
      var2 = this.getStartX();
      var3 = this.getStartY() + 30 + 23 + this.secondScroll;
      this.maxSecondScroll = 2;

      Setting var8;
      Iterator var13;
      for(var13 = secondCategory.iterator(); var13.hasNext(); this.maxSecondScroll += 20) {
         var8 = (Setting)var13.next();
         var8.x = var2;
         var8.y = var3;
         var8.width = 150;
         var8.height = 20;
         if (var3 >= var11 && var3 + 20 < var12) {
            this.buttonList.add(Button.buttonFromSetting(this, var8, var8.x, var8.y));
         }

         var3 += 20;
      }

      var2 = this.getStartX() + 150 + 10;
      var3 = this.getStartY() + 30 + 23 + 10 + this.thirdScroll;
      this.maxThirdScroll = 10;
      if (selectedSecondCategory != null && !selectedSecondCategory.description.equals("")) {
         var7 = this.getLines(selectedSecondCategory.description) * 10 + 5;
         var3 += var7;
         this.maxThirdScroll += var7;
      }

      for(var13 = settings.iterator(); var13.hasNext(); this.maxThirdScroll += var8.height + 5) {
         var8 = (Setting)var13.next();
         var8.x = var2;
         var8.y = var3;
         var8.width = 330;
         int var9 = this.getLines(var8.description);
         var8.height = 18 + 10 * var9;
         if (var3 >= var11 && var3 + 14 < var12) {
            if (var8 instanceof TextSetting) {
               TextInput var10 = new TextInput(this, (TextSetting)var8, var8.x, var8.y);
               this.textInputs.add(var10);
               this.buttonList.add(new ClearTextButton(this, (TextSetting)var8, var8.x, var8.y));
            } else {
               this.buttonList.add(Button.buttonFromSetting(this, var8, var8.x, var8.y));
            }
         }

         var3 += var8.height + 5;
      }

      if (searchBar == null || this.width != this.lastWidth || this.height != this.lastHeight) {
         searchBar = new GuiTextField(1, this.fontRendererObj, this.getStartX() + 500 - 9 - 120 + 14 + 1, this.getStartY() + 30 + 4 + 2, 106, 13);
         searchBar.setMaxStringLength(20);
         searchBar.setEnableBackgroundDrawing(false);
         searchBar.setText(searchText);
         searchBar.setFocused(true);
         this.lastWidth = this.width;
         this.lastHeight = this.height;
      }

   }

   private boolean isInThirdCategory(int var1) {
      int var2 = this.getStartY() + 30 + 23 + 5;
      int var3 = this.getStartY() + 260;
      return var1 >= var2 && var3 >= var1;
   }

   public ConfigGuiNew(int var1) {
      this.lastWidth = this.width;
      this.lastHeight = this.height;
      getSettings();
      this.startScale = var1;
   }

   protected void keyTyped(char var1, int var2) throws IOException {
      super.keyTyped(var1, var2);
      if (searchBar.isFocused()) {
         selectedSecondCategory = null;
         selectedFirstCategory = null;
         this.thirdScroll = this.secondScroll = 0;
         searchBar.textboxKeyTyped(var1, var2);
         searchText = searchBar.getText();
         getSettings();
         this.initGui();
      }

      Iterator var3 = this.textInputs.iterator();

      while(var3.hasNext()) {
         TextInput var4 = (TextInput)var3.next();
         if (var4.isFocused()) {
            var4.keyTyped(var1, var2);
         }
      }

   }

   protected void mouseClicked(int var1, int var2, int var3) throws IOException {
      super.mouseClicked(var1, var2, var3);
      searchBar.mouseClicked(var1, var2, var3);
      searchText = searchBar.getText();
      Iterator var4 = this.textInputs.iterator();

      while(var4.hasNext()) {
         TextInput var5 = (TextInput)var4.next();
         var5.mouseClicked(var1, var2, var3);
      }

   }

   private static boolean isFirstCategory(Setting var0) {
      return var0.parent == null;
   }

   public void update(Setting var1, boolean var2) {
      if (isFirstCategory(var1)) {
         this.secondScroll = this.thirdScroll = 0;
         searchBar.setText("");
         if (var2) {
            selectedFirstCategory = var1;
            selectedSecondCategory = null;
         } else {
            selectedSecondCategory = null;
            selectedFirstCategory = null;
         }
      } else if (isSecondCategory(var1)) {
         this.thirdScroll = 0;
         searchBar.setText("");
         if (var2) {
            selectedSecondCategory = var1;
         } else {
            selectedSecondCategory = null;
         }
      }

      getSettings();
      this.initGui();
   }

   private boolean isInSecondCategory(int var1, int var2) {
      int var3 = this.getStartY() + 30 + 23;
      int var4 = this.getStartY() + 260;
      return var1 >= var3 && var2 >= var1 && var4 >= var2;
   }

   private void drawSecondCategory() {
      Iterator var1 = secondCategory.iterator();

      while(var1.hasNext()) {
         Setting var2 = (Setting)var1.next();
         if (var2 == selectedSecondCategory && (this.isInSecondCategory(var2.y + 1) || this.isInSecondCategory(var2.y + var2.height))) {
            this.drawGradientRect(var2.x, Math.max(var2.y + 1, this.getStartY() + 30 + 23), var2.x + var2.width, Math.min(var2.y + var2.height, this.getStartY() + 260), (new Color(53, 255, 53, 104)).getRGB(), (new Color(53, 255, 53, 80)).getRGB());
         }

         if (this.isInSecondCategory(var2.y + 5 + 1, var2.y + 5 + 1 + 9)) {
            this.drawString(this.mc.fontRendererObj, var2.name, var2.x + 5 + 1, var2.y + 5 + 1, -1);
         }

         if (this.isInSecondCategory(var2.y + var2.height, var2.y + var2.height + 1)) {
            drawRect(var2.x, var2.y + var2.height, var2.x + var2.width, var2.y + var2.height + 1, -1);
         }
      }

   }

   private int getLines(String var1) {
      if (var1 != null && !var1.equals("")) {
         int var2 = 1;
         char[] var3 = var1.toCharArray();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            char var6 = var3[var5];
            if (var6 == '\n') {
               ++var2;
            }
         }

         return var2;
      } else {
         return 0;
      }
   }

   public void handleMouseInput() throws IOException {
      int var1 = Mouse.getEventX() * this.width / this.mc.displayWidth;
      int var2 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
      if (Mouse.getEventDWheel() != 0) {
         int var3 = Integer.signum(Mouse.getEventDWheel()) * 10;
         if (var2 >= this.getStartY() + 30 + 23 && var2 <= this.getStartY() + 260) {
            if (var1 >= this.getStartX() && var1 <= this.getStartX() + 150) {
               this.secondScroll = MathHelper.clamp_int(this.secondScroll + var3, Math.min(0, 207 - this.maxSecondScroll), 0);
            } else if (var1 >= this.getStartX() + 150 && var1 <= this.getStartX() + 500) {
               this.thirdScroll = MathHelper.clamp_int(this.thirdScroll + var3, Math.min(0, 207 - this.maxThirdScroll), 0);
            }
         }

         this.initGui();
      }

      super.handleMouseInput();
   }

   private void drawSearchBar(int var1, int var2, int var3, int var4, int var5) {
      int var6 = (var4 - var2 - var5) / 2;
      this.drawGradientRect(var1, var2, var3, var4, (new Color(255, 255, 255, 165)).getRGB(), (new Color(255, 255, 255, 140)).getRGB());
      GuiUtils.drawTexture(new ResourceLocation("xiaojiaaddons:search.png"), var1 + var6, var2 + var6, var5, var5);
      drawRect(var1 + var6 * 2 + var5, var2 + var6 + var5, var3 - var6, var2 + var6 + var5 + 1, (new Color(255, 255, 255, 255)).getRGB());
      searchBar.drawTextBox();
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      super.drawScreen(var1, var2, var3);
      int var4 = this.getStartX();
      int var5 = this.getStartY();
      this.drawGradientRect(var4, var5, var4 + 500, var5 + 260, (new Color(25, 25, 25, 65)).getRGB(), (new Color(25, 25, 25, 100)).getRGB());
      this.drawGradientRect(var4, var5, var4 + 500, var5 + 30, (new Color(15, 15, 15, 180)).getRGB(), (new Color(15, 15, 15, 170)).getRGB());
      drawRect(var4, var5 + 30, var4 + 500, var5 + 30 + 1, Colors.TRANSPARENT.getRGB());
      this.drawGradientRect(var4, var5 + 30 + 1, var4 + 500, var5 + 30 + 23, (new Color(15, 15, 15, 120)).getRGB(), (new Color(15, 15, 15, 110)).getRGB());
      drawRect(var4, var5 + 30 + 23, var4 + 500, var5 + 30 + 23 + 1, Colors.TRANSPARENT.getRGB());
      this.drawGradientRect(var4, var5 + 30 + 23 + 1, var4 + 150, var5 + 260, (new Color(15, 15, 15, 110)).getRGB(), (new Color(15, 15, 15, 100)).getRGB());
      drawRect(var4 + 150, var5 + 30 + 23 + 1, var4 + 150 + 1, var5 + 260, Colors.TRANSPARENT.getRGB());
      byte var6 = 9;
      String var7 = "XiaojiaAddons 2.4.8.3 [CRACK/DEOBF BY ソラ github.com/sora-kawaii]";
      this.drawCenteredString(this.mc.fontRendererObj, var7, var4 + this.mc.fontRendererObj.getStringWidth(var7) / 2 + var6, var5 + 15 - 3, -1);
      GuiUtils.drawTexture(new ResourceLocation("xiaojiaaddons:deny-16x16.png"), var4 + 500 - var6 - 12, var5 + var6, 12, 12);
      this.drawSearchBar(var4 + 500 - var6 - 120, var5 + 30 + 4, var4 + 500 - var6, var5 + 30 + 23 - 4, 9);
      this.drawFirstLine();
      this.drawSecondCategory();
      this.drawSettings();
      this.drawButtons(this.mc, var1, var2);
      Iterator var8 = this.textInputs.iterator();

      while(var8.hasNext()) {
         TextInput var9 = (TextInput)var8.next();
         var9.draw();
      }

   }

   public void updateScreen() {
      super.updateScreen();
      searchBar.updateCursorCounter();
   }

   private int getStartY() {
      return (this.height - 260) / 2;
   }

   private static boolean isSecondCategory(Setting var0) {
      return var0.parent != null && var0.parent == selectedFirstCategory && var0.parent.parent == null;
   }

   private void drawFirstLine() {
      Iterator var1 = firstCategory.iterator();

      while(var1.hasNext()) {
         Setting var2 = (Setting)var1.next();
         Color var3 = new Color(85, 85, 85, 168);
         Color var4 = new Color(85, 85, 85, 160);
         if (var2 == selectedFirstCategory) {
            var3 = new Color(53, 53, 255, 104);
            var4 = new Color(53, 53, 255, 80);
         }

         this.drawGradientRect(var2.x, var2.y, var2.x + var2.width, var2.y + var2.height, var3.getRGB(), var4.getRGB());
         this.drawString(this.mc.fontRendererObj, var2.name, var2.x + 3, var2.y + 3, -1);
      }

   }

   private boolean isInSecondCategory(int var1) {
      int var2 = this.getStartY() + 30 + 23;
      int var3 = this.getStartY() + 260;
      return var1 >= var2 && var3 >= var1;
   }

   public void onGuiClosed() {
      XiaojiaAddons.mc.gameSettings.guiScale = this.startScale;
      Config.save();
   }

   private boolean isInThirdCategory(int var1, int var2) {
      int var3 = this.getStartY() + 30 + 23 + 5;
      int var4 = this.getStartY() + 260;
      return var1 >= var3 && var2 >= var1 && var4 >= var2;
   }
}
