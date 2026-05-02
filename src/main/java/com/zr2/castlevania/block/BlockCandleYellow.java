package com.zr2.castlevania.block;

import net.minecraft.item.ItemStack;

import com.zr2.castlevania.Castlevania;

public class BlockCandleYellow extends AbstractBlockCandle {

    public BlockCandleYellow() {
        super("candle_yellow");
    }

    @Override
    protected ItemStack getLoot() {
        return new ItemStack(Castlevania.COIN);
    }
}
