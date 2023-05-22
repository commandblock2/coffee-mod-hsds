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

package github.commandblock2.coffee_mod.networking

import github.commandblock2.coffee_mod.CoffeeMod
import github.commandblock2.coffee_mod.entity.effect.CoffeeModEffects
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.EntityType
import net.minecraft.entity.projectile.DragonFireballEntity
import net.minecraft.util.Identifier
import kotlin.math.cos
import kotlin.math.sin


object CoffeeModNetworking {
    val fireballRequestID = Identifier(CoffeeMod.MOD_ID, "dragon_fireball_request")

    init {
        ServerPlayNetworking.registerGlobalReceiver(
            fireballRequestID
        ) { _, player, _, _, _ ->
            if (player.hasStatusEffect(CoffeeModEffects.specialEffectByEntityType[EntityType.ENDER_DRAGON])) {
                val pitch = (player.getPitch(1.0f) + 90) * Math.PI / 180
                val yaw = -((player.getYaw(1.0f) + 90) * Math.PI) / 180

                val xDirection = sin(pitch) * cos(yaw)
                val yDirection = cos(pitch)
                val zDirection = -sin(pitch) * sin(yaw)

                val fireballEntity = DragonFireballEntity(player.world, player, xDirection, yDirection, zDirection)

                fireballEntity.setPosition(fireballEntity.pos.add(.0, player.standingEyeHeight.toDouble(),.0))

                player.world.spawnEntity(fireballEntity)
            }
        }
    }
}