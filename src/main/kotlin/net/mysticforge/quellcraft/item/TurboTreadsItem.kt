package net.mysticforge.quellcraft.item

import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents

object TurboTreadsItem : ArmorItem(TurboTreadsArmorMaterial, Type.BOOTS, Settings().maxCount(1)) {
}

object TurboTreadsArmorMaterial: ArmorMaterial {
    override fun getDurability(type: ArmorItem.Type) = 128

    override fun getProtection(type: ArmorItem.Type?) = 1

    override fun getEnchantability() = 1

    override fun getEquipSound(): SoundEvent = SoundEvents.ITEM_ARMOR_EQUIP_LEATHER

    override fun getRepairIngredient(): Ingredient = Ingredient.ofItems(Items.LEATHER)

    override fun getName() = "turbo_treads"

    override fun getToughness() = 0f

    override fun getKnockbackResistance() = 0f
}