package com.zr2.castlevania.properties;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.config.ModConfig;
import com.zr2.castlevania.network.packet.PacketIEEPSync;

public class ExtendedPlayerHeart implements IEEPSyncable {

    public static final String EXT_PROP_NAME = "ExtendedPlayerHeart";
    // datawatcher id unused in current implementation

    private final EntityPlayer player;
    private int maxHeart = 50, currentHeart = 0;
    // applied upgrades (these are applied by using the upgrade items)
    private int appliedHeartUpgrades = 0;
    private int appliedHealthUpgrades = 0;

    public ExtendedPlayerHeart(EntityPlayer player) {
        this.player = player;
        this.updateMaxHeart();
    }

    @Override
    public void saveNBTData(NBTTagCompound nbtTagCompound) {
        NBTTagCompound properties = new NBTTagCompound();
        properties.setInteger("CurrentHeart", this.getCurrentHeart());
        properties.setInteger("AppliedHeartUpgrades", this.appliedHeartUpgrades);
        properties.setInteger("AppliedHealthUpgrades", this.appliedHealthUpgrades);
        nbtTagCompound.setTag(EXT_PROP_NAME, properties);
    }

    @Override
    public void loadNBTData(NBTTagCompound nbtTagCompound) {
        NBTTagCompound properties = (NBTTagCompound) nbtTagCompound.getTag(EXT_PROP_NAME);
        if (properties != null) {
            currentHeart = properties.getInteger("CurrentHeart");
            appliedHeartUpgrades = properties.getInteger("AppliedHeartUpgrades");
            appliedHealthUpgrades = properties.getInteger("AppliedHealthUpgrades");
        }
        this.updateMaxHeart();
    }

    @Override
    public void init(Entity entity, World world) {}

    public int getMaxHeart() {
        return this.maxHeart;
    }

    public int getCurrentHeart() {
        return currentHeart;
    }

    public void setCurrentHeart(int i) {
        currentHeart = i;
        Castlevania.getNetChannel()
            .sendToAll(new PacketIEEPSync(this));
    }

    public void replenishHeart(int amount) {
        this.setCurrentHeart(Math.min(this.getCurrentHeart() + amount, this.getMaxHeart()));
    }

    public boolean isHeartFull() {
        return this.getCurrentHeart() >= this.getMaxHeart();
    }

    public int getAppliedHeartUpgrades() {
        return appliedHeartUpgrades;
    }

    public int getAppliedHealthUpgrades() {
        return appliedHealthUpgrades;
    }

    public boolean applyHeartUpgrade(int count) {
        if (count <= 0) return false;
        if (!ModConfig.enableInfiniteHeartLimit && ModConfig.enableUpgradeCap
            && this.appliedHeartUpgrades >= ModConfig.heartUpgradeCap) return false;
        int allowed = count;
        if (!ModConfig.enableInfiniteHeartLimit && ModConfig.enableUpgradeCap)
            allowed = Math.min(allowed, ModConfig.heartUpgradeCap - this.appliedHeartUpgrades);
        this.appliedHeartUpgrades += allowed;
        this.updateMaxHeart();
        Castlevania.getNetChannel()
            .sendToAll(new PacketIEEPSync(this));
        return allowed > 0;
    }

    public boolean applyHealthUpgrade(int count) {
        if (count <= 0) return false;
        if (ModConfig.enableUpgradeCap && this.appliedHealthUpgrades >= ModConfig.healthUpgradeCap) return false;
        int allowed = count;
        if (ModConfig.enableUpgradeCap)
            allowed = Math.min(allowed, ModConfig.healthUpgradeCap - this.appliedHealthUpgrades);
        this.appliedHealthUpgrades += allowed;
        Castlevania.getNetChannel()
            .sendToAll(new PacketIEEPSync(this));
        return allowed > 0;
    }

    public void updateMaxHeart() {
        this.maxHeart = 50;
        // applied upgrades add 10 each
        this.maxHeart += 10 * this.appliedHeartUpgrades;
    }

    @Override
    public Entity getIEEPOwner() {
        return player;
    }

    @Override
    public String id() {
        return EXT_PROP_NAME;
    }
}
