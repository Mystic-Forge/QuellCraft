package net.mysticforge.quellcraft.block

import com.mojang.serialization.MapCodec
import net.fabricmc.fabric.api.networking.v1.PlayerLookup.world
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView
import net.minecraft.world.tick.ScheduledTickView
import net.mysticforge.quellcraft.block.entity.CrystalBlockEntity
import net.mysticforge.quellcraft.mixin.AbstractBlockSettingsAccessor
import net.mysticforge.quellcraft.state.property.ModProperties
import net.mysticforge.quellcraft.state.property.QuellTypeProperty
import java.util.*

class CrystalBlock(settings: Settings) : BlockWithEntity(
    settings
        .nonOpaque()
        .dynamicBounds()
        .luminance { state -> state.get(ModProperties.intensity).toInt() }
        .emissiveLighting { state, _, _ -> state.get(ModProperties.intensity).toInt() > 0 }
        .noCollision()
        .sounds(BlockSoundGroup.AMETHYST_BLOCK)
        .also { settings ->
            val settingsAccessor = settings as AbstractBlockSettingsAccessor

            settingsAccessor.setOffsetter { state, pos ->
                val l = MathHelper.hashCode(pos.getX(), 0, pos.getZ())
                val maxOffset = 0.25f
                val x = MathHelper.clamp(((l and 15L).toFloat() / 15.0f - 0.5) * 0.5, (-maxOffset).toDouble(), maxOffset.toDouble())
                val y = MathHelper.clamp(((l shr 8 and 15L).toFloat() / 15.0f - 0.5) * 0.5, (-maxOffset).toDouble(), maxOffset.toDouble())

                when (state[Properties.FACING]) {
                    Direction.UP, Direction.DOWN -> Vec3d(x, 0.0, y)
                    Direction.NORTH, Direction.SOUTH -> Vec3d(x, y, 0.0)
                    Direction.WEST, Direction.EAST -> Vec3d(0.0, y, x)
                    else -> error("Invalid direction")
                }
            }
        }

), Waterloggable {
    companion object {
        val CODEC: MapCodec<CrystalBlock> = createCodec(::CrystalBlock)
    }

    init {
        defaultState = defaultState
            .with(Properties.WATERLOGGED, false)
            .with(Properties.FACING, Direction.UP)
            .with(ModProperties.intensity, 0)
            .with(ModProperties.quellType, QuellTypeProperty.Null)
    }

    private val outlines = mapOf(
        Direction.UP to createCuboidShape(4.0, 0.0, 4.0, 12.0, 5.0, 12.0),
        Direction.DOWN to createCuboidShape(4.0, 11.0, 4.0, 12.0, 16.0, 12.0),
        Direction.NORTH to createCuboidShape(4.0, 4.0, 11.0, 12.0, 12.0, 16.0),
        Direction.SOUTH to createCuboidShape(4.0, 4.0, 0.0, 12.0, 12.0, 5.0),
        Direction.WEST to createCuboidShape(11.0, 4.0, 4.0, 16.0, 12.0, 12.0),
        Direction.EAST to createCuboidShape(0.0, 4.0, 4.0, 5.0, 12.0, 12.0),
    )

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape =
        state.getModelOffset(pos).let { offset -> outlines[state[Properties.FACING]]!!.offset(offset.x, offset.y, offset.z) }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getRenderType(state: BlockState?) = BlockRenderType.MODEL

    @Suppress("OVERRIDE_DEPRECATION")
    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        val direction = state.get(Properties.FACING)
        val blockPos = pos.offset(direction.opposite)
        return world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, direction)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val worldAccess: WorldAccess = ctx.world
        val blockPos = ctx.blockPos
        return defaultState
            .with(Properties.WATERLOGGED, worldAccess.getFluidState(blockPos).fluid === Fluids.WATER)
            .with(Properties.FACING, ctx.side)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getStateForNeighborUpdate(
        state: BlockState,
        world: WorldView,
        tickView: ScheduledTickView,
        pos: BlockPos,
        direction: Direction,
        neighborPos: BlockPos,
        neighborState: BlockState,
        random: Random
    ): BlockState {
//        if (state.get(Properties.WATERLOGGED) as Boolean) {
//            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
//        }
        return if (direction == (state.get(Properties.FACING) as Direction).opposite && !state.canPlaceAt(world, pos)) Blocks.AIR.defaultState
        else super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random)
    }

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>) {
        builder.add(Properties.WATERLOGGED, Properties.FACING, ModProperties.intensity, ModProperties.quellType)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState) = CrystalBlockEntity(pos, state)

    override fun onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity): BlockState {
        super.onBreak(world, pos, state, player)
        val blockEntity = world.getBlockEntity(pos) as? CrystalBlockEntity ?: return state
        blockEntity.onBreak(world, pos, state)
        return state
    }

    override fun <T : BlockEntity?> getTicker(world: World, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T>? {
        return validateTicker(
            type, ModBlocks.crystalBlockEntityType
        ) { blockWorld: World, pos: BlockPos, blockState: BlockState, blockEntity: CrystalBlockEntity ->
            blockEntity.tick(
                blockWorld,
                pos,
                blockState,
                blockEntity
            )
        }
    }

    override fun getCodec(): MapCodec<out BlockWithEntity?> = CODEC
}