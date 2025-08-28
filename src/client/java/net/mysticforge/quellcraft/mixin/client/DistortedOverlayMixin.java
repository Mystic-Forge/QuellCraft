package net.mysticforge.quellcraft.mixin.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.mysticforge.quellcraft.client.QuellCraftClient;
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
            target = "Lnet/minecraft/client/gui/hud/InGameHud;renderMiscOverlays(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V",
            shift = At.Shift.AFTER,
            ordinal = 0
        )
    )
    public void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        QuellCraftClient.INSTANCE.drawDistortedEffect(context, tickCounter);
    }
}
