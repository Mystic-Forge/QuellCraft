package net.mysticforge.quellcraft.itemcomponents

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.quellmanagement.QuellContent
import net.mysticforge.quellcraft.state.property.QuellType
import java.util.*

object ModItemComponents {
    val quellContentCodec: Codec<QuellContent?> = RecordCodecBuilder.create { builder ->
        builder.group(
            Codec.INT.fieldOf("stored_thaum").forGetter(QuellContent::storedThaum),
            Codec.STRING.optionalFieldOf("quell_type").forGetter {
                when (it) {
                    is QuellContent.Filled -> Optional.of(it.quellType.serializedName)
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
                    quellType = QuellType.entries.first { it.serializedName == quellType.get() },
                    storedThaum = storedThaum
                )
            }
        }
    }

    val quellContentComponent: DataComponentType<QuellContent?> = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        ResourceLocation.fromNamespaceAndPath(Quellcraft.MOD_ID, "quell_content"), DataComponentType.builder<QuellContent>().persistent(quellContentCodec).build()
    )

    val chargeComponent: DataComponentType<Int?> = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        ResourceLocation.fromNamespaceAndPath(Quellcraft.MOD_ID, "charge"), DataComponentType.builder<Int>().persistent(Codec.INT).build()
    )
}