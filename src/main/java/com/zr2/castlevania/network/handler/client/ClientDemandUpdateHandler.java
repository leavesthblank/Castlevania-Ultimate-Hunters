package com.zr2.castlevania.network.handler.client;

import com.zr2.castlevania.network.packet.PacketDemandUpdate;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ClientDemandUpdateHandler implements IMessageHandler<PacketDemandUpdate, IMessage> {

    @Override
    public IMessage onMessage(PacketDemandUpdate message, MessageContext ctx) {
        return null;
    }
}
