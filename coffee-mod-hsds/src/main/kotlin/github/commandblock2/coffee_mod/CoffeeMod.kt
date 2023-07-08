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

package github.commandblock2.coffee_mod

import github.commandblock2.coffee_mod.config.CoffeeModConfigSomething
import github.commandblock2.coffee_mod.entity.CoffeeModEntitySupport
import github.commandblock2.coffee_mod.entity.effect.CoffeeModEffects
import github.commandblock2.coffee_mod.item.CoffeeModItems
import github.commandblock2.coffee_mod.networking.CoffeeModNetworking
import github.commandblock2.coffee_mod.potion.CoffeeModPotions
import github.commandblock2.coffee_mod.resource.CoffeeModCustomResourceLoader
import net.fabricmc.api.ModInitializer
import net.minecraft.entity.EntityType
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier


@Suppress("UNUSED")
object CoffeeMod : ModInitializer {
    const val MOD_ID = "coffee_mod"
    val config = CoffeeModConfigSomething.config.disablePhantomSpawning()
    // doesn't work here
    override fun onInitialize() {
        CoffeeModEffects
        CoffeeModItems
        CoffeeModPotions
        CoffeeModEntitySupport
        CoffeeModNetworking
        CoffeeModCustomResourceLoader

    }

    val supportedEntityTypes = listOf(
        EntityType.CAT,
        EntityType.ENDER_DRAGON,
        EntityType.VILLAGER
    )

    fun <V, T : V?> register(registry: Registry<V>, path: String, entry: T): T {
        return Registry.register(registry, Identifier(MOD_ID, path), entry)
    }

}