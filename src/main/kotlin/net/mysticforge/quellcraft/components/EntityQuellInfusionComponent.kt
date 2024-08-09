package net.mysticforge.quellcraft.components

import dev.onyxstudios.cca.api.v3.component.ComponentV3
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import net.minecraft.entity.LivingEntity
import net.minecraft.nbt.NbtCompound

interface IntComponent : ComponentV3 {
    fun getValue(): Int
}

class EntityQuellInfusionComponent(private val entity: LivingEntity) : IntComponent, AutoSyncedComponent {
    companion object {
        const val KEY = "quell_infusion"
    }

    private var infusionAmount = 0

    override fun getValue() = infusionAmount

    fun setValue(value: Int) {
        this.infusionAmount = value
        ModComponents.quellInfusion.sync(entity)
    }

    override fun readFromNbt(tag: NbtCompound) {
        infusionAmount = tag.getInt(KEY)
    }

    override fun writeToNbt(tag: NbtCompound) {
        tag.putInt(KEY, infusionAmount)
    }
}
