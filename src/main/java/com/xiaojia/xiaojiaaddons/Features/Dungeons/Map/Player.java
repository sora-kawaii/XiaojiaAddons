package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Image;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ColorUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;

import javax.vecmath.Vector2f;
import java.awt.image.BufferedImage;

public class Player {

    public static Image redIcon = new Image("RedPlayerIcon.png");
    public static Image greenIcon = new Image("GreenPlayerIcon.png");
    public static Image defaultIcon = new Image("defaultPlayerIcon.png");
    public double iconX;
    public double realZ;
    public double realX;
    public Image head;

    public boolean isDead;

    public float yaw;

    public Vector2f size = new Vector2f(5.0F, 5.0F);

    public String className;

    public boolean inRender;

    public String icon;

    public String uuid;

    public double iconY;

    public String name;

    public void render() {
        Image var1 = this.head;
        boolean var2 = this.name.equals(MinecraftUtils.getPlayer().getName());
        if (var1 == null) {
            if (!var2) {
                var1 = defaultIcon;
            } else {
                switch (Configs.SelfIconColor) {
                    case 1:
                        var1 = greenIcon;
                        break;
                    case 2:
                        var1 = redIcon;
                        break;
                    default:
                        var1 = defaultIcon;
                }
            }
        }

        if (this.isDead) {
            this.yaw = 0.0F;
        }

        float var3 = this.isDead ? 0.0F : this.yaw;
        this.size = var2 && this.head != null && Configs.SelfIconBorderColor != 0 ? new Vector2f((float) (Configs.MapScale * Configs.HeadScale) * 0.05F, (float) (Configs.MapScale * Configs.HeadScale) * 0.05F) : new Vector2f((float) (Configs.MapScale * Configs.HeadScale) * 0.04F, (float) (Configs.MapScale * Configs.HeadScale) * 0.04F);
        Vector2f var4 = this.isDead ? new Vector2f((float) (Configs.MapScale * Configs.HeadScale) * 0.03F, (float) (Configs.MapScale * Configs.HeadScale) * 0.03F) : this.size;
        RenderUtils.translate((double) Configs.MapX + this.iconX, (double) Configs.MapY + this.iconY);
        RenderUtils.translate(var4.x / 2.0F, var4.y / 2.0F);
        RenderUtils.rotate(var3);
        RenderUtils.translate(-var4.x / 2.0F, -var4.y / 2.0F);
        RenderUtils.drawImage(var1, 0.0, 0.0, var4.x, var4.y);
    }

    public void renderName() {
        RenderUtils.translate((double) Configs.MapX + this.iconX, (double) Configs.MapY + this.iconY);
        RenderUtils.scale(0.1F * (float) Configs.MapScale, 0.1F * (float) Configs.MapScale);
        RenderUtils.drawStringWithShadow(ColorUtils.colors[Configs.PlayerNameColor] + this.name, ((float) (-XiaojiaAddons.mc.fontRendererObj.getStringWidth(this.name)) + this.size.x * 2.0F) / 2.0F, this.size.y * 2.0F);
    }

    public void fetchHead() {
        (new Thread(() -> {
            try {
                BufferedImage var1 = MinecraftUtils.getHead(this.name);
                if (MinecraftUtils.getPlayer().getName().equals(this.name) && Configs.SelfIconBorderColor != 0) {
                    BufferedImage var2 = new BufferedImage(10, 10, 6);
                    var2.getGraphics().drawImage(var1, 1, 1, null);
                    short var3;
                    short var4;
                    short var5;
                    switch (Configs.SelfIconBorderColor) {
                        case 1:
                            var3 = 0;
                            var4 = 255;
                            var5 = 0;
                            break;
                        case 2:
                            var3 = 255;
                            var4 = 0;
                            var5 = 0;
                            break;
                        default:
                            var3 = 0;
                            var4 = 0;
                            var5 = 255;
                    }

                    int var6 = -16777216 + (var3 << 16) + (var4 << 8) + var5;

                    for (int var7 = 0; var7 < 10; ++var7) {
                        var2.setRGB(var7, 9, var6);
                        var2.setRGB(var7, 0, var6);
                        var2.setRGB(9, var7, var6);
                        var2.setRGB(0, var7, var6);
                    }

                    this.head = new Image(var2);
                } else {
                    this.head = new Image(var1);
                }
            } catch (Exception var8) {
                var8.printStackTrace();
            }

        })).start();
    }
}
