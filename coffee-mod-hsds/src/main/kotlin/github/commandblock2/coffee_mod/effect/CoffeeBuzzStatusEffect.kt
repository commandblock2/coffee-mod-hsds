package github.commandblock2.coffee_mod.effect

import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory

class CoffeeBuzzStatusEffect : StatusEffect(StatusEffectCategory.NEUTRAL, 0x873200) {
    override fun canApplyUpdateEffect(duration: Int, amplifier: Int): Boolean {
        return false
    }
}