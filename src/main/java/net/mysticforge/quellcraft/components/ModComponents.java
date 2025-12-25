package net.mysticforge.quellcraft.components;

import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.mysticforge.quellcraft.Quellcraft;

public final class ModComponents implements EntityComponentInitializer {
    public static final ComponentKey<EntityQuellInfusionComponent> quellInfusion =
        ComponentRegistry.getOrCreate(ResourceLocation.fromNamespaceAndPath(Quellcraft.MOD_ID, EntityQuellInfusionComponent.KEY), EntityQuellInfusionComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(LivingEntity.class, quellInfusion, EntityQuellInfusionComponent::new);
    }
}