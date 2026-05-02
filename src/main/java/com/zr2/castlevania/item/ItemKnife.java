package com.zr2.castlevania.item;

import com.zr2.castlevania.entity.EntityKnife;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemKnife extends BasicItem {

    public ItemKnife() {
        super("short_knife");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (consumeHeart(player, 1)) {
            EntityKnife knife = new EntityKnife(world, player);
            world.spawnEntityInWorld(knife);
        }

        return itemStack;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

}
