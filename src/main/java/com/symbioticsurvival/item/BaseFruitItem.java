package com.symbioticsurvival.item;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;

/**
 * Base class for all fruit items from special trees.
 * Each fruit has unique properties based on its biome.
 */
public abstract class BaseFruitItem extends Item {

    protected final String biomeType;

    public BaseFruitItem(Settings settings, String biomeType) {
        super(settings);
        this.biomeType = biomeType;
    }

    /**
     * Create a food component for edible fruits
     */
    public static FoodComponent createFoodComponent(int hunger, float saturation) {
        return new FoodComponent.Builder()
            .nutrition(hunger)
            .saturationModifier(saturation)
            .build();
    }

    public String getBiomeType() {
        return biomeType;
    }
}
