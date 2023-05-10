package github.commandblock2.coffee_mod.mixins.net.minecraft.entity;

import github.commandblock2.coffee_mod.entity.effect.EffectRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
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

    @Unique
    static private long COFFEE_SAFE_TICKS = 20 * 60 * 30;

    @Unique
    private long coffeeCountdown;

    @Inject(method = "isTarget", at = @At("RETURN"), cancellable = true)
    private void exemptPlayersWithCoffeeEffect(LivingEntity entity, TargetPredicate predicate, CallbackInfoReturnable<Boolean> info) {
        final var this_ = (LivingEntity) (Object) this;
        if (this_ instanceof PhantomEntity &&
                entity instanceof PlayerEntity &&
                entity.hasStatusEffect(EffectRegistry.INSTANCE.getCoffeeBuzzStatusEffect())
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
        if (!hasStatusEffect(EffectRegistry.INSTANCE.getCoffeeBuzzStatusEffect()) || this_.world.isClient)
            return;

        coffeeCountdown--;
        if (coffeeCountdown < 0 && coffeeCountdown % 20 + 19 == this_.getId() % 20) {
            // 1 / 2 death expectation at 30 min
            final var secs = 30 * 60;
            final var oneOverDeathRate = secs * secs * 2;

            if (coffeeCountdown / -20 > getRandom().nextInt(oneOverDeathRate)) {
                this.kill();
            }
        }
    }

    @Inject(method = "onStatusEffectRemoved", at = @At("HEAD"))
    private void resetCoffeeCountdown(StatusEffectInstance effect, CallbackInfo ci) {
        coffeeCountdown = COFFEE_SAFE_TICKS;
    }
}
