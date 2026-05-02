package com.zr2.castlevania.network.handler.client;

import com.zr2.castlevania.network.packet.PacketUseStone;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ClientUseStoneHandler implements IMessageHandler<PacketUseStone, IMessage> {

    @Override
    public IMessage onMessage(PacketUseStone message, MessageContext ctx) {
        return null;
    }
}
