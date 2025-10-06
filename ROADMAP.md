# Development Roadmap
## Symbiotic Survival Mod - Phase 1

---

## MILESTONE OVERVIEW

```
Month 1: Foundation & Core Systems
Month 2: Entity AI & Behaviors
Month 3: World Generation & Biome Pairs
Month 4: Polish, Testing & Release
```

---

## MONTH 1: FOUNDATION & CORE SYSTEMS

### Week 1: Project Setup & Infrastructure

**Goals:**
- ✓ Set up development environment
- ✓ Create project structure
- ✓ Implement basic mod scaffolding

**Tasks:**
1. **Day 1-2: Environment Setup**
   - Install IntelliJ IDEA, JDK 17, Gradle
   - Download Fabric Example Mod template
   - Set up Git repository
   - Configure build.gradle with dependencies

2. **Day 3-4: Project Structure**
   - Create package structure (`com.symbiotic.*`)
   - Set up resource directories (assets, data)
   - Create basic mod initializer
   - Test mod loads in development environment

3. **Day 5-7: Config System**
   - Implement `SymbioticConfig` class
   - Set up TOML file loading/saving
   - Create default config values
   - Add config validation logic

**Deliverables:**
- [ ] Working development environment
- [ ] Mod loads without errors
- [ ] Config file generates and loads correctly

---

### Week 2: Block System Foundation

**Goals:**
- ✓ Implement special tree blocks
- ✓ Implement pollinator nest blocks
- ✓ Create fruit state system

**Tasks:**
1. **Day 1-2: SpecialTreeBlock**
   - Create `SpecialTreeBlock` class with `FRUIT_STATE` property
   - Implement state transitions (immature → pollinated → mature)
   - Add random tick logic for fruit growth
   - Create basic textures (placeholder art)

2. **Day 3-4: SpecialTreeBlockEntity**
   - Implement BlockEntity with NBT storage
   - Add `linkedNest` field and getter/setter
   - Implement NBT write/read methods
   - Add linkage validation logic

3. **Day 5-6: PollinatorNestBlock**
   - Create `PollinatorNestBlock` class
   - Implement `PollinatorNestBlockEntity` with tree linkage
   - Add nest destruction event handling
   - Create basic nest textures

4. **Day 7: Testing**
   - Write unit tests for block state transitions
   - Test NBT persistence (save/load world)
   - Test linkage creation and validation

**Deliverables:**
- [ ] Functional tree blocks with fruit states
- [ ] Functional nest blocks with linkage
- [ ] Passing unit tests for block system

---

### Week 3: Item System & Crafting

**Goals:**
- ✓ Implement fruit items
- ✓ Create crafting recipes
- ✓ Add item properties

**Tasks:**
1. **Day 1-2: Base Fruit Items**
   - Create `BaseFruitItem` abstract class
   - Implement 10 specific fruit items (Fig, Yucca Pod, etc.)
   - Set nutrition values and properties
   - Create item textures (placeholder)

2. **Day 3-4: Crafting Recipes**
   - Design unique crafting recipes for each fruit
   - Implement recipe JSON files
   - Add shaped/shapeless recipes as appropriate
   - Test recipes in-game

3. **Day 5-6: Item Properties**
   - Add food properties (hunger, saturation)
   - Implement special effects (water breathing, status removal)
   - Add item tooltips
   - Test all item functionalities

4. **Day 7: Integration**
   - Integrate fruits with tree blocks (correct drops)
   - Test harvest → craft workflow
   - Balance nutrition values

**Deliverables:**
- [ ] 10 functional fruit items
- [ ] All crafting recipes working
- [ ] Items drop correctly from trees

---

### Week 4: Flower Pollination System

**Goals:**
- ✓ Implement bee-flower pollination
- ✓ Add pollination tracking
- ✓ Modify seed drop mechanics

**Tasks:**
1. **Day 1-2: FlowerBlockEntity**
   - Create `FlowerBlockEntity` with pollination tracking
   - Add `pollinated` boolean and `pollinationTime` fields
   - Implement NBT persistence
   - Add pollination expiry logic (24 MC days)

2. **Day 3-4: Bee Behavior Integration**
   - Create mixin for `BeeEntity.tickMovement()`
   - Detect when bee visits flower
   - Mark flower as pollinated
   - Spawn pollination particles

3. **Day 5-6: Seed Drop Modification**
   - Create mixin for `FlowerBlock.onBreak()`
   - Check pollination status before dropping seeds
   - Pollinated = 100% drop, unpollinated = 0% drop
   - Test with various flower types

4. **Day 7: Testing & Polish**
   - Test full pollination cycle
   - Verify vanilla bee behavior unchanged
   - Add config option to enable/disable feature
   - Performance testing

