package com.zr2.castlevania.asm;

import java.util.Map;

import com.zr2.castlevania.asm.transformer.TransformerRendering;
import com.zr2.castlevania.asm.transformer.TransformerTickRate;

import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.TransformerExclusions({ "com.zr2.castlevania.asm" })
public class CastlevaniaAsm implements IFMLLoadingPlugin, IFMLCallHook {

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { TransformerTickRate.class.getName(), TransformerRendering.class.getName() };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return "com.zr2.castlevania.asm.CastlevaniaAsm";
    }

    @Override
    public void injectData(Map<String, Object> map) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public Void call() throws Exception {
        return null;
    }
}
