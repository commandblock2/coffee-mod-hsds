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

package github.commandblock2.coffee_mod.world

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory

import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry

import net.minecraft.world.GameRules
import net.minecraft.world.GameRules.BooleanRule


object CoffeeModGamerules {
    val spawnPhantoms =
        GameRuleRegistry.register("spawnPhantomsForCoffeedPlayer", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true))
    val catchupSpawn =
        GameRuleRegistry.register("respawnDiscardedPhantoms", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(false))
}