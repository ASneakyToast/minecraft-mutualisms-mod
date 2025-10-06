# Mutualism Mod - "Symbiotic Survival"
## Project Requirements Document

### Executive Summary

**Project Name:** Symbiotic Survival
**Type:** Minecraft Fabric Mod
**Target Minecraft Version:** 1.21+ (latest stable)
**Primary Goal:** Educate players about mutualistic relationships through engaging gameplay mechanics

**Core Philosophy:**
- Show, don't tell - players learn through observation and interaction
- Real-world biological accuracy where possible, simplified for gameplay
- Reward understanding and preservation over exploitation
- Make "small creatures" valuable rather than annoying

### Project Vision

Create a Minecraft mod that transforms the game's ecosystem by introducing authentic mutualistic relationships. Players discover that success comes from understanding and preserving ecological partnerships rather than simple resource extraction.

### Target Audience

- **Primary:** Minecraft players ages 10-18 interested in nature/science
- **Secondary:** Educators using Minecraft for environmental education
- **Tertiary:** Adult players interested in complex ecosystem simulation

### Success Metrics

- Players can identify 3+ real-world mutualisms after playing
- 70%+ of players choose to preserve nests rather than destroy them (telemetry)
- Positive feedback from educators on educational value
- Seamless integration with vanilla Minecraft (no jarring mechanics)

---

## PHASE 1 FEATURES - "Pollinators & Guides"

### Feature 1: Honeyguide Birds

**User Story:** As a player exploring the world, I want to be led to valuable bee nests by helpful birds, so I can discover resources through natural animal behavior.

#### Functional Requirements

**FR-HG-1:** Honeyguide birds spawn naturally in Plains, Savanna, and Sunflower Plains biomes
- Spawn rate: 1 per 500 blocks (configurable)
- Spawn when player is within 64 blocks of a bee nest

**FR-HG-2:** Honeyguides exhibit leading behavior
- Bird chirps distinctively (unique sound)
- Flies in circles above player to get attention
- Leads toward nearest bee nest within 128 blocks
- Periodically stops and waits if player falls behind
- Abandons leading after 5 minutes or if player moves too far away

**FR-HG-3:** Reward system establishes mutualism
- When player breaks bee nest, honeycomb and larvae drop
- Honeyguide eats larvae from ground (items marked for bird)
- If player collects all larvae, bird "remembers" (NBT tag)
- Remembered player: bird won't lead for 20 minutes (real-time)
- If player leaves larvae: bird more likely to approach that player again

**FR-HG-4:** Villager interaction demonstrates behavior
- Beekeepers (villagers) respond to honeyguide calls
- Follow bird to nest
- Harvest honey, intentionally drop some larvae
- Creates observable AI behavior players can learn from

#### Non-Functional Requirements

**NFR-HG-1:** Performance
- Maximum 5 active honeyguides per loaded chunks area
- Bird pathfinding updates every 20 ticks (1 second)

**NFR-HG-2:** Audio
- Distinctive call audible at 32 blocks
- Different call when waiting vs. actively leading

**NFR-HG-3:** Visual
- Bird model distinct from parrots/chickens
- Animation: head turns toward target, wing flapping

---

### Feature 2: Biome-Specific Pollination Pairs

**User Story:** As a player exploring different biomes, I want to discover unique tree-pollinator relationships, so I learn that different ecosystems have specialized partnerships.

#### Functional Requirements

**FR-BP-1:** Ten biome-specific pairs generate naturally
- Each biome has one exclusive tree-pollinator pair
- Trees generate during world generation (structure feature)
- Nests generate within 20 blocks of paired tree
- Tree and nest store each other's coordinates (NBT linkage)

**FR-BP-2:** Pollination dependency system
- Trees produce immature fruit naturally
- Immature fruit remains indefinitely without pollination
- Pollinator entity travels from nest to tree (configurable interval: 2-5 minutes)
- Pollination event changes fruit state to "pollinated"
- Pollinated fruit matures over time (3-5 minutes) to harvestable state

**FR-BP-3:** Nest destruction consequences
- When nest destroyed, linked tree marked as "unpollinated" (permanent)
- Tree continues growing immature fruit but cannot be pollinated
- No regeneration system (permanent loss reinforces lesson)
- Visual indicator: immature fruit has different texture/color

