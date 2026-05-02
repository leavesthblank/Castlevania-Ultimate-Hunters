package com.zr2.castlevania.event.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.potion.Potion;

import com.zr2.castlevania.entity.EntitySerpentStone;
import com.zr2.castlevania.event.StoneAbilityEvent;
import com.zr2.castlevania.network.packet.PacketUseStoneAbility;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ServerStoneAbilityEventHandler extends StoneAbilityEvent
    implements IMessageHandler<PacketUseStoneAbility, IMessage> {

    public static final ServerStoneAbilityEventHandler INSTANCE = new ServerStoneAbilityEventHandler();

    private ServerStoneAbilityEventHandler() {}

    protected void serverTick(EntityPlayer player, AbilityStone stone) {
        switch (stone) {
            case JUMP:
                grantPotionEffect(player, Potion.jump, 2);
                player.fallDistance = 0;
                break;
            case SPIDER:
                if (isClimbing(player) != null) {
                    player.fallDistance = 0;
                }
                break;
            case MERMAID:
                grantPotionEffect(player, Potion.waterBreathing, 0);
                break;
            case GRYPHON:
                player.fallDistance = 0;
                break;
        }
    }

    @Override
    public IMessage onMessage(PacketUseStoneAbility message, MessageContext ctx) {
        switch (message.abilityStone) {
            case DASH:
                NetHandlerPlayServer netHandler = ctx.getServerHandler();
                EntityPlayer player = netHandler.playerEntity;
                EntitySerpentStone serpentStone = new EntitySerpentStone(ctx.getServerHandler().playerEntity);
                player.worldObj.spawnEntityInWorld(serpentStone);
                netHandler.sendPacket(new S1BPacketEntityAttach(0, player, serpentStone));
                break;
        }
        return null;
    }
}
