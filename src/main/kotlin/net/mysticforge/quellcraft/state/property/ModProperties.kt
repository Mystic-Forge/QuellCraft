package net.mysticforge.quellcraft.state.property

import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.IntProperty
import net.minecraft.util.StringIdentifiable

object ModProperties {
    val intensity: IntProperty = IntProperty.of("intensity", 0, 5)
    val quellType: EnumProperty<QuellTypeProperty> = EnumProperty.of("quell_type", QuellTypeProperty::class.java)
    val quellLevel: IntProperty = IntProperty.of("quell_level", 0, 3)
}

enum class QuellType(private val propertyEquivalent: QuellTypeProperty) : StringIdentifiable {
    Void(QuellTypeProperty.Void),
    Thermal(QuellTypeProperty.Thermal),
    Life(QuellTypeProperty.Life);

    override fun toString() = propertyEquivalent.toString()
    override fun asString() = propertyEquivalent.asString()

    fun asProperty(): QuellTypeProperty = when (this) {
        Void -> QuellTypeProperty.Void
        Thermal -> QuellTypeProperty.Thermal
        Life -> QuellTypeProperty.Life
    }
}

enum class QuellTypeProperty(val typeName: String) : StringIdentifiable {
    Void("void"),
    Thermal("thermal"),
    Life("life"),
    Null("null");

    override fun toString() = typeName
    override fun asString() = typeName

    fun asQuellType(): QuellType? = when (this) {
        Void -> QuellType.Void
        Thermal -> QuellType.Thermal
        Life -> QuellType.Life
        Null -> null
    }
}


