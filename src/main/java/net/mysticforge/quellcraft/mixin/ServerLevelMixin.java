package net.mysticforge.quellcraft.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import kotlin.Unit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.mysticforge.quellcraft.components.ModComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Redirect(
            method= "Lnet/minecraft/server/level/ServerLevel;tickNonPassenger(Lnet/minecraft/world/entity/Entity;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setOldPosAndRot()V")
    )
    public void dontSetOldPosAndRot(Entity instance) {}

    @WrapMethod(
        method = "Lnet/minecraft/server/level/ServerLevel;tickNonPassenger(Lnet/minecraft/world/entity/Entity;)V"
    )
    private void tickNonPassenger(Entity entity, Operation<Void> original) {
        ModComponents.tickMod.get(entity).runTicks(1f / 20f, (e) -> {
            original.call(e);
            return Unit.INSTANCE;
        });
    }
}
