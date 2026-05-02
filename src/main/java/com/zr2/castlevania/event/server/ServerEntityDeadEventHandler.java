package com.zr2.castlevania.event.server;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.item.ItemStone;
import com.zr2.castlevania.properties.ExtendedPlayerStones;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;

public class ServerEntityDeadEventHandler {

    @SubscribeEvent
    public void onEntityDeath(LivingDropsEvent event) {
        if (!event.entityLiving.worldObj.isRemote) {
            if(event.entityLiving instanceof IMob) {
                if (Math.random() <= 0.25) {
                    addDrop(event, new ItemStack(Castlevania.BIG_HEART));
                } else if (Math.random() > 0.25) {
                    addDrop(event, new ItemStack(Castlevania.SMALL_HEART));
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerDeath(PlayerDropsEvent event) {
        for(ItemStone itemStone : ItemStone.STONES) {
            ExtendedPlayerStones playerStones = (ExtendedPlayerStones) event.entityPlayer.getExtendedProperties(ExtendedPlayerStones.EXT_PROP_NAME);
            if(playerStones.isActive(itemStone)) {
                addDrop(event, new ItemStack(itemStone));
            }
        }
    }

    private void addDrop(LivingDropsEvent event, ItemStack drop) {
        EntityItem entityItem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ);
        entityItem.setEntityItemStack(drop);
        event.drops.add(entityItem);
    }

}
