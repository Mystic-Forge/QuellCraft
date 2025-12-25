package net.mysticforge.quellcraft.block

import com.mojang.serialization.MapCodec
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.CrossCollisionBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.InteractionResult
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.ScheduledTickAccess
import net.mysticforge.quellcraft.block.entity.ProjectDeskEntity


class ThaumicAssemblerBlock(settings: Properties) : Block(settings) {
    override fun codec(): MapCodec<out Block> {
        return simpleCodec(::ThaumicAssemblerBlock)
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState? {
        val world = ctx.level
        val pos = ctx.clickedPos
        var state = super.getStateForPlacement(ctx)
        if (world != null && pos != null && state != null) {
            val northBlock = world.getBlockState(pos.north()).block
            val southBlock = world.getBlockState(pos.south()).block
            val eastBlock = world.getBlockState(pos.east()).block
            val westBlock = world.getBlockState(pos.west()).block

            state = state.setValue(BlockStateProperties.NORTH, northBlock is ThaumicAssemblerBlock)
                .setValue(BlockStateProperties.SOUTH, southBlock is ThaumicAssemblerBlock)
                .setValue(BlockStateProperties.EAST, eastBlock is ThaumicAssemblerBlock)
                .setValue(BlockStateProperties.WEST, westBlock is ThaumicAssemblerBlock)
        }
        return state
    }

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
        if (direction.axis.isHorizontal)
            return state.setValue(
                CrossCollisionBlock.PROPERTY_BY_DIRECTION.get(direction),
                neighborState.block == ModBlocks.thaumicAssembler
            )
        return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(BlockStateProperties.NORTH).add(BlockStateProperties.EAST).add(BlockStateProperties.SOUTH).add(BlockStateProperties.WEST)
    }
}