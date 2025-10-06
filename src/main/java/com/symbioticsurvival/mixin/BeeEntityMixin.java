package com.symbioticsurvival.mixin;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.data.FlowerPollinationData;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hooks into vanilla bee behavior to track flower pollination.
 *
 * IMPORTANT: Method names must match Yarn mappings for 1.21.9
 * Verify at: https://linkie.shedaniel.me/mappings
 */
@Mixin(BeeEntity.class)
public abstract class BeeEntityMixin {

    @Shadow
    public abstract boolean hasFlower();

    @Shadow
    public abstract BlockPos getFlowerPos();

    /**
     * Inject into bee movement to detect flower visits.
     *
     * Method name verified for Yarn 1.21.9
     */
    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void onBeeTickMovement(CallbackInfo ci) {
        BeeEntity bee = (BeeEntity) (Object) this;

        // Only process on server
        if (bee.getEntityWorld().isClient()) return;

        // Only track if config enabled
        if (!SymbioticSurvival.CONFIG.beePollination.enableFlowerPollination) return;

        // Check if bee has a flower and is at the flower
        if (this.hasFlower()) {
            BlockPos flowerPos = this.getFlowerPos();

            if (flowerPos != null && bee.getBlockPos().isWithinDistance(flowerPos, 1.5)) {
                ServerWorld world = (ServerWorld) bee.getEntityWorld();
                FlowerPollinationData data = FlowerPollinationData.get(world);

                // Mark flower as pollinated
                data.markPollinated(flowerPos, world.getTime(), bee.getUuid());

                // Spawn particles if enabled
                if (SymbioticSurvival.CONFIG.beePollination.pollinationParticleEffect) {
                    world.spawnParticles(
                        net.minecraft.particle.ParticleTypes.HAPPY_VILLAGER,
                        flowerPos.getX() + 0.5,
                        flowerPos.getY() + 0.5,
                        flowerPos.getZ() + 0.5,
                        3,
                        0.2, 0.2, 0.2,
                        0.0
                    );
                }

                // Log for debugging
                if (SymbioticSurvival.CONFIG.debug.enableDebugLogging) {
                    SymbioticSurvival.LOGGER.debug(
                        "Bee {} pollinated flower at {}",
                        bee.getUuid(),
                        flowerPos
                    );
                }
            }
        }
    }
}
