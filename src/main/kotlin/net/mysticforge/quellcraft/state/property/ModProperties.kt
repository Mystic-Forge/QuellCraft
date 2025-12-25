package net.mysticforge.quellcraft.state.property

import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.util.StringRepresentable

object ModProperties {
    val intensity: IntegerProperty = IntegerProperty.create("intensity", 0, 5)
    val quellType: EnumProperty<QuellTypeProperty> = EnumProperty.create("quell_type", QuellTypeProperty::class.java)
    val quellLevel: IntegerProperty = IntegerProperty.create("quell_level", 0, 3)
}

enum class QuellType(private val propertyEquivalent: QuellTypeProperty) : StringRepresentable {
    Void(QuellTypeProperty.Void),
    Thermal(QuellTypeProperty.Thermal),
    Life(QuellTypeProperty.Life);

    override fun toString() = propertyEquivalent.toString()
    override fun getSerializedName() = propertyEquivalent.serializedName

    fun asProperty(): QuellTypeProperty = when (this) {
        Void -> QuellTypeProperty.Void
        Thermal -> QuellTypeProperty.Thermal
        Life -> QuellTypeProperty.Life
    }
}

enum class QuellTypeProperty(val typeName: String) : StringRepresentable {
    Void("void"),
    Thermal("thermal"),
    Life("life"),
    Null("null");

    override fun toString() = typeName
    override fun getSerializedName() = typeName

    fun asQuellType(): QuellType? = when (this) {
        Void -> QuellType.Void
        Thermal -> QuellType.Thermal
        Life -> QuellType.Life
        Null -> null
    }
}


