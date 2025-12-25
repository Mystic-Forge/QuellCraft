package net.mysticforge.quellcraft.block

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.mysticforge.quellcraft.state.property.ModProperties.quellLevel

class QuellBlock(settings: Properties) : Block(settings.sound(SoundType.MUDDY_MANGROVE_ROOTS).randomTicks()) {
    override fun randomTick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        trySpread(
            state, world, pos, UPDATE_SHAPE_ORDER.random(), 4, random
        )
    }


    fun trySpread(state: BlockState, world: ServerLevel, pos: BlockPos, direction: Direction, attemptsLeft: Int = 4, random: RandomSource) {
        var thisLevel = state.getValue(quellLevel)

        val otherPos = pos.relative(direction)
        var otherBlockState = world.getBlockState(otherPos)
        if (otherBlockState.isAir) {
            if (attemptsLeft > 0 && thisLevel > 0) trySpread(state, world, pos, UPDATE_SHAPE_ORDER.random(), attemptsLeft - 1, random)
            return
        }

        if (otherBlockState.block == this) {
            val otherLevel = otherBlockState.getValue(quellLevel)
            val difference = (thisLevel - otherLevel)

            if (difference > 0 && thisLevel > 1) {
                otherBlockState = otherBlockState.setValue(quellLevel, otherLevel + 1)
                world.setBlock(otherPos, otherBlockState, UPDATE_ALL)
                world.setBlock(pos, state.setValue(quellLevel, thisLevel - 1), UPDATE_ALL)

                if (attemptsLeft > 0) trySpread(otherBlockState, world, otherPos, if (random.nextBoolean()) direction else UPDATE_SHAPE_ORDER.random(), attemptsLeft - 1, random)
            } else if (difference < 0 && otherLevel > 1) {
                val newBlockState = state.setValue(quellLevel, thisLevel + 1)
                world.setBlock(pos, newBlockState, UPDATE_ALL)
                world.setBlock(otherPos, otherBlockState.setValue(quellLevel, otherLevel - 1), UPDATE_ALL)

                if (attemptsLeft > 0) trySpread(newBlockState, world, pos, if (random.nextBoolean()) direction.opposite else UPDATE_SHAPE_ORDER.random(), attemptsLeft - 1, random)
            }
        } else {
            world.setBlock(otherPos, ModBlocks.quellBlock.defaultBlockState().setValue(quellLevel, 2), UPDATE_ALL)
        }

        thisLevel = state.getValue(quellLevel)
        if (thisLevel > 0 && random.nextFloat() < 0.1f) world.setBlock(pos, state.setValue(quellLevel, thisLevel - 1), UPDATE_ALL)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(quellLevel)
    }
}