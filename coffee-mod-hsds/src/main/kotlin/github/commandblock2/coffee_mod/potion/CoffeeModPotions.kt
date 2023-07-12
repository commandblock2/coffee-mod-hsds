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

package github.commandblock2.coffee_mod.potion

import github.commandblock2.coffee_mod.CoffeeMod
import github.commandblock2.coffee_mod.entity.effect.CoffeeModEffects
import github.commandblock2.coffee_mod.item.CoffeeModItems
import github.commandblock2.lang.interop.util.TriConsumer
import net.minecraft.entity.EntityType
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.potion.Potion
import net.minecraft.potion.Potions
import net.minecraft.registry.Registries

object CoffeeModPotions {

    private const val REGULAR_DURATION = 20 * 60 * 40
    private const val REGULAR_HASTE_DURATION = 20 * 60 * 5

    private const val BOOST_DURATION = 20 * 60 * 40
    private const val BOOST_HASTE_DURATION = 20 * 60 * 5

    private const val PROLONGED_DURATION = 20 * 60 * 40
    private const val PROLONGED_HASTE_DURATION = 20 * 60 * 5

    val REGULAR_COFFEE_POTION = CoffeeMod.register(
        Registries.POTION, "regular_coffee_potion", Potion("regular_coffee_potion",
            StatusEffectInstance(CoffeeModEffects.coffeeBuzzStatusEffect, REGULAR_DURATION),
            StatusEffectInstance(StatusEffects.HASTE, REGULAR_HASTE_DURATION)
        )
    )

    val BOOSTED_COFFEE_POTION = CoffeeMod.register(
        Registries.POTION, "boosted_coffee_potion", Potion("boosted_coffee_potion",
            StatusEffectInstance(CoffeeModEffects.coffeeBuzzStatusEffect, BOOST_DURATION, 1),
            StatusEffectInstance(StatusEffects.HASTE, BOOST_HASTE_DURATION, 1)
        )
    )

    val PROLONGED_COFFEE_POTION = CoffeeMod.register(
        Registries.POTION, "prolonged_coffee_potion", Potion("prolonged_coffee_potion",
            StatusEffectInstance(CoffeeModEffects.coffeeBuzzStatusEffect, PROLONGED_DURATION),
            StatusEffectInstance(StatusEffects.HASTE, PROLONGED_HASTE_DURATION)
        )
    )

    val specialShitCoffeeBrewingRecipe = CoffeeMod.supportedEntityTypes.map {

        val shitCoffeeRegularPotion = CoffeeMod.register(
            Registries.POTION,
            it.untranslatedName + "_shit_coffee_potion", Potion(it.untranslatedName + "_shit_coffee_potion",
                StatusEffectInstance(CoffeeModEffects.specialEffectByEntityType[it], REGULAR_DURATION),
                StatusEffectInstance(CoffeeModEffects.coffeeBuzzStatusEffect, REGULAR_DURATION),
                StatusEffectInstance(StatusEffects.HASTE, REGULAR_HASTE_DURATION, if (it == EntityType.CAT) 1 else 0)
            )
        )
        val shitCoffeeBoostedPotion = CoffeeMod.register(
            Registries.POTION,
            it.untranslatedName + "_shit_coffee_boosted_potion", Potion(it.untranslatedName + "_shit_coffee_boosted_potion",
                StatusEffectInstance(CoffeeModEffects.specialEffectByEntityType[it], BOOST_DURATION),
                StatusEffectInstance(CoffeeModEffects.coffeeBuzzStatusEffect, BOOST_DURATION),
                StatusEffectInstance(StatusEffects.HASTE, BOOST_HASTE_DURATION, if (it == EntityType.CAT) 2 else 1)
            )
        )
        val shitCoffeeProlongedPotion = CoffeeMod.register(
            Registries.POTION,
            it.untranslatedName + "_shit_coffee_prolonged_potion", Potion(it.untranslatedName + "_shit_coffee_prolonged_potion",
                StatusEffectInstance(CoffeeModEffects.specialEffectByEntityType[it], PROLONGED_DURATION),
                StatusEffectInstance(CoffeeModEffects.coffeeBuzzStatusEffect, PROLONGED_DURATION),
                StatusEffectInstance(StatusEffects.HASTE, PROLONGED_HASTE_DURATION, if (it == EntityType.CAT) 1 else 0)
            )
        )

        val item = CoffeeModItems.shitCoffeeBeanItemByEntityType[it]
        val triple = Triple(shitCoffeeRegularPotion, shitCoffeeBoostedPotion, shitCoffeeProlongedPotion)
        Pair(
            item,
            triple
        )
    }

    val allPotions = specialShitCoffeeBrewingRecipe
        .flatMap {
            it.second.toList()
        }
        .union(
            listOf(
                REGULAR_COFFEE_POTION,
                BOOSTED_COFFEE_POTION,
                PROLONGED_COFFEE_POTION
            )
        ).toList()

    fun registerPotions(func: TriConsumer<Potion, Item, Potion>) {
        func.accept(Potions.WATER, Items.COCOA_BEANS, REGULAR_COFFEE_POTION)
        func.accept(REGULAR_COFFEE_POTION, Items.GLOWSTONE_DUST, BOOSTED_COFFEE_POTION)
        func.accept(REGULAR_COFFEE_POTION, Items.REDSTONE, PROLONGED_COFFEE_POTION)

        for (brewingRecipe in specialShitCoffeeBrewingRecipe) {
            func.accept(Potions.WATER, brewingRecipe.first, brewingRecipe.second.first)
            func.accept(brewingRecipe.second.first, Items.GLOWSTONE_DUST, brewingRecipe.second.second)
            func.accept(brewingRecipe.second.first, Items.REDSTONE, brewingRecipe.second.third)
        }
    }

}