/*
 * This file is part of CoffeeMod (https://github.com/commandblock2/coffee-mod-hsds)
 *
 * Copyright (c) 2023 commandblock2
 *
 * CoffeeMod is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CoffeeMod is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CoffeeMod. If not, see <https://www.gnu.org/licenses/>.
 */

package github.commandblock2.coffee_mod.entity.effect

import com.google.common.collect.ImmutableSet
import com.mojang.datafixers.util.Pair
import github.commandblock2.coffee_mod.entity.CoffeeModEntitySupport
import github.commandblock2.coffee_mod.world.CoffeeModGamerules.clearTimerOnSleep
import net.minecraft.entity.EntityData
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.ai.brain.Activity
import net.minecraft.entity.ai.brain.MemoryModuleState
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.Schedule
import net.minecraft.entity.ai.brain.task.VillagerTaskListProvider
import net.minecraft.entity.attribute.AttributeContainer
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.stat.Stats
import net.minecraft.util.math.MathHelper
import net.minecraft.village.VillagerProfession
import net.minecraft.world.SpawnHelper

class CoffeeBuzzStatusEffect : StatusEffect(StatusEffectCategory.NEUTRAL, 0x6c4c37) {
    override fun canApplyUpdateEffect(duration: Int, amplifier: Int): Boolean {
        return false
    }

    override fun onApplied(entity: LivingEntity?, attributes: AttributeContainer?, amplifier: Int) {
        super.onApplied(entity, attributes, amplifier)
        entity ?: return
        val world = entity.entityWorld
        if (world !is ServerWorld)
            return

        CoffeeModEntitySupport.addToCoffeeDeathTracker(entity)
        if (entity is VillagerEntity && entity.world is ServerWorld) {
            entity.reinitializeBrain(entity.world as ServerWorld)
        }
    }

    override fun onRemoved(entity: LivingEntity?, attributes: AttributeContainer?, amplifier: Int) {
        super.onRemoved(entity, attributes, amplifier)
        entity ?: return
        val world = entity.entityWorld
        if (world !is ServerWorld)
            return

        world.updateSleepingPlayers()

        if (!world.getGameRules().getBoolean(clearTimerOnSleep))
            CoffeeModEntitySupport.removeEntityFromDeathTracker(entity)

        if (entity is VillagerEntity) {
            val villagerProfession: VillagerProfession = entity.villagerData.profession
            if (entity.isBaby()) {
                entity.brain.schedule = Schedule.VILLAGER_BABY
                entity.brain.setTaskList(Activity.PLAY, VillagerTaskListProvider.createPlayTasks(0.5f))
            } else {
                entity.brain.schedule = Schedule.VILLAGER_DEFAULT
                entity.brain.setTaskList(
                    Activity.WORK,
                    VillagerTaskListProvider.createWorkTasks(villagerProfession, 0.5f),
                    ImmutableSet.of<Pair<MemoryModuleType<*>, MemoryModuleState>>(
                        Pair.of(
                            MemoryModuleType.JOB_SITE,
                            MemoryModuleState.VALUE_PRESENT
                        )
                    )
                )
            }
        }

        if (CoffeeModEntitySupport.catchupPhantomSpawnList.containsKey(entity) && !entity.world.isClient) {
            repeat(CoffeeModEntitySupport.catchupPhantomSpawnList[entity]!!) {
                if (entity is ServerPlayerEntity) {
                    if (entity.isSpectator()) return@repeat
                    val blockPos = entity.getBlockPos()
                    val localDifficulty = world.getLocalDifficulty(blockPos)

                    if (world.dimension.hasSkyLight() && (blockPos.y < world.seaLevel || !world.isSkyVisible(blockPos)) || !localDifficulty.isHarderThan(
                            world.random.nextFloat() * 3.0f
                        )
                    ) return@repeat
                    val serverStatHandler = entity.statHandler
                    val lastRested = MathHelper.clamp(
                        serverStatHandler.getStat(Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST)),
                        1,
                        Int.MAX_VALUE
                    )


                    val spawnPos = blockPos
                        .up(20 + world.random.nextInt(15))
                        .east(-10 + world.random.nextInt(21))
                        .south(-10 + world.random.nextInt(21))
                    if (world.random.nextInt(lastRested) < 72000 || !SpawnHelper.isClearForSpawn(
                            world,
                            spawnPos,
                            world.getBlockState(spawnPos),
                            world.getFluidState(spawnPos),
                            EntityType.PHANTOM
                        )
                    ) return@repeat
                    var entityData: EntityData? = null
                    val count: Int = 1 + world.random.nextInt(localDifficulty.globalDifficulty.id + 1)
                    repeat(count) {
                        (EntityType.PHANTOM.create(world))?.apply {
                            refreshPositionAndAngles(spawnPos, 0.0f, 0.0f)
                            entityData =
                                initialize(world, localDifficulty, SpawnReason.NATURAL, entityData, null)
                        }?.also {
                            world.spawnEntityAndPassengers(it)
                        }

                    }
                }
            }
        }
    }

}