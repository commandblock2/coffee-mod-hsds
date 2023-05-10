package github.commandblock2.coffee_mod.mixins.net.minecraft.entity;

import github.commandblock2.coffee_mod.effect.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
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
}
