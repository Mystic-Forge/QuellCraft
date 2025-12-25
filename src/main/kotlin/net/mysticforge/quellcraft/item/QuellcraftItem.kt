package net.mysticforge.quellcraft.item

import net.fabricmc.fabric.api.item.v1.CustomDamageHandler
import net.fabricmc.fabric.api.item.v1.EquipmentSlotProvider
import net.minecraft.core.component.DataComponentType
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.flag.FeatureFlag
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.equipment.ArmorMaterial
import net.minecraft.world.item.equipment.ArmorType

open class QuellcraftItem(settings: Properties) : Item(settings) {
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

    class QuellcraftItemSettings : Properties() {
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
         * Note that this is only called on an ItemStack if [ItemStack.isDamageableItem] returns true.
         *
         * @see CustomDamageHandler
         */
        override fun customDamage(handler: CustomDamageHandler?): QuellcraftItemSettings {
            super.customDamage(handler)
            return this
        }


        // Overrides of vanilla methods
        override fun food(foodComponent: FoodProperties): QuellcraftItemSettings {
            super.food(foodComponent)
            return this
        }

        override fun stacksTo(maxCount: Int): QuellcraftItemSettings {
            super.stacksTo(maxCount)
            return this
        }

        override fun durability(maxDamage: Int): QuellcraftItemSettings {
            super.durability(maxDamage)
            return this
        }

        override fun craftRemainder(recipeRemainder: Item): QuellcraftItemSettings {
            super.craftRemainder(recipeRemainder)
            return this
        }

        override fun rarity(rarity: Rarity): QuellcraftItemSettings {
            super.rarity(rarity)
            return this
        }

        override fun fireResistant(): QuellcraftItemSettings {
            super.fireResistant()
            return this
        }

        override fun requiredFeatures(vararg features: FeatureFlag): QuellcraftItemSettings {
            super.requiredFeatures(*features)
            return this
        }

        override fun <T : Any> component(type: DataComponentType<T>, value: T): QuellcraftItemSettings {
            super.component(type, value)
            return this
        }

        override fun repairable(repairIngredientsTag: TagKey<Item?>): QuellcraftItemSettings {
            super.repairable(repairIngredientsTag)
            return this
        }

        override fun humanoidArmor(material: ArmorMaterial, type: ArmorType): QuellcraftItemSettings {
            super.humanoidArmor(material, type)
            return this
        }
    }
}