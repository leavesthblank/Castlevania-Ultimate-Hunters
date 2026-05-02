package com.zr2.castlevania.inventory;

import com.zr2.castlevania.item.ItemStone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class SlotAbilityStone extends Slot {

    public final ItemStone itemStone;

    public SlotAbilityStone(IInventory p_i1824_1_, int x, int y, ItemStone itemStone) {
        super(p_i1824_1_, itemStone.getIndex(), x, y);
        this.itemStone = itemStone;
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValid(ItemStack p_75214_1_) {
        return p_75214_1_ != null && p_75214_1_.getItem() == itemStone;
    }

    @Override
    public IIcon getBackgroundIconIndex() {
        return null;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer p_82870_1_, ItemStack p_82870_2_) {
        super.onPickupFromSlot(p_82870_1_, p_82870_2_);
    }

    @Override
    public void onSlotChange(ItemStack p_75220_1_, ItemStack p_75220_2_) {
        super.onSlotChange(p_75220_1_, p_75220_2_);
    }

    @Override
    public void onSlotChanged() {
        super.onSlotChanged();
    }

    @Override
    public ItemStack decrStackSize(int p_75209_1_) {
        return super.decrStackSize(p_75209_1_);
    }

    @Override
    public void putStack(ItemStack p_75215_1_) {
        super.putStack(p_75215_1_);
    }
}
