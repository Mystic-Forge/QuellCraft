package net.mysticforge.quellcraft.item

import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.InteractionHand
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.mysticforge.quellcraft.ModSoundEvents

class SpringHammerItem(settings: QuellcraftItemSettings) : QuellcraftItem(settings.stacksTo(1).durability(256).knockbackBoost(3)) {
    override fun hurtEnemy(stack: ItemStack, target: LivingEntity?, attacker: LivingEntity) {
        stack.hurtAndBreak(1, attacker, InteractionHand.MAIN_HAND)
        attacker.level().playSound(
            null,
            attacker.x,
            attacker.y,
            attacker.z,
            ModSoundEvents.springHammerHitEvent,
            attacker.soundSource,
            1.0f,
            1.0f
        )
    }

    override fun mineBlock(stack: ItemStack, world: Level?, state: BlockState, pos: BlockPos?, miner: LivingEntity): Boolean {
        if (state.getDestroySpeed(world, pos) != 0.0f) {
            stack.hurtAndBreak(1, miner, InteractionHand.MAIN_HAND)
        }

        return true
    }
}

interface KnockbackBoostItem {
    fun getKnockbackBoost(itemStack: ItemStack, entity: LivingEntity): Int
}