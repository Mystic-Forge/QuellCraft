package net.mysticforge.quellcraft.item

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.world.World
import net.mysticforge.quellcraft.QuellcraftConfig
import net.mysticforge.quellcraft.quellmanagement.QuellContent
import net.mysticforge.quellcraft.quellmanagement.readQuellContent
import net.mysticforge.quellcraft.quellmanagement.writeQuellContent
import net.mysticforge.quellcraft.state.property.QuellType

object TurboTreadsItem : ArmorItem(TurboTreadsArmorMaterial, Type.BOOTS, Settings().maxCount(1)) {
    private const val QUELL_CONTENT_KEY = "quell_content"
    private const val MAX_QUELL_CONTENT = 100
    private const val CHARGE_CHANCE = 0.1

    fun activateTurboTreads(itemStack: ItemStack, entity: Entity, fallDistance: Float) {
        val storedThaum =  itemStack.getOrCreateNbt().readQuellContent(QUELL_CONTENT_KEY).storedThaum
        val launchPower = (storedThaum / MAX_QUELL_CONTENT.toFloat() * QuellcraftConfig.turboTreadsMaxBoost).toDouble()

        println(launchPower)

        entity.addVelocity(0.0, launchPower, 0.0)
    }

    override fun inventoryTick(itemStack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        val nbt = itemStack.getOrCreateNbt()
        val currentQuellContent = nbt.readQuellContent(QUELL_CONTENT_KEY)
        if (currentQuellContent.storedThaum < MAX_QUELL_CONTENT && world.random.nextDouble() < CHARGE_CHANCE) {
            when(currentQuellContent) {
                is QuellContent.Filled -> nbt.writeQuellContent(QUELL_CONTENT_KEY, currentQuellContent + 1)
                else -> nbt.writeQuellContent(QUELL_CONTENT_KEY, QuellContent.Filled(QuellType.VOID, 1))
            }
        }
    }
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