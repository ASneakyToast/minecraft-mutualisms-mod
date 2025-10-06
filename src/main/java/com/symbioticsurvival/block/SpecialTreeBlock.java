package com.symbioticsurvival.block;

import com.symbioticsurvival.block.entity.SpecialTreeBlockEntity;
import com.symbioticsurvival.registry.ModBlockEntities;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for special trees that require pollination.
 * Manages fruit growth states and tree-nest linkage.
 * Extends PillarBlock for proper log behavior (directional placement, leaf support).
 */
public class SpecialTreeBlock extends PillarBlock implements BlockEntityProvider {

    // Fruit states: 0=immature, 1=pollinated, 2=mature
    public static final IntProperty FRUIT_STATE = IntProperty.of("fruit_state", 0, 2);

    private final String biomeType;

    public SpecialTreeBlock(Settings settings, String biomeType) {
        super(settings);
        this.biomeType = biomeType;
        // Set default state with both AXIS (from PillarBlock) and FRUIT_STATE
        setDefaultState(getDefaultState()
            .with(AXIS, net.minecraft.util.math.Direction.Axis.Y)
            .with(FRUIT_STATE, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        // Add both AXIS (from PillarBlock) and FRUIT_STATE
        builder.add(AXIS, FRUIT_STATE);
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

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int fruitState = state.get(FRUIT_STATE);

        if (fruitState == 0) {
            // Immature - wait for pollination
            return;
        } else if (fruitState == 1) {
            // Pollinated - chance to mature
            if (random.nextInt(5) == 0) { // 20% chance per random tick
                world.setBlockState(pos, state.with(FRUIT_STATE, 2));
            }
        }
        // State 2 (mature) - ready for harvest
    }

    /**
     * Called by pollinator to mark tree as pollinated
     */
    public void pollinate(ServerWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);

        if (state.get(FRUIT_STATE) == 0) {
            world.setBlockState(pos, state.with(FRUIT_STATE, 1));

            // Spawn particles
            spawnPollinationParticles(world, pos);
        }
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

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient() && state.get(FRUIT_STATE) == 2) {
            // Drop fruit based on biome type
            ItemStack fruit = getFruitForBiome();
            if (fruit != null) {
                dropStack(world, pos, fruit);
            }
        }

        return super.onBreak(world, pos, state, player);
    }

    /**
     * Get the appropriate fruit item for this tree's biome type.
     * Returns vanilla items that thematically match each biome.
     */
    private ItemStack getFruitForBiome() {
        return switch (biomeType) {
            case "tropical" -> new ItemStack(net.minecraft.item.Items.APPLE, 2);
            case "desert" -> new ItemStack(net.minecraft.item.Items.SWEET_BERRIES, 3);
            case "savanna" -> new ItemStack(net.minecraft.item.Items.APPLE, 2);
            case "taiga" -> new ItemStack(net.minecraft.item.Items.SWEET_BERRIES, 3);
            case "plains" -> new ItemStack(net.minecraft.item.Items.GLOW_BERRIES, 2);
            case "swamp" -> new ItemStack(net.minecraft.item.Items.APPLE, 2);
            case "mushroom" -> new ItemStack(net.minecraft.item.Items.BROWN_MUSHROOM, 3);
            case "birch_forest" -> new ItemStack(net.minecraft.item.Items.APPLE, 2);
            case "cherry_grove" -> new ItemStack(net.minecraft.item.Items.APPLE, 2);
            case "snowy" -> new ItemStack(net.minecraft.item.Items.SWEET_BERRIES, 2);
            default -> new ItemStack(net.minecraft.item.Items.APPLE, 1);
        };
    }

    public String getBiomeType() {
        return biomeType;
    }
}
