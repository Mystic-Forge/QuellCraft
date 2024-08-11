package net.mysticforge.quellcraft.util

import net.minecraft.nbt.NbtCompound
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.experimental.ExperimentalTypeInference

class NbtCompoundBuilder(private val nbtCompound: NbtCompound) {
    fun put(key: String, value: Int) = nbtCompound.putInt(key, value)
    fun put(key: String, value: String) = nbtCompound.putString(key, value)
    fun put(key: String, value: NbtCompound) = nbtCompound.put(key, value)

    fun build() = nbtCompound
}

@OptIn(ExperimentalContracts::class, ExperimentalTypeInference::class)
fun buildNbtCompound(@BuilderInference builderAction: NbtCompoundBuilder.() -> Unit): NbtCompound {
    contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
    return NbtCompoundBuilder(NbtCompound()).apply { builderAction() }.build()
}