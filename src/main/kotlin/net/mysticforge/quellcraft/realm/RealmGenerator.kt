package net.mysticforge.quellcraft.realm

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents
import net.minecraft.block.Blocks
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.SimpleRegistry
import net.minecraft.server.MinecraftServer
import net.minecraft.server.WorldGenerationProgressLogger
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.level.UnmodifiableLevelProperties
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.mixin.MinecraftServerAccessor


object RealmGenerator {
    fun create(server: MinecraftServer, sourceWorld: World, sourcePosition: BlockPos, radius: Int): ServerWorld {
        val serverAccess = server as MinecraftServerAccessor

        val id = Identifier.of(Quellcraft.MOD_ID, sourcePosition.hashCode().toString())
        val worldKey = RegistryKey.of(RegistryKeys.WORLD, id)
//        val dimensionKey = RegistryKey.of(RegistryKeys.DIMENSION, id)

        val registry = server.getCombinedDynamicRegistries().getCombinedRegistryManager().getOrThrow(RegistryKeys.DIMENSION) as SimpleRegistry
//        val wasFrozen = (registry as SimpleRegistryAccessor).frozen
//        (registry as SimpleRegistryAccessor).frozen = false
//        registry.add(dimensionKey, registry.get(DimensionOptions.OVERWORLD), RegistryEntryInfo.DEFAULT)
//        (registry as SimpleRegistryAccessor).frozen = wasFrozen

        val world = ServerWorld(
            server,
            Util.getMainWorkerExecutor(),
            serverAccess.session,
            UnmodifiableLevelProperties(server.saveProperties, server.saveProperties.mainWorldProperties),
            worldKey,
            registry.get(RegistryKey.of(RegistryKeys.DIMENSION, Identifier.of(Quellcraft.MOD_ID, "realm"))),
            WorldGenerationProgressLogger.noSpawnChunks(),
            false,
            server.saveProperties.generatorOptions.seed,
            emptyList(),
            true,
            null
        )

        serverAccess.worlds.put(worldKey, world)
        ServerWorldEvents.LOAD.invoker().onWorldLoad(server, world)
        world.tick { true }

//        world.chunkManager.chunkLoadingManager.

        forBlocksInArea(sourcePosition, radius) { pos ->
            val sourceBlockState = sourceWorld.getBlockState(pos)
            world.setBlockState(pos, sourceBlockState, 0)
//            val sourceBlockEntity = sourceWorld.getBlockEntity(pos)
        }

        forBlocksInArea(sourcePosition, radius) { pos ->
            sourceWorld.setBlockState(pos, Blocks.AIR.getDefaultState(), 0)
        }

        return world
    }

    fun forBlocksInArea(center: BlockPos, radius: Int, action: (BlockPos) -> Unit) {
        val startPos = center.add(-radius, -radius, -radius)
        val endPos = center.add(radius, radius, radius)
        val squareRadius = radius * radius
        for (x in startPos.x until endPos.x) {
            for (y in startPos.y until endPos.y) {
                for (z in startPos.z until endPos.z) {
                    val pos = BlockPos(x, y, z)
                    val centerDistance = Vec3d(
                        (pos.x - center.x).toDouble(),
                        (pos.y - center.y).toDouble(),
                        (pos.z - center.z).toDouble()
                    ).lengthSquared()
                    if (centerDistance > squareRadius) continue

                    action(pos)
                }
            }
        }
    }
}
