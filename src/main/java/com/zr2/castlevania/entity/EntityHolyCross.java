package com.zr2.castlevania.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityHolyCross extends EntityThrowable {

    private EntityLivingBase shootingEntity = null;
    private int canBePickedUp;
    private boolean isReturning = false;
    private int stopTick = 5;

    public EntityHolyCross(World p_i1753_1_) {
        super(p_i1753_1_);
    }

    public EntityHolyCross(World p_i1756_1_, EntityLivingBase shootingEntity) {
        super(p_i1756_1_, shootingEntity);
        this.setPosition(shootingEntity.posX, shootingEntity.posY + shootingEntity.getEyeHeight(), shootingEntity.posZ);
        Vec3 vector = shootingEntity.getLookVec();
        this.motionX = vector.xCoord;
        this.motionY = vector.yCoord;
        this.motionZ = vector.zCoord;
        this.shootingEntity = shootingEntity;
        if (shootingEntity instanceof EntityPlayer) {
            this.canBePickedUp = ((EntityPlayer) shootingEntity).capabilities.isCreativeMode ? 2 : 1;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        if (this.shootingEntity != null) {
            double distance = this.shootingEntity.getDistanceSqToEntity(this);
            if (isReturning) {
                Vec3 vector = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
                vector = vector.subtract(
                    Vec3.createVectorHelper(
                        this.shootingEntity.posX,
                        this.shootingEntity.posY + this.shootingEntity.getEyeHeight(),
                        this.shootingEntity.posZ));
                vector = vector.normalize();
                if (stopTick > 0) {
                    this.motionX = 0;
                    this.motionY = 0;
                    this.motionZ = 0;
                    stopTick--;
                } else if (distance >= 36) {
                    this.motionX = vector.xCoord * 0.125;
                    this.motionY = vector.yCoord * 0.125;
                    this.motionZ = vector.zCoord * 0.125;
                } else {
                    this.motionX = vector.xCoord * 0.5;
                    this.motionY = vector.yCoord * 0.5;
                    this.motionZ = vector.zCoord * 0.5;
                }
                if (distance < 1) {
                    this.setDead();
                    // this.onCollideWithPlayer((EntityPlayer) this.shootingEntity);
                }
            } else if (distance > 64) {
                isReturning = true;
            }
        } else if (!worldObj.isRemote) {
            this.setDead();
        }
    }

    @Override
    protected void onImpact(MovingObjectPosition movingObjectPosition) {
        if (movingObjectPosition.entityHit != null && movingObjectPosition.entityHit != this.shootingEntity) {
            DamageSource damageSource = new EntityDamageSourceIndirect("cross", this, this.shootingEntity);
            movingObjectPosition.entityHit.attackEntityFrom(damageSource, 8);
        }
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer player) {
        // if (!this.worldObj.isRemote && this.isReturning && this.shootingEntity == player) {
        // boolean var2 = this.canBePickedUp == 1 || this.canBePickedUp == 2 && player.capabilities.isCreativeMode;
        // if (this.canBePickedUp == 1 && !player.inventory.addItemStackToInventory(new
        // ItemStack(Castlevania.HOLY_CROSS))) {
        // var2 = false;
        // }
        //
        // if (var2) {
        // this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        // player.onItemPickup(this, 1);
        // this.setDead();
        // }
        // }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
        super.readEntityFromNBT(nbtTagCompound);
        this.canBePickedUp = nbtTagCompound.getInteger("pickup");
        this.isReturning = nbtTagCompound.getBoolean("returning");
        this.stopTick = nbtTagCompound.getInteger("stoptick");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbtTagCompound) {
        super.writeEntityToNBT(nbtTagCompound);
        nbtTagCompound.setInteger("pickup", this.canBePickedUp);
        nbtTagCompound.setBoolean("returning", this.isReturning);
        nbtTagCompound.setInteger("stoptick", this.stopTick);
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }
}
