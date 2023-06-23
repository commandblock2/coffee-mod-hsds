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

package github.commandblock2.coffee_mod.entity.ai.brain

import net.minecraft.entity.ai.brain.Activity
import net.minecraft.entity.ai.brain.Schedule

object CoffeeModSchedule {
    val VILLAGER_BABY_COFFEE =
        Schedule.register("villager_baby_coffee")
            .withActivity(10, Activity.IDLE)
            .withActivity(3000, Activity.PLAY)
            .withActivity(6000, Activity.IDLE)
            .withActivity(10000, Activity.PLAY).build()


    val VILLAGER_COFFEE =
        Schedule.register("villager_coffee")
            .withActivity(10, Activity.IDLE)
            .withActivity(2000, Activity.WORK)
            .withActivity(9000, Activity.MEET)
            .withActivity(11000, Activity.IDLE)
            .withActivity(12000, Activity.WORK)
            .build()
    init {
        VILLAGER_COFFEE
        VILLAGER_BABY_COFFEE
    }
}