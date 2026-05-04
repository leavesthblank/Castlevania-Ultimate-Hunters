package com.zr2.castlevania.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.zr2.castlevania.item.ItemWhip;

public class EntityWhipHook extends EntityArrow {

    public float radiusSq = Float.NaN;
    public double taut;

    public EntityWhipHook(World p_i1753_1_) {
        super(p_i1753_1_);
        this.canBePickedUp = 21;
        this.setDamage(16);
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
        this.setDamage(16);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionY += 0.05f;//hardcoded to compensate gravity of whip hook...
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

    public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset,
                      float velocity, float inaccuracy) {
        float f = -MathHelper.sin(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
        float f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * 0.017453292F);
        float f2 = MathHelper.cos(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
        this.setThrowableHeading(f, f1, f2, velocity, inaccuracy);
        this.motionX += entityThrower.motionX;
        this.motionZ += entityThrower.motionZ;

        if (!entityThrower.onGround) {
            this.motionY += entityThrower.motionY;
        }
    }
}
