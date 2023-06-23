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