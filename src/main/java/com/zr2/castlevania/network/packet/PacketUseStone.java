package com.zr2.castlevania.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class PacketUseStone implements IMessage {

    private int stoneIndex = 0;
    private boolean isActionUse = false;

    public PacketUseStone() {}

    public PacketUseStone(int stoneIndex, boolean isActionUse) {
        this.stoneIndex = stoneIndex;
        this.isActionUse = isActionUse;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        stoneIndex = buf.readShort();
        isActionUse = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeShort(stoneIndex);
        buf.writeBoolean(isActionUse);
    }

    public int getStoneIndex() {
        return stoneIndex;
    }

    public boolean isActionUse() {
        return isActionUse;
    }
}
