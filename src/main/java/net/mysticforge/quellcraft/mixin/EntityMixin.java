package net.mysticforge.quellcraft.mixin;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import net.mysticforge.quellcraft.mixinimpl.EntityMixinImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public class EntityMixin {
    @Redirect(
            method = "move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;onEntityLand(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;)V"
            )
    )
    private void onEntityLand(Block instance, BlockView world, Entity entity) {
        if(!EntityMixinImpl.onEntityLand((Entity) (Object) this))
            instance.onEntityLand(world, entity);
    }
}