package com.zr2.castlevania.item;

import com.google.common.collect.Multimap;
import com.yyon.zr2.grapplinghook.items.GrappleBow;
import com.zr2.castlevania.Castlevania;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;

public class ItemWhip extends GrappleBow {

    private final float damage;

    public ItemWhip(String name, float damage) {
        super();
        this.setTextureName(Castlevania.MODID + ":whip_" + name);
        this.setCreativeTab(Castlevania.CASTLEVANIA_TAB);
        this.setUnlocalizedName("whip_" + name);
        this.setMaxDamage(64);
        this.setMaxStackSize(1);
        this.damage = damage;
    }

    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldRotateAroundWhenRendering() {
        return false;
    }
    @Override
    public Multimap getAttributeModifiers(ItemStack stack) {
        Multimap multimap = super.getAttributeModifiers(stack);
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Tool modifier", (double) this.damage, 0));
        return multimap;
    }
}