package net.mysticforge.quellcraft.util

import com.mojang.serialization.Codec
import net.minecraft.nbt.NbtCompound
import net.minecraft.storage.NbtWriteView
import net.minecraft.storage.WriteView
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.experimental.ExperimentalTypeInference

class WriteViewBuilder<WV : WriteView>(private val writeView: WV) {
    fun put(key: String, value: Int) = writeView.putInt(key, value)
    fun put(key: String, value: String) = writeView.putString(key, value)
    fun <T> put(key: String, codec: Codec<T>, value: T) = writeView.put(key, codec, value)

    fun build() = writeView
}

@OptIn(ExperimentalContracts::class, ExperimentalTypeInference::class)
fun <WV : WriteView> WV.modify(@BuilderInference builderAction: WriteViewBuilder<WV>.() -> Unit): WriteViewBuilder<WV> {
    contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
    return WriteViewBuilder(this).apply(builderAction)
}

@OptIn(ExperimentalContracts::class, ExperimentalTypeInference::class)
fun createNbtCompound(@BuilderInference builderAction: WriteViewBuilder<NbtWriteView>.() -> Unit): NbtCompound {
    contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
    return WriteViewBuilder(NbtWriteView.create(null)).apply(builderAction).build().nbt
}