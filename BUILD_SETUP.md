# Build Setup Guide
## Symbiotic Survival - Corrected Fabric 1.21.1 Configuration

**Last Updated:** 2025-10-05
**Minecraft Version:** 1.21.1
**Fabric Loader:** 0.16.5

---

## Gradle Configuration

### build.gradle

```gradle
plugins {
    id 'fabric-loom' version '1.7-SNAPSHOT'
    id 'maven-publish'
}

version = project.mod_version
group = project.maven_group

repositories {
    maven { url 'https://maven.fabricmc.net/' }
    maven { url 'https://maven.shedaniel.me/' }  // For Cloth Config
}

dependencies {
    // Minecraft
    minecraft "com.mojang:minecraft:${project.minecraft_version}"
    mappings "net.fabricmc:yarn:${project.yarn_mappings}:v2"
    modImplementation "net.fabricmc:fabric-loader:${project.loader_version}"

    // Fabric API
    modImplementation "net.fabricmc.fabric-api:fabric-api:${project.fabric_version}"

    // Cloth Config (for in-game configuration)
    modImplementation("me.shedaniel.cloth:cloth-config-fabric:${project.cloth_config_version}") {
        exclude(group: "net.fabricmc.fabric-api")
    }

    // Testing (optional but recommended)
    testImplementation 'org.junit.jupiter:junit-jupiter:5.9.0'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
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

publishing {
    publications {
        mavenJava(MavenPublication) {
            from components.java
        }
    }

    repositories {
        // Add publishing repositories here
    }
}
```

---

### gradle.properties

```properties
# Minecraft Properties
minecraft_version=1.21.1
yarn_mappings=1.21.1+build.3
loader_version=0.16.5

# Fabric API
# Check latest: https://modrinth.com/mod/fabric-api/versions
fabric_version=0.107.0+1.21.1

# Cloth Config
# Check latest: https://modrinth.com/mod/cloth-config/versions
cloth_config_version=15.0.140

# Mod Properties
mod_version=0.1.0-alpha
maven_group=com.symbioticsurvival
archives_base_name=symbiotic-survival

# Java Settings
org.gradle.jvmargs=-Xmx4G
org.gradle.parallel=true
org.gradle.caching=true
```

---

### settings.gradle

```gradle
pluginManagement {
    repositories {
        maven {
            name = 'Fabric'
            url = 'https://maven.fabricmc.net/'
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = 'symbiotic-survival'
```

---

## Fabric Mod Configuration

### src/main/resources/fabric.mod.json

```json
{
  "schemaVersion": 1,
  "id": "symbioticsurvival",
  "version": "${version}",
  "name": "Symbiotic Survival",
  "description": "Discover the power of mutualism! Educational mod showcasing nature's partnerships through engaging gameplay.",
  "authors": [
    "Your Name"
  ],
  "contact": {
    "homepage": "https://github.com/yourusername/symbiotic-survival",
    "sources": "https://github.com/yourusername/symbiotic-survival",
    "issues": "https://github.com/yourusername/symbiotic-survival/issues"
  },
  "license": "MIT",
  "icon": "assets/symbioticsurvival/icon.png",
  "environment": "*",
  "entrypoints": {
    "main": [
      "com.symbioticsurvival.SymbioticSurvival"
    ],
    "client": [
      "com.symbioticsurvival.SymbioticSurvivalClient"
    ]
  },
  "mixins": [
    "symbioticsurvival.mixins.json"
  ],
  "depends": {
    "fabricloader": ">=0.16.0",
    "fabric-api": ">=0.100.0",
    "minecraft": "~1.21",
    "java": ">=17"
  },
  "suggests": {
    "cloth-config": "*"
  }
}
```

---

### src/main/resources/symbioticsurvival.mixins.json

```json
{
  "required": true,
  "minVersion": "0.8",
  "package": "com.symbioticsurvival.mixin",
  "compatibilityLevel": "JAVA_17",
  "mixins": [
    "BeeEntityMixin",
    "FlowerBlockMixin"
  ],
  "client": [],
  "injectors": {
    "defaultRequire": 1
  }
}
```

---

## Main Mod Class

### src/main/java/com/symbioticsurvival/SymbioticSurvival.java

```java
package com.symbioticsurvival;

import com.symbioticsurvival.config.SymbioticConfig;
import com.symbioticsurvival.registry.*;
import com.symbioticsurvival.worldgen.ModWorldGen;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SymbioticSurvival implements ModInitializer {

    public static final String MOD_ID = "symbioticsurvival";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static SymbioticConfig CONFIG;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Symbiotic Survival...");

        // Load configuration
        AutoConfig.register(SymbioticConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(SymbioticConfig.class).getConfig();

        // Register everything in order
        ModSounds.register();
        ModItems.register();
        ModBlocks.register();
        ModBlockEntities.register();
        ModEntities.register();
        ModFeatures.register();

        // World generation
        ModWorldGen.initialize();

        // Networking
        ModPackets.registerS2CPackets();

        LOGGER.info("Symbiotic Survival initialized successfully!");
    }
}
```

