package net.mysticforge.quellcraft.realm

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents
import net.minecraft.world.level.block.Blocks
import net.minecraft.resources.ResourceKey
import net.minecraft.core.registries.Registries
import net.minecraft.core.MappedRegistry
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.progress.LoggerChunkProgressListener
import net.minecraft.server.level.ServerLevel
import net.minecraft.resources.ResourceLocation
import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.world.phys.Vec3
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.DerivedLevelData
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.mixin.MinecraftServerAccessor


object RealmGenerator {
    fun create(server: MinecraftServer, sourceWorld: Level, sourcePosition: BlockPos, radius: Int): ServerLevel {
        val serverAccess = server as MinecraftServerAccessor

        val id = ResourceLocation.fromNamespaceAndPath(Quellcraft.MOD_ID, sourcePosition.hashCode().toString())
        val worldKey = ResourceKey.create(Registries.DIMENSION, id)
//        val dimensionKey = RegistryKey.of(RegistryKeys.DIMENSION, id)

        val registry = server.registries().compositeAccess().lookupOrThrow(Registries.LEVEL_STEM) as MappedRegistry
//        val wasFrozen = (registry as SimpleRegistryAccessor).frozen
//        (registry as SimpleRegistryAccessor).frozen = false
//        registry.add(dimensionKey, registry.get(DimensionOptions.OVERWORLD), RegistryEntryInfo.DEFAULT)
//        (registry as SimpleRegistryAccessor).frozen = wasFrozen

        val world = ServerLevel(
            server,
            Util.backgroundExecutor(),
            serverAccess.storageSource,
            DerivedLevelData(server.worldData, server.worldData.overworldData()),
            worldKey,
            registry.getValue(ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation.fromNamespaceAndPath(Quellcraft.MOD_ID, "realm")))!!,
            LoggerChunkProgressListener.createCompleted(),
            false,
            server.worldData.worldGenOptions().seed(),
            emptyList(),
            true,
            null
        )

        serverAccess.levels[worldKey] = world
        ServerWorldEvents.LOAD.invoker().onWorldLoad(server, world)
        world.tick { true }

//        world.chunkManager.chunkLoadingManager.

        forBlocksInArea(sourcePosition, radius) { pos ->
            val sourceBlockState = sourceWorld.getBlockState(pos)
            world.setBlock(pos, sourceBlockState, 0)
//            val sourceBlockEntity = sourceWorld.getBlockEntity(pos)
        }

        forBlocksInArea(sourcePosition, radius) { pos ->
            sourceWorld.setBlock(pos, Blocks.AIR.defaultBlockState(), 0)
        }

        return world
    }

    fun forBlocksInArea(center: BlockPos, radius: Int, action: (BlockPos) -> Unit) {
        val startPos = center.offset(-radius, -radius, -radius)
        val endPos = center.offset(radius, radius, radius)
        val squareRadius = radius * radius
        for (x in startPos.x until endPos.x) {
            for (y in startPos.y until endPos.y) {
                for (z in startPos.z until endPos.z) {
                    val pos = BlockPos(x, y, z)
                    val centerDistance = Vec3(
                        (pos.x - center.x).toDouble(),
                        (pos.y - center.y).toDouble(),
                        (pos.z - center.z).toDouble()
                    ).lengthSqr()
                    if (centerDistance > squareRadius) continue

                    action(pos)
                }
            }
        }
    }
}
