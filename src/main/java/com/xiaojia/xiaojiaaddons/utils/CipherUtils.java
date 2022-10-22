package com.xiaojia.xiaojiaaddons.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;

public class CipherUtils {

    private static final String defaultSecretKey = "PotassiumWingsXJA!!)%@";
    private Cipher encryptCipher;
    private Cipher decryptCipher;

    public CipherUtils(String var1) {
        this.encryptCipher = null;
        this.decryptCipher = null;

        try {
            Key var2 = this.getKey(var1.getBytes(StandardCharsets.UTF_8));
            this.encryptCipher = Cipher.getInstance("DES");
            this.encryptCipher.init(1, var2);
            this.decryptCipher = Cipher.getInstance("DES");
            this.decryptCipher.init(2, var2);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public CipherUtils() throws Exception {
        this("PotassiumWingsXJA!!)%@");
    }

    public static void test(String var0) {
        try {
            String var1 = "";
            CipherUtils var2 = new CipherUtils();
            byte[] var3 = var0.getBytes(StandardCharsets.UTF_8);
            byte[] var4 = var3;
            int var5 = var3.length;

            int var6;
            int var7;
            for (var6 = 0; var6 < var5; ++var6) {
                var7 = var4[var6];
                var1 = var1 + var7 + ", ";
            }

            var1 = var1 + "\n";
            var4 = var2.encrypt(var3);
            byte[] var10 = var4;
            var6 = var4.length;

            for (var7 = 0; var7 < var6; ++var7) {
                byte var8 = var10[var7];
                var1 = var1 + var8 + ", ";
            }

            var1 = var1 + "\n" + byteArr2HexStr(var4);
            ChatLib.chat(var1);
        } catch (Exception var9) {
        }

    }

    public static String byteArr2HexStr(byte[] var0) throws Exception {
        int var1 = var0.length;
        StringBuffer var2 = new StringBuffer(var1 * 2);

        for (int var3 = 0; var3 < var1; ++var3) {
            int var4;
            for (var4 = var0[var3]; var4 < 0; var4 += 256) {
            }

            if (var4 / 16 == 0) {
                var2.append("0");
            }

            var2.append(Integer.toString(var4, 16));
        }

        return var2.toString();
    }

    public static byte[] hexStr2ByteArr(String var0) throws Exception {
        byte[] var1 = var0.getBytes(StandardCharsets.UTF_8);
        int var2 = var1.length;
        byte[] var3 = new byte[var2 / 2];

        for (int var4 = 0; var4 < var2; var4 += 2) {
            String var5 = new String(var1, var4, 2);
            var3[var4 / 2] = (byte) Integer.parseInt(var5, 16);
        }

        return var3;
    }

    public byte[] decrypt(byte[] var1) throws Exception {
        return this.decryptCipher.doFinal(var1);
    }

    private Key getKey(byte[] var1) throws Exception {
        byte[] var2 = new byte[8];

        for (int var3 = 0; var3 < var1.length && var3 < var2.length; ++var3) {
            var2[var3] = var1[var3];
        }

        SecretKeySpec var4 = new SecretKeySpec(var2, "DES");
        return var4;
    }

    public String encrypt(String var1) throws Exception {
        return byteArr2HexStr(this.encrypt(var1.getBytes(StandardCharsets.UTF_8)));
    }

    public String decrypt(String var1) throws Exception {
        return new String(this.decrypt(hexStr2ByteArr(var1)), StandardCharsets.UTF_8);
    }

    public byte[] encrypt(byte[] var1) throws Exception {
        return this.encryptCipher.doFinal(var1);
    }
}
