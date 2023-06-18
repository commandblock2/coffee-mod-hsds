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

import github.commandblock2.coffee_mod.CoffeeMod
import net.minecraft.entity.EntityType
import net.minecraft.entity.mob.MobEntity
import net.minecraft.registry.Registries

object CoffeeModEffects {
    val coffeeBuzzStatusEffect = CoffeeBuzzStatusEffect()

    val specialEffectByEntityType = CoffeeMod.supportedEntityTypes.associateBy({ it }) { registerShitCoffeeSpecialEffect(it) }
    private fun registerShitCoffeeSpecialEffect(entityType: EntityType<out MobEntity>): ShitCoffeeSpecialEffect {
        val effect = ShitCoffeeSpecialEffect(entityType)
        CoffeeMod.register(
            Registries.STATUS_EFFECT, entityType.untranslatedName + "_coffee_buzz",
            effect
        )
        return effect
    }

    val catCoffeeEffect = specialEffectByEntityType[EntityType.CAT]

    init {
        CoffeeMod.register(
            Registries.STATUS_EFFECT, "coffee_buzz",
            coffeeBuzzStatusEffect
        )
    }
}