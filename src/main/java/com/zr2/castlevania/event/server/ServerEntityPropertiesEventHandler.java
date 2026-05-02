package com.zr2.castlevania.event.server;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.network.packet.PacketIEEPSync;
import com.zr2.castlevania.properties.ExtendedPlayerBible;
import com.zr2.castlevania.properties.ExtendedPlayerFire;
import com.zr2.castlevania.properties.ExtendedPlayerHeart;
import com.zr2.castlevania.properties.ExtendedPlayerStones;
import com.zr2.castlevania.properties.IEEPSyncable;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class ServerEntityPropertiesEventHandler {

    private final Map<String, Class<? extends IExtendedEntityProperties>> properties = new HashMap<>();
    private final Map<String, PropertyConstructor> propertyFactories = new HashMap<>();

    public ServerEntityPropertiesEventHandler() {
        properties.put(ExtendedPlayerHeart.EXT_PROP_NAME, ExtendedPlayerHeart.class);
        properties.put(ExtendedPlayerFire.EXT_PROP_NAME, ExtendedPlayerFire.class);
        properties.put(ExtendedPlayerBible.EXT_PROP_NAME, ExtendedPlayerBible.class);
        properties.put(ExtendedPlayerStones.EXT_PROP_NAME, ExtendedPlayerStones.class);

        registerPropertyFactory();
    }

    private void registerPropertyFactory() {
        loop: for (Map.Entry<String, Class<? extends IExtendedEntityProperties>> entry : properties.entrySet()) {
            for (Constructor<?> constructor : entry.getValue()
                .getDeclaredConstructors()) {
                Class<?>[] args = constructor.getParameterTypes();
                if (args.length == 1 && Entity.class.isAssignableFrom(args[0])) {
                    propertyFactories
                        .put(entry.getKey(), new PropertyConstructor(constructor, (Class<? extends Entity>) args[0]));
                    continue loop;
                }
            }
            throw new RuntimeException("No constructor for " + entry.getValue());
        }
    }

    @SubscribeEvent
    public void onPlayerConstruct(EntityEvent.EntityConstructing event) {
        for (Map.Entry<String, PropertyConstructor> entry : propertyFactories.entrySet()) {
            if (entry.getValue().type.isAssignableFrom(event.entity.getClass())
                && event.entity.getExtendedProperties(entry.getKey()) == null) {
                event.entity.registerExtendedProperties(
                    entry.getKey(),
                    entry.getValue()
                        .create(event.entity));
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.player.ticksExisted % 20 == 0) {
                ExtendedPlayerHeart player = (ExtendedPlayerHeart) event.player
                    .getExtendedProperties(ExtendedPlayerHeart.EXT_PROP_NAME);
                player.updateMaxHeart();
                player.replenishHeart(0);
            }

            ExtendedPlayerBible bible = (ExtendedPlayerBible) event.player
                .getExtendedProperties(ExtendedPlayerBible.EXT_PROP_NAME);
            bible.tick();
        }
    }

    @SubscribeEvent
    public void onEntityTick(LivingEvent.LivingUpdateEvent event) {
        ExtendedPlayerFire extendedPlayerFire = (ExtendedPlayerFire) event.entityLiving
            .getExtendedProperties(ExtendedPlayerFire.EXT_PROP_NAME);
        if (extendedPlayerFire.getOnFireTick() > 0) {
            extendedPlayerFire.onFireTick();
            event.entityLiving.extinguish();
            if (extendedPlayerFire.getOnFireTick() % 20 == 0) {
                event.entityLiving.attackEntityFrom(DamageSource.onFire, 3);
            }
            if (extendedPlayerFire.getOnFireTick() == 0) {
                Castlevania.getNetChannel()
                    .sendToAll(new PacketIEEPSync(extendedPlayerFire));
            }
        }
    }

    @SubscribeEvent
    public void onPlayerDeath(PlayerEvent.Clone event) {
        if (event.wasDeath && event.entityPlayer.worldObj.getGameRules()
            .getGameRuleBooleanValue("keepInventory")) {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            for (String ieepName : properties.keySet()) {
                IExtendedEntityProperties ieep = event.original.getExtendedProperties(ieepName);
                ieep.saveNBTData(nbtTagCompound);
            }
            for (String ieepName : properties.keySet()) {
                IExtendedEntityProperties ieep = event.entityPlayer.getExtendedProperties(ieepName);
                ieep.loadNBTData(nbtTagCompound);
            }
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!event.world.isRemote) {
            for (String key : properties.keySet()) {
                IExtendedEntityProperties ieep = event.entity.getExtendedProperties(key);
                if (ieep instanceof IEEPSyncable) {
                    Castlevania.getNetChannel()
                        .sendToAll(new PacketIEEPSync((IEEPSyncable) ieep));
                }
            }
        }
    }

    private static class PropertyConstructor {

        private final Constructor<?> constructor;
        public final Class<? extends Entity> type;

        private PropertyConstructor(Constructor<?> constructor, Class<? extends Entity> type) {
            this.constructor = constructor;
            this.type = type;
        }

        public IExtendedEntityProperties create(Entity entity) {
            try {
                return (IExtendedEntityProperties) constructor.newInstance(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

}
