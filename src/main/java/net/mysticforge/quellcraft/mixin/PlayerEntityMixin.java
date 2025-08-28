package net.mysticforge.quellcraft.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.mysticforge.quellcraft.mixinimpl.PlayerEntityMixinImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Redirect(
            method = "attack(Lnet/minecraft/entity/Entity;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttackKnockbackAgainst(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;)F"
            )
    )
    public float getAttackKnockbackAgainst(PlayerEntity instance, Entity entity, DamageSource damageSource) {
        return PlayerEntityMixinImpl.getKnockback(instance, entity, damageSource);
    }
}
