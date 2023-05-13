/*
 * This file is part of CoffeeMod (https://github.com/CCBlueX/CoffeeMod)
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

package github.commandblock2.coffee_mod.potion

import github.commandblock2.coffee_mod.CoffeeMod
import github.commandblock2.coffee_mod.entity.effect.CoffeeModEffects
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.potion.Potion
import net.minecraft.registry.Registries

object CoffeeModPotions {

    val REGULAR_COFFEE_POTION = CoffeeMod.register(Registries.POTION,  "regular_coffee_potion", Potion(
        StatusEffectInstance(CoffeeModEffects.coffeeBuzzStatusEffect, 20 * 60 * 40),
        StatusEffectInstance(StatusEffects.HASTE, 20 * 60 * 5)
    ))

    val ENHANCED_COFFEE_POTION = CoffeeMod.register(Registries.POTION,  "enhanced_coffee_potion", Potion(
        StatusEffectInstance(CoffeeModEffects.coffeeBuzzStatusEffect, 20 * 60 * 30, 2),
        StatusEffectInstance(StatusEffects.HASTE, 20 * 60 * 5, 2)
    ))

    val PROLONGED_COFFEE_POTION = CoffeeMod.register(Registries.POTION,  "prolonged_coffee_potion", Potion(
        StatusEffectInstance(CoffeeModEffects.coffeeBuzzStatusEffect, 20 * 60 * 60),
        StatusEffectInstance(StatusEffects.HASTE, 20 * 60 * 10)
    ))

}