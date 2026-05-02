package com.zr2.castlevania.network.handler.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2FPacketSetSlot;

import com.zr2.castlevania.item.ItemStone;
import com.zr2.castlevania.network.packet.PacketUseStone;
import com.zr2.castlevania.properties.ExtendedPlayerStones;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ServerUseStoneHandler implements IMessageHandler<PacketUseStone, IMessage> {

    @Override
    public IMessage onMessage(PacketUseStone message, MessageContext ctx) {
        ItemStone itemStone = ItemStone.STONES.get(message.getStoneIndex());
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        ItemStack draggedItem = player.inventory.getItemStack();
        ExtendedPlayerStones playerStones = (ExtendedPlayerStones) player
            .getExtendedProperties(ExtendedPlayerStones.EXT_PROP_NAME);

        if (message.isActionUse()) {
            if (!player.capabilities.isCreativeMode && (draggedItem == null || draggedItem.getItem() != itemStone)) {
                return null;
            }
            player.inventory.setItemStack(null);
            playerStones.setActive(itemStone);
        } else if (playerStones.isActive(itemStone) && draggedItem == null) {
            ItemStack item = new ItemStack(itemStone);
            if (!player.capabilities.isCreativeMode) {
                player.inventory.setItemStack(item);
            }
            playerStones.setInactive(itemStone);
            S2FPacketSetSlot packet = new S2FPacketSetSlot(-1, -1, item);
            ctx.getServerHandler()
                .sendPacket(packet);
        }
        return null;
    }

}
