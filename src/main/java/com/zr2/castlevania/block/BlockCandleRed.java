package com.zr2.castlevania.block;

import com.zr2.castlevania.Castlevania;
import net.minecraft.item.ItemStack;

public class BlockCandleRed extends AbstractBlockCandle {

    public BlockCandleRed() {
        super("candle_red");
    }

    @Override
    protected ItemStack getLoot() {
        return new ItemStack(Math.random() > 0.2 ? Castlevania.SMALL_HEART : Castlevania.BIG_HEART);
    }

}
