package com.zr2.castlevania.event.server;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.properties.ExtendedPlayerBible;
import com.zr2.castlevania.properties.ExtendedPlayerHeart;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ServerAmenadielEventHandler {

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == Side.SERVER && event.phase == TickEvent.Phase.END) {
            healItem(isPlayerUsing(event.player, Castlevania.BIBLE));
            healItem(isPlayerUsing(event.player, Castlevania.STOPWATCH));
            if (isPlayerUsing(event.player, Castlevania.BIBLE) != null) {
                AxisAlignedBB aabb = event.player.boundingBox.expand(1.8, 0, 1.8);
                for (Object o : event.player.worldObj.getEntitiesWithinAABBExcludingEntity(event.player, aabb)) {
                    Entity entity = (Entity) o;
                    entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(event.player, event.player), 6);
                    if (entity instanceof EntityLivingBase) {
                        ((EntityLivingBase) entity).knockBack(event.player, 0.5F, 0.5, 0.5);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerHurt(LivingHurtEvent event) {
        if (event.entity instanceof EntityPlayer && isPlayerUsing((EntityPlayer) event.entity, Castlevania.BIBLE) != null) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerUse(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
            ItemStack item = event.entityPlayer.getCurrentEquippedItem();
            if (item != null) {
                if (item.getItem() == Castlevania.BIBLE && consumeHeart(event.entityPlayer, 3)) {
                    item.setItemDamage(1);
                    ExtendedPlayerBible bible = (ExtendedPlayerBible) event.entityPlayer.getExtendedProperties(ExtendedPlayerBible.EXT_PROP_NAME);
                    bible.useBible();
                } else if (item.getItem() == Castlevania.STOPWATCH && ServerTickEventHandler.TICK_RATE <= 0 && consumeHeart(event.entityPlayer, 5)) {
                    ServerTickEventHandler.TICK_RATE = 100;
                }
            }
        }
    }

    private ItemStack isPlayerUsing(EntityPlayer player, Item item) {
        for (ItemStack itemStack : player.inventory.mainInventory) {
            if (itemStack != null && itemStack.getItem() == item && itemStack.getItemDamage() > 0) {
                return itemStack;
            }
        }
        return null;
    }

    private void healItem(ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.getItemDamage() < itemStack.getMaxDamage()) {
                itemStack.setItemDamage(itemStack.getItemDamage() + 1);
            } else {
                itemStack.setItemDamage(0);
            }
        }
    }

    private static boolean consumeHeart(EntityPlayer player, int i) {
        if (!player.worldObj.isRemote) {
            ExtendedPlayerHeart playerHeart = (ExtendedPlayerHeart) player.getExtendedProperties(ExtendedPlayerHeart.EXT_PROP_NAME);
            if (i > playerHeart.getCurrentHeart()) {
                return false;
            }
            playerHeart.setCurrentHeart(playerHeart.getCurrentHeart() - i);
            return true;
        }
        return false;
    }

}
