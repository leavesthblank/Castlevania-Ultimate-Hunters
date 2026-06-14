package com.zr2.castlevania.event.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.properties.ExtendedPlayerHeart;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ServerUseUpgradeEventHandler {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.world.isRemote) return;
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_AIR
            && event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return;

        EntityPlayer player = event.entityPlayer;
        ItemStack itemStack = player.getCurrentEquippedItem();
        if (itemStack == null) return;

        if (itemStack.getItem() == Castlevania.HEART_UPGRADE) {
            ExtendedPlayerHeart ext = (ExtendedPlayerHeart) player
                .getExtendedProperties(ExtendedPlayerHeart.EXT_PROP_NAME);
            if (ext != null) {
                boolean applied = ext.applyHeartUpgrade(1);
                if (applied) {
                    if (!player.capabilities.isCreativeMode) {
                        itemStack.stackSize--;
                        if (itemStack.stackSize <= 0) {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                        }
                    }
                    // feedback
                    player.addChatComponentMessage(new ChatComponentText("Heart upgrade applied."));
                    player.worldObj.playSoundAtEntity(player, "random.levelup", 0.8F, 1.0F);
                    event.setCanceled(true);
                } else {
                    player.addChatComponentMessage(
                        new ChatComponentText("Cannot apply heart upgrade: cap reached or not allowed."));
                    player.worldObj.playSoundAtEntity(player, "random.break", 0.8F, 1.0F);
                }
            }
        } else if (itemStack.getItem() == Castlevania.HEALTH_UPGRADE) {
            ExtendedPlayerHeart ext = (ExtendedPlayerHeart) player
                .getExtendedProperties(ExtendedPlayerHeart.EXT_PROP_NAME);
            if (ext != null) {
                boolean applied = ext.applyHealthUpgrade(1);
                if (applied) {
                    if (!player.capabilities.isCreativeMode) {
                        itemStack.stackSize--;
                        if (itemStack.stackSize <= 0) {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                        }
                    }
                    // feedback
                    player.addChatComponentMessage(new ChatComponentText("Health upgrade applied."));
                    player.worldObj.playSoundAtEntity(player, "random.levelup", 0.8F, 1.0F);
                    event.setCanceled(true);
                } else {
                    player.addChatComponentMessage(
                        new ChatComponentText("Cannot apply health upgrade: cap reached or not allowed."));
                    player.worldObj.playSoundAtEntity(player, "random.break", 0.8F, 1.0F);
                }
            }
        }
    }

}
