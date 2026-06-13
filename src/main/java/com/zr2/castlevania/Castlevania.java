package com.zr2.castlevania;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import org.apache.logging.log4j.Logger;

import com.zr2.castlevania.block.BlockCandleRed;
import com.zr2.castlevania.block.BlockCandleYellow;
import com.zr2.castlevania.block.BlockFakeWall;
import com.zr2.castlevania.item.BasicItem;
import com.zr2.castlevania.item.ItemAxe;
import com.zr2.castlevania.item.ItemHeart;
import com.zr2.castlevania.item.ItemHolyCross;
import com.zr2.castlevania.item.ItemHolyWater;
import com.zr2.castlevania.item.ItemKnife;
import com.zr2.castlevania.item.ItemStone;
import com.zr2.castlevania.item.ItemWhip;
import com.zr2.castlevania.proxy.ClientProxy;
import com.zr2.castlevania.proxy.CommonProxy;
import com.zr2.castlevania.proxy.IModLoadingProxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = Castlevania.MODID, version = Castlevania.VERSION)
public class Castlevania {

    public static final String MODID = "castlevania";
    public static final String VERSION = "1.0";

    public static Logger LOGGER;

    public static final CreativeTabs CASTLEVANIA_TAB = new CreativeTabs("Castlevania") {

        @Override
        public Item getTabIconItem() {
            return WHIP_MORNING_STAR;
        }
    };

    public static final Block CANDLE_YELLOW = new BlockCandleYellow();
    public static final Block CANDLE_RED = new BlockCandleRed();
    public static final Block FAKE_WALL = new BlockFakeWall();

    public static final Item WHIP_VAMPIRE_KILLER = new ItemWhip("vampire_killer", 4);
    public static final Item WHIP_SAVAGE_SILVER = new ItemWhip("savage_silver", 8);
    public static final Item WHIP_HOLY_FLAIL = new ItemWhip("holy_flail", 12);
    public static final Item WHIP_MORNING_STAR = new ItemWhip("morning_star", 16);
    public static final Item KNIFE = new ItemKnife(3);
    public static final Item AXE = new ItemAxe(6);
    public static final Item HOLY_WATER = new ItemHolyWater();
    public static final Item HOLY_CROSS = new ItemHolyCross();
    public static final Item BIBLE = new BasicItem("bible").setMaxDamage(100)
        .setMaxStackSize(1);
    public static final Item STOPWATCH = new BasicItem("stopwatch").setMaxDamage(100)
        .setMaxStackSize(1);
    public static final Item COIN = new BasicItem("coin");
    public static final Item SMALL_HEART = new ItemHeart("small_heart", 1);
    public static final Item BIG_HEART = new ItemHeart("big_heart", 5);
    public static final Item HEART_REFRESH = new ItemHeart("heart_refresh", 50);
    public static final Item CREATIVE_HEART = new ItemHeart("creative_heart", 9999999);
    public static final Item JUMP_STONE = new ItemStone("jump_stone");
    public static final Item SPIDER_STONE = new ItemStone("spider_stone");
    public static final Item MERMAID_STONE = new ItemStone("mermaid_stone");
    public static final Item DASH_STONE = new ItemStone("dash_stone");
    public static final Item LEAP_STONE = new ItemStone("leap_stone");
    public static final Item GRYPHON_STONE = new ItemStone("gryphon_stone");
    public static final Item HEART_UPGRADE = new BasicItem("heart_upgrade");
    public static final Item HEALTH_UPGRADE = new BasicItem("health_upgrade");
    public static final Item FAKE_WALL_WAND = new BasicItem("fake_wall_wand");

    private static Castlevania CASTLEVANIA;
    private static SimpleNetworkWrapper NET_CHANNEL;

    private final IModLoadingProxy commonProxy = new CommonProxy();
    private IModLoadingProxy clientProxy;

    public Castlevania() {
        Castlevania.CASTLEVANIA = this;
        if (FMLCommonHandler.instance()
            .getSide() == Side.CLIENT) {
            this.clientProxy = new ClientProxy();
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws Exception {
        LOGGER = event.getModLog();
        commonProxy.preInit(event);
        if (FMLCommonHandler.instance()
            .getSide() == Side.CLIENT) {
            clientProxy.preInit(event);
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        NET_CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(MODID.toUpperCase());

        commonProxy.init(event);
        if (FMLCommonHandler.instance()
            .getSide() == Side.CLIENT) {
            clientProxy.init(event);
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        commonProxy.postInit(event);
        if (FMLCommonHandler.instance()
            .getSide() == Side.CLIENT) {
            clientProxy.postInit(event);
        }
    }

    public static Castlevania getCastlevania() {
        return CASTLEVANIA;
    }

    public static SimpleNetworkWrapper getNetChannel() {
        return NET_CHANNEL;
    }

}
