package net.mysticforge.quellcraft.item

import net.fabricmc.fabric.api.item.v1.CustomDamageHandler
import net.fabricmc.fabric.api.item.v1.EquipmentSlotProvider
import net.minecraft.component.ComponentType
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.EquippableComponent
import net.minecraft.component.type.FoodComponent
import net.minecraft.component.type.RepairableComponent
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.equipment.ArmorMaterial
import net.minecraft.item.equipment.EquipmentType
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryEntryLookup
import net.minecraft.registry.tag.TagKey
import net.minecraft.resource.featuretoggle.FeatureFlag
import net.minecraft.util.Rarity

open class QuellcraftItem(settings: Settings) : Item(settings) {
    val knockbackBoost: Int

    init {
        if (settings is QuellcraftItemSettings) {
            knockbackBoost = settings.knockbackBoost
        }
        else {
            knockbackBoost = 0
        }
    }

    open fun getKnockbackBoost(itemStack: ItemStack, entity: LivingEntity): Int {
        return knockbackBoost
    }

    class QuellcraftItemSettings : Settings() {
        var knockbackBoost = 0

        fun knockbackBoost(knockbackBoost: Int): QuellcraftItemSettings {
            this.knockbackBoost = knockbackBoost
            return this
        }

        /**
         * Sets the equipment slot provider of the item.
         *
         * @param equipmentSlotProvider the equipment slot provider
         * @return this builder
         */
        override fun equipmentSlot(equipmentSlotProvider: EquipmentSlotProvider?): QuellcraftItemSettings {
            super.equipmentSlot(equipmentSlotProvider)
            return this
        }

        /**
         * Sets the custom damage handler of the item.
         * Note that this is only called on an ItemStack if [ItemStack.isDamageable] returns true.
         *
         * @see CustomDamageHandler
         */
        override fun customDamage(handler: CustomDamageHandler?): QuellcraftItemSettings {
            super.customDamage(handler)
            return this
        }


        // Overrides of vanilla methods
        override fun food(foodComponent: FoodComponent?): QuellcraftItemSettings {
            super.food(foodComponent)
            return this
        }

        override fun maxCount(maxCount: Int): QuellcraftItemSettings {
            super.maxCount(maxCount)
            return this
        }

        override fun maxDamage(maxDamage: Int): QuellcraftItemSettings {
            super.maxDamage(maxDamage)
            return this
        }

        override fun recipeRemainder(recipeRemainder: Item?): QuellcraftItemSettings {
            super.recipeRemainder(recipeRemainder)
            return this
        }

        override fun rarity(rarity: Rarity?): QuellcraftItemSettings {
            super.rarity(rarity)
            return this
        }

        override fun fireproof(): QuellcraftItemSettings {
            super.fireproof()
            return this
        }

        override fun requires(vararg features: FeatureFlag?): QuellcraftItemSettings {
            super.requires(*features)
            return this
        }

        override fun <T> component(type: ComponentType<T?>?, value: T?): QuellcraftItemSettings {
            super.component(type, value)
            return this
        }

        override fun repairable(repairIngredientsTag: TagKey<Item?>?): QuellcraftItemSettings {
            super.repairable(repairIngredientsTag)
            return this
        }

        override fun armor(material: ArmorMaterial, type: EquipmentType): QuellcraftItemSettings {
            super.armor(material, type)
            return this
        }
    }
}