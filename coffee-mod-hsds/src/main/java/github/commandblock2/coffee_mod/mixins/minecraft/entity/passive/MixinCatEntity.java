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

package github.commandblock2.coffee_mod.mixins.minecraft.entity.passive;

import github.commandblock2.coffee_mod.item.CoffeeModItems;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CatEntity.class)
public abstract class MixinCatEntity {


    private static final int INGESTING_LENGTH = 6000;
    @Unique
    private int ingestingTimer = -1;

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        final var this_ = (CatEntity) (Object) this;

        if (ingestingTimer > 0) {
            ingestingTimer--;
        } else if (ingestingTimer == 0) {
            ingestingTimer--;
            this_.dropStack(new ItemStack(CoffeeModItems.INSTANCE.getCatShitCoffeeBeanItem(), this_.getRandom().nextInt(3) + 1));
        }
    }

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    public void consumeCocoabean(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem() == Items.COCOA_BEANS && ingestingTimer == -1) {
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }

            ingestingTimer = INGESTING_LENGTH;

            cir.setReturnValue(ActionResult.SUCCESS);
            cir.cancel();
        }
    }

}
