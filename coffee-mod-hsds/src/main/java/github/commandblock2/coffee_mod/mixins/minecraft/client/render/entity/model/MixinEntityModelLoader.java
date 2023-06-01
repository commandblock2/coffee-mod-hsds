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

package github.commandblock2.coffee_mod.mixins.minecraft.client.render.entity.model;

import com.google.common.collect.ImmutableMap;
import github.commandblock2.coffee_mod.client.render.entity.model.PlayerEntityWithVillagerNoseModel;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.EntityModels;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(EntityModelLoader.class)
public class MixinEntityModelLoader {

    @Inject(method = "reload", at = @At("HEAD"), cancellable = true)
    void reload(ResourceManager manager, CallbackInfo ci) {
        ci.cancel();

        final var this_ = (EntityModelLoader) (Object) this;

        Map<EntityModelLayer, TexturedModelData> intermediate = new HashMap<>(EntityModels.getModels());
        intermediate.put(EntityModelLayers.PLAYER, TexturedModelData.of(PlayerEntityWithVillagerNoseModel.Companion.getTexturedModelData(Dilation.NONE, false), 64, 64));
        intermediate.put(EntityModelLayers.PLAYER_SLIM, TexturedModelData.of(PlayerEntityWithVillagerNoseModel.Companion.getTexturedModelData(Dilation.NONE, true), 64, 64));

        this_.modelParts = ImmutableMap.copyOf(intermediate);
    }
}
