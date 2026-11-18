package name.amori.permadeathrevive.worldgen.structure;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

import java.util.Optional;

public class NetherHeartShrineStructure extends Structure {

    private final Holder<StructureTemplatePool> startPool;
    private final HeightProvider startHeight;

    public NetherHeartShrineStructure(
            StructureSettings settings,
            Holder<StructureTemplatePool> startPool,
            HeightProvider startHeight
    ) {
        super(settings);
        this.startPool = startPool;
        this.startHeight = startHeight;
    }

    public static final MapCodec<NetherHeartShrineStructure> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            settingsCodec(instance),
                            StructureTemplatePool.CODEC
                                    .fieldOf("start_pool")
                                    .forGetter(structure -> structure.startPool),
                            HeightProvider.CODEC
                                    .fieldOf("start_height")
                                    .forGetter(structure -> structure.startHeight)
                    ).apply(instance, NetherHeartShrineStructure::new)
            );

    @Override
    protected Optional<GenerationStub> findGenerationPoint(
            GenerationContext context
    ) {
        ChunkPos chunkPos = context.chunkPos();

        int x = chunkPos.getMiddleBlockX();
        int z = chunkPos.getMiddleBlockZ();

        Optional<BlockPos> floor = findSuitableFloor(context, x, z);

        if (floor.isEmpty()) {
            return Optional.empty();
        }

        BlockPos pos = floor.get().above();

        if (!isSuitableTerrain(context, pos)) {
            return Optional.empty();
        }

        System.out.println(
                "[Permadeath Revive] START CANDIDATE"
                        + " | Chunk: " + context.chunkPos()
                        + " | Position: " + pos
        );

        return JigsawPlacement.addPieces(
                context,
                startPool,
                Optional.empty(),
                1,
                pos,
                false,
                Optional.empty(),
                32,
                PoolAliasLookup.EMPTY,
                DimensionPadding.ZERO,
                LiquidSettings.APPLY_WATERLOGGING
        );
    }

    private boolean isSuitableTerrain(
            GenerationContext context,
            BlockPos origin
    ) {
        final int RADIUS = 6;
        final int MAX_GAP = 5;
        final int STRUCTURE_HEIGHT = 8;

        int baseY = origin.getY();

        int supportedColumns = 0;
        int totalColumns = 0;

        for (int dx = -RADIUS; dx <= RADIUS; dx++) {
            for (int dz = -RADIUS; dz <= RADIUS; dz++) {

                int x = origin.getX() + dx;
                int z = origin.getZ() + dz;

                NoiseColumn column = context.chunkGenerator().getBaseColumn(
                        x,
                        z,
                        context.heightAccessor(),
                        context.randomState()
                );

                totalColumns++;

                boolean hasGround = false;

                // Look for ground within five blocks below the foundation.
                for (int depth = 1; depth <= MAX_GAP + 1; depth++) {

                    BlockState state = column.getBlock(baseY - depth);

                    if (isValidGround(state)) {
                        hasGround = true;
                        break;
                    }

                    // Don't count terrain beneath lava.
                    if (!state.getFluidState().isEmpty()) {
                        break;
                    }
                }

                if (hasGround) {
                    supportedColumns++;
                }

                // Reject tall obstructions within the shrine's space.
                int obstructionCount = 0;

                for (int y = baseY; y < baseY + STRUCTURE_HEIGHT; y++) {

                    BlockState state = column.getBlock(y);

                    if (!state.isAir()) {
                        obstructionCount++;
                    }
                }

                // Allow at most two obstructing blocks per column.
                if (obstructionCount > 2) {
                    return false;
                }
            }
        }

        // At least 70% of the footprint must have nearby terrain.
        double supportRatio = (double) supportedColumns / totalColumns;

        return supportRatio >= 0.70;
    }

    private Optional<BlockPos> findSuitableFloor(
            GenerationContext context,
            int x,
            int z
    ) {
        NoiseColumn column = context.chunkGenerator().getBaseColumn(
                x,
                z,
                context.heightAccessor(),
                context.randomState()
        );

        final int maxY = 110;
        final int minY = 25;

        for (int y = maxY; y >= minY; y--) {

            BlockState ground = column.getBlock(y);
            BlockState above = column.getBlock(y + 1);

            boolean validGround = isValidGround(ground);

            boolean openSpace = above.isAir();

            if (validGround && openSpace) {
                return Optional.of(new BlockPos(x, y + 1, z));
            }
        }

        return Optional.empty();
    }

    private boolean isValidGround(BlockState state) {
        return state.is(Blocks.NETHERRACK)
                || state.is(Blocks.SOUL_SAND)
                || state.is(Blocks.SOUL_SOIL)
                || state.is(Blocks.BASALT)
                || state.is(Blocks.BLACKSTONE)
                || state.is(Blocks.CRIMSON_NYLIUM)
                || state.is(Blocks.WARPED_NYLIUM);
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.NETHER_HEART_SHRINE;
    }
}
