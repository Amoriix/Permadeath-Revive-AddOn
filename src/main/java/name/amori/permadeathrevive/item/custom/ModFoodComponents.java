package name.amori.permadeathrevive.item.custom;

import net.minecraft.world.food.FoodProperties;

public class ModFoodComponents {
    public static final FoodProperties PERMANENT_HEART =
            new FoodProperties
                    .Builder()
                    .alwaysEdible()
                    .saturationModifier(0.0f)
                    .nutrition(0)
                    .build();
}
