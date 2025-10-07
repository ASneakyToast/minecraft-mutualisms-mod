package com.symbioticsurvival.block.entity;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.entity.pollinator.BasePollinatorEntity;
import com.symbioticsurvival.registry.ModBlockEntities;
import com.symbioticsurvival.registry.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
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
        if (world == null) return;

        activePollinators.removeIf(uuid -> world.getEntity(uuid) == null);
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
    public void writeData(net.minecraft.storage.WriteView view) {
        super.writeData(view);

        if (linkedTree != null) {
            view.putLong("LinkedTree", linkedTree.asLong());
        }

        view.putString("BiomeType", biomeType);
        view.putInt("PollinationCooldown", pollinationCooldown);

        // Save active pollinators list - store as comma-separated string
        if (!activePollinators.isEmpty()) {
            String uuidList = activePollinators.stream()
                .map(UUID::toString)
                .reduce((a, b) -> a + "," + b)
                .orElse("");
            view.putString("ActivePollinators", uuidList);
        }
    }

    @Override
    public void readData(net.minecraft.storage.ReadView view) {
        super.readData(view);

        view.getOptionalLong("LinkedTree").ifPresent(value ->
            this.linkedTree = BlockPos.fromLong(value)
        );

        this.biomeType = view.getString("BiomeType", "unknown");
        this.pollinationCooldown = view.getInt("PollinationCooldown", 0);

        // Load active pollinators list from comma-separated string
        this.activePollinators.clear();
        view.getOptionalString("ActivePollinators").ifPresent(uuidList -> {
            if (!uuidList.isEmpty()) {
                String[] uuids = uuidList.split(",");
                for (String uuidString : uuids) {
                    try {
                        this.activePollinators.add(UUID.fromString(uuidString.trim()));
                    } catch (IllegalArgumentException e) {
                        // Skip invalid UUIDs
                    }
                }
            }
        });
    }
}