**Deliverables:**
- [ ] Flowers require pollination for seeds
- [ ] Vanilla bee behavior intact
- [ ] Visible pollination feedback (particles)

---

## MONTH 2: ENTITY AI & BEHAVIORS

### Week 5: Honeyguide Entity Foundation

**Goals:**
- ✓ Create honeyguide entity
- ✓ Implement basic AI
- ✓ Add player detection

**Tasks:**
1. **Day 1-2: Entity Class**
   - Create `HoneyguideEntity` extending `PathAwareEntity`
   - Set up entity attributes (health, speed, etc.)
   - Register entity type
   - Create basic model and texture

2. **Day 3-4: Basic AI Goals**
   - Implement `SwimGoal`
   - Implement `WanderAroundGoal`
   - Implement `LookAtEntityGoal` (for player)
   - Test entity spawning and basic movement

3. **Day 5-6: Player Detection**
   - Implement `FindNestGoal` (locate nearest bee nest)
   - Implement `CallPlayerGoal` (attract player attention)
   - Add calling sound effect
   - Test player detection range

4. **Day 7: NBT & Persistence**
   - Add NBT fields (targetPlayer, targetNest)
   - Implement write/read methods
   - Test entity persistence across save/load

**Deliverables:**
- [ ] Honeyguide entity spawns and moves
- [ ] Basic AI goals functional
- [ ] Player detection working

---

### Week 6: Honeyguide Leading Behavior

**Goals:**
- ✓ Implement leading behavior
- ✓ Add memory system
- ✓ Create villager interaction

**Tasks:**
1. **Day 1-3: LeadToNestGoal**
   - Implement pathfinding toward bee nest
   - Add "waiting" behavior when player lags behind
   - Implement circling behavior at nest
   - Add timeout logic (abandon after 5 minutes)

2. **Day 4-5: Memory System**
   - Create `PlayerMemoryComponent`
   - Implement betrayal recording (player takes all larvae)
   - Add memory expiry (20 minutes)
   - Test memory persistence

3. **Day 6-7: Villager Interaction**
   - Create `VillagerRespondToHoneyguideGoal`
   - Implement villager pathfinding to nest
   - Add "drop larvae" behavior for villagers
   - Test NPC-honeyguide cooperation

**Deliverables:**
- [ ] Honeyguide leads player to nests
- [ ] Memory system prevents repeat leading
- [ ] Villagers respond to honeyguide calls

---

### Week 7: Pollinator Entities - Base System

**Goals:**
- ✓ Create base pollinator entity
- ✓ Implement tree linkage
- ✓ Add pollination behavior

**Tasks:**
1. **Day 1-2: BasePollinatorEntity**
   - Create abstract base class
   - Add `linkedNest` and `linkedTree` fields
   - Implement `ILinkableEntity` interface
   - Set up basic attributes

2. **Day 3-4: Pollination AI**
   - Implement `PollinateTreeGoal`
   - Add pathfinding from nest to tree
   - Implement pollination event trigger
   - Add cooldown system (2-5 minute intervals)

3. **Day 5-6: Nest Interaction**
   - Implement `ReturnToNestGoal`
   - Add "wander near nest" behavior
   - Implement nest destruction detection
   - Create `onNestDestroyed()` handler

4. **Day 7: Testing**
   - Test full pollination cycle
   - Verify tree-pollinator linkage
   - Test nest destruction response

**Deliverables:**
- [ ] Base pollinator class functional
- [ ] Pollinators successfully pollinate trees
- [ ] Nest destruction detected

---

### Week 8: Pollinator Entities - Defensive & Passive

**Goals:**
- ✓ Implement defensive pollinators
- ✓ Implement passive pollinators
- ✓ Create all 10 pollinator types

**Tasks:**
1. **Day 1-2: DefensivePollinatorEntity**
   - Create defensive subclass
   - Implement `DefendNestGoal`
   - Add attack logic (damage + poison)
   - Test aggro range and behavior

2. **Day 3-4: PassivePollinatorEntity**
   - Create passive subclass
   - Implement flee behavior on nest destruction
   - Add despawn timer (5 minutes)
   - Test flee pathfinding

3. **Day 5-6: Specific Pollinator Types**
   - Create 10 entity types (Fig Wasp, Yucca Moth, etc.)
   - Assign defensive/passive behavior to each
   - Create models and textures (can be placeholders)
   - Register all entity types

4. **Day 7: Balance & Testing**
   - Balance damage values
   - Test poison duration
   - Verify correct behavior for each type
   - Performance test with many entities

**Deliverables:**
- [ ] 5 defensive pollinators working
- [ ] 5 passive pollinators working
- [ ] All behaviors balanced and tested

---

## MONTH 3: WORLD GENERATION & BIOME PAIRS

### Week 9: World Gen Foundation

**Goals:**
- ✓ Set up world generation system
- ✓ Implement biome detection
- ✓ Create placement logic

