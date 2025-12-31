package net.mysticforge.quellcraft.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.Entity;
import net.mysticforge.quellcraft.components.ModComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Definition(id = "getGameTimeDeltaPartialTick", method = "Lnet/minecraft/client/DeltaTracker;getGameTimeDeltaPartialTick(Z)F")
    @Expression("?.getGameTimeDeltaPartialTick(?)")
    @ModifyExpressionValue(
        method = "Lnet/minecraft/client/renderer/LevelRenderer;renderEntities(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/Camera;Lnet/minecraft/client/DeltaTracker;Ljava/util/List;)V",
        at = @At(value = "MIXINEXTRAS:EXPRESSION")
    )
    public float scaleEntityPartialTickTime(float original, @Local Entity entity) {
        var tickModComponent = ModComponents.tickMod.get(entity);
        return tickModComponent.getPartialTickTime(original);
    }
}
