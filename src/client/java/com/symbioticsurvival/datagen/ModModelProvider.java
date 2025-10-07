package com.symbioticsurvival.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.block.SpecialLeavesBlock;
import com.symbioticsurvival.registry.ModBlocks;
import com.symbioticsurvival.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * Data generation provider for block models, blockstates, and item models.
 * Generates all visual asset JSON files programmatically.
 */
public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator gen) {
        // Tree blocks - simple cube_all pattern
        // These use a single texture for all 6 faces
        gen.registerSimpleCubeAll(ModBlocks.FIG_TREE);
        gen.registerSimpleCubeAll(ModBlocks.YUCCA_PLANT);
        gen.registerSimpleCubeAll(ModBlocks.ACACIA_VARIANT);
        gen.registerSimpleCubeAll(ModBlocks.CONIFER_VARIANT);
        gen.registerSimpleCubeAll(ModBlocks.MILKWEED);
        gen.registerSimpleCubeAll(ModBlocks.MANGROVE_VARIANT);
        gen.registerSimpleCubeAll(ModBlocks.GLOWING_MUSHROOM);
        gen.registerSimpleCubeAll(ModBlocks.FLOWERING_BIRCH);
        gen.registerSimpleCubeAll(ModBlocks.ENHANCED_CHERRY);
        gen.registerSimpleCubeAll(ModBlocks.ARCTIC_WILLOW);

        // Leaves - need custom handling for FRUIT_STATE property
        // These have 3 states: no fruit (0), growing fruit (1), ripe fruit (2)
        registerLeavesWithFruit(gen, ModBlocks.FIG_LEAVES);
        registerLeavesWithFruit(gen, ModBlocks.YUCCA_LEAVES);
        registerLeavesWithFruit(gen, ModBlocks.ACACIA_VARIANT_LEAVES);
        registerLeavesWithFruit(gen, ModBlocks.CONIFER_VARIANT_LEAVES);
        registerLeavesWithFruit(gen, ModBlocks.MILKWEED_LEAVES);
        registerLeavesWithFruit(gen, ModBlocks.MANGROVE_VARIANT_LEAVES);
        registerLeavesWithFruit(gen, ModBlocks.GLOWING_MUSHROOM_LEAVES);
        registerLeavesWithFruit(gen, ModBlocks.FLOWERING_BIRCH_LEAVES);
        registerLeavesWithFruit(gen, ModBlocks.ENHANCED_CHERRY_LEAVES);
        registerLeavesWithFruit(gen, ModBlocks.ARCTIC_WILLOW_LEAVES);

        // Pollinator nests - simple cube_all pattern
        gen.registerSimpleCubeAll(ModBlocks.FIG_WASP_NEST);
        gen.registerSimpleCubeAll(ModBlocks.YUCCA_COCOON);
        gen.registerSimpleCubeAll(ModBlocks.MASON_WASP_NEST);
        gen.registerSimpleCubeAll(ModBlocks.SAWFLY_COCOON);
        gen.registerSimpleCubeAll(ModBlocks.MONARCH_CHRYSALIS);
        gen.registerSimpleCubeAll(ModBlocks.MANGROVE_BEE_HIVE);
        gen.registerSimpleCubeAll(ModBlocks.FUNGUS_GNAT_NEST);
        gen.registerSimpleCubeAll(ModBlocks.BIRCH_BEE_HIVE);
        gen.registerSimpleCubeAll(ModBlocks.ORCHARD_BEE_NEST);
        gen.registerSimpleCubeAll(ModBlocks.BUMBLEBEE_NEST);

        // Saplings - Intentionally skipped from datagen (manual JSON files used)
        //
        // REASON: In Minecraft 1.21.4, Mojang made BlockStateModelGenerator.CrossType private,
        // breaking all public cross-model registration methods (registerTintableCrossBlockState, etc.)
        //
        // SOLUTION: All sapling blockstate and model JSON files are manually maintained at:
        //   - Blockstates: src/main/resources/assets/symbioticsurvival/blockstates/*_sapling.json
        //   - Models: src/main/resources/assets/symbioticsurvival/models/block/*_sapling.json
        //
        // ALTERNATIVES CONSIDERED:
        //   1. Use Models.TINTED_CROSS.upload() with direct collector access (complex, verbose)
        //   2. Wait for Fabric API to provide public wrapper methods (may happen in future updates)
        //   3. Current approach: Manual JSON (simple, works perfectly, minimal maintenance needed)
        //
        // This is intentional and not a bug. Revisit if Fabric API adds public cross-model helpers.

        SymbioticSurvival.LOGGER.info("Generated block models and blockstates for " + SymbioticSurvival.MOD_ID);
    }

    /**
     * Custom method to register leaves with fruit states using multipart blockstates.
     * Generates:
     * - Base leaves model with proper texture
     * - Growing fruit overlay model (cross pattern)
     * - Ripe fruit overlay model (cross pattern)
     * - Multipart blockstate that conditionally applies overlays based on fruit_state
     */
    private void registerLeavesWithFruit(BlockStateModelGenerator gen, Block leaves) {
        String blockName = Registries.BLOCK.getId(leaves).getPath();

        // Map leaves block names to their tree texture names
        String treeName = switch (blockName) {
            case "fig_leaves" -> "fig_tree";
            case "yucca_leaves" -> "yucca_plant";
            case "acacia_variant_leaves" -> "acacia_variant";
            case "conifer_variant_leaves" -> "conifer_variant";
            case "milkweed_leaves" -> "milkweed";
            case "mangrove_variant_leaves" -> "mangrove_variant";
            case "glowing_mushroom_leaves" -> "glowing_mushroom";
            case "flowering_birch_leaves" -> "flowering_birch";
            case "enhanced_cherry_leaves" -> "enhanced_cherry";
            case "arctic_willow_leaves" -> "arctic_willow";
            default -> blockName.replace("_leaves", "");
        };

        // 1. Generate base leaves model using minecraft:block/leaves parent
        Identifier baseModelId = Identifier.of(SymbioticSurvival.MOD_ID, "block/" + blockName);
        Identifier baseTexture = Identifier.of(SymbioticSurvival.MOD_ID, "block/" + treeName + "_0");

        Model leavesModel = new Model(
            Optional.of(Identifier.ofVanilla("block/leaves")),
            Optional.empty(),
            TextureKey.ALL
        );
        leavesModel.upload(baseModelId, TextureMap.all(baseTexture), gen.modelCollector);

        // 2. Generate growing fruit overlay model
        Identifier growingModelId = Identifier.of(SymbioticSurvival.MOD_ID, "block/" + blockName + "_growing_overlay");
        Identifier growingTexture = Identifier.of(SymbioticSurvival.MOD_ID, "block/fruit_growing_overlay");

        Model crossModel = new Model(
            Optional.of(Identifier.ofVanilla("block/cross")),
            Optional.empty(),
            TextureKey.CROSS
        );
        crossModel.upload(growingModelId, TextureMap.cross(growingTexture), gen.modelCollector);

        // 3. Generate ripe fruit overlay model
        Identifier ripeModelId = Identifier.of(SymbioticSurvival.MOD_ID, "block/" + blockName + "_ripe_overlay");
        Identifier ripeTexture = Identifier.of(SymbioticSurvival.MOD_ID, "block/fruit_ripe_overlay");
        crossModel.upload(ripeModelId, TextureMap.cross(ripeTexture), gen.modelCollector);

        // 4. Generate multipart blockstate
        gen.blockStateCollector.accept(new MultipartBlockStateSupplier(leaves, baseModelId, growingModelId, ripeModelId));

        // 5. Generate item model using flat 2D texture
        Identifier itemTexture = Identifier.of(SymbioticSurvival.MOD_ID, "item/" + blockName);
        Models.GENERATED.upload(
            ModelIds.getItemModelId(leaves.asItem()),
            TextureMap.layer0(itemTexture),
            gen.modelCollector
        );
    }

    /**
     * Custom BlockStateSupplier for multipart blockstates with fruit overlays.
     */
    private static class MultipartBlockStateSupplier implements BlockStateSupplier {
        private final Block block;
        private final Identifier baseModel;
        private final Identifier growingModel;
        private final Identifier ripeModel;

        public MultipartBlockStateSupplier(Block block, Identifier baseModel, Identifier growingModel, Identifier ripeModel) {
            this.block = block;
            this.baseModel = baseModel;
            this.growingModel = growingModel;
            this.ripeModel = ripeModel;
        }

        @Override
        public Block getBlock() {
            return block;
        }

        @Override
        public JsonObject get() {
            JsonObject root = new JsonObject();
            JsonArray multipart = new JsonArray();

            // Part 1: Always apply base leaves model
            JsonObject basePart = new JsonObject();
            JsonObject baseApply = new JsonObject();
            baseApply.addProperty("model", baseModel.toString());
            basePart.add("apply", baseApply);
            multipart.add(basePart);

            // Part 2: Apply growing overlay when fruit_state = 1
            JsonObject growingPart = new JsonObject();
            JsonObject growingWhen = new JsonObject();
            growingWhen.addProperty("fruit_state", "1");
            JsonObject growingApply = new JsonObject();
            growingApply.addProperty("model", growingModel.toString());
            growingPart.add("when", growingWhen);
            growingPart.add("apply", growingApply);
            multipart.add(growingPart);

            // Part 3: Apply ripe overlay when fruit_state = 2
            JsonObject ripePart = new JsonObject();
            JsonObject ripeWhen = new JsonObject();
            ripeWhen.addProperty("fruit_state", "2");
            JsonObject ripeApply = new JsonObject();
            ripeApply.addProperty("model", ripeModel.toString());
            ripePart.add("when", ripeWhen);
            ripePart.add("apply", ripeApply);
            multipart.add(ripePart);

            root.add("multipart", multipart);
            return root;
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator gen) {
        // Fruit items - simple generated items with texture
        gen.register(ModItems.FIG, Models.GENERATED);
        gen.register(ModItems.YUCCA_POD, Models.GENERATED);
        gen.register(ModItems.ACACIA_SEED_POD, Models.GENERATED);
        gen.register(ModItems.PINE_CONE, Models.GENERATED);
        gen.register(ModItems.MILKWEED_POD, Models.GENERATED);
        gen.register(ModItems.MANGROVE_FRUIT, Models.GENERATED);
        gen.register(ModItems.GLOWING_SPORE, Models.GENERATED);
        gen.register(ModItems.BIRCH_CATKIN, Models.GENERATED);
        gen.register(ModItems.CHERRY, Models.GENERATED);
        gen.register(ModItems.WILLOW_CATKIN, Models.GENERATED);

        // Spawn eggs - simple generated items with texture
        gen.register(ModItems.HONEYGUIDE_SPAWN_EGG, Models.GENERATED);
        gen.register(ModItems.FIG_WASP_SPAWN_EGG, Models.GENERATED);
        gen.register(ModItems.YUCCA_MOTH_SPAWN_EGG, Models.GENERATED);
        gen.register(ModItems.MONARCH_BUTTERFLY_SPAWN_EGG, Models.GENERATED);
        gen.register(ModItems.MASON_WASP_SPAWN_EGG, Models.GENERATED);
        gen.register(ModItems.SAWFLY_SPAWN_EGG, Models.GENERATED);
        gen.register(ModItems.MANGROVE_POLLINATOR_SPAWN_EGG, Models.GENERATED);
        gen.register(ModItems.FUNGUS_GNAT_SPAWN_EGG, Models.GENERATED);
        gen.register(ModItems.BIRCH_POLLINATOR_SPAWN_EGG, Models.GENERATED);
        gen.register(ModItems.ORCHARD_BEE_SPAWN_EGG, Models.GENERATED);
        gen.register(ModItems.BUMBLEBEE_SPAWN_EGG, Models.GENERATED);

        // Note: Block items (trees, nests, leaves, saplings) are automatically
        // generated from their block models by registerParentedItemModel() calls above
        // and by the block state generator methods

        SymbioticSurvival.LOGGER.info("Generated item models for " + SymbioticSurvival.MOD_ID);
    }
}
