package com.symbioticsurvival.mixin;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.data.FlowerPollinationData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Modifies flower breaking to check pollination status and drop seeds accordingly.
 */
@Mixin(Block.class)
public class FlowerBlockMixin {

    /**
     * Hook into flower breaking to drop seeds based on pollination status
     */
    @Inject(method = "afterBreak", at = @At("TAIL"))
    private void onFlowerBreak(World world, PlayerEntity player, BlockPos pos, BlockState state,
                               BlockEntity blockEntity, ItemStack tool, CallbackInfo ci) {
        // Only apply to flowers
        if (!(state.getBlock() instanceof FlowerBlock)) return;

        // Only process on server
        if (world.isClient()) return;

        // Only apply if config enabled
        if (!SymbioticSurvival.CONFIG.beePollination.enableFlowerPollination) return;

        ServerWorld serverWorld = (ServerWorld) world;
        FlowerPollinationData data = FlowerPollinationData.get(serverWorld);

        // Check if flower was pollinated
        boolean wasPollinated = data.isPollinated(pos, serverWorld.getTime());

        if (SymbioticSurvival.CONFIG.debug.enableDebugLogging) {
            SymbioticSurvival.LOGGER.debug(
                "Flower at {} broken. Pollinated: {}",
                pos,
                wasPollinated
            );
        }

        // Drop seeds if pollinated
        if (wasPollinated) {
            // Drop 1-2 seeds
            int seedCount = 1 + serverWorld.random.nextInt(2);
            for (int i = 0; i < seedCount; i++) {
                FlowerBlock.dropStack(serverWorld, pos, new ItemStack(Items.WHEAT_SEEDS));
            }
        }

        // Remove flower from tracking
        data.removeFlower(pos);
    }
}
