package net.mysticforge.quellcraft

import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier


object ModItems {
    val crystalShard = register(Item(Item.Settings()), "crystal_shard")

    private fun register(item: Item, id: String) =
        Registry.register(Registries.ITEM, Identifier.of(Quellcraft.MOD_ID, id), item)
}