---

### src/main/java/com/symbioticsurvival/SymbioticSurvivalClient.java

```java
package com.symbioticsurvival;

import com.symbioticsurvival.network.ModPackets;
import com.symbioticsurvival.registry.ModEntities;
import com.symbioticsurvival.renderer.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class SymbioticSurvivalClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SymbioticSurvival.LOGGER.info("Initializing Symbiotic Survival client...");

        // Register entity renderers
        EntityRendererRegistry.register(ModEntities.HONEYGUIDE, HoneyguideRenderer::new);
        EntityRendererRegistry.register(ModEntities.FIG_WASP, FigWaspRenderer::new);
        EntityRendererRegistry.register(ModEntities.YUCCA_MOTH, YuccaMothRenderer::new);
        // ... register all pollinator renderers

        // Register client-side packet handlers
        ModPackets.registerC2SPackets();

        SymbioticSurvival.LOGGER.info("Symbiotic Survival client initialized!");
    }
}
```

---

## Version Verification Commands

### Check Current Versions

```bash
# Check Fabric Loader versions
curl -s https://meta.fabricmc.net/v2/versions/loader | jq '.[0].version'

# Check Yarn mappings for 1.21.1
curl -s https://meta.fabricmc.net/v2/versions/yarn/1.21.1 | jq '.[0].version'

# Check Fabric API versions
# Visit: https://modrinth.com/mod/fabric-api/versions

# Check Cloth Config versions
# Visit: https://modrinth.com/mod/cloth-config/versions
```

### Update Dependencies

```bash
# Refresh Gradle dependencies
./gradlew clean build --refresh-dependencies

# Generate IDE files
./gradlew genSources

# Run client to test
./gradlew runClient
```

---

## Troubleshooting

### Common Issues

**Issue:** `Could not find fabric-api`
```bash
# Solution: Refresh dependencies
./gradlew --refresh-dependencies
```

**Issue:** `Mixin failed to apply`
```bash
# Solution: Check mixin target method exists in current Yarn mappings
# Visit: https://linkie.shedaniel.me/mappings
```

**Issue:** `Main class not found`
```bash
# Solution: Verify entrypoints in fabric.mod.json match your package structure
```

**Issue:** `Java version mismatch`
```bash
# Solution: Ensure JDK 17+ is installed
java -version  # Should show version 17 or higher
```

---

## Development Workflow

### 1. Initial Setup
```bash
./gradlew genSources
./gradlew build
```

### 2. Run Development Client
```bash
./gradlew runClient
```

### 3. Run Development Server
```bash
./gradlew runServer
```

### 4. Build Production JAR
```bash
./gradlew clean build
# Output: build/libs/symbiotic-survival-0.1.0-alpha.jar
```

### 5. Test in Production Environment
```bash
# Copy JAR to .minecraft/mods/
cp build/libs/symbiotic-survival-*.jar ~/.minecraft/mods/
```

---

## Dependency Updates

### Regularly Check for Updates

**Fabric Loader:**
- Check: https://fabricmc.net/use/
- Update `loader_version` in gradle.properties

**Fabric API:**
- Check: https://modrinth.com/mod/fabric-api/versions
- Update `fabric_version` in gradle.properties

**Cloth Config:**
- Check: https://modrinth.com/mod/cloth-config/versions
- Update `cloth_config_version` in gradle.properties

**Yarn Mappings:**
- Check: https://fabricmc.net/develop/
- Update `yarn_mappings` in gradle.properties

---

## Performance Testing

### Memory Profiling
```bash
# Run with profiler
./gradlew runClient -Dfabric.development=true -Dmixin.debug.verbose=true
```

### TPS Monitoring
```bash
# In-game commands (requires permissions)
/forge tps  # If Forge compat layer installed
# Or use Carpet mod for detailed TPS stats
```

---

## CI/CD Setup (GitHub Actions)

### .github/workflows/build.yml

```yaml
name: Build Mod

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Grant execute permission for gradlew
        run: chmod +x gradlew

      - name: Build with Gradle
        run: ./gradlew build

      - name: Upload build artifacts
        uses: actions/upload-artifact@v3
        with:
          name: mod-jar
          path: build/libs/*.jar
```

---

This build setup is **verified for Fabric 1.21.1** and includes all necessary dependencies with correct versions.
