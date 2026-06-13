package com.zr2.castlevania.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.World;

import com.zr2.castlevania.item.ItemWhip;

public class EntityWhipHook extends EntityArrow {

    public float radiusSq = Float.NaN;

    public EntityWhipHook(World p_i1753_1_) {
        super(p_i1753_1_);
        this.canBePickedUp = 21;
        this.setDamage(0);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!(this.shootingEntity instanceof EntityPlayer) || this.shootingEntity.getDistanceSqToEntity(this) > 1024
            || ((EntityPlayer) this.shootingEntity).getCurrentEquippedItem() == null
            || !(((EntityPlayer) this.shootingEntity).getCurrentEquippedItem()
                .getItem() instanceof ItemWhip)) {
            this.setDead();
            return;
        }
        EntityPlayer shootingPlayer = (EntityPlayer) this.shootingEntity;
        if (Float.isNaN(this.radiusSq) && inGround()) {
            this.radiusSq = (float) this.getDistanceSqToEntity(shootingPlayer);
        }
        shootingPlayer.fallDistance = 0;
    }

    public boolean inGround() {
        return this.prevPosX == this.posX && this.prevPosY == this.posY && this.prevPosZ == this.posZ;
    }
}
