package com.symbioticsurvival.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

/**
 * Custom leaves block that grows fruit when the parent tree is pollinated.
 * Supports both destructive (break) and non-destructive (shears) harvesting.
 */
public class SpecialLeavesBlock extends LeavesBlock {

    public static final MapCodec<SpecialLeavesBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(AbstractBlock.createSettingsCodec())
            .apply(instance, settings -> new SpecialLeavesBlock(settings, "unknown"))
    );

    // Fruit states: 0=none, 1=growing, 2=ripe
    public static final IntProperty FRUIT_STATE = IntProperty.of("fruit_state", 0, 2);

    private final String biomeType;

    public SpecialLeavesBlock(Settings settings, String biomeType) {
        super(settings);
        this.biomeType = biomeType;
        setDefaultState(getDefaultState()
            .with(DISTANCE, 7)
            .with(PERSISTENT, false)
            .with(WATERLOGGED, false)
            .with(FRUIT_STATE, 0));
    }

    @Override
    public MapCodec<? extends LeavesBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FRUIT_STATE);
    }

    @Override
    protected boolean hasRandomTicks(BlockState state) {
        return true; // Explicitly enable random ticking for fruit growth
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // Handle leaf decay first
        super.randomTick(state, world, pos, random);

        // Only grow fruit if leaves are still there and not decaying
        int distance = state.get(DISTANCE);
        if (distance == 7) {
            return; // Leaf is decaying, no fruit growth
        }

        int fruitState = state.get(FRUIT_STATE);
        boolean isTreePollinated = isTreePollinated(world, pos);

        // Check if parent tree is pollinated
        if (fruitState == 0 && isTreePollinated) {
            // Start growing fruit (0 → 1)
            world.setBlockState(pos, state.with(FRUIT_STATE, 1));
            com.symbioticsurvival.SymbioticSurvival.LOGGER.info(
                "Leaf at {} started growing fruit (0→1)", pos
            );
        } else if (fruitState == 1) {
            // Mature fruit (1 → 2)
            if (random.nextInt(5) == 0) { // 20% chance per random tick
                world.setBlockState(pos, state.with(FRUIT_STATE, 2));
                com.symbioticsurvival.SymbioticSurvival.LOGGER.info(
                    "Leaf at {} fruit ripened (1→2)", pos
                );
            }
        }
    }

    /**
     * Check if the parent tree is pollinated by scanning nearby for trunk blocks
     */
    private boolean isTreePollinated(World world, BlockPos leafPos) {
        int distance = leafPos.getY() > 0 ? world.getBlockState(leafPos).get(DISTANCE) : 7;

        // Scan downward first (trunks are typically below leaves)
        for (int y = 0; y <= distance + 2; y++) {
            BlockPos checkPos = leafPos.down(y);
            BlockState checkState = world.getBlockState(checkPos);

            if (checkState.getBlock() instanceof SpecialTreeBlock treeBlock) {
                // Found the tree trunk, check if it's pollinated
                if (treeBlock.getBiomeType().equals(this.biomeType)) {
                    return checkState.get(SpecialTreeBlock.POLLINATED);
                }
            }
        }

        // Also check horizontally nearby (within 3 blocks, for wide canopies)
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                if (x == 0 && z == 0) continue; // Already checked vertical above

                BlockPos checkPos = leafPos.add(x, 0, z);
                BlockState checkState = world.getBlockState(checkPos);

                if (checkState.getBlock() instanceof SpecialTreeBlock treeBlock) {
                    if (treeBlock.getBiomeType().equals(this.biomeType)) {
                        return checkState.get(SpecialTreeBlock.POLLINATED);
                    }
                }
            }
        }

        return false;
    }

    /**
     * Shears harvesting - non-destructive
     */
    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos,
                                            PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        // Check if using shears on ripe fruit
        if (stack.isOf(Items.SHEARS) && state.get(FRUIT_STATE) == 2) {
            // Drop fruit
            ItemStack fruit = getFruitForBiome();
            dropStack(world, pos, fruit);

            // Reset fruit state to none
            world.setBlockState(pos, state.with(FRUIT_STATE, 0));

            // Damage shears
            stack.damage(1, player, player.getPreferredEquipmentSlot(stack));

            return ActionResult.SUCCESS;
        }

        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    /**
     * Destructive harvesting - when leaves are broken
     */
    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient() && state.get(FRUIT_STATE) == 2) {
            // Drop fruit in addition to normal leaf drops
            ItemStack fruit = getFruitForBiome();
            dropStack(world, pos, fruit);
        }

        return super.onBreak(world, pos, state, player);
    }

    /**
     * Get the appropriate fruit item for this leaf's biome type.
     * Returns vanilla items that thematically match each biome.
     */
    private ItemStack getFruitForBiome() {
        return switch (biomeType) {
            case "tropical" -> new ItemStack(Items.APPLE, 2);
            case "desert" -> new ItemStack(Items.SWEET_BERRIES, 3);
            case "savanna" -> new ItemStack(Items.APPLE, 2);
            case "taiga" -> new ItemStack(Items.SWEET_BERRIES, 3);
            case "plains" -> new ItemStack(Items.GLOW_BERRIES, 2);
            case "swamp" -> new ItemStack(Items.APPLE, 2);
            case "mushroom" -> new ItemStack(Items.BROWN_MUSHROOM, 3);
            case "birch_forest" -> new ItemStack(Items.APPLE, 2);
            case "cherry_grove" -> new ItemStack(Items.APPLE, 2);
            case "snowy" -> new ItemStack(Items.SWEET_BERRIES, 2);
            default -> new ItemStack(Items.APPLE, 1);
        };
    }

    public String getBiomeType() {
        return biomeType;
    }

    // Note: spawnLeafParticle method was removed in Minecraft 1.21.4
    // Particle spawning can be handled via client-side code if needed
}
