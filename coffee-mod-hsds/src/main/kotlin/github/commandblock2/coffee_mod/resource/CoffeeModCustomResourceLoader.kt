package github.commandblock2.coffee_mod.resource

import com.google.gson.JsonParser
import github.commandblock2.coffee_mod.CoffeeMod
import java.util.Map
import java.util.function.Function
import java.util.stream.Collectors

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
                val entries =
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
                        .parallelStream()
                        .filter {
                            it.value.isJsonPrimitive
                        }

                entries
                    .collect(Collectors.toMap(
                        { entry -> entry.key },
                        { entry -> entry.value }
                    )
                    )
            }
}