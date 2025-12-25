package net.mysticforge.quellcraft.item.equipment

import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipDisplay
import net.minecraft.world.item.equipment.ArmorType
import net.minecraft.world.phys.Vec3
import net.mysticforge.quellcraft.QuellcraftConfig
import net.mysticforge.quellcraft.itemcomponents.ModItemComponents
import net.mysticforge.quellcraft.quellmanagement.QuellContent
import net.mysticforge.quellcraft.quellmanagement.doQuellExplosion
import net.mysticforge.quellcraft.state.property.QuellType
import java.util.function.Consumer

class TurboTreadsItem(settings: Properties) : Item(settings.humanoidArmor(ModArmorMaterials.turboTreads, ArmorType.BOOTS).stacksTo(1)) {
    companion object {
        fun tryActivateTurboTreads(itemStack: ItemStack, entity: Entity): Boolean {
            if (!entity.isShiftKeyDown || !entity.onGround() || entity.deltaMovement.y > -0.2f) return false

            val world = entity.level()

            if (entity is Player && entity.abilities.flying) return false

            val charge = itemStack.get(ModItemComponents.chargeComponent) ?: 0
            if (charge < QuellcraftConfig.turboTreadsChargeTime) return false

            val launchPower = QuellcraftConfig.turboTreadsBoost.toDouble()
            itemStack.set(ModItemComponents.chargeComponent, 0)

            val velocity = entity.deltaMovement
            val horizontalDirection = velocity.with(Direction.Axis.Y, 0.0)
            val horizontalBoosting = horizontalDirection.length() > 0.05
            val horizontalBoost = if (horizontalBoosting)
                velocity.with(Direction.Axis.Y, 0.0).normalize().scale(launchPower)
            else Vec3.ZERO
            val finalVelocity = Vec3(horizontalBoost.x, if (horizontalBoosting) launchPower * 0.7 else launchPower, horizontalBoost.z)

            entity.deltaMovement = finalVelocity

            if (entity is LivingEntity) {
                if (world is ServerLevel) entity.hurtServer(world, entity.damageSources().generic(), 3f)
                world.doQuellExplosion(QuellContent.Filled(QuellType.Void, 20), entity.position(), 5.0)
            }
            return true
        }
    }

    override fun inventoryTick(itemStack: ItemStack, world: ServerLevel, entity: Entity, slot: EquipmentSlot?) {
        val charge = itemStack.get(ModItemComponents.chargeComponent) ?: 0
        if (charge < QuellcraftConfig.turboTreadsChargeTime) {
            if (charge == QuellcraftConfig.turboTreadsChargeTime - 1)
                world.playLocalSound(entity.x, entity.y, entity.z, SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1f, 1f, false)
            itemStack.set(ModItemComponents.chargeComponent, charge + 1)
        }
    }

    override fun appendHoverText(stack: ItemStack, context: TooltipContext, displayComponent: TooltipDisplay, textConsumer: Consumer<Component>, type: TooltipFlag) {
        val charge = stack.get(ModItemComponents.chargeComponent)
        if (charge != null) {
            textConsumer.accept(Component.nullToEmpty("Charge: ${(charge / QuellcraftConfig.turboTreadsChargeTime.toFloat() * 100).toInt()}%"))
        }

        textConsumer.accept(Component.nullToEmpty("Once charged, landing while sneaking"))
        textConsumer.accept(Component.nullToEmpty("will cause a violent explosion beneath"))
        textConsumer.accept(Component.nullToEmpty("you, sending you flying into the air!"))
    }
}