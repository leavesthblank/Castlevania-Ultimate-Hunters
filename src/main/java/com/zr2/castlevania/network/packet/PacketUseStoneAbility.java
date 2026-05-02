package com.zr2.castlevania.network.packet;

import com.zr2.castlevania.event.StoneAbilityEvent;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class PacketUseStoneAbility implements IMessage {

    public StoneAbilityEvent.AbilityStone abilityStone = null;

    public PacketUseStoneAbility(StoneAbilityEvent.AbilityStone abilityStone) {
        this.abilityStone = abilityStone;
    }

    public PacketUseStoneAbility() {}

    @Override
    public void fromBytes(ByteBuf buf) {
        abilityStone = StoneAbilityEvent.AbilityStone.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(abilityStone.ordinal());
    }

}
