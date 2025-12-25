package net.mysticforge.quellcraft.item

import io.wispforest.accessories.api.core.AccessoryItem
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemStack
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.mysticforge.quellcraft.itemcomponents.ModItemComponents
import net.mysticforge.quellcraft.quellmanagement.QuellAbsorbentItem
import net.mysticforge.quellcraft.quellmanagement.QuellContent

class LuckyCrystal(settings: Properties) : AccessoryItem(settings.stacksTo(1)), QuellAbsorbentItem {
    companion object {
        private const val MAX_QUELL_CONTENT = 100
        private const val ABSORPTION_RATIO = 0.5
        private const val DECAY_CHANCE = 0.1
    }

    override fun getPossibleQuellAbsorption(itemStack: ItemStack, quellContent: QuellContent.Filled): QuellContent {
        when (val currentQuellContent = itemStack.components.get(ModItemComponents.quellContentComponent)) {
            is QuellContent.Filled -> {
                if (currentQuellContent.quellType == quellContent.quellType) {
                    val absorbedAmount =
                        (currentQuellContent.storedThaum + quellContent.storedThaum * ABSORPTION_RATIO).toInt().coerceAtMost(MAX_QUELL_CONTENT) - currentQuellContent.storedThaum
                    return if (absorbedAmount > 0) QuellContent.Filled(quellContent.quellType, absorbedAmount) else QuellContent.Empty
                } else return QuellContent.Empty
            }
            else -> {
                val absorbedAmount = (quellContent.storedThaum * ABSORPTION_RATIO).toInt().coerceAtMost(MAX_QUELL_CONTENT)
                return if (absorbedAmount > 0) QuellContent.Filled(quellContent.quellType, absorbedAmount) else QuellContent.Empty
            }
        }
    }

    override fun doAbsorbQuell(itemStack: ItemStack, quellContent: QuellContent.Filled) {
        val currentQuellContent = itemStack.components.get(ModItemComponents.quellContentComponent)
        val newQuellContent = if (currentQuellContent is QuellContent.Filled && currentQuellContent.quellType == quellContent.quellType)
            currentQuellContent + quellContent.storedThaum else quellContent

        itemStack.set(ModItemComponents.quellContentComponent, newQuellContent)
    }

    override fun inventoryTick(stack: ItemStack, world: ServerLevel, entity: Entity, slot: EquipmentSlot?) {
        tryDecayQuellContent(stack, world.random)
    }

    private fun tryDecayQuellContent(itemStack: ItemStack, random: RandomSource) {
        val currentQuellContent = itemStack.components.get(ModItemComponents.quellContentComponent)
        if (currentQuellContent is QuellContent.Filled && random.nextDouble() < DECAY_CHANCE) {
            itemStack.set(ModItemComponents.quellContentComponent, currentQuellContent - 1)
        }
    }
}