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

package github.commandblock2.coffee_mod.datagen

import github.commandblock2.coffee_mod.item.CoffeeModItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Models

class CoffeeModModels(fabricDataOutput: FabricDataOutput): FabricModelProvider(fabricDataOutput) {
    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {

    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {

        CoffeeModItems.shitCoffeeBeanItemByEntityType.forEach {
            itemModelGenerator.register(it.value, CoffeeModItems.catShitCoffeeBean, Models.GENERATED)
        }


    }
}