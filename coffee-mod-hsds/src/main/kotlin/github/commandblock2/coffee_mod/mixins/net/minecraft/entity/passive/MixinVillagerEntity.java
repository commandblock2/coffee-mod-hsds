package github.commandblock2.coffee_mod.mixins.net.minecraft.entity.passive;


import github.commandblock2.coffee_mod.entity.effect.CoffeeModEffects;
import net.minecraft.entity.passive.VillagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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

}
