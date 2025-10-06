# Lesson 01: Debugging Custom Leaf Decay

**Date**: October 2025
**Minecraft Version**: 1.21.9
**Issue**: Custom leaves decaying even when adjacent to custom tree blocks

## The Problem

After implementing custom tree blocks and custom leaf blocks, leaves were immediately decaying even when directly adjacent to their corresponding tree trunks. The expected behavior was for leaves to persist when within 6 blocks of a log, similar to vanilla Minecraft.

## Previous Attempts

A previous commit attempted to fix this by adding custom tree blocks to the `minecraft:logs` tag, but this alone was insufficient.

## The Debugging Journey

### Attempt 1: PillarBlock Architecture Refactor
**Hypothesis**: Custom tree blocks need to extend `PillarBlock` to be recognized as logs.

**Implementation**:
- Refactored `SpecialTreeBlock` from `extends BlockWithEntity` to `extends PillarBlock implements BlockEntityProvider`
- Added `AXIS` property handling
- Updated all blockstate files to handle axis variations (x/y/z)
- Updated block models from `cube_all` to `cube_column` format

**Result**: ✗ Leaves still decayed

### Attempt 2: Custom Leaf Blocks
**Hypothesis**: Custom leaves are needed to explicitly recognize custom tree blocks.

**Implementation**:
- Created `SpecialLeavesBlock extends LeavesBlock`
- Registered 10 custom leaf block types in `ModBlocks.java`
- Created blockstate and model files for all leaf variants
- Updated `BiomePairFeature` to use custom leaves during worldgen

**Result**: ✗ Leaves still decayed

### Attempt 3: Manual DISTANCE Property Calculation
**Hypothesis**: Leaves need proper `DISTANCE` property set during world generation.

**Implementation**:
```java
private int calculateDistanceToTrunk(BlockPos leafPos, BlockPos basePos, int trunkHeight) {
    int minDistance = Integer.MAX_VALUE;
    for (int y = 0; y < trunkHeight; y++) {
        BlockPos trunkPos = basePos.up(y);
        int distance = Math.abs(leafPos.getX() - trunkPos.getX()) +
                      Math.abs(leafPos.getY() - trunkPos.getY()) +
                      Math.abs(leafPos.getZ() - trunkPos.getZ());
        minDistance = Math.min(minDistance, distance);
    }
    return minDistance;
}
```

Applied during leaf placement:
```java
BlockState leafStateWithDistance = foliageState
    .with(LeavesBlock.DISTANCE, Math.min(leafDistance, 6))
    .with(LeavesBlock.PERSISTENT, false);
```

**Result**: ✗ Leaves still decayed

### Attempt 4: Override Distance Calculation Method
**Hypothesis**: Need to override decay logic to recognize custom trees.

**Implementation**:
Attempted to override `getDistanceFromLog()` in `SpecialLeavesBlock` to check for `instanceof SpecialTreeBlock`.

**Result**: ✗ Compilation error - method doesn't exist in Minecraft 1.21.9 API

## The Solution

### Root Cause Discovery
Through documentation research, discovered that **custom leaves MUST be added to the `minecraft:leaves` tag** for vanilla's decay system to recognize them. We had added custom trees to `minecraft:logs` but never added leaves to `minecraft:leaves`.

### Final Implementation

**Part 1: Add custom leaves to minecraft:leaves tag**

Created `/src/main/resources/data/minecraft/tags/blocks/leaves.json`:
```json
{
  "replace": false,
  "values": [
    "symbioticsurvival:fig_leaves",
    "symbioticsurvival:yucca_leaves",
    "symbioticsurvival:acacia_variant_leaves",
    "symbioticsurvival:conifer_variant_leaves",
    "symbioticsurvival:milkweed_leaves",
    "symbioticsurvival:mangrove_variant_leaves",
    "symbioticsurvival:glowing_mushroom_leaves",
    "symbioticsurvival:flowering_birch_leaves",
    "symbioticsurvival:enhanced_cherry_leaves",
    "symbioticsurvival:arctic_willow_leaves"
  ]
}
```

**Part 2: Schedule block tick after placement**

In `BiomePairFeature.java`:
```java
world.setBlockState(leafPos, leafStateWithDistance, 3);
world.scheduleBlockTick(leafPos, leafStateWithDistance.getBlock(), 1);
```

**Part 3: Simplified SpecialLeavesBlock**

Removed the attempted `getDistanceFromLog()` override and kept the class minimal, letting vanilla decay mechanics handle everything.

**Result**: ✓ **Success!** Leaves now persist properly and decay correctly when tree is removed.

## Key Lessons Learned

### 1. Block Tags Are Critical
Both `minecraft:logs` and `minecraft:leaves` tags are required for the vanilla decay system to work with custom blocks. **Missing either tag will cause decay issues.**

### 2. Work With Vanilla Systems
Don't try to override complex vanilla mechanics. Instead:
- Use block tags to integrate with existing systems
- Let vanilla code handle the logic when possible
- Override methods only when absolutely necessary

### 3. Minecraft 1.21.9 API Changes
Several APIs changed in this version:
- `LeavesBlock` constructor now requires `float particleSpawnChance` parameter
- New required methods: `getCodec()`, `spawnLeafParticle()`
- Removed methods: `getDistanceFromLog()` no longer exists
- `BlockRenderLayerMap` API not available
- `validateTicker` method signature changed

### 4. Systematic Debugging Process
When debugging complex issues:
1. Start with the simplest solution
2. Make one change at a time
3. Test thoroughly after each change
4. Research vanilla mechanics before reimplementing them
5. Step back and research when stuck

### 5. Architecture Decisions Matter
The combination of `extends PillarBlock` with `implements BlockEntityProvider` gave us:
- Vanilla-like directional log behavior
- Custom block entity functionality
- Compatibility with vanilla decay systems

## Code References

- `SpecialTreeBlock.java:17-25` - PillarBlock + BlockEntityProvider implementation
- `SpecialLeavesBlock.java:17-35` - Minimal custom leaves implementation
- `BiomePairFeature.java:208-219` - Leaf placement with DISTANCE and block tick
- `BiomePairFeature.java:308-321` - Distance calculation helper
- `src/main/resources/data/minecraft/tags/blocks/leaves.json` - **Critical tag file**
- `src/main/resources/data/minecraft/tags/blocks/logs.json` - Tree blocks tag

## Takeaway

**The biggest lesson**: Minecraft modding works best when you integrate with vanilla systems (tags, properties, behaviors) rather than trying to recreate them from scratch. Always check which block tags are required for the mechanics you're implementing.
