package com.zr2.castlevania.event.server;

import java.util.UUID;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.zr2.castlevania.Castlevania;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class ServerHealthUpgradeEventHandler {

    private static final UUID ATTR_UUID = UUID.fromString("b3c8867b-88d4-4635-9765-a5aa93573a1f"); // Thanks DuckDuckGo

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        int i = (int) calculateUpgrade(event.player);
        boolean updateAmount = true;

        if (event.phase == TickEvent.Phase.END && event.player.ticksExisted % 10 == 0) {
            BaseAttributeMap attributeMap = event.player.getAttributeMap();
            if (i > 0) {
                for (Object obj : attributeMap.getAllAttributes()) {
                    ModifiableAttributeInstance instance = (ModifiableAttributeInstance) obj;
                    AttributeModifier modifier = instance.getModifier(ATTR_UUID);
                    if (modifier != null) {
                        if (modifier.getAmount() == i) {
                            updateAmount = false;
                        }
                        break;
                    }
                }
                if (updateAmount) {
                    Multimap multimap = HashMultimap.create();
                    multimap.put(
                        SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(),
                        new AttributeModifier(ATTR_UUID, "Health Upgrade", i, 0));
                    attributeMap.removeAttributeModifiers(multimap);
                    event.player.getAttributeMap()
                        .applyAttributeModifiers(multimap);
                }
            } else {
                Multimap multimap = HashMultimap.create();
                multimap.put(
                    SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(),
                    new AttributeModifier(ATTR_UUID, "Health Upgrade", i, 0));
                attributeMap.removeAttributeModifiers(multimap);
            }
        }
    }

    private float calculateUpgrade(EntityPlayer player) {
        float i = 0;
        for (ItemStack itemStack : player.inventory.mainInventory) {
            if (itemStack != null && itemStack.getItem() == Castlevania.HEALTH_UPGRADE) {
                i += 2 * itemStack.stackSize;
            }
        }
        return i;
    }

}
