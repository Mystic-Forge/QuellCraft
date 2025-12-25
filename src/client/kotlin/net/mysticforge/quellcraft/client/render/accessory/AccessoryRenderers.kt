package net.mysticforge.quellcraft.client.render.accessory

import io.wispforest.accessories.api.client.AccessoriesRendererRegistry
import io.wispforest.accessories.api.client.renderers.AccessoryRenderer
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.item.ModItems

object AccessoryRenderers {
    init {
        register("hat_renderer", HatAccessoryRenderer(), ModItems.sorcererHat)
    }

    private fun register(name: String, renderer: AccessoryRenderer, vararg items: Item) {
        val id = Identifier.of(Quellcraft.MOD_ID, name)
        AccessoriesRendererRegistry.registerRenderer(id, { renderer })
        for (item in items) AccessoriesRendererRegistry.bindItemToRenderer(item, id)
    }
}