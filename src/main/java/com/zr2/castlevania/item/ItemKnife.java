package com.zr2.castlevania.item;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.google.common.collect.Multimap;
import com.zr2.castlevania.entity.EntityKnife;

public class ItemKnife extends BasicItem {

    private final float damage;

    public ItemKnife(float damage) {
        super("short_knife");
        this.damage = damage;
        this.setMaxStackSize(1);
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

    @Override
    public Multimap getAttributeModifiers(ItemStack stack) {
        Multimap multimap = super.getAttributeModifiers(stack);
        multimap.put(
            SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(),
            new AttributeModifier(field_111210_e, "Tool modifier", (double) this.damage, 0));
        return multimap;
    }
}
