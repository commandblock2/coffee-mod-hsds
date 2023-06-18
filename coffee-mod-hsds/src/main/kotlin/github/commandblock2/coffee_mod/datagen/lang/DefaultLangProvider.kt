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

package github.commandblock2.coffee_mod.datagen.lang

import com.google.gson.JsonParser
import github.commandblock2.coffee_mod.CoffeeMod
import github.commandblock2.coffee_mod.entity.effect.CoffeeModEffects
import github.commandblock2.coffee_mod.item.CoffeeModItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import kotlin.io.path.reader

class DefaultLangProvider(val fabricDataOutput: FabricDataOutput?, val languageCode: String) :
    FabricLanguageProvider(fabricDataOutput, languageCode) {


    override fun generateTranslations(translationBuilder: TranslationBuilder?) {

        val existingPath = fabricDataOutput!!.modContainer.findPath(
            "assets/${CoffeeMod.MOD_ID}/lang/${languageCode}.pattern.json"
        ).get()

        val json = existingPath.reader().let {
            JsonParser.parseReader(it).asJsonObject
        }

        val shitCoffeeBeanItemBaseTranslationKey = "base.item.${CoffeeMod.MOD_ID}.shit_coffee_bean"
        CoffeeModItems.shitCoffeeBeanItemByEntityType.entries.forEach {
            translationBuilder!!.add(it.value, json[shitCoffeeBeanItemBaseTranslationKey].asString.format(it.key.name.string))
        }

        translationBuilder!!.add(CoffeeModEffects.coffeeBuzzStatusEffect, json["effect.${CoffeeMod.MOD_ID}.coffee_buzz"].asString)
        val coffeeEffectBaseTranslationKey = "base.effect.${CoffeeMod.MOD_ID}.effect_name"
        CoffeeModEffects.specialEffectByEntityType.entries.forEach {
            translationBuilder!!.add(it.value, json[coffeeEffectBaseTranslationKey].asString.format(it.key.name.string))
        }
    }
}