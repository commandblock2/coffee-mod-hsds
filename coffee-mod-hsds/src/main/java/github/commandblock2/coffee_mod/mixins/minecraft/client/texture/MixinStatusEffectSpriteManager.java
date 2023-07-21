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

package github.commandblock2.coffee_mod.mixins.minecraft.client.texture;

import github.commandblock2.coffee_mod.entity.effect.CoffeeModEffects;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.entity.effect.StatusEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StatusEffectSpriteManager.class)
public abstract class MixinStatusEffectSpriteManager {

    @Shadow public abstract Sprite getSprite(StatusEffect effect);

    @Inject(method = "getSprite", at = @At("RETURN"), cancellable = true)
    private void sprite(StatusEffect effect, CallbackInfoReturnable<Sprite> cir) {
        if (CoffeeModEffects.INSTANCE.getSpecialEffectByEntityType().containsValue(effect))
            cir.setReturnValue(getSprite(CoffeeModEffects.INSTANCE.getCoffeeBuzzStatusEffect())); // imagine recursive calling
    }
}
