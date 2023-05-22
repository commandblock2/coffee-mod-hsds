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

package github.commandblock2.coffee_mod.mixins.minecraft.entity.passive;


import github.commandblock2.coffee_mod.entity.ai.brain.CoffeeModSchedule;
import github.commandblock2.coffee_mod.entity.effect.CoffeeModEffects;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.passive.VillagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerEntity.class)
public class MixinVillagerEntity {

    @Inject(method = "canBreed", at = @At("RETURN"), cancellable = true)
    public void canBreed(CallbackInfoReturnable<Boolean> cir) {
        if(((VillagerEntity)(Object)this).hasStatusEffect(CoffeeModEffects.INSTANCE.getCoffeeBuzzStatusEffect()))
            cir.setReturnValue(false);
    }

    @Inject(method = "wantsToStartBreeding", at = @At("RETURN"), cancellable = true)
    public void wantsTOStartBreeding(CallbackInfoReturnable<Boolean> cir) {
        if(((VillagerEntity)(Object)this).hasStatusEffect(CoffeeModEffects.INSTANCE.getCoffeeBuzzStatusEffect()))
            cir.setReturnValue(false);
    }


    @Inject(method = "initBrain", at = @At("TAIL"))
    public void initBrain(Brain<VillagerEntity> brain, CallbackInfo ci) {
        final var entity = ((VillagerEntity)(Object)this);
        if(entity.hasStatusEffect(CoffeeModEffects.INSTANCE.getCoffeeBuzzStatusEffect())) {
            if (entity.isBaby())
                entity.getBrain()
                        .setSchedule(CoffeeModSchedule.INSTANCE.getVILLAGER_BABY_COFFEE());
            else
                entity.getBrain()
                        .setSchedule(CoffeeModSchedule.INSTANCE.getVILLAGER_COFFEE());
        }
    }

}
