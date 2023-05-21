package github.commandblock2.coffee_mod.entity.effect

import net.minecraft.entity.EntityType
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.entity.mob.MobEntity
import net.minecraft.item.SpawnEggItem

class ShitCoffeeSpecialEffect(val entityType: EntityType<out MobEntity>) : StatusEffect(
    StatusEffectCategory.NEUTRAL, SpawnEggItem.forEntity(entityType)?.getColor(0) ?: 0x0000
)