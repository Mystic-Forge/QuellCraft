package net.mysticforge.quellcraft.screenhandler

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.resource.featuretoggle.FeatureSet
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.Identifier
import net.mysticforge.quellcraft.Quellcraft


object ModScreenHandlers {
    val projectDesk = register("project_desk", ::ProjectDeskScreenHandler)


    private fun <T : ScreenHandler>register(name: String, factory: ScreenHandlerType.Factory<T>) : ScreenHandlerType<T> {
        return Registry.register(
            Registries.SCREEN_HANDLER,
            Identifier.of(Quellcraft.MOD_ID, name),
            ScreenHandlerType(factory, FeatureSet.empty())
        )
    }
}