package net.mysticforge.quellcraft

import net.fabricmc.api.ModInitializer
import net.mysticforge.quellcraft.block.ModBlocks
import net.mysticforge.quellcraft.item.ModItems
import net.mysticforge.quellcraft.state.property.ModProperties

object Quellcraft : ModInitializer {
    const val MOD_ID = "quellcraft"

    override fun onInitialize() {
        ModProperties
        ModBlocks
        ModItems.inititalize()
        ModEffects
    }
}
