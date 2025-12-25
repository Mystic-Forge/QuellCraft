package net.mysticforge.quellcraft.util

import com.mojang.serialization.Codec
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.storage.TagValueOutput
import net.minecraft.world.level.storage.ValueOutput
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.experimental.ExperimentalTypeInference

class WriteViewBuilder<WV : ValueOutput>(private val writeView: WV) {
    fun put(key: String, value: Int) = writeView.putInt(key, value)
    fun put(key: String, value: String) = writeView.putString(key, value)
    fun <T> put(key: String, codec: Codec<T>, value: T) = writeView.store(key, codec, value)

    fun build() = writeView
}

@OptIn(ExperimentalContracts::class, ExperimentalTypeInference::class)
fun <WV : ValueOutput> WV.modify(@BuilderInference builderAction: WriteViewBuilder<WV>.() -> Unit): WriteViewBuilder<WV> {
    contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
    return WriteViewBuilder(this).apply(builderAction)
}

@OptIn(ExperimentalContracts::class, ExperimentalTypeInference::class)
fun createNbtCompound(@BuilderInference builderAction: WriteViewBuilder<TagValueOutput>.() -> Unit): CompoundTag {
    contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
    return WriteViewBuilder(TagValueOutput.createWithoutContext(null)).apply(builderAction).build().buildResult()
}