package com.symbioticsurvival.block.entity;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
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

        // TODO: Pollinator spawning logic when entities are implemented
        // Would create entity based on biomeType
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

    // TODO: NBT Serialization (API changed in 1.21.9, needs research)
}
