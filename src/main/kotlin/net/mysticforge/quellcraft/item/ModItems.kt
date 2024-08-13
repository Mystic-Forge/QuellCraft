package net.mysticforge.quellcraft.item

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.block.ModBlocks
import net.mysticforge.quellcraft.state.property.QuellType


object ModItems {
    private val customItemGroupKey: RegistryKey<ItemGroup> = RegistryKey.of(Registries.ITEM_GROUP.key, Identifier.of(Quellcraft.MOD_ID, "quellcraft"))
    private val customItemGroup: ItemGroup = FabricItemGroup.builder().icon { ItemStack(mistikTolis) }.displayName(Text.translatable("itemGroup.quellcraft")).build()

    private val shards = QuellType.entries.map { it.typeName }.plus("spectrite").map { register(Item(Item.Settings()), "${it}_shard") }

    private val depletedCrystalDust: Item = register(Item(Item.Settings()), "spectrite_dust")
    private val mortarAndPestle: Item = register(Item(Item.Settings().maxCount(1)), "mortar_and_pestle")
    private val mistikTolis: Item = register(MistikTolisItem(Item.Settings()), "mistik_tolis")
    private val luckyNecklace: Item = register(LuckyCrystal, "lucky_crystal")
    private val turboTreads: Item = register(TurboTreadsItem, "turbo_treads")

    private fun register(item: Item, id: String) = Registry.register(Registries.ITEM, Identifier.of(Quellcraft.MOD_ID, id), item)

    init {
        // Register the group.
        Registry.register(Registries.ITEM_GROUP, customItemGroupKey, customItemGroup)

        // Register items to the custom item group.
        ItemGroupEvents.modifyEntriesEvent(customItemGroupKey).register { itemGroup ->
            itemGroup.add(mistikTolis)
            itemGroup.add(ModBlocks.crystalCluster.asItem())
            shards.forEach(itemGroup::add)
            itemGroup.add(depletedCrystalDust)
            itemGroup.add(mortarAndPestle)
            itemGroup.add(luckyNecklace)
            itemGroup.add(turboTreads)
        }
    }
}