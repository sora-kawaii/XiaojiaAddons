package com.xiaojia.xiaojiaaddons.Features.Remote;

import org.apache.http.Consts;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RemoteUtils {

    private static final String baseURL = "http://47.94.243.9:11050/";

    public static void post(String var0, String var1) {
        try {
            CloseableHttpClient var2 = HttpClients.createDefault();
            RequestConfig var3 = RequestConfig.custom().setConnectTimeout(20000).setConnectionRequestTimeout(20000).setSocketTimeout(20000).build();
            HttpPost var4 = new HttpPost("http://47.94.243.9:11050/" + var0);
            var4.addHeader("Content-type", "application/json; charset=utf-8");
            var4.setConfig(var3);
            var4.setEntity(new StringEntity(var1, StandardCharsets.UTF_8));
            var2.execute(var4);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public static String get(String var0, List var1, boolean var2) {
        String var3 = null;

        try {
            HttpClientBuilder var4 = HttpClients.custom();
            var4.addInterceptorFirst((HttpResponseInterceptor) (var0x, var1x) -> {
                if (!var0x.containsHeader("Pragma")) {
                    var0x.addHeader("Pragma", "no-cache");
                }

                if (!var0x.containsHeader("Cache-Control")) {
                    var0x.addHeader("Cache-Control", "no-cache");
                }

            });
            var4.setUserAgent("XiaojiaAddons/2.4.8.3");
            RequestConfig var5 = RequestConfig.custom().setConnectTimeout(20000).setConnectionRequestTimeout(20000).setSocketTimeout(20000).build();
            String var6 = EntityUtils.toString(new UrlEncodedFormEntity(var1, Consts.UTF_8));
            String var7 = var0;
            if (var6.length() > 0) {
                var7 = var0 + "?" + var6;
            }

            if (var2) {
                var7 = "http://47.94.243.9:11050/" + var7;
            } else {
                System.err.println(var7);
            }

            HttpGet var8 = new HttpGet(var7);
            var8.setConfig(var5);
            var3 = EntityUtils.toString(var4.build().execute(var8).getEntity(), "UTF-8");
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        return var3;
    }

    public static String get(String var0) {
        return get(var0, new ArrayList());
    }

    public static String get(String var0, List var1) {
        return get(var0, var1, true);
    }
}
