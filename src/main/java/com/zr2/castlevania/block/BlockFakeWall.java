package com.zr2.castlevania.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import com.zr2.castlevania.tile.TileBlockFakeWall;

public class BlockFakeWall extends Block implements ITileEntityProvider {

    private static final AxisAlignedBB EMPTY_AABB = AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);

    public BlockFakeWall() {
        super(Material.glass);
        this.setBlockName("fake_wall");
        this.setBlockUnbreakable();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int v0, float v1, float v2,
        float v3) {
        world.markBlockForUpdate(x, y, z);
        return false;
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
        TileBlockFakeWall tileBlockFakeWall = (TileBlockFakeWall) world.getTileEntity(x, y, z);
        world.setBlock(x, y, z, tileBlockFakeWall.getBlockDisguise());
        world.setBlockMetadataWithNotify(x, y, z, tileBlockFakeWall.getMetadata(), 0);
        world.func_147480_a(x, y, z, true);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_,
        int p_149668_4_) {
        return EMPTY_AABB;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileBlockFakeWall();
    }

    @Override
    public int getRenderType() {
        return 22;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getLightOpacity() {
        return 0;
    }

    @Override
    protected void dropBlockAsItem(World p_149642_1_, int p_149642_2_, int p_149642_3_, int p_149642_4_,
        ItemStack p_149642_5_) {}
}