**Tasks:**
1. **Day 1-2: BiomePairRegistry**
   - Create registry class
   - Map 10 biomes to tree/pollinator pairs
   - Implement `getPairForBiome()` method
   - Add config for spawn chances

2. **Day 3-4: BiomePairFeature**
   - Create `Feature` class for biome pairs
   - Implement `generate()` method
   - Add biome checking logic
   - Register feature with Fabric API

3. **Day 5-6: Tree Generation**
   - Create `TreeStructure` for each tree type
   - Implement placement logic (find suitable ground)
   - Add randomization (tree size, shape variation)
   - Test tree generation in creative mode

4. **Day 7: Testing**
   - Generate new worlds with different seeds
   - Verify trees spawn in correct biomes
   - Check generation frequency
   - Performance profiling

**Deliverables:**
- [ ] Trees generate in world
- [ ] Correct biome assignment
- [ ] Acceptable generation frequency

---

### Week 10: Nest Generation & Linking

**Goals:**
- ✓ Implement nest generation
- ✓ Create tree-nest linking
- ✓ Spawn pollinators

**Tasks:**
1. **Day 1-2: Nest Placement**
   - Create `NestStructure` for each pollinator type
   - Implement placement relative to tree (within 20 blocks)
   - Add ground/tree attachment logic
   - Test nest generation

2. **Day 3-4: Linkage System**
   - Implement `linkTreeAndNest()` in feature generator
   - Set NBT tags on both tree and nest BlockEntities
   - Verify bidirectional linkage
   - Test linkage persistence

3. **Day 5-6: Pollinator Spawning**
   - Spawn pollinator entity when nest generated
   - Link entity to nest and tree via NBT
   - Add initial pollination cooldown
   - Test entity spawning

4. **Day 7: Integration Testing**
   - Full cycle: tree gen → nest gen → linkage → pollinator spawn
   - Test pollination occurs naturally
   - Verify fruit growth after pollination
   - Performance testing

**Deliverables:**
- [ ] Nests generate near trees
- [ ] Tree-nest linkage functional
- [ ] Pollinators spawn and pollinate

---

### Week 11: Biome Pair Refinement

**Goals:**
- ✓ Implement all 10 biome pairs
- ✓ Balance generation rates
- ✓ Add variation and polish

**Tasks:**
1. **Day 1-2: Remaining Biome Pairs**
   - Implement any missing pairs
   - Create unique tree structures for each biome
   - Create unique nest appearances
   - Test each pair individually

2. **Day 3-4: Generation Balance**
   - Adjust spawn rates per biome
   - Balance tree/nest rarity
   - Ensure pairs discoverable but not overwhelming
   - Playtest in survival mode

3. **Day 5-6: Variation & Detail**
   - Add tree size variation (small, medium, large)
   - Randomize nest placement angles
   - Add decorative blocks (flowers, vines, etc.)
   - Improve textures

4. **Day 7: Playtesting**
   - Full survival playtest
   - Check all biomes for correct pairs
   - Balance fruit value vs. rarity
   - Gather feedback

**Deliverables:**
- [ ] All 10 biome pairs fully functional
- [ ] Balanced generation across biomes
- [ ] Visually polished structures

---

### Week 12: Player Cultivation System

**Goals:**
- ✓ Allow players to farm trees/nests
- ✓ Create sapling and nest crafting
- ✓ Implement manual linking

**Tasks:**
1. **Day 1-2: Sapling System**
   - Create sapling items for all 10 trees
   - Implement sapling → tree growth
   - Add bone meal compatibility
   - Test sapling planting

2. **Day 3-4: Nest Crafting**
   - Design crafting recipes for each nest type
   - Require appropriate materials (sticks, wool, etc.)
   - Add nest placement mechanics
   - Test nest crafting and placement

3. **Day 5-6: Manual Linking**
   - Implement auto-linking when nest placed near tree
   - Add "linking range" check (20 blocks)
   - Prevent duplicate links
   - Add visual feedback (particles) on successful link

4. **Day 7: Balance & Testing**
   - Balance crafting recipe difficulty
   - Test sustainable farming loop
   - Verify linkage works as expected
   - Playtest cultivation system

**Deliverables:**
- [ ] Players can plant saplings
- [ ] Players can craft and place nests
- [ ] Manual linking works reliably

---

## MONTH 4: POLISH, TESTING & RELEASE

### Week 13: Audio & Visual Polish

**Goals:**
- ✓ Add sound effects
- ✓ Improve particle effects
- ✓ Enhance textures

**Tasks:**
1. **Day 1-2: Sound Effects**
   - Create/source honeyguide call sounds
   - Add pollination event sounds
   - Add nest destruction sounds
   - Implement 3D positional audio

