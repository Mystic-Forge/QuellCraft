package net.mysticforge.quellcraft.item

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.equipment.EquipmentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.block.ModBlocks
import net.mysticforge.quellcraft.item.equipment.ModArmorMaterials
import net.mysticforge.quellcraft.item.equipment.SorcererHatItem
import net.mysticforge.quellcraft.item.equipment.TurboTreadsItem
import net.mysticforge.quellcraft.state.property.QuellType


object ModItems {
    val customItemGroupKey: RegistryKey<ItemGroup> = RegistryKey.of(Registries.ITEM_GROUP.key, Identifier.of(Quellcraft.MOD_ID, "quellcraft"))
    val customItemGroup: ItemGroup = FabricItemGroup.builder().icon { ItemStack(mistikTolis) }.displayName(Text.translatable("itemGroup.quellcraft")).build()

    val shards = QuellType.entries.map { it.asString() }.plus("spectrite").map { register(::Item, "${it}_shard") }

    val mistikTolis: Item = register(::MistikTolisItem, "mistik_tolis")
    val springHammer: Item = register(::SpringHammerItem, "spring_hammer")
    val spectriteDust: Item = register(::Item, "spectrite_dust")
    val spring: Item = register(::Item, "spring")
    val quell_capacitor: Item = register(::Item, "quell_capacitor")
    val mortarAndPestle: Item = register(::Item, QuellcraftItem.QuellcraftItemSettings().maxCount(1), "mortar_and_pestle")
    val luckyCrystal: Item = register(::LuckyCrystal, "lucky_crystal")
    val turboTreads: Item = register(::TurboTreadsItem, "turbo_treads")
    val blueprint: Item = register(::Blueprint, "blueprint")
    val thaumicDrill: Item = register(::Item, "thaumic_drill")

    val sorcererHat: Item = register(::SorcererHatItem, QuellcraftItem.QuellcraftItemSettings(), "sorcerer_hat")

    private fun register(itemFactory: (settings: QuellcraftItem.QuellcraftItemSettings) -> Item, id: String): Item {
        val settings = QuellcraftItem.QuellcraftItemSettings()
        return register(itemFactory, settings, id)
    }

    private fun register(itemFactory: (settings: QuellcraftItem.QuellcraftItemSettings) -> Item, settings: QuellcraftItem.QuellcraftItemSettings, id: String): Item {
        val id = Identifier.of(Quellcraft.MOD_ID, id)
        settings.registryKey(RegistryKey.of(Registries.ITEM.key, id))
        val item = itemFactory(settings)
        Registry.register(Registries.ITEM, id, item)
        return item
    }

    init {
        // Register the group.
        Registry.register(Registries.ITEM_GROUP, customItemGroupKey, customItemGroup)

        // Register items to the custom item group.
        ItemGroupEvents.modifyEntriesEvent(customItemGroupKey).register { itemGroup ->
            itemGroup.add(mistikTolis)
            itemGroup.add(springHammer)
            itemGroup.add(ModBlocks.crystalCluster.asItem())
            shards.forEach(itemGroup::add)
            itemGroup.add(spectriteDust)
            itemGroup.add(spring)
            itemGroup.add(quell_capacitor)
            itemGroup.add(mortarAndPestle)
            itemGroup.add(luckyCrystal)
            itemGroup.add(turboTreads)
            itemGroup.add(thaumicDrill)
            itemGroup.add(blueprint)
            itemGroup.add(sorcererHat)
        }
    }
}