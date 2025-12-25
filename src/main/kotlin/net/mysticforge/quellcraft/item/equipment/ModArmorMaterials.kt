package net.mysticforge.quellcraft.item.equipment

import net.minecraft.item.equipment.ArmorMaterial
import net.minecraft.item.equipment.EquipmentAssetKeys
import net.minecraft.item.equipment.EquipmentType
import net.minecraft.registry.tag.ItemTags
import net.minecraft.sound.SoundEvents

object ModArmorMaterials {
    private val turboTreadsEquipmentAsset = EquipmentAssetKeys.register("turbo_treads")
    val turboTreads = ArmorMaterial(
        15, // Durability multiplier
        mapOf( // Protection values
            EquipmentType.BOOTS to 1,
            EquipmentType.LEGGINGS to 2,
            EquipmentType.CHESTPLATE to 3,
            EquipmentType.HELMET to 1,
            EquipmentType.BODY to 3
        ),
        15, // Enchantability,
        SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, // Equip sound
        0.0f, // Toughness
        0.0f, // Knockback resistance
        ItemTags.REPAIRS_LEATHER_ARMOR, // Repair ingredient tag
        turboTreadsEquipmentAsset // Asset ID
    )

    private val sorcererEquipmentAsset = EquipmentAssetKeys.register("sorcerer")
    val sorcerer = ArmorMaterial(
        15, // Durability multiplier
        mapOf( // Protection values
            EquipmentType.BOOTS to 1,
            EquipmentType.LEGGINGS to 2,
            EquipmentType.CHESTPLATE to 3,
            EquipmentType.HELMET to 1,
            EquipmentType.BODY to 3
        ),
        15, // Enchantability,
        SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, // Equip sound
        0.0f, // Toughness
        0.0f, // Knockback resistance
        ItemTags.REPAIRS_LEATHER_ARMOR, // Repair ingredient tag
        sorcererEquipmentAsset // Asset ID
    )
}