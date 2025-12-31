package net.mysticforge.quellcraft.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.mysticforge.quellcraft.components.ModComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyVariable(
        method = "Lnet/minecraft/client/renderer/GameRenderer;renderLevel(Lnet/minecraft/client/DeltaTracker;)V",
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/TickRateManager;isEntityFrozen(Lnet/minecraft/world/entity/Entity;)Z"
            ),
            to = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/Camera;setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V"
            )
        ),
        at = @At("STORE"),
        ordinal = 1
    )
    public float scalePlayerPartialTickTime(float original, @Local Entity entity) {
//        var skippedTicks = ModComponents.tickMod.get(entity).getValue();
//        return (original + skippedTicks) * 0.5f;
        return original;
    }
}
