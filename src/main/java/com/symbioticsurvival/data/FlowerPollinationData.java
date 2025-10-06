package com.symbioticsurvival.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Stores flower pollination data in world save data.
 * This is the CORRECT approach - vanilla flowers cannot have BlockEntities.
 *
 * TODO: Integrate with PersistentState API for 1.21.9
 */
public class FlowerPollinationData {

    private final Map<BlockPos, PollinationInfo> flowers = new HashMap<>();

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
    }

    public FlowerPollinationData() {
    }

    /**
     * Mark a flower as pollinated
     */
    public void markPollinated(BlockPos pos, long time, UUID pollinator) {
        flowers.put(pos.toImmutable(), new PollinationInfo(time, pollinator));
        // TODO: markDirty() when integrated with PersistentState
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
        // TODO: markDirty() when integrated with PersistentState
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
        flowers.entrySet().removeIf(entry ->
            (currentTime - entry.getValue().pollinationTime) > 48000 // 2 days
        );
        // TODO: markDirty() when integrated with PersistentState
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList list = new NbtList();

        for (Map.Entry<BlockPos, PollinationInfo> entry : flowers.entrySet()) {
            NbtCompound flowerNbt = new NbtCompound();
            flowerNbt.putLong("Pos", entry.getKey().asLong());
            flowerNbt.putLong("Time", entry.getValue().pollinationTime);

            if (entry.getValue().pollinatorUUID != null) {
                flowerNbt.putString("Pollinator", entry.getValue().pollinatorUUID.toString());
            }

            list.add(flowerNbt);
        }

        nbt.put("Flowers", list);
        return nbt;
    }

    public static FlowerPollinationData fromNbt(NbtCompound nbt) {
        FlowerPollinationData data = new FlowerPollinationData();
        NbtList list = nbt.getList("Flowers").orElse(new NbtList());

        for (int i = 0; i < list.size(); i++) {
            NbtCompound flowerNbt = list.getCompound(i).orElse(new NbtCompound());
            BlockPos pos = BlockPos.fromLong(flowerNbt.getLong("Pos").orElse(0L));
            long time = flowerNbt.getLong("Time").orElse(0L);
            UUID uuid = flowerNbt.contains("Pollinator") ?
                UUID.fromString(flowerNbt.getString("Pollinator").orElse("")) : null;

            data.flowers.put(pos, new PollinationInfo(time, uuid));
        }

        return data;
    }

    // Temporary storage per world (TODO: integrate with PersistentState)
    private static final Map<ServerWorld, FlowerPollinationData> WORLD_DATA = new HashMap<>();

    /**
     * Get or create the FlowerPollinationData for a world
     * TODO: Integrate with PersistentState API for proper world saving
     */
    public static FlowerPollinationData get(ServerWorld world) {
        return WORLD_DATA.computeIfAbsent(world, w -> new FlowerPollinationData());
    }
}
