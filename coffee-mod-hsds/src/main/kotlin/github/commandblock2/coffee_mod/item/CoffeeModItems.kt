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

package github.commandblock2.coffee_mod.item

import github.commandblock2.coffee_mod.CoffeeMod
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.registry.Registries


object CoffeeModItems {

    val shitCoffeeBeanItemByEntityType = CoffeeMod.supportedEntityTypes
        .associateBy({ it }) {
            CoffeeMod.register(
                Registries.ITEM,
                it.untranslatedName + "_shit_coffee_bean",
                Item(
                    FabricItemSettings()
                        .maxCount(64)
                )
            )
        }

    init {
        shitCoffeeBeanItemByEntityType
    }
}