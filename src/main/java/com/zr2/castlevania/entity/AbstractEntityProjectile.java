package com.zr2.castlevania.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class AbstractEntityProjectile extends EntityArrow {

    private int leverFilpCooldown = 0;

    public AbstractEntityProjectile(World p_i1753_1_) {
        super(p_i1753_1_);
    }

    public AbstractEntityProjectile(World p_i1756_1_, EntityLivingBase p_i1756_2_, float p_i1756_3_) {
        super(p_i1756_1_, p_i1756_2_, p_i1756_3_);
        if (p_i1756_2_ instanceof EntityPlayer && ((EntityPlayer) p_i1756_2_).capabilities.isCreativeMode) {
            this.canBePickedUp = 2;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (leverFilpCooldown > 0) {
            --leverFilpCooldown;
        }
        if (!worldObj.isRemote && leverFilpCooldown <= 0) {
            AxisAlignedBB p_72830_1_ = this.boundingBox;
            int i = MathHelper.floor_double(p_72830_1_.minX);
            int j = MathHelper.floor_double(p_72830_1_.maxX + 1.0D);
            int k = MathHelper.floor_double(p_72830_1_.minY);
            int l = MathHelper.floor_double(p_72830_1_.maxY + 1.0D);
            int i1 = MathHelper.floor_double(p_72830_1_.minZ);
            int j1 = MathHelper.floor_double(p_72830_1_.maxZ + 1.0D);

            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        Block block = this.worldObj.getBlock(k1, l1, i2);
                        if (block == Blocks.lever) {
                            int j2 = this.worldObj.getBlockMetadata(k1, l1, i2);
                            double d0 = (double) (l1 + 1);
                            if (j2 < 8) {
                                d0 = (double) (l1 + 1) - (double) j2 / 8.0D;
                            }

                            if (d0 >= p_72830_1_.minY) {
                                block.onBlockActivated(
                                    this.worldObj,
                                    k1,
                                    l1,
                                    i2,
                                    (EntityPlayer) this.shootingEntity,
                                    0,
                                    0,
                                    0,
                                    0);
                                this.leverFilpCooldown = 20;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer p_onCollideWithPlayer_1_) {
        // if (!this.worldObj.isRemote && this.inGround() && this.arrowShake <= 0) {
        // boolean var2 = this.canBePickedUp == 1 || this.canBePickedUp == 2 &&
        // p_onCollideWithPlayer_1_.capabilities.isCreativeMode;
        // if (this.canBePickedUp == 1 && !p_onCollideWithPlayer_1_.inventory.addItemStackToInventory(itemForm())) {
        // var2 = false;
        // }
        //
        // if (var2) {
        // this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        // p_onCollideWithPlayer_1_.onItemPickup(this, 1);
        // this.setDead();
        // }
        //
        // }
    }

    public boolean inGround() {
        return this.prevPosX == this.posX && this.prevPosY == this.posY && this.prevPosZ == this.posZ;
    }

    protected abstract ItemStack itemForm();

}
