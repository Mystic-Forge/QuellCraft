package net.mysticforge.quellcraft.components;

import net.minecraft.world.entity.Entity;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.mysticforge.quellcraft.Quellcraft;

public final class ModComponents implements EntityComponentInitializer {
    public static final ComponentKey<EntityQuellInfusionComponent> quellInfusion =
        ComponentRegistry.getOrCreate(ResourceLocation.fromNamespaceAndPath(Quellcraft.MOD_ID, "quell_infusion"), EntityQuellInfusionComponent.class);

    public static final ComponentKey<EntityTickModComponent> tickMod =
        ComponentRegistry.getOrCreate(ResourceLocation.fromNamespaceAndPath(Quellcraft.MOD_ID, "tick_mod"), EntityTickModComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(LivingEntity.class, quellInfusion, EntityQuellInfusionComponent::new);
        registry.registerFor(Entity.class, tickMod, EntityTickModComponent::new);
    }
}