package com.symbioticsurvival.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.symbioticsurvival.block.entity.SpecialTreeBlockEntity;
import com.symbioticsurvival.registry.ModBlockEntities;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Base class for special trees that require pollination.
 * Manages fruit growth states and tree-nest linkage.
 * Extends PillarBlock for proper log behavior (directional placement, leaf support).
 */
public class SpecialTreeBlock extends PillarBlock implements BlockEntityProvider {

    /**
     * Registry-based biome type lookup.
     * Maps block registry IDs to their biome types.
     * This ensures biomeType is preserved even after codec deserialization.
     */
    private static final Map<String, String> BIOME_TYPE_MAP = Map.ofEntries(
        Map.entry("symbioticsurvival:fig_tree", "tropical"),
        Map.entry("symbioticsurvival:yucca_plant", "desert"),
        Map.entry("symbioticsurvival:acacia_variant", "savanna"),
        Map.entry("symbioticsurvival:conifer_variant", "taiga"),
        Map.entry("symbioticsurvival:milkweed", "plains"),
        Map.entry("symbioticsurvival:mangrove_variant", "swamp"),
        Map.entry("symbioticsurvival:glowing_mushroom", "mushroom"),
        Map.entry("symbioticsurvival:flowering_birch", "birch_forest"),
        Map.entry("symbioticsurvival:enhanced_cherry", "cherry_grove"),
        Map.entry("symbioticsurvival:arctic_willow", "snowy")
    );

    /**
     * Codec that only serializes block settings.
     * BiomeType is determined by registry lookup in getBiomeType(), not stored in codec.
     */
    public static final MapCodec<SpecialTreeBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(AbstractBlock.createSettingsCodec())
            .apply(instance, settings -> new SpecialTreeBlock(settings, "unknown"))
    );

    // Pollination state - when true, nearby leaves can grow fruit
    public static final net.minecraft.state.property.BooleanProperty POLLINATED =
        net.minecraft.state.property.BooleanProperty.of("pollinated");

    private final String biomeType;

    public SpecialTreeBlock(Settings settings, String biomeType) {
        super(settings);
        this.biomeType = biomeType;
        // Set default state with both AXIS (from PillarBlock) and POLLINATED
        setDefaultState(getDefaultState()
            .with(AXIS, net.minecraft.util.math.Direction.Axis.Y)
            .with(POLLINATED, false));
    }

    @Override
    public MapCodec<? extends PillarBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        // Add both AXIS (from PillarBlock) and POLLINATED
        builder.add(AXIS, POLLINATED);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SpecialTreeBlockEntity(pos, state, biomeType);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            World world,
            BlockState state,
            BlockEntityType<T> type) {
        if (world.isClient()) {
            return null;
        }
        // Check if the type matches our block entity type
        if (type == ModBlockEntities.SPECIAL_TREE) {
            return (BlockEntityTicker<T>) (BlockEntityTicker<SpecialTreeBlockEntity>) SpecialTreeBlockEntity::tick;
        }
        return null;
    }

    // Removed randomTick - fruit growth now handled by leaves

    /**
     * Called by pollinator to mark tree as pollinated.
     * Triggers fruit growth on nearby leaves.
     */
    public void pollinate(ServerWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);

        if (!state.get(POLLINATED)) {
            world.setBlockState(pos, state.with(POLLINATED, true));

            // Spawn particles
            spawnPollinationParticles(world, pos);

            // Notify nearby leaves they can grow fruit
            notifyLeavesToGrowFruit(world, pos);
        }
    }

    /**
     * Notify nearby leaves that the tree is pollinated.
     * Leaves will check tree's pollination status in their randomTick.
     */
    private void notifyLeavesToGrowFruit(ServerWorld world, BlockPos treePos) {
        // Leaves will scan for the pollinated tree in their randomTick
        // No need for explicit notification - they check the tree's POLLINATED state
    }

    private void spawnPollinationParticles(ServerWorld world, BlockPos pos) {
        world.spawnParticles(
            net.minecraft.particle.ParticleTypes.HAPPY_VILLAGER,
            pos.getX() + 0.5,
            pos.getY() + 0.5,
            pos.getZ() + 0.5,
            5,
            0.3, 0.3, 0.3,
            0.0
        );
    }

    // Removed fruit dropping from trunk - now handled by leaves

    /**
     * Get the biome type for this tree block.
     * Uses registry-based lookup to ensure correctness even after codec deserialization.
     */
    public String getBiomeType() {
        // Look up biome type from registry based on block's ID
        Identifier id = Registries.BLOCK.getId(this);
        if (id != null) {
            String registryId = id.toString();
            return BIOME_TYPE_MAP.getOrDefault(registryId, "unknown");
        }
        // Fallback to instance field if not in registry (shouldn't happen)
        return biomeType;
    }
}