**FR-BP-4:** Pollinator behavior (defensive types)
- Fig Wasp, Mason Wasp, Mangrove Bee, Birch Bee, Orchard Bee
- Attack player/mob that damages nest
- Damage: 1 heart, applies 3 seconds of Poison I
- Aggro range: 8 blocks from nest
- Return to passive after 30 seconds

**FR-BP-5:** Pollinator behavior (passive types)
- Yucca Moth, Sawfly, Monarch Butterfly, Fungus Gnat, Arctic Bumblebee
- Flee when nest is damaged
- Do not attack under any circumstances
- Despawn after 5 minutes if nest destroyed

**FR-BP-6:** Unique fruit properties per biome

| Biome | Tree | Pollinator | Fruit | Properties |
|-------|------|------------|-------|------------|
| Jungle | Fig Tree | Fig Wasp (defensive) | Figs | Food: 4 hunger, 5 saturation; Crafting: sweetener |
| Desert | Yucca Plant | Yucca Moth (passive) | Yucca Pods | Crafting: fiber (string alternative), soap |
| Savanna | Acacia Variant | Mason Wasp (defensive) | Seed Pods | Animal breeding item, brown dye |
| Taiga | Conifer Variant | Sawfly (passive) | Pine Cones | Resin extraction, fire starter |
| Plains | Milkweed | Monarch (passive) | Milkweed Pods | Fiber, fletching, spreads seeds when broken |
| Swamp | Mangrove Variant | Mangrove Bee (defensive) | Mangrove Fruit | Food, water breathing potion ingredient |
| Dark Forest | Glowing Mushroom | Fungus Gnat (passive) | Spore Pods | Light potion ingredient, spreads mushroom |
| Birch Forest | Flowering Birch | Birch Bee (defensive) | Catkins | Tea (brewing), paper alternative |
| Cherry Grove | Enhanced Cherry | Orchard Bee (mild defensive) | Cherries | Better yield/quality than vanilla |
| Snowy Taiga | Arctic Willow | Arctic Bumblebee (passive) | Willow Catkins | Medicinal (removes negative effects) |

**FR-BP-7:** Player cultivation support
- Players can plant saplings of special trees
- Must manually place nest nearby (crafting recipe)
- Nest placement within 20 blocks auto-links to tree
- Allows sustainable farming if player preserves ecosystem

#### Non-Functional Requirements

**NFR-BP-1:** Performance
- Maximum 2 active pollinators per linked tree
- Pollination check occurs every 100 ticks (5 seconds)
- Fruit maturation happens on random tick

**NFR-BP-2:** Visual clarity
- Immature fruit: dull, small
- Pollinated fruit: slightly larger, color shift
- Mature fruit: full size, vibrant color
- Clear visual progression teaches the system

**NFR-BP-3:** Balance
- Fruit valuable enough to make preservation worthwhile
- Not so rare that players resort to destruction
- Crafting recipes create sustained demand

---

### Feature 3: Bee-Flower Pollination Dependency

**User Story:** As a player trying to cultivate flowers, I want to understand that bees are essential for flower reproduction, so I learn to protect pollinators.

#### Functional Requirements

**FR-BF-1:** Flower pollination requirement
- All flowers (vanilla and modded) require bee pollination to produce seeds
- Unpollinated flowers have 0% chance to drop seeds when broken
- Pollinated flowers have 100% chance to drop 1-2 seeds

**FR-BF-2:** Bee pollination tracking
- When bee visits flower (vanilla behavior), flower marked as "pollinated" (NBT tag)
- Pollinated flowers have subtle visual change (slightly brighter, particle effect)
- Pollination status persists until flower broken or 24 hours pass (Minecraft days)

**FR-BF-3:** Natural flower spreading
- Pollinated flowers can spread to adjacent grass blocks
- Spread chance: 5% per Minecraft day
- Creates natural flower patches in bee-populated areas
- Unpollinated flowers never spread

**FR-BF-4:** Crop pollination extension (optional, configurable)
- Melons and pumpkins require bee visits to produce fruit
- Stem grows normally, but fruit generation requires pollination
- Creates incentive to maintain flower gardens near farms

#### Non-Functional Requirements

**NFR-BF-1:** Compatibility
- Must not break vanilla bee behavior
- Works with vanilla bee AI, no modifications needed
- Compatible with other mods that add flowers

