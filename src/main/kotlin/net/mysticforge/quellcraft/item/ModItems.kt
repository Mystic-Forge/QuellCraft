package net.mysticforge.quellcraft.item

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.block.ModBlocks
import net.mysticforge.quellcraft.item.equipment.SorcererHatItem
import net.mysticforge.quellcraft.item.equipment.TurboTreadsItem
import net.mysticforge.quellcraft.state.property.QuellType


object ModItems {
    val customItemGroupKey: ResourceKey<CreativeModeTab> = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), ResourceLocation.fromNamespaceAndPath(Quellcraft.MOD_ID, "quellcraft"))
    val customItemGroup: CreativeModeTab = FabricItemGroup.builder().icon { ItemStack(mistikTolis) }.title(Component.translatable("itemGroup.quellcraft")).build()

    val shards = QuellType.entries.map { it.serializedName }.plus("spectrite").map { register(::Item, "${it}_shard") }

    val mistikTolis: Item = register(::MistikTolisItem, "mistik_tolis")
    val springHammer: Item = register(::SpringHammerItem, "spring_hammer")
    val spectriteDust: Item = register(::Item, "spectrite_dust")
    val spring: Item = register(::Item, "spring")
    val quell_capacitor: Item = register(::Item, "quell_capacitor")
    val mortarAndPestle: Item = register(::Item, QuellcraftItem.QuellcraftItemSettings().stacksTo(1), "mortar_and_pestle")
    val luckyCrystal: Item = register(::LuckyCrystal, "lucky_crystal")
    val turboTreads: Item = register(::TurboTreadsItem, "turbo_treads")
    val blueprint: Item = register(::Blueprint, "blueprint")
    val thaumicDrill: Item = register(::Item, "thaumic_drill")

    val sorcererHat = register(::SorcererHatItem, QuellcraftItem.QuellcraftItemSettings(), "sorcerer_hat")

    private fun <T: Item>register(itemFactory: (settings: QuellcraftItem.QuellcraftItemSettings) -> T, id: String): T {
        val settings = QuellcraftItem.QuellcraftItemSettings()
        return register(itemFactory, settings, id)
    }

    private fun <T: Item>register(itemFactory: (settings: QuellcraftItem.QuellcraftItemSettings) -> T, settings: QuellcraftItem.QuellcraftItemSettings, id: String): T {
        val id = ResourceLocation.fromNamespaceAndPath(Quellcraft.MOD_ID, id)
        settings.setId(ResourceKey.create(BuiltInRegistries.ITEM.key(), id))
        val item = itemFactory(settings)
        Registry.register(BuiltInRegistries.ITEM, id, item)
        return item
    }

    init {
        // Register the group.
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, customItemGroupKey, customItemGroup)

        // Register items to the custom item group.
        ItemGroupEvents.modifyEntriesEvent(customItemGroupKey).register { itemGroup ->
            itemGroup.accept(mistikTolis)
            itemGroup.accept(springHammer)
            itemGroup.accept(ModBlocks.crystalCluster.asItem())
            shards.forEach(itemGroup::accept)
            itemGroup.accept(spectriteDust)
            itemGroup.accept(spring)
            itemGroup.accept(quell_capacitor)
            itemGroup.accept(mortarAndPestle)
            itemGroup.accept(luckyCrystal)
            itemGroup.accept(turboTreads)
            itemGroup.accept(thaumicDrill)
            itemGroup.accept(blueprint)
            itemGroup.accept(sorcererHat)
        }
    }
}