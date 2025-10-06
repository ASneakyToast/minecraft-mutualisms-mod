# Getting Started Guide
## Symbiotic Survival Mod - Development Setup

---

## 1. PREREQUISITES

### 1.1 Required Software

**Java Development Kit (JDK) 17+:**
- Download from [Adoptium](https://adoptium.net/) or [Oracle](https://www.oracle.com/java/technologies/downloads/)
- Verify installation: `java -version` should show version 17 or higher

**IntelliJ IDEA (Recommended) or Eclipse:**
- [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download/) (Free)
- OR [Eclipse IDE for Java Developers](https://www.eclipse.org/downloads/)

**Git:**
- Download from [git-scm.com](https://git-scm.com/)
- Verify: `git --version`

**Gradle (Optional - Wrapper Included):**
- Gradle will be downloaded automatically via the wrapper
- Manual install: [gradle.org](https://gradle.org/install/)

---

### 1.2 Recommended Tools

- **Minecraft Launcher** (Java Edition) - for testing
- **GitHub Desktop** (optional) - for easier Git management
- **Visual Studio Code** (optional) - for markdown/JSON editing
- **GIMP/Photoshop** - for texture creation
- **Audacity** - for sound editing

---

## 2. PROJECT SETUP

### 2.1 Clone or Initialize Project

**Option 1: Clone from GitHub (if repository exists):**
```bash
git clone https://github.com/yourusername/minecraft-mutualisms-mod.git
cd minecraft-mutualisms-mod
```

**Option 2: Start Fresh with Fabric Template:**
```bash
# Download Fabric Example Mod
git clone https://github.com/FabricMC/fabric-example-mod.git symbiotic-survival
cd symbiotic-survival

# Remove example mod files (keep structure)
rm -rf src/main/java/net/fabricmc/example
rm -rf src/main/resources/assets/modid

# Initialize new Git repository
git init
git add .
git commit -m "Initial commit from Fabric template"
```

---

### 2.2 Configure Build Settings

**Edit `gradle.properties`:**
```properties
# Minecraft Properties
minecraft_version=1.21
yarn_mappings=1.21+build.9
loader_version=0.15.0

# Fabric API
fabric_version=0.92.0+1.21

# Mod Properties
mod_version=0.1.0
maven_group=com.symbiotic
archives_base_name=symbiotic-survival

# Java
org.gradle.jvmargs=-Xmx2G
```

**Edit `build.gradle`:**
```gradle
plugins {
    id 'fabric-loom' version '1.5-SNAPSHOT'
    id 'maven-publish'
}

version = project.mod_version
group = project.maven_group

repositories {
    // Add any custom repositories here
    maven { url 'https://maven.fabricmc.net/' }
}

dependencies {
    // Core dependencies
    minecraft "com.mojang:minecraft:${project.minecraft_version}"
    mappings "net.fabricmc:yarn:${project.yarn_mappings}:v2"
    modImplementation "net.fabricmc:fabric-loader:${project.loader_version}"
    modImplementation "net.fabricmc.fabric-api:fabric-api:${project.fabric_version}"

    // Optional: GeckoLib for advanced animations
    // modImplementation "software.bernie.geckolib:geckolib-fabric-1.21:4.4"

    // Testing
    testImplementation 'org.junit.jupiter:junit-jupiter:5.9.0'
}

processResources {
    inputs.property "version", project.version
    filteringCharset "UTF-8"

    filesMatching("fabric.mod.json") {
        expand "version": project.version
    }
}

tasks.withType(JavaCompile).configureEach {
    it.options.release = 17
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

jar {
    from("LICENSE") {
        rename { "${it}_${project.archivesBaseName}"}
    }
}
```

---

### 2.3 Create Project Structure

```bash
# Create package structure
mkdir -p src/main/java/com/symbiotic/block
mkdir -p src/main/java/com/symbiotic/entity
mkdir -p src/main/java/com/symbiotic/entity/pollinator
mkdir -p src/main/java/com/symbiotic/item
mkdir -p src/main/java/com/symbiotic/worldgen
mkdir -p src/main/java/com/symbiotic/config
mkdir -p src/main/java/com/symbiotic/util
mkdir -p src/main/java/com/symbiotic/mixin

# Create resource structure
mkdir -p src/main/resources/assets/symbiotic/blockstates
mkdir -p src/main/resources/assets/symbiotic/models/block
mkdir -p src/main/resources/assets/symbiotic/models/item
mkdir -p src/main/resources/assets/symbiotic/textures/block
mkdir -p src/main/resources/assets/symbiotic/textures/item
mkdir -p src/main/resources/assets/symbiotic/textures/entity
mkdir -p src/main/resources/assets/symbiotic/sounds
mkdir -p src/main/resources/assets/symbiotic/lang

mkdir -p src/main/resources/data/symbiotic/recipes
mkdir -p src/main/resources/data/symbiotic/loot_tables/blocks
mkdir -p src/main/resources/data/symbiotic/worldgen/configured_feature
mkdir -p src/main/resources/data/symbiotic/worldgen/placed_feature

# Create test structure
mkdir -p src/test/java/com/symbiotic
```

---

### 2.4 Create Mod Entry Point

**`src/main/java/com/symbiotic/SymbioticSurvival.java`:**
```java
package com.symbiotic;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SymbioticSurvival implements ModInitializer {
    public static final String MOD_ID = "symbiotic";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Symbiotic Survival is initializing...");

        // Register blocks, items, entities, etc.
        // ModBlocks.register();
        // ModItems.register();
        // ModEntities.register();

        LOGGER.info("Symbiotic Survival initialized successfully!");
    }
}
```

**`src/main/java/com/symbiotic/SymbioticSurvivalClient.java`:**
```java
package com.symbiotic;

import net.fabricmc.api.ClientModInitializer;

public class SymbioticSurvivalClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SymbioticSurvival.LOGGER.info("Symbiotic Survival client initializing...");

        // Register client-side rendering, etc.
        // EntityRendererRegistry.register(ModEntities.HONEYGUIDE, HoneyguideRenderer::new);

        SymbioticSurvival.LOGGER.info("Symbiotic Survival client initialized!");
    }
}
```

---

### 2.5 Create `fabric.mod.json`

**`src/main/resources/fabric.mod.json`:**
```json
{
  "schemaVersion": 1,
  "id": "symbiotic",
  "version": "${version}",
  "name": "Symbiotic Survival",
  "description": "Educational mod showcasing mutualistic relationships in nature",
  "authors": [
    "YourName"
  ],
  "contact": {
    "homepage": "https://github.com/yourusername/symbiotic-survival",
    "sources": "https://github.com/yourusername/symbiotic-survival",
    "issues": "https://github.com/yourusername/symbiotic-survival/issues"
  },
  "license": "MIT",
  "icon": "assets/symbiotic/icon.png",
  "environment": "*",
  "entrypoints": {
    "main": [
      "com.symbiotic.SymbioticSurvival"
    ],
    "client": [
      "com.symbiotic.SymbioticSurvivalClient"
    ]
  },
  "mixins": [
    "symbiotic.mixins.json"
  ],
  "depends": {
    "fabricloader": ">=0.15.0",
    "fabric-api": ">=0.92.0",
    "minecraft": "~1.21",
    "java": ">=17"
  },
  "suggests": {
    "another-mod": "*"
  }
}
```

---

### 2.6 Create Mixin Configuration

**`src/main/resources/symbiotic.mixins.json`:**
```json
{
  "required": true,
  "minVersion": "0.8",
  "package": "com.symbiotic.mixin",
  "compatibilityLevel": "JAVA_17",
  "mixins": [
    "FlowerBlockMixin",
    "BeeEntityMixin"
  ],
  "client": [
  ],
  "injectors": {
    "defaultRequire": 1
  }
}
```

---

## 3. IDE SETUP

### 3.1 IntelliJ IDEA

**Import Project:**
1. Open IntelliJ IDEA
2. Click "Open" and select the project directory
3. IntelliJ will detect Gradle and import automatically
4. Wait for Gradle sync to complete (check bottom-right corner)

**Generate Run Configurations:**
```bash
./gradlew genSources
```

This creates:
- **Minecraft Client** - Run the game with your mod
- **Minecraft Server** - Run a test server

**Run the Client:**
1. Click the "Run" dropdown in the toolbar
2. Select "Minecraft Client"
3. Click the green "Run" button
4. Minecraft should launch with your mod loaded

**Enable Hot Swap (Optional):**
- Edit run configuration → Add VM options:
  ```
  -XX:+AllowEnhancedClassRedefinition
  ```

---

### 3.2 Eclipse

**Import Project:**
1. File → Import → Existing Gradle Project
2. Select project directory
3. Click "Finish"

**Generate Sources:**
```bash
./gradlew eclipse
./gradlew genSources
```

**Run Configurations:**
- Eclipse should auto-generate run configs
- Look for "Minecraft Client" and "Minecraft Server" in Run menu

---

### 3.3 Visual Studio Code (Alternative)

**Install Extensions:**
- Extension Pack for Java
- Gradle for Java

**Open Project:**
```bash
code .
```

**Run Tasks:**
- Press `Ctrl+Shift+P` → "Tasks: Run Task" → "runClient"

---

## 4. BUILDING THE MOD

### 4.1 Development Build

**Build JAR:**
```bash
./gradlew build
```

Output: `build/libs/symbiotic-survival-0.1.0.jar`

**Run Tests:**
```bash
./gradlew test
```

**Clean Build:**
```bash
./gradlew clean build
```

---

### 4.2 Production Release

**Update Version:**
1. Edit `gradle.properties` → change `mod_version`
2. Update `CHANGELOG.md`

**Build Release:**
```bash
./gradlew clean build
```

**Verify JAR:**
- Test in clean Minecraft instance
- Check file size (should be <50MB)
- Verify dependencies included

---

## 5. TESTING YOUR MOD

### 5.1 In-Game Testing

**Launch Test Client:**
```bash
./gradlew runClient
```

**Check Mod Loaded:**
1. Main menu → Mods
2. Look for "Symbiotic Survival"

**Debug Logging:**
- Check `logs/latest.log` for mod messages
- Look for `[symbiotic]` prefix

**Creative Testing:**
1. Create creative world
2. Use `/give` for mod items:
   ```
   /give @p symbiotic:fig
   ```

---

### 5.2 Unit Testing

**Create Test Class:**
```java
// src/test/java/com/symbiotic/TreeNestLinkageTest.java
package com.symbiotic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TreeNestLinkageTest {

    @Test
    public void testLinkageCreation() {
        // Test logic here
        assertTrue(true, "Linkage should be created");
    }
}
```

**Run Tests:**
```bash
./gradlew test
```

---

### 5.3 Performance Testing

**Enable Profiling:**
1. Run client with profiler:
   ```bash
   ./gradlew runClient -Pmixin.debug.profiler=true
   ```

2. In-game: Press F3 → Performance Metrics

**Check TPS:**
1. Run server
2. Use `/tps` command (if available)
3. Target: >18 TPS with mod loaded

**Entity Count Test:**
1. Spawn 100+ pollinators
2. Monitor FPS and TPS
3. Check for memory leaks

---

## 6. COMMON TASKS

### 6.1 Adding a New Block

**1. Create Block Class:**
```java
// src/main/java/com/symbiotic/block/ExampleBlock.java
package com.symbiotic.block;

import net.minecraft.block.Block;

public class ExampleBlock extends Block {
    public ExampleBlock(Settings settings) {
        super(settings);
    }
}
```

**2. Register Block:**
```java
// src/main/java/com/symbiotic/block/ModBlocks.java
public class ModBlocks {
    public static final Block EXAMPLE_BLOCK = register("example_block",
        new ExampleBlock(FabricBlockSettings.of(Material.STONE)));

    private static Block register(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier("symbiotic", name), block);
    }

    public static void register() {
        // Called from main mod initializer
    }
}
```

**3. Create Block Model:**
```json
// src/main/resources/assets/symbiotic/models/block/example_block.json
{
  "parent": "block/cube_all",
  "textures": {
    "all": "symbiotic:block/example_block"
  }
}
```

**4. Create Blockstate:**
```json
// src/main/resources/assets/symbiotic/blockstates/example_block.json
{
  "variants": {
    "": {
      "model": "symbiotic:block/example_block"
    }
  }
}
```

**5. Create Texture:**
- Place 16x16 PNG at: `src/main/resources/assets/symbiotic/textures/block/example_block.png`

---

### 6.2 Adding a New Entity

**1. Create Entity Class:**
```java
package com.symbiotic.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

public class ExampleEntity extends PathAwareEntity {
    public ExampleEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        // Add AI goals
    }
}
```

**2. Register Entity:**
```java
public class ModEntities {
    public static final EntityType<ExampleEntity> EXAMPLE = register("example",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, ExampleEntity::new)
            .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
            .build());

    private static <T extends Entity> EntityType<T> register(String name, EntityType<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, new Identifier("symbiotic", name), type);
    }

    public static void register() {
        // Called from main mod initializer
    }
}
```

**3. Create Entity Renderer:**
```java
// Client-side only
public class ExampleEntityRenderer extends EntityRenderer<ExampleEntity> {
    public ExampleEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(ExampleEntity entity) {
        return new Identifier("symbiotic", "textures/entity/example.png");
    }
}
```

---

### 6.3 Adding Configuration

**Using Fabric's Config API:**

**1. Add Dependency:**
```gradle
// build.gradle
dependencies {
    modApi("me.shedaniel.cloth:cloth-config-fabric:11.0.99") {
        exclude(group: "net.fabricmc.fabric-api")
    }
}
```

**2. Create Config Class:**
```java
package com.symbiotic.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "symbiotic")
public class SymbioticConfig implements ConfigData {
    public double honeyguideSpawnRate = 0.002;
    public int leadingRange = 128;
    // ... more fields
}
```

**3. Register Config:**
```java
// In main mod initializer
AutoConfig.register(SymbioticConfig.class, JanksonConfigSerializer::new);
```

---

## 7. TROUBLESHOOTING

### 7.1 Common Errors

**Error: "Could not find fabric-loader"**
- Solution: Run `./gradlew --refresh-dependencies`

**Error: "Main class not found"**
- Solution: Check `fabric.mod.json` entrypoints match your class names

**Error: "Mixin failed to apply"**
- Solution: Check mixin target class/method exists in current MC version

**Error: "Duplicate mod ID"**
- Solution: Change `id` in `fabric.mod.json`

**Game Crashes on Launch:**
1. Check `logs/latest.log`
2. Look for `Caused by:` lines
3. Common issues:
   - Missing dependencies
   - Invalid JSON files
   - Incorrect registry names

---

### 7.2 Debugging Tips

**Enable Debug Logging:**
```java
// In mod initializer
System.setProperty("fabric.log.level", "debug");
```

**Use Breakpoints:**
1. Set breakpoint in IntelliJ (click line number gutter)
2. Run "Minecraft Client" in **Debug** mode
3. Game pauses when breakpoint hit

**Print Entity Data:**
```java
SymbioticSurvival.LOGGER.info("Entity NBT: {}", entity.writeNbt(new NbtCompound()));
```

---

## 8. RESOURCES & REFERENCES

### 8.1 Official Documentation

- [Fabric Wiki](https://fabricmc.net/wiki/)
- [Fabric API Javadocs](https://maven.fabricmc.net/docs/fabric-api-0.92.0+1.21/)
- [Yarn Mappings](https://github.com/FabricMC/yarn)

### 8.2 Community Resources

- [Fabric Discord](https://discord.gg/v6v4pMv)
- [r/fabricmc Subreddit](https://reddit.com/r/fabricmc)
- [Minecraft Mod Development Discord](https://discord.gg/modded-minecraft)

### 8.3 Tools

- [Blockbench](https://blockbench.net/) - 3D model editor
- [MCreator](https://mcreator.net/) - Visual mod creator (reference only)
- [NBT Explorer](https://github.com/jaquadro/NBTExplorer) - View save data

---

## 9. NEXT STEPS

**Week 1 Tasks:**
1. ✓ Complete environment setup
2. ✓ Run test client successfully
3. Create first block (test block)
4. Create first item (test item)
5. Verify registration works

**Learn By Doing:**
- Follow [Fabric Wiki Tutorials](https://fabricmc.net/wiki/tutorial:start)
- Study vanilla Minecraft code (decompiled via Gradle)
- Join community and ask questions

**Ready to Code?**
- Review `PROJECT_REQUIREMENTS.md` for feature details
- Check `ROADMAP.md` for development plan
- Reference `TECHNICAL_SPEC.md` for implementation details

---

**You're all set! Start with Week 1 of the roadmap and build incrementally. Good luck!** 🎮
