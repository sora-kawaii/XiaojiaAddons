package com.xiaojia.xiaojiaaddons;

import com.xiaojia.xiaojiaaddons.Commands.*;
import com.xiaojia.xiaojiaaddons.Config.Config;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Accentry.*;
import com.xiaojia.xiaojiaaddons.Features.Bestiary.*;
import com.xiaojia.xiaojiaaddons.Features.Dragons.AutoShootCrystal;
import com.xiaojia.xiaojiaaddons.Features.Dragons.EnderCrystalESP;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.*;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Map;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.MapUpdater;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.RoomLoader;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.AutoBlaze;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Quiz;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.TeleportMaze;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.ThreeWeirdos;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water.DevWater;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water.WaterSolver;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.*;
import com.xiaojia.xiaojiaaddons.Features.Nether.*;
import com.xiaojia.xiaojiaaddons.Features.Nether.Dojo.Discipline;
import com.xiaojia.xiaojiaaddons.Features.Nether.Dojo.DojoUtils;
import com.xiaojia.xiaojiaaddons.Features.Nether.Dojo.Force;
import com.xiaojia.xiaojiaaddons.Features.Nether.Dojo.Mastery;
import com.xiaojia.xiaojiaaddons.Features.QOL.*;
import com.xiaojia.xiaojiaaddons.Features.Remote.API.ApiKey;
import com.xiaojia.xiaojiaaddons.Features.Remote.DungeonLoot;
import com.xiaojia.xiaojiaaddons.Features.Remote.DupedItems;
import com.xiaojia.xiaojiaaddons.Features.Remote.LowestBin;
import com.xiaojia.xiaojiaaddons.Features.Skills.*;
import com.xiaojia.xiaojiaaddons.Features.Slayers.*;
import com.xiaojia.xiaojiaaddons.Features.Tests.GuiTest;
import com.xiaojia.xiaojiaaddons.Objects.Display.DisplayHandler;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.Objects.ScoreBoard;
import com.xiaojia.xiaojiaaddons.Objects.TestCubeGUI;
import com.xiaojia.xiaojiaaddons.Sounds.SoundHandler;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Iterator;

@Mod(
        modid = "xiaojiaaddons",
        version = "2.4.8.3",
        acceptedMinecraftVersions = "[1.8.9]"
)
public class XiaojiaAddons {

