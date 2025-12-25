package net.mysticforge.quellcraft.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.HorizontalConnectingBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.world.World
import net.minecraft.world.WorldView
import net.minecraft.world.tick.ScheduledTickView
import net.mysticforge.quellcraft.block.entity.ProjectDeskEntity


class ThaumicAssemblerBlock(settings: Settings) : Block(settings) {
    override fun getCodec(): MapCodec<out Block> {
        return createCodec(::ThaumicAssemblerBlock)
    }

    override fun getPlacementState(ctx: ItemPlacementContext?): BlockState? {
        val world = ctx?.world
        val pos = ctx?.blockPos
        var state = super.getPlacementState(ctx)
        if (world != null && pos != null && state != null) {
            val northBlock = world.getBlockState(pos.north()).block
            val southBlock = world.getBlockState(pos.south()).block
            val eastBlock = world.getBlockState(pos.east()).block
            val westBlock = world.getBlockState(pos.west()).block

            state = state.with(Properties.NORTH, northBlock is ThaumicAssemblerBlock)
                .with(Properties.SOUTH, southBlock is ThaumicAssemblerBlock)
                .with(Properties.EAST, eastBlock is ThaumicAssemblerBlock)
                .with(Properties.WEST, westBlock is ThaumicAssemblerBlock)
        }
        return state
    }

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
        if (direction.getAxis().isHorizontal())
            return state.with(
                HorizontalConnectingBlock.FACING_PROPERTIES.get(direction),
                neighborState.block == ModBlocks.thaumicAssembler
            )
        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(Properties.NORTH).add(Properties.EAST).add(Properties.SOUTH).add(Properties.WEST)
    }
}