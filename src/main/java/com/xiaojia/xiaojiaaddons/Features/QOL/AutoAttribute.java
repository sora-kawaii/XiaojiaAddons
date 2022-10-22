package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class AutoAttribute extends AutoFuse {

    private static final ArrayList array2 = new ArrayList();

    private static final ArrayList array3 = new ArrayList();

    private static final ArrayList array1 = new ArrayList();

    private static Pair getLevels(ItemStack var0) {
        NBTTagCompound var1 = NBTUtils.getCompoundFromExtraAttributes(var0, "attributes");
        if (var1 == null) {
            return null;
        } else {
            String var2 = Configs.FuseAttribute1.toLowerCase().replaceAll(" ", "_");
            String var3 = Configs.FuseAttribute2.toLowerCase().replaceAll(" ", "_");
            if (!var1.hasKey(var2) && !var1.hasKey(var3)) {
                return null;
            } else {
                int var4 = 0;
                int var5 = 0;
                if (var1.hasKey(var2)) {
                    var4 = var1.getInteger(var2);
                }

                if (var1.hasKey(var3)) {
                    var5 = var1.getInteger(var3);
                }

                return new Pair(var4, var5);
            }
        }
    }

    public void clear() {
        array1.clear();
        array2.clear();
        array3.clear();
    }

    public boolean enabled() {
        return Configs.FuseAttribute;
    }

    public void add(ItemStack var1, int var2) {
        Pair var3 = getLevels(var1);
        if (var3 != null) {
            int var4 = (Integer) var3.getKey();
            int var5 = (Integer) var3.getValue();
            if (var4 != 0 && var5 != 0) {
                array3.add(new AttributeTypePos(var4, var5, var2));
            } else if (var4 != 0) {
                array1.add(new AttributeTypePos(var4, var5, var2));
            } else if (var5 != 0) {
                array2.add(new AttributeTypePos(var4, var5, var2));
            }

        }
    }

    public Pair getNext(ArrayList var1) {
        Pair var2 = this.getNext(array3, var1);
        if (var2 == null) {
            var2 = this.getNext(array2, var1);
        }

        if (var2 == null) {
            var2 = this.getNext(array1, var1);
        }

        return var2;
    }

    public boolean canFuse() {
        List var1 = getItemLore(ControlUtils.getOpenedInventory().getItemInSlot(22));
        return var1.size() > 5 && ChatLib.removeFormatting((String) var1.get(4)).equals("Click to transfer!");
    }

    public String inventoryName() {
        return "Attribute Fusion";
    }

    private Pair getNext(ArrayList var1, ArrayList var2) {
        for (int var3 = 0; var3 < var1.size(); ++var3) {
            if (!(Boolean) var2.get(((AttributeTypePos) var1.get(var3)).slot)) {
                for (int var4 = var3 + 1; var4 < var1.size(); ++var4) {
                    if (!(Boolean) var2.get(((AttributeTypePos) var1.get(var4)).slot) && ((AttributeTypePos) var1.get(var3)).level1 == ((AttributeTypePos) var1.get(var4)).level1 && ((AttributeTypePos) var1.get(var3)).level2 == ((AttributeTypePos) var1.get(var4)).level2) {
                        return new Pair(((AttributeTypePos) var1.get(var3)).slot, ((AttributeTypePos) var1.get(var4)).slot);
                    }
                }
            }
        }

        return null;
    }

    public boolean checkFuse() {
        Inventory var1 = ControlUtils.getOpenedInventory();
        if (var1 == null) {
            return false;
        } else {
            Pair var2 = getLevels(var1.getItemInSlot(29));
            Pair var3 = getLevels(var1.getItemInSlot(33));
            return var2 != null && var3 != null && var2.getKey().equals(var3.getKey()) && var2.getValue().equals(var3.getValue());
        }
    }
}
