
package name.amori.permadeathrevive.worldgen.structure;

import name.amori.permadeathrevive.PermadeathRevive;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class ModStructures {

    public static final StructureType<NetherHeartShrineStructure>
            NETHER_HEART_SHRINE = Registry.register(
            BuiltInRegistries.STRUCTURE_TYPE,
            ResourceLocation.fromNamespaceAndPath(
                    "permadeath-revive",
                    "nether_heart_shrine"
            ),
            () -> NetherHeartShrineStructure.CODEC
    );

    public static void register() {
        PermadeathRevive.LOGGER.info("Registering custom structures");
    }
}
