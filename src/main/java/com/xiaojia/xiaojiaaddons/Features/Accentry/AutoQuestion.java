package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoQuestion {

   public static HashSet newQuestions = new HashSet();

   public static String currentQuestion = "";

   public HashMap answer = new HashMap() {
      {
         this.put("如果你有更强大的副本武器，那么你如何处理你的主线武器？ [单选题]", new String[]{"[以副本武器为辅，来赚钱提升主线武器]"});
         this.put("如果想催更怎么办？ [单选题]", new String[]{"[忍住不去催更]"});
         this.put("临渊之殿的等级限制为多少？ [单选题]", new String[]{"[3000级]"});
         this.put("迷雾森林需要多少级才可进入？ [单选题]", new String[]{"[2600级]", "[2600]"});
         this.put("如果有人在公屏上宣传服务器，拉在线玩家，你怎么做？ [单选题]", new String[]{"[举报]"});
         this.put("如果遇到BUG怎么办？ [单选题]", new String[]{"[私聊管理反馈Bug]"});
         this.put("临渊之殿为几城？ [单选题]", new String[]{"[6城]"});
         this.put("最好的变强的方法是什么？ [单选题]", new String[]{"[享受游戏过程，点点滴滴间提升]"});
         this.put("关于交易，你该怎么做？ [单选题]", new String[]{"[不轻易相信别人，在全球市场买东西]"});
         this.put("关于自身财产问题，你怎么做？ [单选题]", new String[]{"[剧情什么的都保存好]"});
         this.put("挂机的地方叫什么名字 [简答题]", new String[]{"甘泉挂机池"});
         this.put("服务器周年庆时间？ [简答题] [x月x日]", new String[]{"1月7日"});
         this.put("如何用2个字母打开拍卖行？<不用打/号>？ [简答题]", new String[]{"ya"});
         this.put("怎么展示武器？<不用打/号>？ [简答题]", new String[]{"mkshow"});
         this.put("白蚁一击多少伤害？ [简答题]", new String[]{"2"});
         this.put("有人跟你抢怪怎么办？ [单选题]", new String[]{"[要么不要打，要么平分]"});
         this.put("白羽鸡有几滴血？ [简答题]", new String[]{"3"});
         this.put("远古主城是第几城 [单选题]", new String[]{"[4]"});
         this.put("如果有人在公屏上骂人，你怎么做? [单选题]", new String[]{"[屏蔽他]"});
         this.put("如果有人蹭经验怎么办？ [单选题]", new String[]{"[找管理举报并按制度处罚该玩家]"});
         this.put("梦境遇事找谁处理？ [简答题]", new String[]{"管理"});
         this.put("服务器允许注册小号么？ [单选题]", new String[]{"[允许，但只能注册1个]"});
      }
   };

   public static void display() {
      ChatLib.chat("Current question: " + currentQuestion);
      Iterator var0 = newQuestions.iterator();

      while(var0.hasNext()) {
         String var1 = (String)var0.next();
         ChatLib.chat("Q: " + var1);
      }

   }

   private boolean click(IChatComponent var1) {
      ChatStyle var2 = var1.getChatStyle();
      if (var2 != null && var2.getChatClickEvent() != null && var2.getChatClickEvent().getAction() == Action.RUN_COMMAND) {
         ChatLib.say(var2.getChatClickEvent().getValue());
         return true;
      } else {
         Iterator var3 = var1.getSiblings().iterator();

         IChatComponent var4;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            var4 = (IChatComponent)var3.next();
         } while(!this.click(var4));

         return true;
      }
   }

   @SubscribeEvent
   public void onReceive(ClientChatReceivedEvent var1) {
      if (Checker.enabled) {
         if (Configs.AutoQuestion) {
            String var2 = ChatLib.removeFormatting(var1.message.getUnformattedText());
            if (this.answer.containsKey(currentQuestion) && Arrays.asList((Object[])this.answer.get(currentQuestion)).contains(var2)) {
               (new Thread(() -> {
                  try {
                     Thread.sleep((long)Configs.AutoQuestionCD);
                     this.click(var1.message);
                  } catch (Exception var3) {
                     var3.printStackTrace();
                  }

               })).start();
            } else if (this.answer.containsKey(var2)) {
               currentQuestion = var2;
               if (var2.contains("[简答题]")) {
                  (new Thread(() -> {
                     try {
                        Thread.sleep((long)Configs.AutoQuestionCD);
                        ChatLib.say(((String[])this.answer.get(var2))[0]);
                     } catch (Exception var3) {
                        var3.printStackTrace();
                     }

                  })).start();
               }
            } else if (var2.contains("[单选题]") || var2.contains("[简答题]")) {
               newQuestions.add(var2);
            }

         }
      }
   }
}
