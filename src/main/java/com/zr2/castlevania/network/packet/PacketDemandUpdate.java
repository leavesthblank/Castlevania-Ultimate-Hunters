package com.zr2.castlevania.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class PacketDemandUpdate implements IMessage {

    private int x;
    private int y;
    private int z;

    public PacketDemandUpdate() {}

    public PacketDemandUpdate(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        x = byteBuf.readInt();
        y = byteBuf.readByte();
        z = byteBuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(x);
        byteBuf.writeByte(y);
        byteBuf.writeInt(z);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

}
