package net.mysticforge.quellcraft.block.entity

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.storage.TagValueOutput
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.core.BlockPos
import net.minecraft.world.phys.Vec3
import net.minecraft.world.level.Level
import net.mysticforge.quellcraft.block.ModBlocks
import net.mysticforge.quellcraft.quellmanagement.*
import net.mysticforge.quellcraft.state.property.ModProperties
import net.mysticforge.quellcraft.state.property.QuellType
import kotlin.math.ceil


class CrystalBlockEntity(blockPos: BlockPos, blockState: BlockState) :
    BlockEntity(ModBlocks.crystalBlockEntityType, blockPos, blockState),
    BlockEntityTicker<CrystalBlockEntity> {
    companion object {
        const val NBT_KEY = "quell_content"
    }

    var quellContent: QuellContent = QuellContent.Empty
        set(value) {
            field = value
            when (value) {
                is QuellContent.Empty -> {
                    if (blockState.getValue(ModProperties.intensity) != 0) {
                        level?.setBlockAndUpdate(worldPosition, blockState.setValue(ModProperties.intensity, 0))
                        level?.sendBlockUpdated(worldPosition, blockState, blockState, Block.UPDATE_ALL)
                    }
                    return
                }

                is QuellContent.Filled -> {
                    val powerLevel = ceil((value.storedThaum / 50f) * 5).toInt().coerceAtMost(5)
                    level?.setBlockAndUpdate(
                        worldPosition, blockState
                            .setValue(ModProperties.intensity, powerLevel)
                            .setValue(ModProperties.quellType, value.quellType.asProperty())
                    )
                    level?.sendBlockUpdated(worldPosition, blockState, blockState, Block.UPDATE_ALL)
                }
            }
        }

    /**
     * Utility for getting the position of the visual model. This is useful since the model has random global offsets and 6 rotations
     * @return The world position of the model
     */
    private fun BlockState.getModelPos(pos: BlockPos): Vec3 {
        val modelOffset = getOffset(pos)
        val facingOffset = getValue(BlockStateProperties.FACING).opposite.step().mul(0.5f)
        return pos.center.add(facingOffset.x.toDouble(), facingOffset.y.toDouble(), facingOffset.z.toDouble()).add(modelOffset)
    }

    override fun tick(world: Level, pos: BlockPos, state: BlockState, blockEntity: CrystalBlockEntity) {
        if (!world.isClientSide) return

        val random = world.random
        val modelPos = state.getModelPos(pos)

        (quellContent as? QuellContent.Filled)?.let { filledContent ->
            if (random.nextInt(50) < filledContent.storedThaum) {
                when (filledContent.quellType) {
                    QuellType.Void -> {
                        val randomOffset = Vec3(random.nextDouble() * 5 - 2.5, random.nextDouble() * 5 - 4, random.nextDouble() * 5 - 2.5).scale(0.2)
                        val targetOffset = randomOffset.normalize().scale(0.2)
                        world.addParticle(
                            ParticleTypes.PORTAL,
                            modelPos.x + targetOffset.x,
                            modelPos.y + targetOffset.y,
                            modelPos.z + targetOffset.z,
                            randomOffset.x,
                            randomOffset.y,
                            randomOffset.z
                        )
                    }

                    QuellType.Thermal -> {
                        val randomOffset = Vec3(random.nextDouble() * 5 - 2.5, random.nextDouble() * 5 - 4, random.nextDouble() * 5 - 2.5).scale(0.2)
                        val targetOffset = randomOffset.normalize().scale(0.2)
                        world.addParticle(
                            ParticleTypes.SMOKE,
                            modelPos.x + targetOffset.x,
                            modelPos.y + targetOffset.y,
                            modelPos.z + targetOffset.z,
                            0.0,
                            0.05,
                            0.0
                        )
                    }

                    QuellType.Life -> {
                        val randomOffset = Vec3(random.nextDouble() * 5 - 2.5, random.nextDouble() * 5 - 4, random.nextDouble() * 5 - 2.5).scale(0.2)
                        val targetOffset = randomOffset.normalize().scale(0.2)
                        world.addParticle(
                            ParticleTypes.HAPPY_VILLAGER,
                            modelPos.x + targetOffset.x,
                            modelPos.y + targetOffset.y,
                            modelPos.z + targetOffset.z,
                            0.0,
                            0.05,
                            0.0
                        )
                    }
                }
            }
        }
    }

    fun onBreak(world: Level, pos: BlockPos, state: BlockState) {
        world.emitQuell(quellContent, state.getModelPos(pos), 5.0)
        val point = state.getModelPos(pos)
        world.doQuellExplosion(quellContent, point, 5.0)
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag = TagValueOutput.createWithoutContext(null).writeQuellContent(NBT_KEY, quellContent).buildResult()

    override fun loadAdditional(view: ValueInput) {
        super.loadAdditional(view)
        quellContent = view.readQuellContent(NBT_KEY)
    }

    override fun saveAdditional(view: ValueOutput) {
        super.saveAdditional(view)
        view.writeQuellContent(NBT_KEY, quellContent)
    }

    override fun getRenderData() = quellContent
}