package com.symbioticsurvival.block.entity;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.block.PollinatorNestBlock;
import com.symbioticsurvival.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Stores tree-nest linkage and manages pollination state.
 */
public class SpecialTreeBlockEntity extends BlockEntity {

    private BlockPos linkedNest;
    private String biomeType;
    private boolean canBePollinated = false;
    private boolean hasAttemptedLinking = false;

    public SpecialTreeBlockEntity(BlockPos pos, BlockState state, String biomeType) {
        super(ModBlockEntities.SPECIAL_TREE, pos, state);
        this.biomeType = biomeType;
    }

    // Default constructor for registration
    public SpecialTreeBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, "unknown");
    }

    /**
     * Ticker method
     * Optimized to run every 20 ticks (1 second) instead of every tick for better performance
     */
    public static void tick(World world, BlockPos pos, BlockState state, SpecialTreeBlockEntity blockEntity) {
        if (world.isClient()) return;

        // Only tick every 20 game ticks (1 second) to reduce overhead
        if (world.getTime() % 20 != 0) return;

        // Auto-link to nearby nest on first tick (if not already linked)
        if (!blockEntity.hasAttemptedLinking && blockEntity.linkedNest == null) {
            blockEntity.attemptNestLinking(world, pos);
            blockEntity.hasAttemptedLinking = true;
            blockEntity.markDirty();
        }

        // Verify nest still exists periodically (every 5 seconds = 100 ticks)
        if (world.getTime() % 100 == 0) {
            blockEntity.validateNestLink();
        }
    }

    /**
     * Link this tree to a pollinator nest
     */
    public void linkToNest(BlockPos nestPos) {
        this.linkedNest = nestPos;
        this.canBePollinated = true;
        markDirty();
    }

    /**
     * Link this tree to a pollinator nest with biome type
     */
    public void linkToNest(BlockPos nestPos, String biomeType) {
        this.linkedNest = nestPos;
        this.biomeType = biomeType;
        this.canBePollinated = true;
        markDirty();
    }

    /**
     * Check if tree has a valid nest link
     */
    public boolean hasLinkedNest() {
        if (linkedNest == null) return false;

        // Verify nest still exists
        if (world != null) {
            BlockState nestState = world.getBlockState(linkedNest);
            return nestState.getBlock() instanceof PollinatorNestBlock;
        }

        return false;
    }

    /**
     * Called when linked nest is destroyed
     */
    public void onNestDestroyed() {
        this.canBePollinated = false;
        markDirty();
    }

    /**
     * Validate that the nest link is still valid
     */
    private void validateNestLink() {
        if (linkedNest != null && world != null) {
            BlockState nestState = world.getBlockState(linkedNest);

            if (!(nestState.getBlock() instanceof PollinatorNestBlock)) {
                // Nest was destroyed
                onNestDestroyed();
            }
        }
    }

    /**
     * Attempt to find and link to a nearby nest
     */
    private void attemptNestLinking(World world, BlockPos treePos) {
        int searchRadius = 16; // Search within 16 blocks
        BlockPos closestNest = null;
        double closestDistance = Double.MAX_VALUE;

        // Search in a cube around the tree
        for (int x = -searchRadius; x <= searchRadius; x++) {
            for (int y = -searchRadius; y <= searchRadius; y++) {
                for (int z = -searchRadius; z <= searchRadius; z++) {
                    BlockPos checkPos = treePos.add(x, y, z);
                    BlockState state = world.getBlockState(checkPos);

                    // Check if this is a pollinator nest block
                    if (state.getBlock() instanceof PollinatorNestBlock nestBlock) {
                        // Optionally check biome type match
                        if (biomeType.equals("unknown") || biomeType.equals(nestBlock.getBiomeType())) {
                            double distance = treePos.getSquaredDistance(checkPos);
                            if (distance < closestDistance) {
                                closestDistance = distance;
                                closestNest = checkPos;
                            }
                        }
                    }
                }
            }
        }

        // Link to the closest nest found
        if (closestNest != null) {
            this.linkedNest = closestNest;
            this.canBePollinated = true;

            // Also link the nest back to this tree
            BlockEntity nestEntity = world.getBlockEntity(closestNest);
            if (nestEntity instanceof PollinatorNestBlockEntity nest) {
                nest.linkToTree(treePos);
            }

            if (SymbioticSurvival.CONFIG.debug.enableDebugLogging) {
                SymbioticSurvival.LOGGER.info(
                    "Tree at {} auto-linked to nest at {}, distance={}",
                    treePos, closestNest, Math.sqrt(closestDistance)
                );
            }
        } else {
            if (SymbioticSurvival.CONFIG.debug.enableDebugLogging) {
                SymbioticSurvival.LOGGER.warn(
                    "Tree at {} could not find a nest within {} blocks",
                    treePos, searchRadius
                );
            }
        }
    }

    public BlockPos getLinkedNest() {
        return linkedNest;
    }

    public String getBiomeType() {
        return biomeType;
    }

    public boolean canBePollinated() {
        return canBePollinated;
    }

    @Override
    protected void writeNbt(net.minecraft.nbt.NbtCompound nbt, net.minecraft.registry.RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);

        if (linkedNest != null) {
            nbt.putLong("LinkedNest", linkedNest.asLong());
        }

        nbt.putString("BiomeType", biomeType);
        nbt.putBoolean("CanBePollinated", canBePollinated);
        nbt.putBoolean("HasAttemptedLinking", hasAttemptedLinking);
    }

    @Override
    protected void readNbt(net.minecraft.nbt.NbtCompound nbt, net.minecraft.registry.RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);

        if (nbt.contains("LinkedNest")) {
            this.linkedNest = BlockPos.fromLong(nbt.getLong("LinkedNest"));
        }

        if (nbt.contains("BiomeType")) {
            this.biomeType = nbt.getString("BiomeType");
        } else {
            this.biomeType = "unknown";
        }

        if (nbt.contains("CanBePollinated")) {
            this.canBePollinated = nbt.getBoolean("CanBePollinated");
        }

        if (nbt.contains("HasAttemptedLinking")) {
            this.hasAttemptedLinking = nbt.getBoolean("HasAttemptedLinking");
        }
    }
}
