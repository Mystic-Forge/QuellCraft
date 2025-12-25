package net.mysticforge.quellcraft.block

import com.mojang.serialization.MapCodec
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.InteractionResult
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.mysticforge.quellcraft.block.entity.ProjectDeskEntity


class ProjectDeskBlock(settings: Properties) : BaseEntityBlock(settings.noOcclusion().sound(SoundType.WOOD)) {
    override fun codec(): MapCodec<out BaseEntityBlock> {
        return simpleCodec(::ProjectDeskBlock)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = ProjectDeskEntity(pos, state)

    public override fun useWithoutItem(state: BlockState, world: Level, pos: BlockPos, player: Player, hit: BlockHitResult): InteractionResult {
        if (!world.isClientSide) {
            val screenHandlerFactory = state.getMenuProvider(world, pos)
            if (screenHandlerFactory != null) {
                player.openMenu(screenHandlerFactory)
            }
        }

        return InteractionResult.SUCCESS
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        var state = super.getStateForPlacement(ctx)
        val playerFacing = ctx.horizontalDirection
        if (state != null && playerFacing != null) {
            state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, playerFacing.opposite)
        }
        return state!!
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING)
    }
}