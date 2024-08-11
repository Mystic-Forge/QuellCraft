package net.mysticforge.quellcraft

import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer
import net.fabricmc.api.ModInitializer
import net.mysticforge.quellcraft.block.ModBlocks
import net.mysticforge.quellcraft.item.ModItems
import net.mysticforge.quellcraft.state.property.ModProperties

object Quellcraft : ModInitializer {
    const val MOD_ID = "quellcraft"

    override fun onInitialize() {
        AutoConfig.register(QuellcraftConfig::class.java, ::Toml4jConfigSerializer)
        ModProperties
        ModBlocks
        ModItems
        ModStatusEffects
    }
}
