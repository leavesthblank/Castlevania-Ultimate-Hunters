package com.zr2.castlevania.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.zr2.castlevania.item.ItemWhip;

public class EntityWhipHook extends EntityArrow {

    public float radiusSq = Float.NaN;
    public double taut;

    public EntityWhipHook(World p_i1753_1_) {
        super(p_i1753_1_);
        this.canBePickedUp = 21;
        this.setDamage(0);
    }

    public EntityWhipHook(EntityPlayer player) {
        this(player.worldObj);
        this.shootingEntity = player;
        this.posX = player.posX;
        this.posY = player.posY;
        this.posZ = player.posZ;
        Vec3 vector = player.getLookVec()
            .normalize();
        this.motionX = vector.xCoord * 10;
        this.motionY = vector.yCoord * 10;
        this.motionZ = vector.zCoord * 10;
        for (Object e : player.worldObj.loadedEntityList) {
            if (e instanceof EntityWhipHook && ((EntityWhipHook) e).shootingEntity == player) {
                ((EntityWhipHook) e).setDead();
            }
        }
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