2. **Day 3-4: Particle Effects**
   - Enhance pollination particles (petals, sparkles)
   - Add nest destruction particles
   - Add fruit maturation particles
   - Test particle performance

3. **Day 5-6: Texture Improvements**
   - Create final textures for all blocks
   - Create entity textures/models
   - Add fruit item textures
   - Ensure consistent art style

4. **Day 7: Animation**
   - Add entity animations (flying, pollinating)
   - Add block animations (swaying fruit)
   - Test animation performance

**Deliverables:**
- [ ] Professional sound design
- [ ] Polished particle effects
- [ ] Final textures and animations

---

### Week 14: Documentation & Tutorials

**Goals:**
- ✓ Create in-game guidebook
- ✓ Write external documentation
- ✓ Create tutorial content

**Tasks:**
1. **Day 1-3: In-Game Guidebook**
   - Integrate Patchouli (or similar)
   - Write guidebook entries for each feature
   - Add discovery hints
   - Include crafting recipes

2. **Day 4-5: External Documentation**
   - Write comprehensive wiki
   - Document all config options
   - Create troubleshooting guide
   - Write modpack integration guide

3. **Day 6-7: Tutorial Content**
   - Create screenshots/GIFs
   - Write "Getting Started" guide
   - Create video script (optional)
   - Test documentation clarity

**Deliverables:**
- [ ] In-game guidebook functional
- [ ] Complete external documentation
- [ ] Clear tutorial materials

---

### Week 15: Testing & Bug Fixing

**Goals:**
- ✓ Comprehensive testing
- ✓ Fix all critical bugs
- ✓ Performance optimization

**Tasks:**
1. **Day 1-2: Unit Testing**
   - Write/expand unit tests for all systems
   - Achieve >80% code coverage
   - Fix failing tests
   - Automate test runs

2. **Day 3-4: Integration Testing**
   - Test all features together
   - Check mod compatibility (popular mods)
   - Test multiplayer functionality
   - Stress test with many entities

3. **Day 5-6: Performance Profiling**
   - Profile entity tick performance
   - Optimize world gen
   - Reduce memory allocations
   - Test on low-end hardware

4. **Day 7: Bug Triage**
   - Collect all discovered bugs
   - Prioritize (critical, major, minor)
   - Fix critical and major bugs
   - Document known minor issues

**Deliverables:**
- [ ] No critical bugs
- [ ] Performance targets met
- [ ] Passing test suite

---

### Week 16: Release Preparation

**Goals:**
- ✓ Finalize release build
- ✓ Create marketing materials
- ✓ Publish to mod platforms

**Tasks:**
1. **Day 1-2: Release Build**
   - Finalize version number (1.0.0)
   - Update changelog
   - Create release JAR
   - Test release build thoroughly

2. **Day 3-4: Marketing Materials**
   - Write CurseForge/Modrinth descriptions
   - Create showcase screenshots
   - Record demonstration video
   - Design mod logo/banner

3. **Day 5: Platform Publishing**
   - Upload to CurseForge
   - Upload to Modrinth
   - Create GitHub release
   - Submit to mod lists/directories

4. **Day 6-7: Community Engagement**
   - Post on Minecraft forums
   - Share on social media (Reddit, Twitter)
   - Respond to initial feedback
   - Monitor for urgent issues

**Deliverables:**
- [ ] Mod published on all platforms
- [ ] Professional marketing materials
- [ ] Active community engagement

---

## POST-RELEASE: MAINTENANCE & ITERATION

### Ongoing Tasks

**Bug Fixes:**
- Monitor issue tracker
- Fix critical bugs within 24 hours
- Release patch versions as needed

**Feature Requests:**
- Collect community feedback
- Prioritize requested features
- Plan future phases (Phase 2, 3, etc.)

**Updates:**
- Keep compatible with latest Minecraft versions
- Update dependencies (Fabric API, etc.)
- Maintain documentation

**Community:**
- Engage with players
- Showcase community creations
- Consider translations

---

## SUCCESS METRICS

**Phase 1 Complete When:**
- [ ] All 3 core features implemented
- [ ] 10 biome pairs fully functional
- [ ] No critical bugs
- [ ] Performance requirements met (<10% TPS impact)
- [ ] Published on CurseForge & Modrinth
- [ ] 100+ downloads in first week
- [ ] Positive community feedback (>80% approval rating)

---

## RISK MITIGATION

**Potential Delays:**
- Entity AI more complex than expected → **Simplify behaviors, cut nice-to-have features**
- World gen performance issues → **Reduce spawn rates, optimize algorithms**
- Art assets take longer → **Use placeholders, commission artists**

**Contingency Time:**
- 1 week buffer built into Month 4 for unexpected issues

---

This roadmap provides a clear path from project start to release, with weekly milestones and daily task breakdowns. Adjust as needed based on actual progress.
