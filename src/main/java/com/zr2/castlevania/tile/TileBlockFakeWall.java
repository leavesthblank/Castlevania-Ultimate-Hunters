package com.zr2.castlevania.tile;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileBlockFakeWall extends TileEntity {

    private Block blockDisguise = Blocks.air;
    private int metadata = 0;

    public Block getBlockDisguise() {
        return blockDisguise == null ? Blocks.air : blockDisguise;
    }

    public void setBlockDisguise(Block blockDisguise) {
        this.blockDisguise = blockDisguise == null ? Blocks.air : blockDisguise;
    }

    public int getMetadata() {
        return metadata;
    }

    public void setMetadata(int metadata) {
        this.metadata = metadata;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.setBlockDisguise(Block.getBlockById(nbt.getInteger("tileID")));
        this.setMetadata(nbt.getInteger("tileMetadata"));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("tileID", Block.getIdFromBlock(this.getBlockDisguise()));
        nbt.setInteger("tileMetadata", this.getMetadata());
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
        this.disguiseSelf();
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeToNBT(nbtTagCompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbtTagCompound);
    }

    public void disguiseSelf() {
        if (worldObj.isRemote) {
            worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, this.blockDisguise);
            worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, this.metadata, 0);
        }
    }

}
