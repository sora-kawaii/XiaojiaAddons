package com.xiaojia.xiaojiaaddons.Features.Remote.API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpClient {

   private URL url;

   private final String urlstr;

   public HttpClient(String var1) {
      this.urlstr = var1;

      try {
         this.url = new URL(var1);
      } catch (MalformedURLException var3) {
         var3.printStackTrace();
      }

   }

   public String getrawresponse() throws ApiException {
      StringBuilder var3 = new StringBuilder();

      try {
         HttpURLConnection var4 = (HttpURLConnection)this.url.openConnection();
         var4.setRequestMethod("GET");
         var4.setConnectTimeout(10000);
         var4.setReadTimeout(10000);
         int var5 = var4.getResponseCode();
         if (var5 != 200) {
            if (var5 == 429 && this.urlstr.contains("api.hypixel.net")) {
               throw new ApiException("Exceeding amount of requests per minute allowed by Hypixel");
            } else if (var5 == 403 && this.urlstr.contains("api.hypixel.net")) {
               throw new ApiException("Invalid API key");
            } else {
               return null;
            }
         } else {
            BufferedReader var1 = new BufferedReader(new InputStreamReader(var4.getInputStream(), StandardCharsets.UTF_8));

            String var2;
            while((var2 = var1.readLine()) != null) {
               var3.append(var2);
            }

            var1.close();
            return var3.toString();
         }
      } catch (IOException var6) {
         var6.printStackTrace();
         throw new ApiException("An error occured while contacting the Api");
      }
   }
}
