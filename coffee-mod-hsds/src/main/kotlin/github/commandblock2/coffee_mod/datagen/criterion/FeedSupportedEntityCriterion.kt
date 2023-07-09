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

package github.commandblock2.coffee_mod.datagen.criterion

import com.google.gson.JsonObject
import github.commandblock2.coffee_mod.CoffeeMod
import net.minecraft.advancement.criterion.AbstractCriterion
import net.minecraft.advancement.criterion.AbstractCriterionConditions
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer
import net.minecraft.predicate.entity.LootContextPredicate
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

class FeedSupportedEntityCriterion : AbstractCriterion<FeedSupportedEntityCriterion.Condition>() {
    companion object{
        @JvmStatic
        val identifier = Identifier(CoffeeMod.MOD_ID, "feed_any_supported_entity")
    }

    override fun getId(): Identifier {
        return identifier
    }

    class Condition :
        AbstractCriterionConditions(identifier, LootContextPredicate.EMPTY)
    override fun conditionsFromJson(
        obj: JsonObject?,
        playerPredicate: LootContextPredicate?,
        predicateDeserializer: AdvancementEntityPredicateDeserializer?
    ): Condition {
        return Condition()
    }

    fun trigger(serverPlayerEntity: ServerPlayerEntity) {
        trigger(serverPlayerEntity) {
            true
        }
    }
}