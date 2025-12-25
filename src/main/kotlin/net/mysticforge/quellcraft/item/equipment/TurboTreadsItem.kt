package net.mysticforge.quellcraft.item.equipment

import net.minecraft.component.type.TooltipDisplayComponent
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.equipment.ArmorMaterial
import net.minecraft.item.equipment.EquipmentAssetKeys
import net.minecraft.item.equipment.EquipmentType
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.registry.tag.ItemTags
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.mysticforge.quellcraft.QuellcraftConfig
import net.mysticforge.quellcraft.itemcomponents.ModItemComponents
import net.mysticforge.quellcraft.quellmanagement.QuellContent
import net.mysticforge.quellcraft.quellmanagement.doQuellExplosion
import net.mysticforge.quellcraft.state.property.QuellType
import java.util.function.Consumer

class TurboTreadsItem(settings: Settings) : Item(settings.armor(ModArmorMaterials.turboTreads, EquipmentType.BOOTS).maxCount(1)) {
    companion object {
        fun tryActivateTurboTreads(itemStack: ItemStack, entity: Entity): Boolean {
            if (!entity.isSneaking || !entity.isOnGround || entity.velocity.y > -0.2f) return false

            val world = entity.world

            if (entity is PlayerEntity && entity.abilities.flying) return false

            val charge = itemStack.get(ModItemComponents.chargeComponent) ?: 0
            if (charge < QuellcraftConfig.turboTreadsChargeTime) return false

            val launchPower = QuellcraftConfig.turboTreadsBoost.toDouble()
            itemStack.set(ModItemComponents.chargeComponent, 0)

            val velocity = entity.velocity
            val horizontalDirection = velocity.withAxis(Direction.Axis.Y, 0.0)
            val horizontalBoosting = horizontalDirection.length() > 0.05
            val horizontalBoost = if (horizontalBoosting)
                velocity.withAxis(Direction.Axis.Y, 0.0).normalize().multiply(launchPower)
            else Vec3d.ZERO
            val finalVelocity = Vec3d(horizontalBoost.x, if (horizontalBoosting) launchPower * 0.7 else launchPower, horizontalBoost.z)

            entity.velocity = finalVelocity

            if (entity is LivingEntity) {
                if (world is ServerWorld) entity.damage(world, entity.damageSources.generic(), 3f)
                world.doQuellExplosion(QuellContent.Filled(QuellType.Void, 20), entity.pos, 5.0)
            }
            return true
        }
    }

    override fun inventoryTick(itemStack: ItemStack, world: ServerWorld, entity: Entity, slot: EquipmentSlot?) {
        val charge = itemStack.get(ModItemComponents.chargeComponent) ?: 0
        if (charge < QuellcraftConfig.turboTreadsChargeTime) {
            if (charge == QuellcraftConfig.turboTreadsChargeTime - 1)
                world.playSoundClient(entity.x, entity.y, entity.z, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS, 1f, 1f, false)
            itemStack.set(ModItemComponents.chargeComponent, charge + 1)
        }
    }

    override fun appendTooltip(stack: ItemStack, context: TooltipContext, displayComponent: TooltipDisplayComponent, textConsumer: Consumer<Text>, type: TooltipType) {
        val charge = stack.get(ModItemComponents.chargeComponent)
        if (charge != null) {
            textConsumer.accept(Text.of("Charge: ${(charge / QuellcraftConfig.turboTreadsChargeTime.toFloat() * 100).toInt()}%"))
        }

        textConsumer.accept(Text.of("Once charged, landing while sneaking"))
        textConsumer.accept(Text.of("will cause a violent explosion beneath"))
        textConsumer.accept(Text.of("you, sending you flying into the air!"))
    }
}