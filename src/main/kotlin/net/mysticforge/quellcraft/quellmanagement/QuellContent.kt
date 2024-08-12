package net.mysticforge.quellcraft.quellmanagement

import net.minecraft.nbt.NbtCompound
import net.mysticforge.quellcraft.quellmanagement.QuellContent.Empty
import net.mysticforge.quellcraft.quellmanagement.QuellContent.Filled
import net.mysticforge.quellcraft.state.property.QuellType
import net.mysticforge.quellcraft.util.buildNbtCompound

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
                    require(quellType == other.quellType)
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

fun NbtCompound.writeQuellContent(key: String, quellContent: QuellContent): NbtCompound {
    put(
        key,
        buildNbtCompound {
            put(
                "quell_type",
                when (quellContent) {
                    Empty -> "empty"
                    is Filled -> quellContent.quellType.typeName
                }
            )

            if (quellContent is Filled) {
                put("stored_thaum", quellContent.storedThaum)
            }
        }
    )

    return this
}

fun NbtCompound.readQuellContent(key: String): QuellContent =
    with(getCompound(key)) {
        if (this == null) {
            return@with null
        }

        when (val quellTypeName = getString("quell_type")) {
            "empty" -> Empty
            else -> {
                QuellType.entries.find { it.typeName == quellTypeName }?.let {
                    try {
                        Filled(
                            quellType = it,
                            storedThaum = getInt("stored_thaum")
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