package net.mysticforge.quellcraft.state.property

import net.minecraft.state.property.IntProperty
import net.minecraft.util.StringIdentifiable

object ModProperties {
    val intensity: IntProperty = IntProperty.of("intensity", 0, 5)
}

enum class QuellType(val typeName: String) : StringIdentifiable {
    VOID("void"),
    THERMAL("thermal");

    override fun toString() = typeName
    override fun asString() = typeName
}