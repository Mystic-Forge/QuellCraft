package net.mysticforge.quellcraft.block.entity

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.mysticforge.quellcraft.block.ModBlocks
import net.mysticforge.quellcraft.components.ModComponents
import net.mysticforge.quellcraft.entity.EntityUtilities
import net.mysticforge.quellcraft.state.property.QuellType


class CrystalBlockEntity(blockPos: BlockPos, blockState: BlockState) : BlockEntity(ModBlocks.crystalBlockEntityType, blockPos, blockState), BlockEntityTicker<CrystalBlockEntity> {
    var storedThaum = 0
        private set

    var quellType = QuellType.NONE
        private set

    override fun tick(world: World, pos: BlockPos, state: BlockState, blockEntity: CrystalBlockEntity) {
        if (!world.isClient()) return

        val offset = Vec3d(0.5, 0.5, 0.5)
        val random = world.random
        for (i in 0 until 1) {
            val randomOffset = offset.add(Vec3d(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5))
            world.addParticle(ParticleTypes.ASH, pos.x.toDouble() + randomOffset.x, pos.y.toDouble() + randomOffset.y, pos.z.toDouble() + randomOffset.z, 0.0, 0.0, 0.0)
        }
    }

    fun addThaum(type: QuellType, amount: Int) {
        if (quellType == type) {
            storedThaum += amount
        } else {
            quellType = type
            storedThaum = amount
        }
    }

    fun onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity) {
        EntityUtilities.getEntitiesOfType<LivingEntity>(world, Box(pos).expand(3.0)).forEach { entity ->
            val quellInfusionComponent = ModComponents.quellInfusion.get(entity)
            println("Entity ${entity.name.string} has ${quellInfusionComponent.getValue()} thaum")
            quellInfusionComponent.setValue(quellInfusionComponent.getValue() + storedThaum)
            println("Added $storedThaum to ${entity.name.string}")
            println("Entity ${entity.name.string} now has ${quellInfusionComponent.getValue()} thaum")
        }
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener> = BlockEntityUpdateS2CPacket.create(this)

    override fun toInitialChunkDataNbt(): NbtCompound = createNbt()

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        if (nbt.contains("stored_thaum")) {
            this.storedThaum = nbt.getShort("stored_thaum").toInt()

//            this.markDirty()
        }

        if (nbt.contains("quell_type")) {
            val typeName = nbt.getString("quell_type")
            val type = QuellType.entries.first { it.typeName == typeName }
            if (quellType != type){
                quellType = type
                world?.updateListeners(pos, cachedState, cachedState, Block.NOTIFY_ALL)
            }

            println("Read quell type: $quellType")

//            this.markDirty()
        }
    }

    override fun writeNbt(nbt: NbtCompound) {
        super.writeNbt(nbt)
        nbt.putShort("stored_thaum", storedThaum.toShort())
        nbt.putString("quell_type", quellType.typeName)

        println("Wrote quell type: $quellType")
    }

    override fun getRenderData() = quellType
}