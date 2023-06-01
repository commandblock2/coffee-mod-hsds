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

package github.commandblock2.coffee_mod.mixins.minecraft.client.render.entity;

import github.commandblock2.coffee_mod.client.render.entity.model.PlayerEntityWithVillagerNoseModel;
import github.commandblock2.coffee_mod.entity.effect.CoffeeModEffects;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class MixinPlayerEntityRenderer {


    @Inject(method = "<init>", at = @At("TAIL"))
    void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        final var this_ = (PlayerEntityRenderer) (Object) this;

        this_.model = new PlayerEntityWithVillagerNoseModel<>(ctx.getPart(slim ? EntityModelLayers.PLAYER_SLIM : EntityModelLayers.PLAYER), slim);
    }


    @Inject(method = "setModelPose", at = @At("TAIL"))
    private void updateNoseVisibility(AbstractClientPlayerEntity player, CallbackInfo ci) {
        final var this_ = (PlayerEntityRenderer) (Object) this;
        final var model = this_.getModel();
        if (model instanceof PlayerEntityWithVillagerNoseModel<AbstractClientPlayerEntity>)
            ((PlayerEntityWithVillagerNoseModel<AbstractClientPlayerEntity>) model)
                    .getNose().visible
                    =
                    player.hasStatusEffect(
                            CoffeeModEffects.INSTANCE
                                    .getSpecialEffectByEntityType()
                                    .get(EntityType.VILLAGER));
    }

}

