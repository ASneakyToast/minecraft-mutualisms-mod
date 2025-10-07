package com.symbioticsurvival.block.entity;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.block.SpecialTreeBlock;
import com.symbioticsurvival.entity.pollinator.BasePollinatorEntity;
import com.symbioticsurvival.registry.ModBlockEntities;
import com.symbioticsurvival.registry.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Stores nest-tree linkage and manages pollinator spawning.
 */
public class PollinatorNestBlockEntity extends BlockEntity {

    private BlockPos linkedTree;
    private String biomeType;
    private int pollinationCooldown;
    private List<UUID> activePollinators = new ArrayList<>();
    private boolean hasAttemptedLinking = false;

    public PollinatorNestBlockEntity(BlockPos pos, BlockState state, String biomeType) {
        super(ModBlockEntities.POLLINATOR_NEST, pos, state);
        this.biomeType = biomeType;
        this.pollinationCooldown = getRandomInitialCooldown();
    }

    // Default constructor
    public PollinatorNestBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, "unknown");
    }

    /**
     * Ticker method
     * Optimized to run every 20 ticks (1 second) instead of every tick for better performance
     */
    public static void tick(World world, BlockPos pos, BlockState state,
                           PollinatorNestBlockEntity blockEntity) {
        if (world.isClient()) return;

        // Only tick every 20 game ticks (1 second) to reduce overhead
        if (world.getTime() % 20 != 0) return;

        // Auto-link to nearby tree on first tick (if not already linked)
        if (!blockEntity.hasAttemptedLinking && blockEntity.linkedTree == null) {
            blockEntity.attemptTreeLinking(world, pos);
            blockEntity.hasAttemptedLinking = true;
            blockEntity.markDirty();
        }

        // Countdown to next pollination (scaled by 20 since we tick every 20 ticks)
        if (blockEntity.pollinationCooldown > 0) {
            blockEntity.pollinationCooldown -= 20;
        }

        // Spawn pollinator if it's time
        if (blockEntity.pollinationCooldown <= 0 && blockEntity.linkedTree != null) {
            blockEntity.spawnPollinator();
            blockEntity.pollinationCooldown = blockEntity.getRandomPollinationInterval();
        }

        // Cleanup dead pollinators from list (only needed once per second)
        blockEntity.cleanupPollinators();
    }

    private void spawnPollinator() {
        // Don't spawn if too many active
        if (activePollinators.size() >= 2) return;

        if (world == null || world.isClient()) return;

        // Get entity type based on biome type
        EntityType<? extends BasePollinatorEntity> entityType = getEntityTypeForBiome(biomeType);
        if (entityType == null) return;

        // Create entity with spawn reason
        BasePollinatorEntity pollinator = entityType.create(world, net.minecraft.entity.SpawnReason.NATURAL);
        if (pollinator == null) return;

        // Position near nest (with random offset)
        double offsetX = world.random.nextDouble() * 2 - 1; // -1 to 1
        double offsetZ = world.random.nextDouble() * 2 - 1;
        pollinator.refreshPositionAndAngles(
            pos.getX() + 0.5 + offsetX,
            pos.getY() + 1,
            pos.getZ() + 0.5 + offsetZ,
            world.random.nextFloat() * 360f,
            0
        );

        // Link pollinator to nest and tree
        pollinator.linkToNest(pos);
        if (linkedTree != null) {
            pollinator.linkToTree(linkedTree);
        }

        // Spawn entity
        if (world.spawnEntity(pollinator)) {
            activePollinators.add(pollinator.getUuid());
            markDirty();

            if (SymbioticSurvival.CONFIG.debug.enableDebugLogging) {
                SymbioticSurvival.LOGGER.info("Spawned {} at {} from nest at {}",
                    pollinator.getType().getTranslationKey(), pollinator.getBlockPos(), pos);
            }
        }
    }

    private EntityType<? extends BasePollinatorEntity> getEntityTypeForBiome(String biomeType) {
        return switch (biomeType) {
            case "tropical" -> ModEntities.FIG_WASP;
            case "desert" -> ModEntities.YUCCA_MOTH;
            case "savanna" -> ModEntities.MASON_WASP;
            case "taiga" -> ModEntities.SAWFLY;
            case "plains" -> ModEntities.MONARCH_BUTTERFLY;
            case "swamp" -> ModEntities.MANGROVE_POLLINATOR;
            case "mushroom" -> ModEntities.FUNGUS_GNAT;
            case "birch_forest" -> ModEntities.BIRCH_POLLINATOR;
            case "cherry_grove" -> ModEntities.ORCHARD_BEE;
            case "snowy" -> ModEntities.BUMBLEBEE;
            default -> null;
        };
    }

    private void cleanupPollinators() {
        // Note: Entity lookup by UUID was simplified in 1.21.4
        // Dead entities will be cleaned up when pollinators check in
        // This method is kept for future cleanup logic if needed
    }

    /**
     * Attempt to find and link to a nearby tree
     */
    private void attemptTreeLinking(World world, BlockPos nestPos) {
        int searchRadius = 16; // Search within 16 blocks
        BlockPos closestTree = null;
        double closestDistance = Double.MAX_VALUE;

        // Search in a cube around the nest
        for (int x = -searchRadius; x <= searchRadius; x++) {
            for (int y = -searchRadius; y <= searchRadius; y++) {
                for (int z = -searchRadius; z <= searchRadius; z++) {
                    BlockPos checkPos = nestPos.add(x, y, z);
                    BlockState state = world.getBlockState(checkPos);

                    // Check if this is a special tree block
                    if (state.getBlock() instanceof SpecialTreeBlock) {
                        double distance = nestPos.getSquaredDistance(checkPos);
                        if (distance < closestDistance) {
                            closestDistance = distance;
                            closestTree = checkPos;
                        }
                    }
                }
            }
        }

        // Link to the closest tree found
        if (closestTree != null) {
            this.linkedTree = closestTree;

            // Also link the tree back to this nest
            BlockEntity treeEntity = world.getBlockEntity(closestTree);
            if (treeEntity instanceof SpecialTreeBlockEntity tree) {
                tree.linkToNest(nestPos, biomeType);
            }

            if (SymbioticSurvival.CONFIG.debug.enableDebugLogging) {
                SymbioticSurvival.LOGGER.info(
                    "Nest at {} auto-linked to tree at {}, distance={}",
                    nestPos, closestTree, Math.sqrt(closestDistance)
                );
            }
        } else {
            if (SymbioticSurvival.CONFIG.debug.enableDebugLogging) {
                SymbioticSurvival.LOGGER.warn(
                    "Nest at {} could not find a tree within {} blocks",
                    nestPos, searchRadius
                );
            }
        }
    }

    /**
     * Link this nest to a tree
     */
    public void linkToTree(BlockPos treePos) {
        this.linkedTree = treePos;
        markDirty();
    }

    /**
     * Called when this nest is destroyed
     */
    public void onNestDestroyed() {
        if (linkedTree != null && world != null) {
            BlockEntity treeBe = world.getBlockEntity(linkedTree);

            if (treeBe instanceof SpecialTreeBlockEntity tree) {
                tree.onNestDestroyed();
            }
        }
    }

    public BlockPos getLinkedTree() {
        return linkedTree;
    }

    public String getBiomeType() {
        return biomeType;
    }

    private int getRandomPollinationInterval() {
        int min = SymbioticSurvival.CONFIG.pollination.intervalMin;
        int max = SymbioticSurvival.CONFIG.pollination.intervalMax;
        return min + (world != null ? world.random.nextInt(max - min) : (max - min) / 2);
    }

    private int getRandomInitialCooldown() {
        // Random initial cooldown between 1-3 minutes
        return 1200 + (world != null ? world.random.nextInt(2400) : 1200);
    }

    @Override
    protected void writeNbt(net.minecraft.nbt.NbtCompound nbt, net.minecraft.registry.RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);

        if (linkedTree != null) {
            nbt.putLong("LinkedTree", linkedTree.asLong());
        }

        nbt.putString("BiomeType", biomeType);
        nbt.putInt("PollinationCooldown", pollinationCooldown);
        nbt.putBoolean("HasAttemptedLinking", hasAttemptedLinking);

        // TODO: Save active pollinators list
        // Skipping for now - not essential for core functionality
    }

    @Override
    protected void readNbt(net.minecraft.nbt.NbtCompound nbt, net.minecraft.registry.RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);

        if (nbt.contains("LinkedTree")) {
            this.linkedTree = BlockPos.fromLong(nbt.getLong("LinkedTree"));
        }

        if (nbt.contains("BiomeType")) {
            this.biomeType = nbt.getString("BiomeType");
        } else {
            this.biomeType = "unknown";
        }

        if (nbt.contains("PollinationCooldown")) {
            this.pollinationCooldown = nbt.getInt("PollinationCooldown");
        }

        if (nbt.contains("HasAttemptedLinking")) {
            this.hasAttemptedLinking = nbt.getBoolean("HasAttemptedLinking");
        }

        // TODO: Load active pollinators list
        // Skipping for now - not essential for core functionality
        this.activePollinators.clear();
    }
}
