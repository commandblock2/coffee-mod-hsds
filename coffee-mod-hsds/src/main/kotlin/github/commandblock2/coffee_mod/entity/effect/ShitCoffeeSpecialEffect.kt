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

import net.minecraft.entity.EntityType
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.entity.mob.MobEntity
import net.minecraft.item.SpawnEggItem
import net.minecraft.util.math.ColorHelper

class ShitCoffeeSpecialEffect(entityType: EntityType<out MobEntity>) : StatusEffect(
    StatusEffectCategory.NEUTRAL, ColorHelper.Argb.mixColor(
        SpawnEggItem.forEntity(entityType)?.getColor(0) ?: 0x0,
        SpawnEggItem.forEntity(entityType)?.getColor(1) ?: 0x0
    )
)