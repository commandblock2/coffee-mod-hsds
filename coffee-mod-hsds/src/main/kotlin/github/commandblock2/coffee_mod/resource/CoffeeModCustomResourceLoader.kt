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

package github.commandblock2.coffee_mod.resource

import com.google.gson.JsonParser
import github.commandblock2.coffee_mod.CoffeeMod

object CoffeeModCustomResourceLoader {

    val translations =
        Thread
            .currentThread()
            .contextClassLoader
            .getResourceAsStream("assets/${CoffeeMod.MOD_ID}/lang/langs.txt")!!
            .bufferedReader()
            .readLines()
            .filter {
                it.endsWith(".json")
            }
            .associateBy({ it.substringBeforeLast('.') }) { filename ->
                    JsonParser.parseString(
                        Thread
                            .currentThread()
                            .contextClassLoader
                            .getResourceAsStream("assets/${CoffeeMod.MOD_ID}/lang/$filename")!!
                            .bufferedReader()
                            .readText()
                    )
                        .asJsonObject
                        .entrySet()
                        .filter {
                            it.value.isJsonPrimitive
                        }
                        .associateBy ({ it.key }) {
                            it.value.asString
                        }
            }
}