**NFR-BF-2:** Performance
- Pollination check piggybacks on vanilla bee pathfinding
- No additional entity ticking overhead
- Flower spreading uses random tick (like vanilla crops)

---

## DEVELOPMENT REQUIREMENTS

### Technology Stack

**Platform:** Minecraft Java Edition 1.21+
**Mod Loader:** Fabric
**Language:** Java 17+
**Build Tool:** Gradle

**Key Libraries:**
- Fabric API
- Fabric Loader
- Geckolib (for entity animations, if needed)

### Code Standards

**Naming Conventions:**
- Classes: PascalCase (e.g., `HoneyguideEntity`)
- Methods: camelCase (e.g., `leadToNest()`)
- Constants: UPPER_SNAKE_CASE (e.g., `MAX_LEADING_DISTANCE`)

**Package Structure:**
```
src/main/java/com/symbiotic/
├── entity/
│   ├── HoneyguideEntity.java
│   ├── pollinator/
│   │   ├── BasePollinatorEntity.java
│   │   ├── DefensivePollinatorEntity.java
│   │   └── PassivePollinatorEntity.java
├── block/
│   ├── SpecialTreeBlock.java
│   ├── PollinatorNestBlock.java
├── item/
│   ├── SpecialFruitItem.java
├── worldgen/
│   ├── BiomePairFeature.java
├── config/
│   ├── SymbioticConfig.java
```

---

## CONFIGURATION & CUSTOMIZATION

### Config File Structure (TOML)

```toml
[honeyguide]
spawn_rate = 0.002  # Probability per chunk
leading_range = 128  # Blocks
memory_duration = 1200  # Ticks (1 minute)

[pollination]
pollination_interval_min = 2400  # Ticks (2 minutes)
pollination_interval_max = 6000  # Ticks (5 minutes)
fruit_maturation_time = 3600  # Ticks (3 minutes)
nest_generation_range = 20  # Blocks from tree

[bees]
enable_flower_pollination = true
enable_crop_pollination = false  # Optional feature
pollination_particle_effect = true

[balance]
defensive_wasp_damage = 2.0  # Half a heart
poison_duration = 60  # Ticks (3 seconds)
```

---

## USER EXPERIENCE REQUIREMENTS

### Discovery Flow

**First Encounter (Honeyguides):**
1. Player hears distinctive bird call
2. Sees bird circling overhead
3. Follows bird (curiosity)
4. Discovers bee nest
5. Breaks nest, notices bird eating larvae
6. (Optional) Takes all larvae, bird ignores player later
7. Learns: "Sharing rewards cooperation"

**First Encounter (Pollination Pairs):**
1. Player finds special tree with immature fruit
2. Waits, fruit doesn't mature
3. Explores nearby area
4. Finds nest, sees pollinator
5. Observes pollinator visiting tree
6. (If defensive) Might attack nest, gets stung
7. Realizes: "Nest is needed for fruit"
8. Learns preservation > exploitation

---

## CONSTRAINTS & ASSUMPTIONS

### Constraints

**Technical:**
- Must work on servers with 20+ players
- Must not cause more than 5% TPS drop
- File size under 50MB

**Compatibility:**
- Must not conflict with major tech/magic mods
- Should work with popular biome mods
- Must respect vanilla progression (no OP items)

**Scope:**
- Phase 1 only (other phases are future work)
- No custom dimensions
- No integration with non-vanilla villager types (for now)

---

## SUCCESS CRITERIA - PHASE 1

### Must Have (MVP)

- ✓ Honeyguide birds lead players to bee nests
- ✓ At least 5 biome pollination pairs functional
- ✓ Bee-flower pollination system working
- ✓ Nest destruction prevents fruit maturation
- ✓ Basic config file for customization
- ✓ No game-breaking bugs
- ✓ Performance impact < 10% TPS drop

### Should Have

- ✓ All 10 biome pairs implemented
- ✓ Villager-honeyguide interaction
- ✓ Unique crafting recipes for all fruits
- ✓ Defensive vs passive pollinator behaviors
- ✓ Visual polish (particles, sounds)

### Nice to Have

- ✓ Achievement system for discovering all pairs
- ✓ In-game guidebook (Patchouli)
- ✓ Integration with popular biome mods
- ✓ Custom advancement tree
- ✓ Statistics tracking (nests preserved vs destroyed)
