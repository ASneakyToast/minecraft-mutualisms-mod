package com.symbioticsurvival.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.symbioticsurvival.block.entity.PollinatorNestBlockEntity;
import com.symbioticsurvival.registry.ModBlockEntities;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Pollinator nest that spawns pollinators and links to trees.
 */
public class PollinatorNestBlock extends BlockWithEntity {

    public static final MapCodec<PollinatorNestBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(AbstractBlock.createSettingsCodec())
            .apply(instance, settings -> new PollinatorNestBlock(settings, "unknown", false))
    );

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    private final String biomeType;
    private final boolean isDefensive;

    public PollinatorNestBlock(Settings settings, String biomeType, boolean isDefensive) {
        super(settings);
        this.biomeType = biomeType;
        this.isDefensive = isDefensive;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PollinatorNestBlockEntity(pos, state, biomeType);
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
        return validateTicker(
            type,
            ModBlockEntities.POLLINATOR_NEST,
            PollinatorNestBlockEntity::tick
        );
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient()) {
            BlockEntity be = world.getBlockEntity(pos);

            if (be instanceof PollinatorNestBlockEntity nest) {
                // Notify linked tree
                nest.onNestDestroyed();

                // TODO: Spawn angry pollinators if defensive (when entities are implemented)
                // if (isDefensive) {
                //     spawnDefensivePollinators(world, pos, player);
                // }

                // TODO: Notify existing pollinators (when entities are implemented)
                // notifyPollinators(world, pos);
            }
        }

        return super.onBreak(world, pos, state, player);
    }

    public String getBiomeType() {
        return biomeType;
    }

    public boolean isDefensive() {
        return isDefensive;
    }
}
