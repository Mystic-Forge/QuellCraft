package net.mysticforge.quellcraft.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.mysticforge.quellcraft.block.entity.ThaumicAssemblerEntity


class ThaumicAssembler(settings: Settings) : BlockWithEntity(settings) {
    override fun getCodec(): MapCodec<out BlockWithEntity?>? {
        return createCodec(::ThaumicAssembler)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = ThaumicAssemblerEntity(pos, state)

    public override fun onUse(state: BlockState, world: World, pos: BlockPos?, player: PlayerEntity, hit: BlockHitResult?): ActionResult {
        if (!world.isClient) {
            val screenHandlerFactory = state.createScreenHandlerFactory(world, pos)
            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory)
            }
        }
        return ActionResult.SUCCESS
    }
}