package com.zr2.castlevania.network.handler.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

import com.zr2.castlevania.network.handler.server.ServerIEEPSyncHandler;
import com.zr2.castlevania.network.packet.PacketIEEPSync;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ClientIEEPSyncHandler extends ServerIEEPSyncHandler {

    @Override
    public IMessage onMessage(PacketIEEPSync message, MessageContext ctx) {
        Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(message.getEntityId());
        this.sync(message, entity);
        return null;
    }

}
