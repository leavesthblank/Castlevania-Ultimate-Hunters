package com.yyon.zr2.grapplinghook.items;

import java.util.HashMap;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.yyon.zr2.grapplinghook.CommonProxyClass;
import com.yyon.zr2.grapplinghook.GrappleMod;
import com.yyon.zr2.grapplinghook.entities.GrappleArrow;
import com.yyon.zr2.grapplinghook.network.GrappleClickMessage;

import cpw.mods.fml.common.FMLCommonHandler;

/*
 * This file is part of GrappleMod.
 * GrappleMod is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * GrappleMod is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with GrappleMod. If not, see <http://www.gnu.org/licenses/>.
 */

public class GrappleBow extends Item {

    public static HashMap<Entity, GrappleArrow> grapplearrows = new HashMap<Entity, GrappleArrow>();

    public GrappleBow() {
        super();
        maxStackSize = 1;
        setFull3D();
        setUnlocalizedName("grapplinghook");

        setCreativeTab(CreativeTabs.tabTransport);

        FMLCommonHandler.instance()
            .bus()
            .register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack) {
        return 72000;
    }

    public GrappleArrow getArrow(Entity entity, World world) {
        if (GrappleBow.grapplearrows.containsKey(entity)) {
            GrappleArrow arrow = GrappleBow.grapplearrows.get(entity);
            if (arrow != null && !arrow.isDead) {
                return arrow;
            }
        }
        return null;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        ItemStack mat = new ItemStack(Items.leather, 1);
        if (mat != null && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false)) return true;
        return super.getIsRepairable(toRepair, repair);
    }

    public void setArrow(Entity entity, ItemStack stack, GrappleArrow arrow) {
        GrappleBow.grapplearrows.put(entity, arrow);
    }

    public void dorightclick(ItemStack stack, World worldIn, EntityLivingBase entityLiving, boolean righthand) {
        if (!worldIn.isRemote) {
            GrappleArrow entityarrow = getArrow(entityLiving, worldIn);

            if (entityarrow != null) {
                int id = entityarrow.shootingEntityID;
                if (!GrappleMod.attached.contains(id)) {
                    setArrow(entityLiving, stack, null);

                    if (!entityarrow.isDead) {
                        entityarrow.removeServer();
                        return;
                    }

                    entityarrow = null;
                }
            }

            float f = 2.0F;
            if (entityarrow == null) {
                entityarrow = this.createarrow(stack, worldIn, entityLiving, righthand);
                setArrow(entityLiving, stack, entityarrow);

                worldIn.playSoundAtEntity(
                    entityLiving,
                    "random.bow",
                    1.0F,
                    1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                worldIn.spawnEntityInWorld(entityarrow);
            } else {
                GrappleMod.sendtocorrectclient(
                    new GrappleClickMessage(entityarrow.shootingEntityID, false),
                    entityarrow.shootingEntityID,
                    entityarrow.worldObj);
                GrappleMod.attached.remove(new Integer(entityarrow.shootingEntityID));
                this.setArrow(entityLiving, stack, null);
            }
        }
    }

    public GrappleArrow createarrow(ItemStack stack, World worldIn, EntityLivingBase entityLiving, boolean righthand) {
        return new GrappleArrow(worldIn, entityLiving, righthand);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World worldIn, EntityPlayer entityLiving) {
        if (!worldIn.isRemote) {
            this.dorightclick(stack, worldIn, entityLiving, true);
        }
        return stack;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z, EntityPlayer player) {
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        list.add("A basic grappling hook for swinging");
        list.add("");
        list.add(GrappleMod.proxy.getkeyname(CommonProxyClass.keys.keyBindUseItem) + " - Throw grappling hook");
        list.add(GrappleMod.proxy.getkeyname(CommonProxyClass.keys.keyBindUseItem) + " again - Release");
        list.add(
            "Double-" + GrappleMod.proxy.getkeyname(CommonProxyClass.keys.keyBindUseItem)
                + " - Release and throw again");
        list.add(
            GrappleMod.proxy.getkeyname(CommonProxyClass.keys.keyBindForward) + ", "
                + GrappleMod.proxy.getkeyname(CommonProxyClass.keys.keyBindLeft)
                + ", "
                + GrappleMod.proxy.getkeyname(CommonProxyClass.keys.keyBindBack)
                + ", "
                + GrappleMod.proxy.getkeyname(CommonProxyClass.keys.keyBindRight)
                + " - Swing");
        list.add(
            GrappleMod.proxy.getkeyname(CommonProxyClass.keys.keyBindJump) + " - Release and jump (while in midair)");
        list.add(GrappleMod.proxy.getkeyname(CommonProxyClass.keys.keyBindSneak) + " - Stop swinging");
        list.add(
            GrappleMod.proxy.getkeyname(CommonProxyClass.keys.keyBindSneak) + " + "
                + GrappleMod.proxy.getkeyname(CommonProxyClass.keys.keyBindForward)
                + " - Climb up");
        list.add(
            GrappleMod.proxy.getkeyname(CommonProxyClass.keys.keyBindSneak) + " + "
                + GrappleMod.proxy.getkeyname(CommonProxyClass.keys.keyBindBack)
                + " - Climb down");
    }
}
