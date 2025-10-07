package com.symbioticsurvival;

import com.symbioticsurvival.config.SymbioticConfig;
import com.symbioticsurvival.registry.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.ModInitializer;

public class SymbioticSurvival implements ModInitializer {

    public static final String MOD_ID = "symbioticsurvival";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static SymbioticConfig CONFIG;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Symbiotic Survival...");

        // Load configuration
        AutoConfig.register(SymbioticConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(SymbioticConfig.class).getConfig();

        // Register everything in order
        // TODO: ModSounds.register();
        ModBlocks.register();
        ModPointOfInterest.register(); // POI must be registered after blocks
        ModItems.register();
        ModVanillaIntegration.register(); // Vanilla integrations (flammability, composting, fuel)
        ModItemGroups.register();
        ModBlockEntities.register();
        ModEntities.register();
        ModEntitySpawns.register();
        ModFeatures.register();
        com.symbioticsurvival.worldgen.ModTreeDecoratorTypes.register();
        com.symbioticsurvival.worldgen.ModWorldGen.initialize();
        // TODO: ModPackets.registerS2CPackets();

        LOGGER.info("Symbiotic Survival initialized successfully!");
    }
}
