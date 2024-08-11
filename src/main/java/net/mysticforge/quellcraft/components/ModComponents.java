package net.mysticforge.quellcraft.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.mysticforge.quellcraft.Quellcraft;

public final class ModComponents implements EntityComponentInitializer {
    public static final ComponentKey<EntityQuellInfusionComponent> quellInfusion =
        ComponentRegistry.getOrCreate(Identifier.of(Quellcraft.MOD_ID, EntityQuellInfusionComponent.KEY), EntityQuellInfusionComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(LivingEntity.class, quellInfusion, EntityQuellInfusionComponent::new);
    }
}