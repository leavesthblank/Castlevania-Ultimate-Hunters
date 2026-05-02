package com.zr2.castlevania.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.zr2.castlevania.entity.EntityHolyCross;

public class ItemHolyCross extends BasicItem {

    public ItemHolyCross() {
        super("holy_cross");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (consumeHeart(player, 2)) {
            Entity knife = new EntityHolyCross(world, player);
            world.spawnEntityInWorld(knife);
        }
        return itemStack;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

}
