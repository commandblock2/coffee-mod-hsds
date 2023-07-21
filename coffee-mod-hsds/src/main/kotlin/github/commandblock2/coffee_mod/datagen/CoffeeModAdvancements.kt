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

package github.commandblock2.coffee_mod.datagen

import github.commandblock2.coffee_mod.CoffeeMod
import github.commandblock2.coffee_mod.datagen.criterion.FeedSupportedEntityCriterion
import github.commandblock2.coffee_mod.datagen.criterion.ServerPlayerTriggerble
import github.commandblock2.coffee_mod.datagen.criterion.SuddenDeathCriterion
import github.commandblock2.coffee_mod.item.CoffeeModItems
import github.commandblock2.coffee_mod.potion.CoffeeModPotions
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementDisplay
import net.minecraft.advancement.AdvancementFrame
import net.minecraft.advancement.CriterionMerger
import net.minecraft.advancement.criterion.BrewedPotionCriterion
import net.minecraft.advancement.criterion.Criteria
import net.minecraft.advancement.criterion.Criterion
import net.minecraft.advancement.criterion.InventoryChangedCriterion
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.item.PotionItem
import net.minecraft.potion.PotionUtil
import net.minecraft.predicate.entity.LootContextPredicate
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import java.util.function.Consumer

class CoffeeModAdvancements(fabricDataOutput: FabricDataOutput) : FabricAdvancementProvider(fabricDataOutput) {
    override fun generateAdvancement(consumer: Consumer<Advancement>) {

        val defaultBackground = Identifier("textures/gui/advancements/backgrounds/adventure.png")

        Consumer<Consumer<Advancement>> {
            val rootAdvancement = Advancement.Builder
                .create()
                .display(
                    AdvancementDisplay(
                        ItemStack(Items.COCOA_BEANS, 1),
                        Text.translatable("advancements.${CoffeeMod.MOD_ID}.root.title"),
                        Text.translatable("advancements.${CoffeeMod.MOD_ID}.root.desc"),
                        defaultBackground,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                    )
                )
                .criterion("got_coffee_beans", InventoryChangedCriterion.Conditions.items(Items.COCOA_BEANS))
                .build(it, "${CoffeeMod.MOD_ID}/root")

            val feedingAnyWithBean = Advancement.Builder
                .create()
                .display(
                    AdvancementDisplay(
                        ItemStack(CoffeeModItems.catShitCoffeeBean, 1),
                        Text.translatable("advancements.${CoffeeMod.MOD_ID}.feed-any.title"),
                        Text.translatable("advancements.${CoffeeMod.MOD_ID}.feed-any.desc"),
                        defaultBackground,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                    )
                )
                .parent(rootAdvancement)
                .criterion("feed_any_supported_entity", FeedSupportedEntityCriterion.Condition())
                .build(it, "${CoffeeMod.MOD_ID}/feed-any")

            val brewWithBrewingTableButNotComplete = Advancement.Builder
                .create()
                .display(
                    AdvancementDisplay(
                        ItemStack(Items.BREWING_STAND, 1),
                        Text.translatable("advancements.${CoffeeMod.MOD_ID}.brew.title"),
                        Text.translatable("advancements.${CoffeeMod.MOD_ID}.brew.desc"),
                        defaultBackground,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                    )
                )
                .parent(rootAdvancement)

            val brewWithBrewingTable = CoffeeModPotions.allPotions.fold(brewWithBrewingTableButNotComplete) {
                acc, potion ->
                acc.criterion("brew_${potion.baseName}",
                    BrewedPotionCriterion.Conditions(LootContextPredicate.EMPTY, potion)
                )
            }
                .criteriaMerger(CriterionMerger.OR)
                .build(it, "${CoffeeMod.MOD_ID}/brew")

            val heartAttack = Advancement.Builder
                .create()
                .display(
                    AdvancementDisplay(
                        ItemStack(Items.HEARTBREAK_POTTERY_SHERD, 1),
                        Text.translatable("advancements.${CoffeeMod.MOD_ID}.die.title"),
                        Text.translatable("advancements.${CoffeeMod.MOD_ID}.die.desc"),
                        defaultBackground,
                        AdvancementFrame.GOAL,
                        true,
                        false,
                        true
                    )
                ).criterion("heart_attack", SuddenDeathCriterion.Condition())
                .parent(brewWithBrewingTable)
                .build(it, "${CoffeeMod.MOD_ID}/heart-attack")


        }.accept(consumer)
    }

    companion object {
        private val customCriteriaClasses : List<Class<out ServerPlayerTriggerble>> = listOf(
            FeedSupportedEntityCriterion::class.java,
        )

        private val _customCriteria = customCriteriaClasses.associateBy({ it }) {
            Criteria.register(it.getConstructor().newInstance() as Criterion<*>)
        }

        fun getCustomCriteria(clazz: Class<out ServerPlayerTriggerble>) : ServerPlayerTriggerble {
            return _customCriteria[clazz] as ServerPlayerTriggerble
        }
    }
}