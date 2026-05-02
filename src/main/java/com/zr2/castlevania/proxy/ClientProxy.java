package com.zr2.castlevania.proxy;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.entity.*;
import com.zr2.castlevania.event.client.*;
import com.zr2.castlevania.render.RenderGrapple;
import com.zr2.castlevania.render.RenderHolyFire;
import com.zr2.castlevania.render.RenderItemKnife;
import com.zr2.castlevania.render.RenderSerpentStone;
import com.zr2.castlevania.render.item.RenderItemWhip;
import com.zr2.castlevania.render.tile.RenderTileFakeWall;
import com.zr2.castlevania.tile.TileBlockFakeWall;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy implements IModLoadingProxy {

    public static final RenderItem RENDER_ITEM = new RenderItem();

    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ClientRenderEventHandler());
        MinecraftForge.EVENT_BUS.register(new ClientHeartOverlayEventHandler());
        FMLCommonHandler.instance().bus().register(ClientStoneAbilityEventHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(ClientStoneAbilityEventHandler.INSTANCE);
        FMLCommonHandler.instance().bus().register(new ClientFakeWallEventHandler());
        MinecraftForge.EVENT_BUS.register(new ClientLeaveJoinWorldEventHandler());
        MinecraftForge.EVENT_BUS.register(new ClientGuiEventHandler());
        FMLCommonHandler.instance().bus().register(new ClientConnectToServerEventHandler());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityKnife.class, new RenderItemKnife(Castlevania.KNIFE));
        RenderingRegistry.registerEntityRenderingHandler(EntityAxe.class, new RenderItemKnife(Castlevania.AXE));
        RenderingRegistry.registerEntityRenderingHandler(EntityHolyCross.class, new RenderItemKnife(Castlevania.HOLY_CROSS));
        RenderingRegistry.registerEntityRenderingHandler(EntityHolyWater.class, new RenderSnowball(Castlevania.HOLY_WATER, 16384));
        RenderingRegistry.registerEntityRenderingHandler(EntityHolyFire.class, new RenderHolyFire());
        RenderingRegistry.registerEntityRenderingHandler(EntityWhipHook.class, new RenderGrapple());
        RenderingRegistry.registerEntityRenderingHandler(EntitySerpentStone.class, new RenderSerpentStone());

        ClientRegistry.bindTileEntitySpecialRenderer(TileBlockFakeWall.class, new RenderTileFakeWall());

        IItemRenderer whipRenderer = new RenderItemWhip();
        MinecraftForgeClient.registerItemRenderer(Castlevania.WHIP_VAMPIRE_KILLER, whipRenderer);
        MinecraftForgeClient.registerItemRenderer(Castlevania.WHIP_SAVAGE_SILVER, whipRenderer);
        MinecraftForgeClient.registerItemRenderer(Castlevania.WHIP_HOLY_FLAIL, whipRenderer);
        MinecraftForgeClient.registerItemRenderer(Castlevania.WHIP_MORNING_STAR, whipRenderer);
    }

}
