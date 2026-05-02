package com.zr2.castlevania.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.entity.EntityAxe;

public class ItemAxe extends BasicItem {

    public ItemAxe() {
        super("throwable_axe");
        this.setCreativeTab(Castlevania.CASTLEVANIA_TAB);
        this.setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (consumeHeart(player, 2)) {
            Entity knife = new EntityAxe(world, player);
            world.spawnEntityInWorld(knife);
        }

        return itemStack;
    }
}
