package github.commandblock2.coffee_mod.mixins.net.minecraft.recipe;

import github.commandblock2.coffee_mod.potion.CoffeeModPotions;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingRecipeRegistry.class)
public class MixinBrewingRecipeRegistry {
    @Shadow
    public static void registerPotionRecipe(Potion input, Item item, Potion output) {

    }

    @Inject(method = "registerDefaults", at = @At("TAIL"))
    private static void onRegister(CallbackInfo ci) {
        registerPotionRecipe(Potions.WATER, Items.COCOA_BEANS, CoffeeModPotions.INSTANCE.getREGULAR_COFFEE_POTION());
        registerPotionRecipe(CoffeeModPotions.INSTANCE.getREGULAR_COFFEE_POTION(), Items.REDSTONE, CoffeeModPotions.INSTANCE.getPROLONGED_COFFEE_POTION());
        registerPotionRecipe(CoffeeModPotions.INSTANCE.getREGULAR_COFFEE_POTION(), Items.GLOWSTONE_DUST, CoffeeModPotions.INSTANCE.getENHANCED_COFFEE_POTION());
    }
}
