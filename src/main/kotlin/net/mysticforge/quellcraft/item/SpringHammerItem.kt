package net.mysticforge.quellcraft.item

import com.google.common.collect.ImmutableMultimap
import com.google.common.collect.Multimap
import net.minecraft.block.BlockState
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

object SpringHammerItem : Item(Settings().maxCount(1).maxDamage(256)) {
    private val attributeModifiers: Multimap<EntityAttribute, EntityAttributeModifier>
    private val ATTACK_KNOCKBACK_MODIFIER_ID = UUID.fromString("96e7a4c2-1f41-4088-bc6b-63ed589b268f");

    init {
        val builder = ImmutableMultimap.builder<EntityAttribute, EntityAttributeModifier>()
        builder.put(
            EntityAttributes.GENERIC_ATTACK_KNOCKBACK,
            EntityAttributeModifier(ATTACK_KNOCKBACK_MODIFIER_ID, "Weapon modifier", 1.0, EntityAttributeModifier.Operation.ADDITION)
        )

        attributeModifiers = builder.build()
    }

    override fun getAttributeModifiers(slot: EquipmentSlot): Multimap<EntityAttribute, EntityAttributeModifier> {
        return if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND)
            attributeModifiers else super.getAttributeModifiers(slot)
    }

    override fun postHit(stack: ItemStack, target: LivingEntity?, attacker: LivingEntity): Boolean {
        stack.damage(1, attacker) { e: LivingEntity -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND) }
        return true
    }

    override fun postMine(stack: ItemStack, world: World?, state: BlockState, pos: BlockPos?, miner: LivingEntity): Boolean {
        if (state.getHardness(world, pos) != 0.0f) {
            stack.damage(2, miner) { e: LivingEntity -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND) }
        }

        return true
    }
}