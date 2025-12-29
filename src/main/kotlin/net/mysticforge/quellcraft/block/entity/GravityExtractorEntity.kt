package net.mysticforge.quellcraft.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.mysticforge.quellcraft.block.ModBlocks

class GravityExtractorEntity(blockPos: BlockPos, blockState: BlockState) :
    BlockEntity(ModBlocks.gravityExtractorEntityType, blockPos, blockState) {
}