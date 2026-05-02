package com.zr2.castlevania.block;

import com.zr2.castlevania.Castlevania;
import net.minecraft.item.ItemStack;

public class BlockCandleYellow extends AbstractBlockCandle {

    public BlockCandleYellow() {
        super("candle_yellow");
    }

    @Override
    protected ItemStack getLoot() {
        return new ItemStack(Castlevania.COIN);
    }
}
