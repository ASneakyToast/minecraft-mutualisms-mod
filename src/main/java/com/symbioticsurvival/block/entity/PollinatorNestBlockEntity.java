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
     */
    public static void tick(World world, BlockPos pos, BlockState state,
                           PollinatorNestBlockEntity blockEntity) {
        if (world.isClient()) return;

        // Countdown to next pollination
        if (blockEntity.pollinationCooldown > 0) {
            blockEntity.pollinationCooldown--;
        }

        // Spawn pollinator if it's time
        if (blockEntity.pollinationCooldown == 0 && blockEntity.linkedTree != null) {
            blockEntity.spawnPollinator();
            blockEntity.pollinationCooldown = blockEntity.getRandomPollinationInterval();
        }

        // Cleanup dead pollinators from list
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

    // TODO: NBT methods need updating for 1.21.9+ API (method names/signatures changed)
    // The NbtCompound getter methods now return Optional<T> instead of primitive types
    /*
    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);

        if (linkedTree != null) {
            nbt.putLong("LinkedTree", linkedTree.asLong());
        }

        nbt.putString("BiomeType", biomeType);
        nbt.putInt("PollinationCooldown", pollinationCooldown);

        // Save active pollinators list as strings
        NbtList pollinatorsList = new NbtList();
        for (UUID uuid : activePollinators) {
            NbtCompound uuidNbt = new NbtCompound();
            uuidNbt.putString("UUID", uuid.toString());
            pollinatorsList.add(uuidNbt);
        }
        nbt.put("ActivePollinators", pollinatorsList);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

        if (nbt.contains("LinkedTree")) {
            this.linkedTree = BlockPos.fromLong(nbt.getLong("LinkedTree"));
        }

        this.biomeType = nbt.getString("BiomeType");
        this.pollinationCooldown = nbt.getInt("PollinationCooldown");

        // Load active pollinators list
        this.activePollinators.clear();
        if (nbt.contains("ActivePollinators", 9)) { // 9 is list type
            NbtList pollinatorsList = nbt.getList("ActivePollinators", 10); // 10 is compound type
            for (int i = 0; i < pollinatorsList.size(); i++) {
                NbtCompound uuidNbt = pollinatorsList.getCompound(i);
                if (uuidNbt != null && uuidNbt.contains("UUID", 8)) { // 8 is string type
                    try {
                        this.activePollinators.add(UUID.fromString(uuidNbt.getString("UUID")));
                    } catch (IllegalArgumentException e) {
                        // Skip invalid UUIDs
                    }
                }
            }
        }
    }
    */
}
