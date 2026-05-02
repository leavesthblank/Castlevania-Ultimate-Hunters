package com.zr2.castlevania.network.handler.server;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S23PacketBlockChange;

import com.zr2.castlevania.network.packet.PacketDemandUpdate;
import com.zr2.castlevania.tile.TileBlockFakeWall;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class ServerDemandUpdateHandler implements IMessageHandler<PacketDemandUpdate, IMessage> {

    private static final ByteBuf FAKE_BLOCK_CHANGE_PACKET_BUF = new PooledByteBufAllocator().buffer(14);

    @Override
    public IMessage onMessage(PacketDemandUpdate iMessage, MessageContext messageContext) {
        EntityPlayerMP player = messageContext.getServerHandler().playerEntity;
        handlePacketDemandUpdate(player, messageContext.getServerHandler(), iMessage);
        return null;
    }

    public void handlePacketDemandUpdate(EntityPlayerMP player, NetHandlerPlayServer handlerPlayServer,
        PacketDemandUpdate packet) {
        S23PacketBlockChange returnPacket = new S23PacketBlockChange();
        TileBlockFakeWall fakeWall = (TileBlockFakeWall) player.worldObj
            .getTileEntity(packet.getX(), packet.getY(), packet.getZ());
        FAKE_BLOCK_CHANGE_PACKET_BUF.clear();
        PacketBuffer packetBuffer = new PacketBuffer(FAKE_BLOCK_CHANGE_PACKET_BUF);
        packetBuffer.writeInt(fakeWall.xCoord);
        packetBuffer.writeByte(fakeWall.yCoord);
        packetBuffer.writeInt(fakeWall.zCoord);
        packetBuffer.writeVarIntToBuffer(Block.getIdFromBlock(fakeWall.getBlockDisguise()));
        packetBuffer.writeByte(fakeWall.getMetadata());
        try {
            returnPacket.readPacketData(packetBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        handlerPlayServer.sendPacket(returnPacket);
    }

}
