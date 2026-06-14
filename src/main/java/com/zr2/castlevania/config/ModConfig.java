package com.zr2.castlevania.config;

import net.minecraftforge.common.config.Configuration;

import com.zr2.castlevania.Castlevania;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ModConfig {

    public static boolean enableUpgradeCap = false;
    public static int heartUpgradeCap = 5;
    public static int healthUpgradeCap = 5;
    public static boolean infiniteHeartsInSurvival = false;
    public static boolean enableInfiniteHeartLimit = false;

    public static void load(FMLPreInitializationEvent event) {
        Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
        try {
            cfg.load();
            enableUpgradeCap = cfg.getBoolean(
                "enableUpgradeCap",
                "upgrades",
                false,
                "Enable upgrade caps that limit number of applied upgrades");
            heartUpgradeCap = cfg.getInt(
                "heartUpgradeCap",
                "upgrades",
                5,
                0,
                100,
                "Maximum number of applied heart upgrades per player");
            healthUpgradeCap = cfg.getInt(
                "healthUpgradeCap",
                "upgrades",
                5,
                0,
                100,
                "Maximum number of applied health upgrades per player");
            infiniteHeartsInSurvival = cfg.getBoolean(
                "infiniteHeartsInSurvival",
                "hearts",
                false,
                "If true, survival players do not consume hearts when using abilities/items that would normally cost hearts");
            enableInfiniteHeartLimit = cfg.getBoolean(
                "enableInfiniteHeartLimit",
                "hearts",
                false,
                "If true, players can have infinite max heart limit (no cap on upgrades). Renders as 'infinite / current'");
        } catch (Exception ex) {
            Castlevania.LOGGER.error("Failed to load Castlevania config", ex);
        } finally {
            if (cfg.hasChanged()) cfg.save();
        }
    }

}
