package com.symbioticsurvival.block.entity;

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
    public void writeData(net.minecraft.storage.WriteView view) {
        super.writeData(view);

        if (linkedNest != null) {
            view.putLong("LinkedNest", linkedNest.asLong());
        }

        view.putString("BiomeType", biomeType);
        view.putBoolean("CanBePollinated", canBePollinated);
    }

    @Override
    public void readData(net.minecraft.storage.ReadView view) {
        super.readData(view);

        view.getOptionalLong("LinkedNest").ifPresent(value ->
            this.linkedNest = BlockPos.fromLong(value)
        );

        this.biomeType = view.getString("BiomeType", "unknown");
        this.canBePollinated = view.getBoolean("CanBePollinated", false);
    }
}
