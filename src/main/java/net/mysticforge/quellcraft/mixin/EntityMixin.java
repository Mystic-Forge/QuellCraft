package net.mysticforge.quellcraft.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.mysticforge.quellcraft.mixinimpl.EntityMixinImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public class EntityMixin {
    @Redirect(
            method = "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Block;updateEntityMovementAfterFallOn(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;)V"
            )
    )
    private void onEntityLand(Block instance, BlockGetter world, Entity entity) {
        if(!EntityMixinImpl.onEntityLand((Entity) (Object) this))
            instance.updateEntityMovementAfterFallOn(world, entity);
    }
}