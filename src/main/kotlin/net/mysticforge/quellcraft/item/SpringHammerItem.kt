package net.mysticforge.quellcraft.item

import net.minecraft.block.BlockState
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.mysticforge.quellcraft.ModSoundEvents

class SpringHammerItem(settings: QuellcraftItemSettings) : QuellcraftItem(settings.maxCount(1).maxDamage(256).knockbackBoost(3)) {
    override fun postHit(stack: ItemStack, target: LivingEntity?, attacker: LivingEntity) {
        stack.damage(1, attacker, Hand.MAIN_HAND)
        attacker.world.playSound(
            null,
            attacker.x,
            attacker.y,
            attacker.z,
            ModSoundEvents.springHammerHitEvent,
            attacker.soundCategory,
            1.0f,
            1.0f
        )
    }

    override fun postMine(stack: ItemStack, world: World?, state: BlockState, pos: BlockPos?, miner: LivingEntity): Boolean {
        if (state.getHardness(world, pos) != 0.0f) {
            stack.damage(1, miner, Hand.MAIN_HAND)
        }

        return true
    }
}

interface KnockbackBoostItem {
    fun getKnockbackBoost(itemStack: ItemStack, entity: LivingEntity): Int
}