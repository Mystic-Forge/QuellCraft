package net.mysticforge.quellcraft.quellmanagement

import net.minecraft.nbt.NbtCompound
import net.minecraft.storage.ReadView
import net.minecraft.storage.WriteView
import net.mysticforge.quellcraft.quellmanagement.QuellContent.Empty
import net.mysticforge.quellcraft.quellmanagement.QuellContent.Filled
import net.mysticforge.quellcraft.state.property.QuellType
import net.mysticforge.quellcraft.util.createNbtCompound
import net.mysticforge.quellcraft.util.modify

sealed interface QuellContent {
    data object Empty : QuellContent {
        override val storedThaum = 0

        override fun plus(other: QuellContent) = other
    }

    data class Filled(
        val quellType: QuellType,
        override val storedThaum: Int
    ) : QuellContent {
        init {
            require(storedThaum > 0)
        }

        operator fun plus(amount: Int) = if (storedThaum + amount <= 0) Empty else copy(storedThaum = storedThaum + amount)

        operator fun minus(amount: Int) = this + -amount

        override fun plus(other: QuellContent): QuellContent {
            when (other) {
                Empty -> return this
                is Filled -> {
                    require(quellType == other.quellType) { "Cannot combine QuellContent of different types: $quellType and ${other.quellType}" }
                    return this + other.storedThaum
                }
            }
        }
    }

    val storedThaum: Int

    fun isCompatibleWith(other: QuellContent): Boolean {
        return when (this) {
            Empty -> true
            is Filled -> other is Empty || other is Filled && quellType == other.quellType
        }
    }

    operator fun plus(other: QuellContent): QuellContent
}

fun <WV : WriteView> WV.writeQuellContent(key: String, quellContent: QuellContent): WV =
    modify {
        put(
            key,
            NbtCompound.CODEC,
            createNbtCompound {
                put(
                    "quell_type",
                    when (quellContent) {
                        Empty -> "empty"
                        is Filled -> quellContent.quellType.asString()
                    }
                )

                if (quellContent is Filled) {
                    put("stored_thaum", quellContent.storedThaum)
                }
            }
        )
    }.build()

fun ReadView.readQuellContent(key: String): QuellContent =
    with(getReadView(key)) {
        if (this == null) {
            return@with null
        }

        when (val quellTypeName = getString("quell_type", null)) {
            "empty" -> Empty
            else -> {
                QuellType.entries.find { it.asString() == quellTypeName }?.let {
                    try {
                        Filled(
                            quellType = it,
                            storedThaum = getInt("stored_thaum", 0)
                        )
                    } catch (e: Throwable) {
                        null
                    }
                }
            }
        }
    } ?: run {
//        println("Failed to read QuellContent from NBT: $this. Defaulting to Empty.")
        Empty
    }