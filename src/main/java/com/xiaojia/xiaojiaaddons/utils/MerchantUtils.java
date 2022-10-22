package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import net.minecraft.entity.IMerchant;
import net.minecraft.inventory.ContainerMerchant;

import java.lang.reflect.Field;

public class MerchantUtils {

    private static Field merchantField = null;

    static {
        try {
            merchantField = ContainerMerchant.class.getDeclaredField("field_75178_e");
        } catch (NoSuchFieldException var3) {
            try {
                merchantField = ContainerMerchant.class.getDeclaredField("theMerchant");
            } catch (NoSuchFieldException var2) {
                var2.printStackTrace();
            }
        }

        merchantField.setAccessible(true);
    }

    public static String getCurrentMerchant() {
        if (!Checker.enabled) {
            return null;
        } else {
            Inventory var0 = ControlUtils.getOpenedInventory();
            if (var0 == null) {
                return null;
            } else if (!(var0.container instanceof ContainerMerchant)) {
                return null;
            } else {
                ContainerMerchant var1 = (ContainerMerchant) var0.container;
                String var2 = "";

                try {
                    var2 = ((IMerchant) merchantField.get(var1)).getDisplayName().getFormattedText();
                } catch (IllegalAccessException var4) {
                }

                return var2;
            }
        }
    }
}
