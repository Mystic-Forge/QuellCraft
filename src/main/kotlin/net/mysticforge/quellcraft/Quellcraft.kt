package net.mysticforge.quellcraft

import net.fabricmc.api.ModInitializer

object Quellcraft : ModInitializer {
    const val MOD_ID = "quellcraft"

    override fun onInitialize() {
        ModBlocks
        ModItems
    }
}
