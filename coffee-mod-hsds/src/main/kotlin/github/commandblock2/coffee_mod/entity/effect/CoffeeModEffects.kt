package github.commandblock2.coffee_mod.entity.effect

import github.commandblock2.coffee_mod.CoffeeMod
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object CoffeeModEffects {
     val coffeeBuzzStatusEffect = CoffeeBuzzStatusEffect()
    init {
        CoffeeMod.register(
            Registries.STATUS_EFFECT,  "coffee_buzz",
            coffeeBuzzStatusEffect
        )
    }
}