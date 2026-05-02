package com.zr2.castlevania.render.tile;

import com.zr2.castlevania.tile.TileBlockFakeWall;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class RenderTileFakeWall extends TileEntitySpecialRenderer {

    private final RenderBlocks renderBlocks = new RenderBlocks();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float v3) {
        TileBlockFakeWall tileBlockFakeWall = (TileBlockFakeWall) tileEntity;
        World var10 = tileEntity.getWorldObj();
        Block var11 = tileBlockFakeWall.getBlockDisguise();
        int var12 = MathHelper.floor_double(x);
        int var13 = MathHelper.floor_double(y);
        int var14 = MathHelper.floor_double(z);
        if (var11 != null && var11 != var10.getBlock(var12, var13, var14)) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
            this.bindTexture(TextureMap.locationBlocksTexture);
            GL11.glDisable(2896);
            Tessellator var15;
            if (var11 instanceof BlockAnvil) {
                this.renderBlocks.blockAccess = var10;
                var15 = Tessellator.instance;
                var15.startDrawingQuads();
                var15.setTranslation((double) ((float) (-var12) - 0.5F), (double) ((float) (-var13) - 0.5F), (double) ((float) (-var14) - 0.5F));
                this.renderBlocks.renderBlockAnvilMetadata((BlockAnvil) var11, var12, var13, var14, 0);
                var15.setTranslation(0.0D, 0.0D, 0.0D);
                var15.draw();
            } else if (var11 instanceof BlockDragonEgg) {
                this.renderBlocks.blockAccess = var10;
                var15 = Tessellator.instance;
                var15.startDrawingQuads();
                var15.setTranslation((double) ((float) (-var12) - 0.5F), (double) ((float) (-var13) - 0.5F), (double) ((float) (-var14) - 0.5F));
                this.renderBlocks.renderBlockDragonEgg((BlockDragonEgg) var11, var12, var13, var14);
                var15.setTranslation(0.0D, 0.0D, 0.0D);
                var15.draw();
            } else {
                this.renderBlocks.setRenderBoundsFromBlock(var11);
                this.renderBlocks.renderBlockSandFalling(var11, var10, var12, var13, var14, 0);
            }

            GL11.glEnable(2896);
            GL11.glPopMatrix();
        }
    }


}
