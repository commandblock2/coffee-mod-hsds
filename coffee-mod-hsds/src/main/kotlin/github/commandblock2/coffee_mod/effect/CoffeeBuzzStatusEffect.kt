package github.commandblock2.coffee_mod.effect

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.AttributeContainer
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.server.world.ServerWorld

class CoffeeBuzzStatusEffect : StatusEffect(StatusEffectCategory.NEUTRAL, 0x6c4c37) {
    override fun canApplyUpdateEffect(duration: Int, amplifier: Int): Boolean {
        return false
    }

    override fun onRemoved(entity: LivingEntity?, attributes: AttributeContainer?, amplifier: Int) {
        super.onRemoved(entity, attributes, amplifier)
        entity ?: return
        val world = entity.entityWorld
        if (world is ServerWorld)
            world.updateSleepingPlayers()
    }
}