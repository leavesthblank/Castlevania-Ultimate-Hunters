package com.zr2.castlevania.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.zr2.castlevania.Castlevania;

public class EntityAxe extends AbstractEntityProjectile {

    public EntityAxe(World p_i1753_1_) {
        super(p_i1753_1_);
    }

    public EntityAxe(World p_i1756_1_, EntityLivingBase p_i1756_2_) {
        super(p_i1756_1_, p_i1756_2_, 0.416666666667F);
    }

    @Override
    public double getDamage() {
        return 10;
    }

    @Override
    protected ItemStack itemForm() {
        return new ItemStack(Castlevania.AXE);
    }
}
