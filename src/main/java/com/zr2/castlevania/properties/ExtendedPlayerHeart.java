package com.zr2.castlevania.properties;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.network.packet.PacketIEEPSync;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ExtendedPlayerHeart implements IEEPSyncable {

    public static final String EXT_PROP_NAME = "ExtendedPlayerHeart";
    public static final int DATAWATCHER_ID_CURRENT_HEART = 28;

    private final EntityPlayer player;
    private int maxHeart = 50, currentHeart = 0;

    public ExtendedPlayerHeart(EntityPlayer player) {
        this.player = player;
        this.updateMaxHeart();
    }

    @Override
    public void saveNBTData(NBTTagCompound nbtTagCompound) {
        NBTTagCompound properties = new NBTTagCompound();
        properties.setInteger("CurrentHeart", this.getCurrentHeart());
        nbtTagCompound.setTag(EXT_PROP_NAME, properties);
    }

    @Override
    public void loadNBTData(NBTTagCompound nbtTagCompound) {
        NBTTagCompound properties = (NBTTagCompound) nbtTagCompound.getTag(EXT_PROP_NAME);
        currentHeart = properties.getInteger("CurrentHeart");
        this.updateMaxHeart();
    }

    @Override
    public void init(Entity entity, World world) {
    }

    public int getMaxHeart() {
        return this.maxHeart;
    }

    public int getCurrentHeart() {
        return currentHeart;
    }

    public void setCurrentHeart(int i) {
        currentHeart = i;
        Castlevania.getNetChannel().sendToAll(new PacketIEEPSync(this));
    }

    public void replenishHeart(int amount) {
        this.setCurrentHeart(Math.min(this.getCurrentHeart() + amount, this.getMaxHeart()));
    }

    public boolean isHeartFull() {
        return this.getCurrentHeart() >= this.getMaxHeart();
    }

    public void updateMaxHeart() {
        this.maxHeart = 50;
        if (player.inventory != null) {
            for (ItemStack itemStack : player.inventory.mainInventory) {
                if (itemStack != null && itemStack.getItem() == Castlevania.HEART_UPGRADE) {
                    this.maxHeart += 10 * itemStack.stackSize;
                }
            }
        }
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
