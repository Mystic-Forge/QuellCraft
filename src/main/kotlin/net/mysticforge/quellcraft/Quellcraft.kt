package net.mysticforge.quellcraft

import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer
import net.fabricmc.api.ModInitializer
import net.mysticforge.quellcraft.block.ModBlocks
import net.mysticforge.quellcraft.item.ModItems
import net.mysticforge.quellcraft.itemcomponents.ModItemComponents
import net.mysticforge.quellcraft.networking.ModPayloads
import net.mysticforge.quellcraft.screenhandler.ModScreenHandlers
import net.mysticforge.quellcraft.state.property.ModProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Quellcraft : ModInitializer {
    const val MOD_ID = "quellcraft"

    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

    @Suppress("UnusedExpression")
    override fun onInitialize() {
        AutoConfig.register(QuellcraftConfig::class.java, ::Toml4jConfigSerializer)
        ModProperties
        ModBlocks
        ModItems
        ModItemComponents
        ModStatusEffects
        ModSoundEvents
        ModPayloads
        ModScreenHandlers
    }
}
