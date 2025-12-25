package net.mysticforge.quellcraft.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.mysticforge.quellcraft.block.entity.ProjectDeskEntity


class ProjectDeskBlock(settings: Settings) : BlockWithEntity(settings.nonOpaque().sounds(BlockSoundGroup.WOOD)) {
    override fun getCodec(): MapCodec<out BlockWithEntity> {
        return createCodec(::ProjectDeskBlock)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = ProjectDeskEntity(pos, state)

    public override fun onUse(state: BlockState, world: World, pos: BlockPos?, player: PlayerEntity, hit: BlockHitResult?): ActionResult {
        if (!world.isClient) {
            val screenHandlerFactory = state.createScreenHandlerFactory(world, pos)
            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory)
            }
        }

        return ActionResult.SUCCESS
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        var state = super.getPlacementState(ctx)
        val playerFacing = ctx.horizontalPlayerFacing
        if (state != null && playerFacing != null) {
            state = state.with(Properties.HORIZONTAL_FACING, playerFacing.opposite)
        }
        return state!!
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(Properties.HORIZONTAL_FACING)
    }
}