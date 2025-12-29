package net.mysticforge.quellcraft.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.state.BlockState
import net.mysticforge.quellcraft.block.ModBlocks

class GravityExtractorEntity(blockPos: BlockPos, blockState: BlockState) :
    BlockEntity(ModBlocks.gravityExtractorEntityType, blockPos, blockState),
    BlockEntityTicker<GravityExtractorEntity> {

    val renderState = GravityExtractorRenderState()

    override fun tick(
        level: Level,
        blockPos: BlockPos,
        blockState: BlockState,
        blockEntity: GravityExtractorEntity
    ) {
        if (!level.isClientSide) return
        renderState.time += 0.05f
    }

    class GravityExtractorRenderState {
        var time: Float = 0f
        val rotation: Float
            get() = time
        val y
            get() = (Math.sin(time.toDouble()).toFloat() + 1f)
    }
}