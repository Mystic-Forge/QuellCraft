package net.mysticforge.quellcraft.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.VoxelShape
import net.mysticforge.quellcraft.block.entity.GravityExtractorEntity

class GravityExtractorBlock(settings: Properties) : QuellcraftBigBlock(settings.noOcclusion()) {
    override val proxyOffsets: List<BlockPos> get() = mutableListOf<BlockPos>().apply {
        for (x in -1..1) {
            for (y in 0..2) {
                for (z in -1..1) {
                    if (x == 0 && y == 0 && z == 0) continue
                    add(BlockPos(x, y, z))
                }
            }
        }
    }

    override val shape: VoxelShape get() = box(-16.0, 0.0, -16.0, 32.0, 48.0, 32.0)

    override fun getProxyBlock(): QuellcraftProxyBlock = ModBlocks.gravityExtractorProxy

    override fun codec(): MapCodec<out BaseEntityBlock> = codec

    override fun newBlockEntity(
        blockPos: BlockPos,
        blockState: BlockState
    ): BlockEntity = GravityExtractorEntity(blockPos, blockState)

    companion object {
        val codec: MapCodec<GravityExtractorBlock> = simpleCodec(::GravityExtractorBlock)
    }
}