package net.mysticforge.quellcraft.state.property

import net.minecraft.util.StringIdentifiable

object ModProperties {
//    val quellType: EnumProperty<QuellType> = EnumProperty.of("quell_type", QuellType::class.java)
}

enum class QuellType(val typeName: String) : StringIdentifiable {
    NONE("none"),
    VOID("void");

    override fun toString(): String {
        return this.typeName
    }

    override fun asString(): String {
        return this.typeName
    }
}