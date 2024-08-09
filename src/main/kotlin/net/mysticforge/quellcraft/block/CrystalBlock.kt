package net.mysticforge.quellcraft.block

import net.minecraft.block.*
import net.minecraft.block.AbstractBlock.Offsetter
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView
import net.mysticforge.quellcraft.block.entity.CrystalBlockEntity
import java.util.*

object CrystalBlock : BlockWithEntity(
    Settings.create()
        .nonOpaque()
        .dynamicBounds()
//        .luminance { light }
//        .emissiveLighting { _, _, _ -> light > 0 }
        .noCollision()
        .sounds(BlockSoundGroup.AMETHYST_BLOCK)
        .offset(OffsetType.XZ)
        .also { settings ->
            val offsetterField = Settings::class.java.declaredFields.first { it.type == Optional::class.java }

            @Suppress("UNCHECKED_CAST")
            val oldOffsetter = offsetterField.get(settings) as Optional<Offsetter>

            offsetterField.set(settings, Optional.of(Offsetter { state, world, pos ->
                val offset = oldOffsetter.get().evaluate(state, world, pos)

                when (state[Properties.FACING]) {
                    Direction.UP, Direction.DOWN -> offset
                    Direction.NORTH, Direction.SOUTH -> Vec3d(offset.x, offset.z, 0.0)
                    Direction.WEST, Direction.EAST -> Vec3d(0.0, offset.z, offset.x)
                    else -> error("Invalid direction")
                }
            }))
        }
), Waterloggable {

    private val outlines = mapOf(
        Direction.UP to createCuboidShape(4.0, 0.0, 4.0, 12.0, 5.0, 12.0),
        Direction.DOWN to createCuboidShape(4.0, 11.0, 4.0, 12.0, 16.0, 12.0),
        Direction.NORTH to createCuboidShape(4.0, 4.0, 11.0, 12.0, 12.0, 16.0),
        Direction.SOUTH to createCuboidShape(4.0, 4.0, 0.0, 12.0, 12.0, 5.0),
        Direction.WEST to createCuboidShape(11.0, 4.0, 4.0, 16.0, 12.0, 12.0),
        Direction.EAST to createCuboidShape(0.0, 4.0, 4.0, 5.0, 12.0, 12.0),
    )

    init {
        defaultState = defaultState.with(Properties.WATERLOGGED, false).with(Properties.FACING, Direction.UP)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape =
        state.getModelOffset(world, pos).let { offset -> outlines[state[Properties.FACING]]!!.offset(offset.x, offset.y, offset.z) }

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
        state: BlockState, direction: Direction, neighborState: BlockState?, world: WorldAccess, pos: BlockPos?, neighborPos: BlockPos?
    ): BlockState? {
        if (state.get(Properties.WATERLOGGED) as Boolean) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }
        return if (direction == (state.get(Properties.FACING) as Direction).opposite && !state.canPlaceAt(world, pos)) Blocks.AIR.defaultState
        else super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>) {
        builder.add(Properties.WATERLOGGED, Properties.FACING)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState) = CrystalBlockEntity(pos, state)

    override fun onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity) {
        super.onBreak(world, pos, state, player)
        val blockEntity = world?.getBlockEntity(pos) as? CrystalBlockEntity ?: return
        blockEntity.onBreak(world, pos, state, player)
    }

    override fun <T : BlockEntity?> getTicker(world: World, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T>? {
        return checkType(
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
}