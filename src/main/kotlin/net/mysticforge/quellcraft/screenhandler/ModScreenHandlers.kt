package net.mysticforge.quellcraft.screenhandler

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.mysticforge.quellcraft.Quellcraft


object ModScreenHandlers {
    val projectDesk = register("project_desk", ::ProjectDeskScreenHandler)


    private fun <T : AbstractContainerMenu>register(name: String, factory: MenuType.MenuSupplier<T>) : MenuType<T> {
        return Registry.register(
            BuiltInRegistries.MENU,
            ResourceLocation.fromNamespaceAndPath(Quellcraft.MOD_ID, name),
            MenuType(factory, FeatureFlagSet.of())
        )
    }
}