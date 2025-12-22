package net.mysticforge.quellcraft.mixin;

import net.minecraft.registry.SimpleRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SimpleRegistry.class)
public interface SimpleRegistryAccessor {
    @Accessor("frozen")
    boolean getFrozen();

    @Accessor("frozen")
    void setFrozen(boolean frozen);
}
