package github.commandblock2.coffee_mod.potion

import github.commandblock2.coffee_mod.CoffeeMod
import github.commandblock2.coffee_mod.entity.effect.CoffeeModEffects
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.potion.Potion
import net.minecraft.registry.Registries

object CoffeeModPotions {

    val REGULAR_COFFEE_POTION = CoffeeMod.register(Registries.POTION,  "regular_coffee_potion", Potion(
        StatusEffectInstance(CoffeeModEffects.coffeeBuzzStatusEffect, 20 * 60 * 40),
        StatusEffectInstance(StatusEffects.HASTE, 20 * 60 * 5)
    ))

    val ENHANCED_COFFEE_POTION = CoffeeMod.register(Registries.POTION,  "enhanced_coffee_potion", Potion(
        StatusEffectInstance(CoffeeModEffects.coffeeBuzzStatusEffect, 20 * 60 * 30, 2),
        StatusEffectInstance(StatusEffects.HASTE, 20 * 60 * 5, 2)
    ))

    val PROLONGED_COFFEE_POTION = CoffeeMod.register(Registries.POTION,  "prolonged_coffee_potion", Potion(
        StatusEffectInstance(CoffeeModEffects.coffeeBuzzStatusEffect, 20 * 60 * 60),
        StatusEffectInstance(StatusEffects.HASTE, 20 * 60 * 10)
    ))

}