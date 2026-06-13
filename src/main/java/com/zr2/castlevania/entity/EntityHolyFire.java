package com.zr2.castlevania.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.zr2.castlevania.properties.ExtendedPlayerFire;

public class EntityHolyFire extends Entity {

    public EntityHolyFire(World world) {
        super(world);
        this.setSize(1F, 2F);
    }

    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(16, 0);
    }

    public void setDormant(int dormant) {
        this.dataWatcher.updateObject(16, dormant);
    }

    public int getDormant() {
        return this.dataWatcher.getWatchableObjectInt(16);
    }

    @Override
    public void onUpdate() {
        this.noClip = true;
        super.onUpdate();
        this.posX = (int) (this.posX - 0.5F) + 0.5F;
        this.posZ = (int) (this.posZ - 0.5F) + 0.5F;
        if (ticksExisted - this.getDormant() > 30) {
            this.setDead();
        } else if (!this.worldObj.isRemote && this.ticksExisted > this.getDormant()) {
            for (EntityLivingBase o : this.worldObj.getEntitiesWithinAABB(
                EntityLivingBase.class,
                this.boundingBox.copy()
                    .offset(this.posX - 1, this.posY, this.posZ))) {
                ExtendedPlayerFire extendedPlayerFire = (ExtendedPlayerFire) o
                    .getExtendedProperties(ExtendedPlayerFire.EXT_PROP_NAME);
                if (extendedPlayerFire.getOnFireTick() <= 0) {
                    extendedPlayerFire.setOnFireTick(60);
                    if (!(o instanceof EntityPlayer) && !o.isEntityInvulnerable()) {
                        o.attackEntityFrom(DamageSource.inFire, 6);
                        o.attackEntityFrom(DamageSource.causeIndirectMagicDamage(o, o), 6);
                    }
                }
            }
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbtTagCompound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbtTagCompound) {

    }

}
