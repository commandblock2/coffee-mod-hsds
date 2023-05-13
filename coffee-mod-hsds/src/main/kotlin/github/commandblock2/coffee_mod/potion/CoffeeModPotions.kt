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
import github.commandblock2.coffee_mod.item.CoffeeModItems
import github.commandblock2.lang.interop.util.TriConsumer
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.potion.Potion
import net.minecraft.potion.Potions
import net.minecraft.registry.Registries
import java.util.*

object CoffeeModPotions {

    private val REGULAR_COFFEE_POTION = CoffeeMod.register(
        Registries.POTION, "regular_coffee_potion", Potion(
            StatusEffectInstance(CoffeeModEffects.coffeeBuzzStatusEffect, 20 * 60 * 40),
            StatusEffectInstance(StatusEffects.HASTE, 20 * 60 * 5)
        )
    )

    private val ENHANCED_COFFEE_POTION = CoffeeMod.register(
        Registries.POTION, "enhanced_coffee_potion", Potion(
            StatusEffectInstance(CoffeeModEffects.coffeeBuzzStatusEffect, 20 * 60 * 30, 1),
            StatusEffectInstance(StatusEffects.HASTE, 20 * 60 * 5, 1)
        )
    )

    private val PROLONGED_COFFEE_POTION = CoffeeMod.register(
        Registries.POTION, "prolonged_coffee_potion", Potion(
            StatusEffectInstance(CoffeeModEffects.coffeeBuzzStatusEffect, 20 * 60 * 60),
            StatusEffectInstance(StatusEffects.HASTE, 20 * 60 * 10)
        )
    )

    private val CAT_SHIT_COFFEE_POTION = CoffeeMod.register(
        Registries.POTION, "cat_shit_coffee_potion", Potion(
            StatusEffectInstance(CoffeeModEffects.coffeeBuzzStatusEffect, 20 * 60 * 40, 1),
            StatusEffectInstance(StatusEffects.HASTE, 20 * 60 * 5, 1),
            StatusEffectInstance(
                StatusEffects.NAUSEA, 20 * 15, 0, false, true, true,
                StatusEffectInstance(StatusEffects.NAUSEA, 20 * 15 * 30, 0), Optional.empty()
            )
            // How do I actually make this hidden?
        )
    )

    private val ENHANCED_CAT_SHIT_COFFEE_POTION = CoffeeMod.register(
        Registries.POTION, "enhanced_cat_shit_coffee_potion", Potion(
            StatusEffectInstance(CoffeeModEffects.coffeeBuzzStatusEffect, 20 * 60 * 30, 2),
            StatusEffectInstance(StatusEffects.HASTE, 20 * 60 * 5, 2),
            StatusEffectInstance(
                StatusEffects.NAUSEA, 20 * 30, 0, false, true, true,
                StatusEffectInstance(StatusEffects.NAUSEA, 20 * 15 * 30, 0), Optional.empty()
            )
        )
    )

    private val PROLONGED_CAT_SHIT_COFFEE_POTION = CoffeeMod.register(
        Registries.POTION, "prolonged_cat_shit_coffee_potion", Potion(
            StatusEffectInstance(CoffeeModEffects.coffeeBuzzStatusEffect, 20 * 60 * 30, 1),
            StatusEffectInstance(
                StatusEffects.HASTE, 20 * 60 * 10, 1, false, true, true,
                StatusEffectInstance(StatusEffects.NAUSEA, 20 * 15 * 30, 0), Optional.empty()
            )

        )
    )

    fun registerPotions(func: TriConsumer<Potion, Item, Potion>) {
        func.accept(Potions.WATER, Items.COCOA_BEANS, REGULAR_COFFEE_POTION)
        func.accept(REGULAR_COFFEE_POTION, Items.GLOWSTONE_DUST, ENHANCED_COFFEE_POTION)
        func.accept(REGULAR_COFFEE_POTION, Items.REDSTONE, PROLONGED_COFFEE_POTION)

        func.accept(Potions.WATER, CoffeeModItems.CatShitCoffeeBeanItem, CAT_SHIT_COFFEE_POTION)
        func.accept(CAT_SHIT_COFFEE_POTION, Items.GLOWSTONE_DUST, ENHANCED_CAT_SHIT_COFFEE_POTION)
        func.accept(CAT_SHIT_COFFEE_POTION, Items.REDSTONE, PROLONGED_CAT_SHIT_COFFEE_POTION)
    }

}