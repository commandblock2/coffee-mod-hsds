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

package github.commandblock2.coffee_mod.entity

import github.commandblock2.coffee_mod.CoffeeMod
import github.commandblock2.coffee_mod.entity.ai.brain.CoffeeModSchedule
import github.commandblock2.coffee_mod.entity.effect.CoffeeModEffects
import github.commandblock2.coffee_mod.item.CoffeeModItems
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents
import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.boss.dragon.EnderDragonPart
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.PersistentState
import net.minecraft.world.World
import java.util.*
import kotlin.math.abs

object CoffeeModEntitySupport {

    private const val COFFEE_BEAN_TIMER_LENGTH = 6000
    val coffeeBeanTimers: MutableMap<LivingEntity, Int> = mutableMapOf()
    val coffeeDeathList: MutableSet<ServerPlayerEntity> = mutableSetOf()

    private const val COFFEE_SAFE_TICKS = 20L * 60 * 30
    val suddenDeathCountdown: MutableMap<LivingEntity, Long> = mutableMapOf()


    val catchupPhantomSpawnList: MutableMap<PlayerEntity, Int> = mutableMapOf()

    init {
        CoffeeModSchedule


        UseEntityCallback.EVENT.register { player, _, hand, entityCouldBeEnderDragonPart, _ ->

            val entity =
                if (entityCouldBeEnderDragonPart is EnderDragonPart) entityCouldBeEnderDragonPart.owner else entityCouldBeEnderDragonPart



            if (hand != Hand.MAIN_HAND ||
                entity !is LivingEntity ||
                !CoffeeMod.supportedEntityTypes.contains(entity.type)
            ) {
                return@register ActionResult.PASS
            }


            val itemStack = player.getStackInHand(hand)
            if (itemStack.item == Items.COCOA_BEANS && startIngesting(entity)) {

                if (!player.isCreative)
                    itemStack.decrement(1)

                return@register ActionResult.success(entity.world.isClient)
            }

            ActionResult.PASS
        }


        ServerTickEvents.START_SERVER_TICK.register {
            processCoffeeBeanTick()
            processCoffeeDeathTick()
        }

        ServerLifecycleEvents.SERVER_STARTED.register { server ->
            val storage = server.getWorld(World.OVERWORLD)!!.persistentStateManager.getOrCreate(
                { nbt ->
                    loadCoffeeBeanTickFromNBT(nbt, server)
                    loadCoffeeDeathTickFromNBT(nbt, server)
                    PersistentStorage()
                },
                {
                    PersistentStorage()
                },
                CoffeeMod.MOD_ID
            )

            ServerWorldEvents.UNLOAD.register { _, _ ->
                storage.markDirty()
            }
        }
    }

    private fun processCoffeeDeathTick() {
        for (entity in suddenDeathCountdown.keys) {
            val effectLevel = entity.getStatusEffect(CoffeeModEffects.coffeeBuzzStatusEffect)!!.amplifier + 1
            val tickSpeed = effectLevel + if (entity.hasStatusEffect(CoffeeModEffects.catCoffeeEffect)) 1 else 0

            repeat(tickSpeed) {
                val coffeeCountdown = suddenDeathCountdown[entity]!! - 1
                suddenDeathCountdown[entity] = coffeeCountdown

                if (coffeeCountdown < 0 && (abs(coffeeCountdown) % 20).toInt() == entity.id % 20) {
                    val secs = 15 * 60
                    val oneOverDeathRate = secs * secs

                    if (coffeeCountdown / -20 > entity.random.nextInt(oneOverDeathRate) && !entity.isDead) {
                        if (entity is ServerPlayerEntity)
                            coffeeDeathList.add(entity)
                        entity.kill()
                    }
                }
            }
        }
    }

    private fun loadCoffeeBeanTickFromNBT(nbt: NbtCompound, server: MinecraftServer) {
        coffeeBeanTimers.clear()

        coffeeBeanTimers.putAll(

            (nbt.get("beanTimers")
                    as NbtList)
                .associateBy({
                    val nbtCompound = (it as NbtCompound)
                    server.worlds.toList()[nbtCompound.getInt("dimension")].getEntity(
                        UUID.fromString(
                            nbtCompound
                                .getString("entityId")!!
                        )
                    ) as LivingEntity
                }) {
                    (it as NbtCompound).getInt("coffeeBean")
                }

        )
    }


    private fun loadCoffeeDeathTickFromNBT(nbt: NbtCompound, server: MinecraftServer) {
        suddenDeathCountdown.clear()

        suddenDeathCountdown.putAll(

            (nbt.get("deathTimers")
                    as NbtList)
                .associateBy({
                    val nbtCompound = (it as NbtCompound)
                    server.worlds.toList()[nbtCompound.getInt("dimension")].getEntity(
                        UUID.fromString(
                            nbtCompound
                                .getString("entityId")!!
                        )
                    ) as LivingEntity
                }) {
                    (it as NbtCompound).getLong("death")
                }

        )
    }

    private fun processCoffeeBeanTick() {
        for (entity in coffeeBeanTimers.keys) {
            val timer = coffeeBeanTimers[entity] ?: continue

            if (timer > 0)
                coffeeBeanTimers[entity] = timer - 1

            if (timer - 1 == 0)
                entity.dropStack(
                    ItemStack(
                        CoffeeModItems.shitCoffeeBeanItemByEntityType[entity.type],
                        entity.random.nextInt(3) + 1
                    )
                )
        }

        coffeeBeanTimers.keys.removeIf {
            coffeeBeanTimers[it] == 0
        }
    }

    private fun startIngesting(entity: LivingEntity): Boolean {
        if (coffeeBeanTimers.containsKey(entity)) {
            return false
        }

        if (entity.world.isClient)
            return true

        coffeeBeanTimers[entity] = COFFEE_BEAN_TIMER_LENGTH
        return true
    }

    fun addToCoffeeDeathTracker(entity: LivingEntity) {
        suddenDeathCountdown[entity] = COFFEE_SAFE_TICKS
    }

    fun removeEntityFromDeathTracker(entity: LivingEntity) {
        suddenDeathCountdown.remove(entity)
    }
}

class PersistentStorage : PersistentState() {

    override fun writeNbt(nbt: NbtCompound?): NbtCompound {

        val beanTimers = CoffeeModEntitySupport.coffeeBeanTimers.map { (entity, value) ->
            val entryTag = NbtCompound()
            entryTag.putString("entityId", entity.uuidAsString)
            entryTag.putInt("dimension", entity.server!!.worlds.indexOf(entity.world))
            entryTag.putInt("coffeeBean", value)
            entryTag
        }.toCollection(NbtList())

        nbt!!.put("beanTimers", beanTimers)

        val deathTimers = CoffeeModEntitySupport.suddenDeathCountdown.map { (entity, value) ->
            val entryTag = NbtCompound()
            entryTag.putString("entityId", entity.uuidAsString)
            entryTag.putInt("dimension", entity.server!!.worlds.indexOf(entity.world))
            entryTag.putLong("death", value)
            entryTag
        }.toCollection(NbtList())

        nbt.put("deathTimers", deathTimers)

        return nbt
    }

}