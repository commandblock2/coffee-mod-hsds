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

package github.commandblock2.coffee_mod.mixins.minecraft.client.resource.language;

import github.commandblock2.coffee_mod.CoffeeMod;
import github.commandblock2.coffee_mod.resource.CoffeeModCustomResourceLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(TranslationStorage.class)
public class MixinTranslationStorage {

    @Inject(
            method =
                    "load(Lnet/minecraft/resource/ResourceManager;Ljava/util/List;Z)Lnet/minecraft/client/resource/language/TranslationStorage;",
            at = @At("RETURN")
    )
    private static void injectPopulatedTranslations(ResourceManager resourceManager, List<String> definitions, boolean rightToLeft, CallbackInfoReturnable<TranslationStorage> cir) {
        final var translationStorage = cir.getReturnValue();
        final var langCode = MinecraftClient.getInstance().getLanguageManager().getLanguage();

        final var coffeeModTranslations = CoffeeModCustomResourceLoader
                .INSTANCE
                .getTranslations()
                .get(langCode);

        Map<String, String> mappedTranslations = coffeeModTranslations
                .keySet()
                .stream()
                .filter((key) -> key.contains("%"))
                .flatMap(translationKey -> CoffeeMod.INSTANCE.getSupportedEntityTypes()
                        .stream()
                        .map(entityType -> {
                            String key = translationKey
                                    .replace("wildcard.", "")
                                    .formatted(entityType.getUntranslatedName());
                            String value = coffeeModTranslations.get(translationKey)
                                    .formatted(translationStorage.translations.get(entityType.getTranslationKey()));
                            return Map.entry(key, value);
                        }))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        mappedTranslations.putAll(translationStorage.translations);
        translationStorage.translations = mappedTranslations;
    }
}