    public static final String MODID = "xiaojiaaddons";
    public static final String VERSION = "2.4.8.3";
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static ArrayList settings = Config.collect(Configs.class);
    public static AutoSneakyCreeper autoSneakyCreeper;
    public static GuiScreen guiToOpen = null;
    private static boolean debug = false;

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug() {
        debug = !debug;
        ChatLib.chat("debug: " + debug);
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (guiToOpen != null) {
            mc.displayGuiScreen(guiToOpen);
            guiToOpen = null;
        }

    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent var1) {
        MinecraftForge.EVENT_BUS.register(new TickEndEvent());
        MinecraftForge.EVENT_BUS.register(new Checker());
        Config.load();
        RoomLoader.load();
        ItemRename.load();
        DupedItems.load();
        RelicESP.load();
        ClientCommandHandler.instance.registerCommand(new Command());
        ClientCommandHandler.instance.registerCommand(new FarmingPoint());
        ClientCommandHandler.instance.registerCommand(new FarmingType());
        ClientCommandHandler.instance.registerCommand(new PP());
        ClientCommandHandler.instance.registerCommand(new AutoForagingCommand());
        ClientCommandHandler.instance.registerCommand(new TransferBackCommand());
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new DisplayHandler());
        MinecraftForge.EVENT_BUS.register(new SoundHandler());
        MinecraftForge.EVENT_BUS.register(new AutoClick());
        MinecraftForge.EVENT_BUS.register(new HoverCommand());
        MinecraftForge.EVENT_BUS.register(new AutoMeat());
        MinecraftForge.EVENT_BUS.register(new AutoRongYao());
        MinecraftForge.EVENT_BUS.register(new FastUse());
        MinecraftForge.EVENT_BUS.register(new AutoQuestion());
        MinecraftForge.EVENT_BUS.register(new AutoBack());
        MinecraftForge.EVENT_BUS.register(new AutoEat());
        MinecraftForge.EVENT_BUS.register(new AutoBottle());
        MinecraftForge.EVENT_BUS.register(new ControlUtils());
        MinecraftForge.EVENT_BUS.register(new MathUtils());
        MinecraftForge.EVENT_BUS.register(new HotbarUtils());
        MinecraftForge.EVENT_BUS.register(new ScoreBoard());
        MinecraftForge.EVENT_BUS.register(new SkyblockUtils());
        MinecraftForge.EVENT_BUS.register(new CommandsUtils());
        MinecraftForge.EVENT_BUS.register(new GuiTest());
        MinecraftForge.EVENT_BUS.register(new ShortbowUtils());
        MinecraftForge.EVENT_BUS.register(new TestCubeGUI());
        MinecraftForge.EVENT_BUS.register(new AutoScatha());
        MinecraftForge.EVENT_BUS.register(new Spider());
        MinecraftForge.EVENT_BUS.register(new SneakyCreeper());
        MinecraftForge.EVENT_BUS.register(autoSneakyCreeper = new AutoSneakyCreeper());
        autoSneakyCreeper.init();
        MinecraftForge.EVENT_BUS.register(new GolemAlert());
        MinecraftForge.EVENT_BUS.register(new AutoShootCrystal());
        MinecraftForge.EVENT_BUS.register(new EnderCrystalESP());
        MinecraftForge.EVENT_BUS.register(new AutoBlaze());
        MinecraftForge.EVENT_BUS.register(new AutoBlood());
        MinecraftForge.EVENT_BUS.register(new AutoCloseSecretChest());
        MinecraftForge.EVENT_BUS.register(new AutoItemFrame());
        MinecraftForge.EVENT_BUS.register(new SimonSays());
        MinecraftForge.EVENT_BUS.register(new AutoLeap());
        MinecraftForge.EVENT_BUS.register(new AutoSalvage());
        MinecraftForge.EVENT_BUS.register(new AutoTerminal());
        MinecraftForge.EVENT_BUS.register(new AutoTerminalNew());
        MinecraftForge.EVENT_BUS.register(new BatESP());
        MinecraftForge.EVENT_BUS.register(new BloodAssist());
        MinecraftForge.EVENT_BUS.register(new CoordsGB());
        MinecraftForge.EVENT_BUS.register(new KeyESP());
        MinecraftForge.EVENT_BUS.register(new LividESP());
        MinecraftForge.EVENT_BUS.register(new M4ESP());
        MinecraftForge.EVENT_BUS.register(new M7Dragon());
        MinecraftForge.EVENT_BUS.register(new MimicWarn());
        MinecraftForge.EVENT_BUS.register(new ShowHiddenMobs());
        MinecraftForge.EVENT_BUS.register(new ShadowAssassinESP());
        MinecraftForge.EVENT_BUS.register(new StarredMobESP());
        MinecraftForge.EVENT_BUS.register(new StarredMobESPBox());
        MinecraftForge.EVENT_BUS.register(new StonklessStonk());
        MinecraftForge.EVENT_BUS.register(new TrapChestESP());
        MinecraftForge.EVENT_BUS.register(new SecretChecker());
        MinecraftForge.EVENT_BUS.register(new Dungeon());
        MinecraftForge.EVENT_BUS.register(new MapUpdater());
        MinecraftForge.EVENT_BUS.register(new Map());
        MinecraftForge.EVENT_BUS.register(new TeleportMaze());
        MinecraftForge.EVENT_BUS.register(new Quiz());
        MinecraftForge.EVENT_BUS.register(new WaterSolver());
        MinecraftForge.EVENT_BUS.register(new ThreeWeirdos());
        MinecraftForge.EVENT_BUS.register(new ChatCopy());
        MinecraftForge.EVENT_BUS.register(new ChestFiller());
        MinecraftForge.EVENT_BUS.register(new ProtectItems());
        MinecraftForge.EVENT_BUS.register(new ColorName());
        MinecraftForge.EVENT_BUS.register(new Velocity());
        MinecraftForge.EVENT_BUS.register(new NoRotate());
        MinecraftForge.EVENT_BUS.register(new RenderRank());
        MinecraftForge.EVENT_BUS.register(new KeepSprint());
        MinecraftForge.EVENT_BUS.register(new ShowLowestBin());
        MinecraftForge.EVENT_BUS.register(new DevMode());
        MinecraftForge.EVENT_BUS.register(new MusicRune());
        MinecraftForge.EVENT_BUS.register(new PacketRelated());
        MinecraftForge.EVENT_BUS.register(new EasyTrigger());
        MinecraftForge.EVENT_BUS.register(new CorruptedESP());
        MinecraftForge.EVENT_BUS.register(new GhastESP());
        MinecraftForge.EVENT_BUS.register(new Kuudra());
        MinecraftForge.EVENT_BUS.register(new ConvergenceESP());
        MinecraftForge.EVENT_BUS.register(new AshFangESP());
        MinecraftForge.EVENT_BUS.register(new AshFangGravityESP());
        MinecraftForge.EVENT_BUS.register(new SpongeESP());
        MinecraftForge.EVENT_BUS.register(new PrismarineESP());
        MinecraftForge.EVENT_BUS.register(new TitaniumESP());
        MinecraftForge.EVENT_BUS.register(new XYZ());
        MinecraftForge.EVENT_BUS.register(new GolemESP());
        MinecraftForge.EVENT_BUS.register(new Force());
        MinecraftForge.EVENT_BUS.register(new Mastery());
        MinecraftForge.EVENT_BUS.register(new Discipline());
        MinecraftForge.EVENT_BUS.register(new DojoUtils());
        MinecraftForge.EVENT_BUS.register(new RunicESP());
        MinecraftForge.EVENT_BUS.register(new FairySoul());
        MinecraftForge.EVENT_BUS.register(new RelicESP());
        MinecraftForge.EVENT_BUS.register(new AutoSnowball());
        MinecraftForge.EVENT_BUS.register(new AutoCombine());
        MinecraftForge.EVENT_BUS.register(new AutoAttribute());
        MinecraftForge.EVENT_BUS.register(new AutoHarp());
        MinecraftForge.EVENT_BUS.register(new AutoJerryBox());
        MinecraftForge.EVENT_BUS.register(new AutoLobby());
        MinecraftForge.EVENT_BUS.register(new AutoIsland());
        MinecraftForge.EVENT_BUS.register(new AutoUseItem());
        MinecraftForge.EVENT_BUS.register(new BlockAbility());
        MinecraftForge.EVENT_BUS.register(new DisableEntityRender());
        MinecraftForge.EVENT_BUS.register(new DisplayDayAndCoords());
        MinecraftForge.EVENT_BUS.register(new EntityQOL());
        MinecraftForge.EVENT_BUS.register(new FindFairy());
        MinecraftForge.EVENT_BUS.register(new GhostBlock());
        MinecraftForge.EVENT_BUS.register(new GhostQOL());
        MinecraftForge.EVENT_BUS.register(new HideCreepers());
        MinecraftForge.EVENT_BUS.register(new NearbyChestESP());
        MinecraftForge.EVENT_BUS.register(new MonolithESP());
        MinecraftForge.EVENT_BUS.register(new InCombatQOL());
        MinecraftForge.EVENT_BUS.register(new NoSlowdown());
        MinecraftForge.EVENT_BUS.register(new OneTick());
        MinecraftForge.EVENT_BUS.register(new ShowBookName());
        MinecraftForge.EVENT_BUS.register(new ShowEtherwarp());
        MinecraftForge.EVENT_BUS.register(new SwordSwap());
        MinecraftForge.EVENT_BUS.register(new RemoveBlindness());
        MinecraftForge.EVENT_BUS.register(new TransferBack());
        MinecraftForge.EVENT_BUS.register(new ShowAttribute());
        MinecraftForge.EVENT_BUS.register(new AttributeFilter());
        MinecraftForge.EVENT_BUS.register(new BurrowHelper());
        MinecraftForge.EVENT_BUS.register(new AutoCloseCrystalHollowsChest());
        MinecraftForge.EVENT_BUS.register(new AutoPowder());
        MinecraftForge.EVENT_BUS.register(new AutoPowderChest());
        MinecraftForge.EVENT_BUS.register(new Foraging());
        MinecraftForge.EVENT_BUS.register(new Fishing());
        MinecraftForge.EVENT_BUS.register(new AutoBuildFarmVertical());
        MinecraftForge.EVENT_BUS.register(new AutoBuildFarmPumpkin());
        MinecraftForge.EVENT_BUS.register(new Farming());
        MinecraftForge.EVENT_BUS.register(new GemstoneESP());
        MinecraftForge.EVENT_BUS.register(new JadeCrystalHelper());
        MinecraftForge.EVENT_BUS.register(new Experimentation());
        MinecraftForge.EVENT_BUS.register(new SuperPairs());
        MinecraftForge.EVENT_BUS.register(new Blaze());
        MinecraftForge.EVENT_BUS.register(new Sven());
        MinecraftForge.EVENT_BUS.register(new Revenant());
        MinecraftForge.EVENT_BUS.register(new Tarantula());
        MinecraftForge.EVENT_BUS.register(new Voidgloom());
        MinecraftForge.EVENT_BUS.register(new ClickScreenMaddox());
        MinecraftForge.EVENT_BUS.register(new DungeonLoot());
        MinecraftForge.EVENT_BUS.register(new LowestBin());
        MinecraftForge.EVENT_BUS.register(new DupedItems());
        MinecraftForge.EVENT_BUS.register(new ChestProfit());
        MinecraftForge.EVENT_BUS.register(new ApiKey());
        MinecraftForge.EVENT_BUS.register(new DevWater());
        MinecraftForge.EVENT_BUS.register(new CommandKeybind());
        CommandKeybind.loadKeyBinds();
        Iterator var2 = KeyBindUtils.keyBinds.iterator();

        while (var2.hasNext()) {
            KeyBind var3 = (KeyBind) var2.next();
            ClientRegistry.registerKeyBinding(var3.mcKeyBinding());
        }

    }
}
