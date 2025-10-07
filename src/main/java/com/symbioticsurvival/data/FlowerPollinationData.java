package com.symbioticsurvival.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.symbioticsurvival.SymbioticSurvival;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Stores flower pollination data in world save data using PersistentState.
 * This is the CORRECT approach - vanilla flowers cannot have BlockEntities.
 * Now properly integrated with Minecraft's PersistentState system for disk persistence.
 */
public class FlowerPollinationData extends PersistentState {

    private final Map<BlockPos, PollinationInfo> flowers;

    public static class PollinationInfo {
        public long pollinationTime;
        public UUID pollinatorUUID;

        public PollinationInfo(long time, UUID uuid) {
            this.pollinationTime = time;
            this.pollinatorUUID = uuid;
        }

        public PollinationInfo() {
            this(0, null);
        }

        // Codec for PollinationInfo serialization
        // Uses Minecraft's built-in UUID codec which properly handles null values
        public static final Codec<PollinationInfo> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                Codec.LONG.fieldOf("pollinationTime").forGetter(info -> info.pollinationTime),
                Uuids.CODEC.optionalFieldOf("pollinatorUUID")
                    .forGetter(info -> Optional.ofNullable(info.pollinatorUUID))
            ).apply(instance, (time, uuidOpt) -> new PollinationInfo(time, uuidOpt.orElse(null)))
        );
    }

    // Codec for the entire FlowerPollinationData
    private static final Codec<FlowerPollinationData> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.unboundedMap(BlockPos.CODEC, PollinationInfo.CODEC)
                .fieldOf("flowers")
                .forGetter(data -> data.flowers)
        ).apply(instance, FlowerPollinationData::new)
    );

    // PersistentStateType for registration
    private static final PersistentStateType<FlowerPollinationData> TYPE =
        new PersistentStateType<>(
            SymbioticSurvival.MOD_ID,        // Mod ID identifier
            FlowerPollinationData::new,      // Constructor for new instances
            CODEC,                            // Codec for deserialization
            null                              // DataFixTypes (null for custom data)
        );

    // Constructor for new instances
    public FlowerPollinationData() {
        this.flowers = new HashMap<>();
    }

    // Constructor for loading from codec
    private FlowerPollinationData(Map<BlockPos, PollinationInfo> flowers) {
        this.flowers = new HashMap<>(flowers);
    }

    /**
     * Mark a flower as pollinated
     */
    public void markPollinated(BlockPos pos, long time, UUID pollinator) {
        flowers.put(pos.toImmutable(), new PollinationInfo(time, pollinator));
        markDirty();
    }

    /**
     * Check if flower is pollinated and pollination is still valid
     */
    public boolean isPollinated(BlockPos pos, long currentTime) {
        PollinationInfo info = flowers.get(pos);
        if (info == null) return false;

        // Expires after 24 MC days (24000 ticks)
        return (currentTime - info.pollinationTime) < 24000;
    }

    /**
     * Remove flower data (when broken)
     */
    public void removeFlower(BlockPos pos) {
        flowers.remove(pos);
        markDirty();
    }

    /**
     * Get pollination info for debugging
     */
    public PollinationInfo getInfo(BlockPos pos) {
        return flowers.get(pos);
    }

    /**
     * Cleanup old pollination data (run periodically)
     */
    public void cleanup(long currentTime) {
        boolean removed = flowers.entrySet().removeIf(entry ->
            (currentTime - entry.getValue().pollinationTime) > 48000 // 2 days
        );
        if (removed) {
            markDirty();
        }
    }

    /**
     * Get or create the FlowerPollinationData for a world using PersistentState API.
     * This properly integrates with Minecraft's world save/load system.
     */
    public static FlowerPollinationData get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(TYPE);
    }
}
