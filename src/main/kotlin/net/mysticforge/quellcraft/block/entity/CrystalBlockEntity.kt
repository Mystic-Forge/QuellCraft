package net.mysticforge.quellcraft.block.entity

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.particle.ParticleTypes
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.mysticforge.quellcraft.block.ModBlocks
import net.mysticforge.quellcraft.quellmanagement.*
import net.mysticforge.quellcraft.state.property.ModProperties
import net.mysticforge.quellcraft.state.property.QuellType
import net.mysticforge.quellcraft.util.nextDouble
import kotlin.math.ceil
import kotlin.random.Random


class CrystalBlockEntity(blockPos: BlockPos, blockState: BlockState) :
    BlockEntity(ModBlocks.crystalBlockEntityType, blockPos, blockState),
    BlockEntityTicker<CrystalBlockEntity>
{
    companion object {
        const val NBT_KEY = "quell_content"
    }

    var quellContent: QuellContent = QuellContent.Empty
    set (value) {
        field = value
        val powerLevel = ceil((value.storedThaum / 50f) * 5).toInt().coerceAtMost(5)
        getWorld()?.setBlockState(pos, cachedState.with(ModProperties.intensity, powerLevel))
        world?.updateListeners(pos, cachedState, cachedState, Block.NOTIFY_ALL)
    }

    /**
     * Utility for getting the position of the visual model. This is useful since the model has random global offsets and 6 rotations
     * @return The world position of the model
     */
    private fun BlockState.getModelPos(world: World, pos: BlockPos): Vec3d {
        val modelOffset = getModelOffset(world, pos)
        val facingOffset = get(Properties.FACING).opposite.unitVector.mul(0.5f)
        return pos.toCenterPos().add(facingOffset.x.toDouble(), facingOffset.y.toDouble(), facingOffset.z.toDouble()).add(modelOffset)
    }

    override fun tick(world: World, pos: BlockPos, state: BlockState, blockEntity: CrystalBlockEntity) {
        if (!world.isClient()) return

        val random = world.random
        val modelPos = state.getModelPos(world, pos)

        (quellContent as? QuellContent.Filled)?.let { filledContent ->
            if (random.nextInt(50) < filledContent.storedThaum) {
                when (filledContent.quellType) {
                    QuellType.VOID -> {
                        val randomOffset = Vec3d(random.nextDouble() * 5 - 2.5, random.nextDouble() * 5 - 4, random.nextDouble() * 5 - 2.5).multiply(0.2)
                        val targetOffset = randomOffset.normalize().multiply(0.2);
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

                    QuellType.THERMAL -> {
                        val randomOffset = Vec3d(random.nextDouble() * 5 - 2.5, random.nextDouble() * 5 - 4, random.nextDouble() * 5 - 2.5).multiply(0.2)
                        val targetOffset = randomOffset.normalize().multiply(0.2);
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
                }
            }
        }
    }

    fun onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity) {
        world.emitQuell(quellContent, state.getModelPos(world, pos), 5.0)
        val point = state.getModelPos(world, pos)
        world.doQuellExplosion(quellContent, point, 5.0)
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener> = BlockEntityUpdateS2CPacket.create(this)

    override fun toInitialChunkDataNbt(): NbtCompound = NbtCompound().writeQuellContent(NBT_KEY, quellContent)

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        quellContent = nbt.readQuellContent(NBT_KEY)
    }

    override fun writeNbt(nbt: NbtCompound) {
        super.writeNbt(nbt)
        nbt.writeQuellContent(NBT_KEY, quellContent)
    }

    override fun getRenderData() = quellContent
}