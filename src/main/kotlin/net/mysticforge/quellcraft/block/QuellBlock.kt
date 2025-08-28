package net.mysticforge.quellcraft.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.state.StateManager
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.mysticforge.quellcraft.state.property.ModProperties.quellLevel

class QuellBlock(settings: Settings) : Block(settings.sounds(BlockSoundGroup.MUDDY_MANGROVE_ROOTS).ticksRandomly()) {
    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        trySpread(
            state, world, pos, DIRECTIONS.random()
        )
    }

    fun trySpread(state: BlockState, world: ServerWorld, pos: BlockPos, direction: Direction, attemptsLeft: Int = 4) {
        val thisLevel = state.get(quellLevel)

        val otherPos = pos.offset(direction)
        var otherBlockState = world.getBlockState(otherPos)
        if (otherBlockState.isAir) {
            if (attemptsLeft > 0 && thisLevel > 0) trySpread(state, world, pos, DIRECTIONS.random(), attemptsLeft - 1)
            return
        }

        if (otherBlockState.block == this) {
            val otherLevel = otherBlockState.get(quellLevel)
            val difference = (thisLevel - otherLevel)

            if (difference > 0 && thisLevel > 1) {
                otherBlockState = otherBlockState.with(quellLevel, otherLevel + 1);
                world.setBlockState(otherPos, otherBlockState, NOTIFY_ALL)
                world.setBlockState(pos, state.with(quellLevel, thisLevel - 1), NOTIFY_ALL)

                if (attemptsLeft > 0) trySpread(otherBlockState, world, otherPos, direction, attemptsLeft - 1)
            } else if (difference < 0 && otherLevel > 1) {
                val newBlockState = state.with(quellLevel, thisLevel + 1);
                world.setBlockState(pos, newBlockState, NOTIFY_ALL)
                world.setBlockState(otherPos, otherBlockState.with(quellLevel, otherLevel - 1), NOTIFY_ALL)

                if (attemptsLeft > 0) trySpread(newBlockState, world, pos, direction.opposite, attemptsLeft - 1)
            }
        } else {
            world.setBlockState(otherPos, ModBlocks.quellBlock.defaultState.with(quellLevel, 2), NOTIFY_ALL)
        }
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(quellLevel)
    }
}