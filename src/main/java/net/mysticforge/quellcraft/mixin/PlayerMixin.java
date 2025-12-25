package net.mysticforge.quellcraft.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.mysticforge.quellcraft.mixinimpl.PlayerEntityMixinImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public class PlayerMixin {
    @Redirect(
            method = "attack(Lnet/minecraft/world/entity/Entity;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getKnockback(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;)F"
            )
    )
    public float getAttackKnockbackAgainst(Player instance, Entity entity, DamageSource damageSource) {
        return PlayerEntityMixinImpl.getKnockback(instance, entity, damageSource);
    }
}
