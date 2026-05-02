package com.zr2.castlevania.event.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.event.world.WorldEvent;

import com.zr2.castlevania.event.server.ServerTickEventHandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ClientLeaveJoinWorldEventHandler {

    @SubscribeEvent
    public void onJoin(WorldEvent.Load event) {
        ServerTickEventHandler.TICK_RATE = 0;
        Minecraft mc = Minecraft.getMinecraft();
        EntityRenderer entityRenderer = mc.entityRenderer;
        if (OpenGlHelper.shadersSupported) {
            entityRenderer.theShaderGroup = null;
        }
    }

}
