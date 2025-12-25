package net.mysticforge.quellcraft.item.equipment

import net.minecraft.world.item.equipment.ArmorMaterial
import net.minecraft.world.item.equipment.EquipmentAssets
import net.minecraft.world.item.equipment.ArmorType
import net.minecraft.tags.ItemTags
import net.minecraft.sounds.SoundEvents

object ModArmorMaterials {
    private val turboTreadsEquipmentAsset = EquipmentAssets.createId("turbo_treads")
    val turboTreads = ArmorMaterial(
        15, // Durability multiplier
        mapOf( // Protection values
            ArmorType.BOOTS to 1,
            ArmorType.LEGGINGS to 2,
            ArmorType.CHESTPLATE to 3,
            ArmorType.HELMET to 1,
            ArmorType.BODY to 3
        ),
        15, // Enchantability,
        SoundEvents.ARMOR_EQUIP_LEATHER, // Equip sound
        0.0f, // Toughness
        0.0f, // Knockback resistance
        ItemTags.REPAIRS_LEATHER_ARMOR, // Repair ingredient tag
        turboTreadsEquipmentAsset // Asset ID
    )

    private val sorcererEquipmentAsset = EquipmentAssets.createId("sorcerer")
    val sorcerer = ArmorMaterial(
        15, // Durability multiplier
        mapOf( // Protection values
            ArmorType.BOOTS to 1,
            ArmorType.LEGGINGS to 2,
            ArmorType.CHESTPLATE to 3,
            ArmorType.HELMET to 1,
            ArmorType.BODY to 3
        ),
        15, // Enchantability,
        SoundEvents.ARMOR_EQUIP_GENERIC, // Equip sound
        0.0f, // Toughness
        0.0f, // Knockback resistance
        ItemTags.REPAIRS_LEATHER_ARMOR, // Repair ingredient tag
        sorcererEquipmentAsset // Asset ID
    )
}