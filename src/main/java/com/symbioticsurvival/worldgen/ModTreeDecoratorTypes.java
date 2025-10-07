package com.symbioticsurvival.worldgen;

import com.symbioticsurvival.SymbioticSurvival;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

/**
 * Registers custom tree decorator types.
 */
public class ModTreeDecoratorTypes {

    public static final TreeDecoratorType<NestLinkingDecorator> NEST_LINKING =
        Registry.register(
            Registries.TREE_DECORATOR_TYPE,
            Identifier.of(SymbioticSurvival.MOD_ID, "nest_linking"),
            new TreeDecoratorType<>(NestLinkingDecorator.CODEC)
        );

    public static void register() {
        SymbioticSurvival.LOGGER.info("Registering tree decorator types for " + SymbioticSurvival.MOD_ID);
    }
}
