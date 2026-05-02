package com.zr2.castlevania.event;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.item.ItemStone;
import com.zr2.castlevania.properties.ExtendedPlayerStones;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;

public abstract class StoneAbilityEvent {

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ExtendedPlayerStones playerStones = (ExtendedPlayerStones) event.player
                .getExtendedProperties(ExtendedPlayerStones.EXT_PROP_NAME);
            for (AbilityStone abilityStone : AbilityStone.values()) {
                if (playerStones.isActive(abilityStone.item)) {
                    if (event.side == Side.CLIENT) {
                        this.clientTick(event.player, abilityStone);
                    } else {
                        this.serverTick(event.player, abilityStone);
                    }
                }
            }
        }
    }

    protected void clientTick(EntityPlayer player, AbilityStone stone) {}

    protected void serverTick(EntityPlayer player, AbilityStone stone) {}

    protected AxisAlignedBB isClimbing(EntityPlayer player) {
        AxisAlignedBB bb = player.boundingBox.expand(0.01, 0, 0.01);

        int i = MathHelper.floor_double(bb.minX);
        int j = MathHelper.floor_double(bb.maxX + 1.0D);
        int k = MathHelper.floor_double(bb.minY);
        int l = MathHelper.floor_double(bb.maxY + 1.0D);
        int i1 = MathHelper.floor_double(bb.minZ);
        int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
        if (bb.minX < 0.0D) {
            --i;
        }

        if (bb.minY < 0.0D) {
            --k;
        }

        if (bb.minZ < 0.0D) {
            --i1;
        }

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    Block block = player.worldObj.getBlock(k1, l1, i2);
                    if (block != null && block.getMaterial() != Material.air) {
                        AxisAlignedBB blockbb = block.getCollisionBoundingBoxFromPool(player.worldObj, k1, l1, i2);
                        if (blockbb != null && blockbb.intersectsWith(bb)) {
                            return blockbb;
                        }
                    }
                }
            }
        }

        return null;
    }

    protected List<Vec3> checkClimbingWall(EntityPlayer player) {
        List<Vec3> aabbs = new ArrayList<>();
        AxisAlignedBB bb = player.boundingBox.expand(0.01, 0, 0.01);

        int i = MathHelper.floor_double(bb.minX);
        int j = MathHelper.floor_double(bb.maxX + 1.0D);
        int k = MathHelper.floor_double(bb.minY);
        int l = MathHelper.floor_double(bb.maxY + 1.0D);
        int i1 = MathHelper.floor_double(bb.minZ);
        int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
        if (bb.minX < 0.0D) {
            --i;
        }

        if (bb.minY < 0.0D) {
            --k;
        }

        if (bb.minZ < 0.0D) {
            --i1;
        }

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    Block block = player.worldObj.getBlock(k1, l1, i2);
                    if (block != null && block.getMaterial() != Material.air) {
                        AxisAlignedBB blockbb = block.getCollisionBoundingBoxFromPool(player.worldObj, k1, l1, i2);
                        if (blockbb != null && blockbb.intersectsWith(bb)) {
                            Vec3 vector = Vec3.createVectorHelper(k1, l1, i2);
                            Vec3 playerVector = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
                            aabbs.add(playerVector.subtract(vector));
                        }
                    }
                }
            }
        }

        return aabbs;
    }

    protected void grantPotionEffect(EntityPlayer player, Potion potion, int amp) {
        player.addPotionEffect(new PotionEffect(potion.id, 20, amp, true));
    }

    public static enum AbilityStone {

        JUMP((ItemStone) Castlevania.JUMP_STONE),
        SPIDER((ItemStone) Castlevania.SPIDER_STONE),
        MERMAID((ItemStone) Castlevania.MERMAID_STONE),
        DASH((ItemStone) Castlevania.DASH_STONE),
        LEAP((ItemStone) Castlevania.LEAP_STONE),
        GRYPHON((ItemStone) Castlevania.GRYPHON_STONE);

        public final ItemStone item;

        AbilityStone(ItemStone item) {
            this.item = item;
        }
    }

}
