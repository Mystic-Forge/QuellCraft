package net.mysticforge.quellcraft

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.ModifyEntries
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.mysticforge.quellcraft.items.MistikTolisItem


object ModItems {
    val customItemGroupKey: RegistryKey<ItemGroup> = RegistryKey.of(Registries.ITEM_GROUP.key, Identifier.of(Quellcraft.MOD_ID, "quellcraft"))
    val customItemGroup: ItemGroup = FabricItemGroup.builder().icon { ItemStack(mistikTolis) }.displayName(Text.translatable("itemGroup.quellcraft")).build()

    private val depletedCrystalShard: Item = register(Item(Item.Settings()), "spectrite_shard")
    private val voidShard: Item = register(Item(Item.Settings()), "void_shard")
    private val depletedCrystalDust: Item = register(Item(Item.Settings()), "spectrite_dust")
    private val mortarAndPestle: Item = register(Item(Item.Settings().maxCount(1)), "mortar_and_pestle")
    private val mistikTolis: Item = register(MistikTolisItem(Item.Settings()), "mistik_tolis")

    private fun register(item: Item, id: String) = Registry.register(Registries.ITEM, Identifier.of(Quellcraft.MOD_ID, id), item)

    fun inititalize() {
        // Register the group.
        Registry.register(Registries.ITEM_GROUP, customItemGroupKey, customItemGroup)

        // Register items to the custom item group.
        ItemGroupEvents.modifyEntriesEvent(customItemGroupKey).register(ModifyEntries { itemGroup: FabricItemGroupEntries ->
            itemGroup.add(mistikTolis)
            itemGroup.add(ModBlocks.spectriteCluster.asItem())
            itemGroup.add(ModBlocks.voidCluster.asItem())
            itemGroup.add(depletedCrystalShard)
            itemGroup.add(voidShard)
            itemGroup.add(depletedCrystalDust)
            itemGroup.add(mortarAndPestle)
        })
    }
}