package net.mysticforge.quellcraft.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.*
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.mysticforge.quellcraft.block.entity.CrystalBlockEntity
import net.mysticforge.quellcraft.mixin.AbstractBlockSettingsAccessor
import net.mysticforge.quellcraft.state.property.ModProperties
import net.mysticforge.quellcraft.state.property.QuellTypeProperty

class CrystalBlock(settings: Properties) : BaseEntityBlock(
    settings
        .noOcclusion()
        .dynamicShape()
        .lightLevel { state -> state.getValue(ModProperties.intensity).toInt() }
        .emissiveRendering { state, _, _ -> state.getValue(ModProperties.intensity).toInt() > 0 }
        .noCollission()
        .sound(SoundType.AMETHYST)
        .also { settings ->
            val settingsAccessor = settings as AbstractBlockSettingsAccessor

            settingsAccessor.setOffsetFunction { state, pos ->
                @Suppress("DEPRECATION")
                val l = Mth.getSeed(pos.x, 0, pos.z)
                val maxOffset = 0.25f
                val x = Mth.clamp(((l and 15L).toFloat() / 15.0f - 0.5) * 0.5, (-maxOffset).toDouble(), maxOffset.toDouble())
                val y = Mth.clamp(((l shr 8 and 15L).toFloat() / 15.0f - 0.5) * 0.5, (-maxOffset).toDouble(), maxOffset.toDouble())

                when (state.getValue(BlockStateProperties.FACING)) {
                    Direction.UP, Direction.DOWN -> Vec3(x, 0.0, y)
                    Direction.NORTH, Direction.SOUTH -> Vec3(x, y, 0.0)
                    Direction.WEST, Direction.EAST -> Vec3(0.0, y, x)
                    else -> error("Invalid direction")
                }
            }
        }

), SimpleWaterloggedBlock {
    companion object {
        val CODEC: MapCodec<CrystalBlock> = simpleCodec(::CrystalBlock)
    }

    init {
        registerDefaultState(
            defaultBlockState()
                .setValue(BlockStateProperties.WATERLOGGED, false)
                .setValue(BlockStateProperties.FACING, Direction.UP)
                .setValue(ModProperties.intensity, 0)
                .setValue(ModProperties.quellType, QuellTypeProperty.Null)
        )
    }

    private val outlines = mapOf(
        Direction.UP to box(4.0, 0.0, 4.0, 12.0, 5.0, 12.0),
        Direction.DOWN to box(4.0, 11.0, 4.0, 12.0, 16.0, 12.0),
        Direction.NORTH to box(4.0, 4.0, 11.0, 12.0, 12.0, 16.0),
        Direction.SOUTH to box(4.0, 4.0, 0.0, 12.0, 12.0, 5.0),
        Direction.WEST to box(11.0, 4.0, 4.0, 16.0, 12.0, 12.0),
        Direction.EAST to box(0.0, 4.0, 4.0, 5.0, 12.0, 12.0),
    )

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape =
        state.getOffset(pos).let { offset -> outlines[state.getValue(BlockStateProperties.FACING)]!!.move(offset.x, offset.y, offset.z) }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getRenderShape(state: BlockState) = RenderShape.MODEL

    @Suppress("OVERRIDE_DEPRECATION")
    override fun canSurvive(state: BlockState, world: LevelReader, pos: BlockPos): Boolean {
        val direction = state.getValue(BlockStateProperties.FACING)
        val blockPos = pos.relative(direction.opposite)
        return world.getBlockState(blockPos).isFaceSturdy(world, blockPos, direction)
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState? {
        val worldAccess: LevelAccessor = ctx.level
        val blockPos = ctx.clickedPos
        return defaultBlockState()
            .setValue(BlockStateProperties.WATERLOGGED, worldAccess.getFluidState(blockPos).type === Fluids.WATER)
            .setValue(BlockStateProperties.FACING, ctx.clickedFace)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun updateShape(
        state: BlockState,
        world: LevelReader,
        tickView: ScheduledTickAccess,
        pos: BlockPos,
        direction: Direction,
        neighborPos: BlockPos,
        neighborState: BlockState,
        random: RandomSource
    ): BlockState {
//        if (state.get(Properties.WATERLOGGED) as Boolean) {
//            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
//        }
        return if (direction == (state.getValue(BlockStateProperties.FACING) as Direction).opposite && !state.canSurvive(world, pos)) Blocks.AIR.defaultBlockState()
        else super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        builder.add(BlockStateProperties.WATERLOGGED, BlockStateProperties.FACING, ModProperties.intensity, ModProperties.quellType)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState) = CrystalBlockEntity(pos, state)

    override fun playerWillDestroy(world: Level, pos: BlockPos, state: BlockState, player: Player): BlockState {
        super.playerWillDestroy(world, pos, state, player)
        val blockEntity = world.getBlockEntity(pos) as? CrystalBlockEntity ?: return state
        blockEntity.onBreak(world, pos, state)
        return state
    }

    override fun <T : BlockEntity?> getTicker(world: Level, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T>? {
        return createTickerHelper(
            type, ModBlocks.crystalBlockEntityType
        ) { blockWorld: Level, pos: BlockPos, blockState: BlockState, blockEntity: CrystalBlockEntity ->
            blockEntity.tick(
                blockWorld,
                pos,
                blockState,
                blockEntity
            )
        }
    }

    override fun codec(): MapCodec<out BaseEntityBlock?> = CODEC
}