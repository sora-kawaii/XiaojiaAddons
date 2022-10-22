package com.xiaojia.xiaojiaaddons.utils;

import java.util.HashMap;

public class StringUtils {

    private static final HashMap romanToInteger = new HashMap() {
        {
            this.put("I", 1);
            this.put("II", 2);
            this.put("III", 3);
            this.put("IV", 4);
            this.put("V", 5);
            this.put("VI", 6);
            this.put("VII", 7);
            this.put("VIII", 8);
            this.put("IX", 9);
            this.put("X", 10);
        }
    };

    public static int getNumberFromRoman(String var0) {
        try {
            return Integer.parseInt(var0);
        } catch (NumberFormatException var2) {
            return (Integer) romanToInteger.getOrDefault(var0, 0);
        }
    }
}
