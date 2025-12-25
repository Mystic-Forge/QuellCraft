package net.mysticforge.quellcraft.mixin.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.DeltaTracker;
import net.mysticforge.quellcraft.client.QuellCraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class DistortedOverlayMixin {
    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Gui;renderCameraOverlays(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V",
            shift = At.Shift.AFTER,
            ordinal = 0
        )
    )
    public void render(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {
        QuellCraftClient.INSTANCE.drawDistortedEffect(context, tickCounter);
    }
}
