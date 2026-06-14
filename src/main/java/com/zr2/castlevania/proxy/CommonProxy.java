package com.zr2.castlevania.proxy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.entity.EntityAxe;
import com.zr2.castlevania.entity.EntityHolyCross;
import com.zr2.castlevania.entity.EntityHolyFire;
import com.zr2.castlevania.entity.EntityHolyWater;
import com.zr2.castlevania.entity.EntityKnife;
import com.zr2.castlevania.entity.EntitySerpentStone;
import com.zr2.castlevania.entity.EntityWhipHook;
import com.zr2.castlevania.event.client.ClientStoneAbilityEventHandler;
import com.zr2.castlevania.event.server.ServerAmenadielEventHandler;
import com.zr2.castlevania.event.server.ServerEntityDeadEventHandler;
import com.zr2.castlevania.event.server.ServerEntityPropertiesEventHandler;
import com.zr2.castlevania.event.server.ServerHealthUpgradeEventHandler;
import com.zr2.castlevania.event.server.ServerKnifeHurtEventEventHandler;
import com.zr2.castlevania.event.server.ServerRightClickBlockEventHandler;
import com.zr2.castlevania.event.server.ServerStoneAbilityEventHandler;
import com.zr2.castlevania.item.ItemStone;
import com.zr2.castlevania.network.handler.client.ClientDemandUpdateHandler;
import com.zr2.castlevania.network.handler.client.ClientIEEPSyncHandler;
import com.zr2.castlevania.network.handler.client.ClientTimeStopHandler;
import com.zr2.castlevania.network.handler.client.ClientUseStoneHandler;
import com.zr2.castlevania.network.handler.server.ServerDemandUpdateHandler;
import com.zr2.castlevania.network.handler.server.ServerIEEPSyncHandler;
import com.zr2.castlevania.network.handler.server.ServerTimeStopHandler;
import com.zr2.castlevania.network.handler.server.ServerUseStoneHandler;
import com.zr2.castlevania.network.packet.PacketDemandUpdate;
import com.zr2.castlevania.network.packet.PacketIEEPSync;
import com.zr2.castlevania.network.packet.PacketTimeStopStage;
import com.zr2.castlevania.network.packet.PacketUseStone;
import com.zr2.castlevania.network.packet.PacketUseStoneAbility;
import com.zr2.castlevania.tile.TileBlockCandle;
import com.zr2.castlevania.tile.TileBlockFakeWall;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy implements IModLoadingProxy {

    private int mobId = 0;
    private int packetId = 0;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        // Load mod configuration first
        com.zr2.castlevania.config.ModConfig.load(event);
        registerItemsAndBlocks();
        GameRegistry.registerTileEntity(TileBlockCandle.class, "candle");
        GameRegistry.registerTileEntity(TileBlockFakeWall.class, "fake_wall");
    }

    @Override
    public void init(FMLInitializationEvent event) {
        registerEntity(EntityWhipHook.class, "WhipHook", 256, 1, true);
        registerEntity(EntityKnife.class, "Knife", 256, 1, true);
        registerEntity(EntityAxe.class, "Axe", 256, 5, true);
        registerEntity(EntityHolyWater.class, "HolyWater", 256, 5, true);
        registerEntity(EntityHolyFire.class, "HolyFire", 256, 1, true);
        registerEntity(EntityHolyCross.class, "HolyCross", 256, 1, true);
        registerEntity(EntitySerpentStone.class, "SerpentStoneEffect", 256, 1, true);

        registerEvents();
        registerPackets(event.getSide());
        registerChestGen();
    }

    private void registerChestGen() {
        ArrayList<WeightedRandomChestContent> contents = new ArrayList<>();
        contents.add(new WeightedRandomChestContent(Castlevania.WHIP_VAMPIRE_KILLER, 0, 1, 1, 5));
        contents.add(new WeightedRandomChestContent(Castlevania.WHIP_SAVAGE_SILVER, 0, 1, 1, 4));
        contents.add(new WeightedRandomChestContent(Castlevania.WHIP_HOLY_FLAIL, 0, 1, 1, 3));
        contents.add(new WeightedRandomChestContent(Castlevania.WHIP_MORNING_STAR, 0, 1, 1, 2));
        contents.add(new WeightedRandomChestContent(Castlevania.KNIFE, 0, 1, 1, 4));
        contents.add(new WeightedRandomChestContent(Castlevania.AXE, 0, 1, 1, 4));
        contents.add(new WeightedRandomChestContent(Castlevania.HOLY_WATER, 0, 1, 1, 4));
        contents.add(new WeightedRandomChestContent(Castlevania.HOLY_CROSS, 0, 1, 1, 4));
        contents.add(new WeightedRandomChestContent(Castlevania.BIBLE, 0, 1, 1, 3));
        contents.add(new WeightedRandomChestContent(Castlevania.STOPWATCH, 0, 1, 1, 3));

        contents.add(new WeightedRandomChestContent(Castlevania.SMALL_HEART, 0, 1, 1, 6));
        contents.add(new WeightedRandomChestContent(Castlevania.BIG_HEART, 0, 1, 1, 4));
        contents.add(new WeightedRandomChestContent(Castlevania.HEALTH_UPGRADE, 0, 1, 1, 3));
        contents.add(new WeightedRandomChestContent(Castlevania.HEART_UPGRADE, 0, 1, 1, 3));
        contents.add(new WeightedRandomChestContent(Castlevania.HEART_REFRESH, 0, 1, 1, 2));

        for (ItemStone stone : ItemStone.STONES) {
            contents.add(new WeightedRandomChestContent(stone, 0, 1, 1, 3));
        }

        for (WeightedRandomChestContent content : contents) {
            ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR)
                .addItem(content);
            ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST)
                .addItem(content);
            ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST)
                .addItem(content);
            ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_DISPENSER)
                .addItem(content);
            ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR)
                .addItem(content);
            ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY)
                .addItem(content);
            ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING)
                .addItem(content);
            ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH)
                .addItem(content);
            ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST)
                .addItem(content);
            ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST)
                .addItem(content);
        }
    }

    private void registerEvents() {
        MinecraftForge.EVENT_BUS.register(new ServerKnifeHurtEventEventHandler());
        MinecraftForge.EVENT_BUS.register(new com.zr2.castlevania.event.server.ServerUseUpgradeEventHandler());
        Object handler = new ServerAmenadielEventHandler();
        FMLCommonHandler.instance()
            .bus()
            .register(handler);
        MinecraftForge.EVENT_BUS.register(handler);
        FMLCommonHandler.instance()
            .bus()
            .register(new ServerHealthUpgradeEventHandler());

        handler = new ServerEntityPropertiesEventHandler();
        MinecraftForge.EVENT_BUS.register(handler);
        FMLCommonHandler.instance()
            .bus()
            .register(handler);
        FMLCommonHandler.instance()
            .bus()
            .register(ServerStoneAbilityEventHandler.INSTANCE);

        MinecraftForge.EVENT_BUS.register(new ServerEntityDeadEventHandler());
        MinecraftForge.EVENT_BUS.register(new ServerRightClickBlockEventHandler());
    }

    private void registerPackets(Side side) {
        registerPacket(
            side,
            PacketDemandUpdate.class,
            ClientDemandUpdateHandler.class,
            ServerDemandUpdateHandler.class);
        registerPacket(side, PacketTimeStopStage.class, ClientTimeStopHandler.class, ServerTimeStopHandler.class);
        registerPacket(side, PacketUseStone.class, ClientUseStoneHandler.class, ServerUseStoneHandler.class);
        registerPacket(side, PacketIEEPSync.class, ClientIEEPSyncHandler.class, ServerIEEPSyncHandler.class);
        registerPacket(
            side,
            PacketUseStoneAbility.class,
            () -> ClientStoneAbilityEventHandler.INSTANCE,
            () -> ServerStoneAbilityEventHandler.INSTANCE);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        GameRegistry.addRecipe(
            new ItemStack(Castlevania.WHIP_VAMPIRE_KILLER),
            "HLH",
            "LHL",
            "HLS",
            'H',
            Castlevania.SMALL_HEART,
            'L',
            Items.leather,
            'S',
            Items.stick);

        GameRegistry.addRecipe(
            new ItemStack(Castlevania.WHIP_SAVAGE_SILVER),
            "HIH",
            "IWI",
            "HIH",
            'H',
            Castlevania.SMALL_HEART,
            'I',
            Items.iron_ingot,
            'W',
            Castlevania.WHIP_VAMPIRE_KILLER);

        GameRegistry.addRecipe(
            new ItemStack(Castlevania.WHIP_HOLY_FLAIL),
            "HIH",
            "IWI",
            "HIH",
            'H',
            Castlevania.SMALL_HEART,
            'I',
            Items.blaze_powder,
            'W',
            Castlevania.WHIP_SAVAGE_SILVER);

        GameRegistry.addRecipe(
            new ItemStack(Castlevania.WHIP_MORNING_STAR),
            "HIH",
            "IWI",
            "HIH",
            'H',
            Castlevania.SMALL_HEART,
            'I',
            Items.fire_charge,
            'W',
            Castlevania.WHIP_HOLY_FLAIL);

        GameRegistry.addRecipe(
            new ItemStack(Castlevania.KNIFE),
            "IHS",
            'H',
            Castlevania.SMALL_HEART,
            'I',
            Items.iron_ingot,
            'S',
            Items.stick);

        GameRegistry.addRecipe(
            new ItemStack(Castlevania.AXE),
            "II",
            "IH",
            " S",
            'H',
            Castlevania.SMALL_HEART,
            'I',
            Items.iron_ingot,
            'S',
            Items.stick);

        GameRegistry.addRecipe(
            new ItemStack(Castlevania.HOLY_WATER),
            " I ",
            "IHI",
            " I ",
            'H',
            Castlevania.SMALL_HEART,
            'I',
            Blocks.glass);

        GameRegistry.addRecipe(
            new ItemStack(Castlevania.HOLY_CROSS),
            " I ",
            "IHI",
            " I ",
            'H',
            Castlevania.SMALL_HEART,
            'I',
            new ItemStack(Items.dye, 1, 4));

        GameRegistry.addRecipe(
            new ItemStack(Castlevania.BIBLE),
            "III",
            "IHI",
            "III",
            'H',
            Castlevania.SMALL_HEART,
            'I',
            Items.paper);

        GameRegistry.addRecipe(
            new ItemStack(Castlevania.STOPWATCH),
            " I ",
            "IHI",
            " I ",
            'H',
            Castlevania.SMALL_HEART,
            'I',
            Items.iron_ingot);

        GameRegistry.addRecipe(
            new ItemStack(Castlevania.COIN),
            " I ",
            "IHI",
            " I ",
            'H',
            Castlevania.SMALL_HEART,
            'I',
            Items.gold_nugget);

        GameRegistry.addRecipe(
            new ItemStack(Castlevania.CANDLE_RED),
            "I",
            "H",
            "S",
            'H',
            Castlevania.SMALL_HEART,
            'I',
            Items.coal,
            'S',
            Items.stick);

        GameRegistry.addRecipe(
            new ItemStack(Castlevania.CANDLE_YELLOW),
            "I",
            "H",
            "S",
            'H',
            Castlevania.SMALL_HEART,
            'I',
            Items.gold_nugget,
            'S',
            Items.stick);

        GameRegistry.addRecipe(
            new ItemStack(Castlevania.HEART_REFRESH),
            "IHI",
            'H',
            Castlevania.SMALL_HEART,
            'I',
            Items.feather);
    }

    private void registerPacket(Side side, Class packetClass, Class clientPacketHandler, Class serverPacketHandler) {
        if (side == Side.CLIENT) {
            Castlevania.getNetChannel()
                .registerMessage(clientPacketHandler, packetClass, packetId, Side.CLIENT);
        }
        Castlevania.getNetChannel()
            .registerMessage(serverPacketHandler, packetClass, packetId, Side.SERVER);
        packetId++;
    }

    private void registerPacket(Side side, Class packetClass, Supplier<IMessageHandler> clientPacketHandler,
        Supplier<IMessageHandler> serverPacketHandler) {
        if (side == Side.CLIENT) {
            Castlevania.getNetChannel()
                .registerMessage(clientPacketHandler.get(), packetClass, packetId, Side.CLIENT);
        }
        Castlevania.getNetChannel()
            .registerMessage(serverPacketHandler.get(), packetClass, packetId, Side.SERVER);
        packetId++;
    }

    private void registerItemsAndBlocks() {
        try {
            for (Field field : Castlevania.class.getDeclaredFields()) {
                int modifier = field.getModifiers();
                if (modifier == 0b11001) {
                    if (field.getType() == Item.class) {
                        Item item = (Item) field.get(null);
                        String name = item.getUnlocalizedName()
                            .substring(5);
                        Castlevania.LOGGER.info("Registering item: " + name);
                        GameRegistry.registerItem(item, name);
                    } else if (field.getType() == Block.class) {
                        Block block = (Block) field.get(null);
                        String name = block.getUnlocalizedName()
                            .substring(5);
                        Castlevania.LOGGER.info("Registering block: " + name);
                        GameRegistry.registerBlock(block, name);
                    }
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void registerEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange,
        int updateFrequency, boolean sendsVelocityUpdates) {
        EntityRegistry.registerModEntity(
            entityClass,
            entityName,
            mobId++,
            Castlevania.getCastlevania(),
            trackingRange,
            updateFrequency,
            sendsVelocityUpdates);
    }

}
