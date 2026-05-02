package com.zr2.castlevania.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class PacketTimeStopStage implements IMessage {

    private int stage;

    public PacketTimeStopStage() {
    }

    public PacketTimeStopStage(int stage) {
        if (stage > Byte.MAX_VALUE || stage < Byte.MIN_VALUE) {
            throw new RuntimeException("stage is not within range of -128 to 127");
        }
        this.stage = stage;
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        this.stage = byteBuf.getByte(0);
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeByte(this.stage);
    }

    public int getStage() {
        return stage;
    }
}
