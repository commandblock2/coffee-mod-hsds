/*
 * This file is part of CoffeeMod (https://github.com/CCBlueX/CoffeeMod)
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

package github.commandblock2.coffee_mod.mixins.net.minecraft.entity;

import github.commandblock2.coffee_mod.entity.effect.CoffeeModEffects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    @Shadow public abstract Random getRandom();

    @Shadow public abstract void kill();

    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow public abstract @Nullable StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Unique
    static private final long COFFEE_SAFE_TICKS = 20 * 60 * 30;

    @Unique
    private long coffeeCountdown;

    @Inject(method = "isTarget", at = @At("RETURN"), cancellable = true)
    private void exemptPlayersWithCoffeeEffect(LivingEntity entity, TargetPredicate predicate, CallbackInfoReturnable<Boolean> info) {
        final var this_ = (LivingEntity) (Object) this;
        if (this_ instanceof PhantomEntity &&
                entity instanceof PlayerEntity &&
                entity.hasStatusEffect(CoffeeModEffects.INSTANCE.getCoffeeBuzzStatusEffect())
        ) {
            info.setReturnValue(false);
            ((PhantomEntity) this_).setTarget(null);
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityType entityType, World world, CallbackInfo ci) {
        coffeeCountdown = COFFEE_SAFE_TICKS;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        final var this_ = (LivingEntity) (Object) this;
        if (this_.world.isClient)
            return;

        final int tickSpeed = hasStatusEffect(CoffeeModEffects.INSTANCE.getCoffeeBuzzStatusEffect()) ? getStatusEffect(CoffeeModEffects.INSTANCE.getCoffeeBuzzStatusEffect()).getAmplifier() : 0;

        for (int i = 0; i < tickSpeed; i++) {
            coffeeCountdown--;
            if (coffeeCountdown < 0 && coffeeCountdown % 20 + 19 == this_.getId() % 20) {
                // 1 / 2 death expectation at 30 min
                final var secs = 15 * 60;
                final var oneOverDeathRate = secs * secs;

                if (coffeeCountdown / -20 > getRandom().nextInt(oneOverDeathRate)) {
                    this.kill();
                }
            }
        }
    }

    @Inject(method = "onStatusEffectRemoved", at = @At("HEAD"))
    private void onStatusEffectRemoved(StatusEffectInstance effect, CallbackInfo ci) {
        final var this_ = (LivingEntity) (Object) this;
        if (effect.getEffectType() == CoffeeModEffects.INSTANCE.getCoffeeBuzzStatusEffect() && !this_.world.isClient) {
            coffeeCountdown = COFFEE_SAFE_TICKS;
        }

    }
}
