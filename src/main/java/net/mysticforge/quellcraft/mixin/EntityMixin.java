package net.mysticforge.quellcraft.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.mysticforge.quellcraft.mixinimpl.EntityMixinImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(at = @At("HEAD"), method = "fall(DZLnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V")
    private void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition, CallbackInfo ci) {
        EntityMixinImpl.fall((Entity) (Object) this, heightDifference, onGround, state, landedPosition, ci);
    }
}