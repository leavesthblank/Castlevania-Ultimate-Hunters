package com.zr2.castlevania.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.zr2.castlevania.properties.ExtendedPlayerHeart;

public class ItemHeart extends BasicItem {

    public final int heartWorth;

    public ItemHeart(String name, int heartWorth) {
        super(name);
        this.heartWorth = heartWorth;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        ExtendedPlayerHeart playerHeart = (ExtendedPlayerHeart) player
            .getExtendedProperties(ExtendedPlayerHeart.EXT_PROP_NAME);
        if (!playerHeart.isHeartFull()) {
            if (!world.isRemote) {
                if (!player.capabilities.isCreativeMode) {
                    itemStack.stackSize--;
                }
                playerHeart.replenishHeart(this.heartWorth);
            }
        }
        return itemStack;
    }

}
