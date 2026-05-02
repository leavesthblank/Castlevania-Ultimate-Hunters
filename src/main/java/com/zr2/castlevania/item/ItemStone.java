package com.zr2.castlevania.item;

import com.zr2.castlevania.properties.ExtendedPlayerStones;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;

public class ItemStone extends BasicItem {

    public static final ArrayList<ItemStone> STONES = new ArrayList<>(6);

    private static int CURRENT_INDEX = 0;

    private final int index;

    public ItemStone(String name) {
        super(name);
        this.setMaxStackSize(1);
        this.index = CURRENT_INDEX;
        CURRENT_INDEX++;
        STONES.add(this);
    }

    public int getIndex() {
        return index;
    }

    public static int getAmountofStones() {
        return CURRENT_INDEX;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        ExtendedPlayerStones playerStones = (ExtendedPlayerStones) player.getExtendedProperties(ExtendedPlayerStones.EXT_PROP_NAME);
        if(!playerStones.isActive(this)) {
            itemStack.stackSize--;
            playerStones.setActive(this);
        }
        return itemStack;
    }
}
