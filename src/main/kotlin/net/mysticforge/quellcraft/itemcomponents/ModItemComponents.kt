package net.mysticforge.quellcraft.itemcomponents

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.component.ComponentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.quellmanagement.QuellContent
import net.mysticforge.quellcraft.state.property.QuellType
import java.util.Optional

object ModItemComponents {
    val quellContentCodec = RecordCodecBuilder.create { builder ->
        builder.group(
            Codec.INT.fieldOf("stored_thaum").forGetter(QuellContent::storedThaum),
            Codec.STRING.optionalFieldOf("quell_type").forGetter {
                when (it) {
                    is QuellContent.Filled -> Optional.of(it.quellType.asString())
                    QuellContent.Empty -> Optional.empty()
                    else -> Optional.empty()
                }
            }
        ).apply(builder) { storedThaum, quellType ->
            if (storedThaum == 0) {
                QuellContent.Empty
            } else {
                requireNotNull(quellType) { "quellType must not be null if storedThaum > 0" }

                QuellContent.Filled(
                    quellType = QuellType.entries.first { it.asString() == quellType.get() },
                    storedThaum = storedThaum
                )
            }
        }
    }

    val quellContentComponent = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        Identifier.of(Quellcraft.MOD_ID, "quell_content"), ComponentType.builder<QuellContent>().codec(quellContentCodec).build()
    )

    val chargeComponent = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        Identifier.of(Quellcraft.MOD_ID, "charge"), ComponentType.builder<Int>().codec(Codec.INT).build()
    )
}