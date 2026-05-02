package com.zr2.castlevania.block;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.tile.TileBlockCandle;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

public abstract class AbstractBlockCandle extends BlockTorch implements ITileEntityProvider {


    protected AbstractBlockCandle(String name) {
        this.setBlockUnbreakable();
        this.setBlockName(name);
        this.setCreativeTab(Castlevania.CASTLEVANIA_TAB);
        this.setHardness(1);
        this.setBlockTextureName(Castlevania.MODID + ":" + name);
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
        if (isToggled(world, x, y, z)) {
            burnout(world, x, y, z);
            if (!world.isRemote) {
                EntityItem item = new EntityItem(world);
                item.setEntityItemStack(this.getLoot());
                item.setPosition(x, y, z);
                world.spawnEntityInWorld(item);
            }
        }
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        TileBlockCandle candle = new TileBlockCandle();
        candle.burn();
        return candle;
    }

    protected abstract ItemStack getLoot();

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if (isToggled(world, x, y, z)) {
            super.randomDisplayTick(world, x, y, z, random);
        }
    }

    protected boolean isToggled(World world, int x, int y, int z) {
        TileBlockCandle candle = (TileBlockCandle) world.getTileEntity(x, y, z);
        return candle.isBruning();
    }

    protected void burnout(World world, int x, int y, int z) {
        TileBlockCandle candle = (TileBlockCandle) world.getTileEntity(x, y, z);
        candle.burnout();
    }

}
