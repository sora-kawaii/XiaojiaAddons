package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AutoCombine extends AutoFuse {

    private static final ArrayList books = new ArrayList();

    private static final HashMap booksLevel = new HashMap() {
        {
            this.put("Feather Falling", 10);
            this.put("Infinite Quiver", 10);
            this.put("Rejuvenate", 5);
            this.put("Ultimate Wise", 5);
            this.put("Wisdom", 5);
            this.put("Last Stand", 5);
            this.put("Combo", 5);
            this.put("Legion", 5);
            this.put("Overload", 5);
            this.put("Soul Eater", 5);
            this.put("Ultimate Jerry", 5);
            this.put("Bank", 5);
            this.put("No Pain No Gain", 5);
            this.put("Mana Steal", 3);
            this.put("Smarty Pants", 5);
            this.put("Swarm", 5);
            this.put("Strong Mana", 10);
            this.put("Hardened Mana", 10);
            this.put("Ferocious Mana", 10);
            this.put("Mana Vampire", 10);
            this.put("Charm", 5);
            this.put("Corruption", 5);
        }
    };

    public String inventoryName() {
        return "Anvil";
    }

    private boolean checkKindEnable(String var1) {
        boolean var2 = false;
        var1 = "Combine" + var1.replace(" ", "");

        try {
            var2 = Configs.class.getField(var1).getBoolean(Configs.class);
        } catch (Exception var4) {
            ChatLib.chat("e");
            var4.printStackTrace();
        }

        return var2;
    }

    public void clear() {
        books.clear();
    }

    public void add(ItemStack var1, int var2) {
        if (var1 != null && var1.hasDisplayName()) {
            String var3 = ChatLib.removeFormatting(var1.getDisplayName()).toLowerCase();
            if (var3.equals("enchanted book")) {
                ArrayList var4 = NBTUtils.getBookNameAndLevel(var1);
                if (var4.size() == 2) {
                    String var5 = (String) var4.get(0);
                    String var6 = (String) var4.get(1);
                    int var7 = Integer.parseInt(var6);
                    if (booksLevel.containsKey(var5) && var7 < (Integer) booksLevel.get(var5) && this.checkKindEnable(var5)) {
                        books.add(new BookTypePos(var5, var7, var2));
                    }

                }
            }
        }
    }

    public boolean canFuse() {
        List var1 = getItemLore(ControlUtils.getOpenedInventory().getItemInSlot(22));
        return var1.size() > 5 && ChatLib.removeFormatting((String) var1.get(5)).equals("0 Exp Levels");
    }

    public boolean checkFuse() {
        Inventory var1 = ControlUtils.getOpenedInventory();
        return var1 != null && getItemLore(var1.getItemInSlot(29)).get(1).equals(getItemLore(var1.getItemInSlot(33)).get(1));
    }

    public Pair getNext(ArrayList var1) {
        for (int var2 = 0; var2 < books.size(); ++var2) {
            if (!(Boolean) var1.get(((BookTypePos) books.get(var2)).slot)) {
                for (int var3 = var2 + 1; var3 < books.size(); ++var3) {
                    if (!(Boolean) var1.get(((BookTypePos) books.get(var3)).slot) && ((BookTypePos) books.get(var2)).type.equals(((BookTypePos) books.get(var3)).type) && ((BookTypePos) books.get(var2)).level == ((BookTypePos) books.get(var3)).level) {
                        return new Pair(((BookTypePos) books.get(var2)).slot, ((BookTypePos) books.get(var3)).slot);
                    }
                }
            }
        }

        return null;
    }
}
