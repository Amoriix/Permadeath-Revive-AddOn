package name.amori.permadeathrevive.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PoolElementStructurePiece.class)
public abstract class PoolElementStructurePieceMixin {

    private static final int MAX_PILLAR_DEPTH = 15;

    private static final int[][] PILLAR_POSITIONS = {
            // Outer supports (already tested)
            {4, 0}, {8, 0},
            {4, 12}, {8, 12},
            {0, 4}, {0, 8},
            {12, 4}, {12, 8},

            // Inner supports
            {4, 4},
            {8, 4},
            {4, 8},
            {8, 8}
    };

    @Inject(method = "place", at = @At("TAIL"))
    private void permadeathRevive$addFoundation(
            WorldGenLevel level,
            StructureManager structureManager,
            ChunkGenerator generator,
            RandomSource random,
            BoundingBox chunkBounds,
            BlockPos pivot,
            boolean keepJigsaws,
            CallbackInfo ci
    ) {
        PoolElementStructurePiece piece =
                (PoolElementStructurePiece) (Object) this;

        StructurePoolElement element = piece.getElement();

        // Verify that we can access the template.
        if (!(element instanceof SinglePoolElement)
                || !(element instanceof SinglePoolElementAccessor accessor)) {
            return;
        }

        ResourceLocation shrineId =
                ResourceLocation.fromNamespaceAndPath(
                        "permadeath-revive",
                        "nether_heart_shrine"
                );

        // Only execute for our Nether Heart Shrine.
        if (accessor.getTemplate().left()
                .filter(shrineId::equals)
                .isEmpty()) {
            return;
        }

        // Get the actual rotated bounding box.
        BoundingBox box = piece.getBoundingBox();

        System.out.println(
                "[Permadeath Revive] PLACED PIECE"
                        + " | Origin: " + piece.getPosition()
                        + " | Rotation: " + piece.getRotation()
                        + " | Bounding box: " + piece.getBoundingBox()
        );

        for (int[] pillar : PILLAR_POSITIONS) {

            BlockPos startPos = new BlockPos(
                    box.minX() + pillar[0],
                    box.minY() - 1,
                    box.minZ() + pillar[1]
            );

            System.out.println(
                    "[Permadeath Revive] Checking pillar at "
                            + startPos
                            + " | Chunk bounds: "
                            + chunkBounds
            );

            // Only process pillars whose entire search column
            // belongs to the current chunk processing region.
            if (!chunkBounds.isInside(startPos)
                    || !chunkBounds.isInside(startPos.below(MAX_PILLAR_DEPTH))) {
                continue;
            }

            int pillarLength = 0;
            boolean foundGround = false;

            // Search for ground beneath the shrine.
            // Check one extra block so a five-block pillar
            // can connect to terrain at depth 5.
            for (int depth = 0; depth <= MAX_PILLAR_DEPTH; depth++) {

                BlockPos checkPos = startPos.below(depth);

                // Don't inspect outside the current chunk region.
                if (!chunkBounds.isInside(checkPos)) {
                    break;
                }

                BlockState state = level.getBlockState(checkPos);

                // Stop if we encounter lava or another fluid.
                if (!state.getFluidState().isEmpty()) {
                    break;
                }

                // Solid ground found!
                if (state.isCollisionShapeFullBlock(level, checkPos)) {
                    foundGround = true;
                    break;
                }

                // Only air is safe to replace for now.
                if (!state.isAir()) {
                    break;
                }

                pillarLength++;
            }

            // Don't build unsupported pillars.
            if (!foundGround) {
                System.out.println(
                        "[Permadeath Revive] No suitable ground beneath "
                                + startPos
                );
                continue;
            }

            // No gap to fill.
            if (pillarLength == 0) {
                continue;
            }

            // Generate the pillar.
            for (int depth = 0; depth < pillarLength; depth++) {

                BlockPos placePos = startPos.below(depth);

                level.setBlock(
                        placePos,
                        Blocks.POLISHED_BLACKSTONE_BRICKS.defaultBlockState(),
                        2
                );
            }

            System.out.println(
                    "[Permadeath Revive] Generated pillar at "
                            + startPos
                            + " with length " + pillarLength
            );
        }
    }
}
