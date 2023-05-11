package github.commandblock2.coffee_mod.entity.effect

import github.commandblock2.coffee_mod.CoffeeMod
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object CoffeModEffects {
     val coffeeBuzzStatusEffect = CoffeeBuzzStatusEffect()
    init {
        Registry.register(
            Registries.STATUS_EFFECT, Identifier(CoffeeMod.MOD_ID, "coffee_buzz"),
            coffeeBuzzStatusEffect
        )
    }
}