package net.mysticforge.quellcraft.mixin.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.mysticforge.quellcraft.client.QuellcraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class DistortedOverlayMixin {
    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableBlend()V",
            shift = At.Shift.AFTER,
            ordinal = 0
        )
    )
    public void render(DrawContext drawContext, float tickDelta, CallbackInfo callbackInfo) {
        QuellcraftClient.INSTANCE.drawDistortedEffect(drawContext, tickDelta);
    }
}
