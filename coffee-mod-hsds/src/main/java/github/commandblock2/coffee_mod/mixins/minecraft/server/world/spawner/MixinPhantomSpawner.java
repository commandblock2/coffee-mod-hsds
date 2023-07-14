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

package github.commandblock2.coffee_mod.mixins.minecraft.server.world.spawner;

import github.commandblock2.coffee_mod.entity.CoffeeModEntitySupport;
import github.commandblock2.coffee_mod.entity.effect.CoffeeModEffects;
import github.commandblock2.coffee_mod.world.CoffeeModGamerules;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(PhantomSpawner.class)
public class MixinPhantomSpawner {

    @Redirect(method = "spawn", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/world/ServerWorld;getPlayers()Ljava/util/List;"
    ))
    private List<ServerPlayerEntity> getPlayers(ServerWorld instance) {

        final var spawnPhantoms = instance.getGameRules().getBoolean(CoffeeModGamerules.INSTANCE.getSpawnPhantoms());
        final var catchupSpawns = instance.getGameRules().getBoolean(CoffeeModGamerules.INSTANCE.getCatchupSpawn());

        final var playersByHasCoffee = instance
                .getPlayers()
                .stream()
                .collect(Collectors.partitioningBy(it -> it.hasStatusEffect(CoffeeModEffects.INSTANCE.getCoffeeBuzzStatusEffect())));

        if (!spawnPhantoms) {
            if (catchupSpawns)
                playersByHasCoffee.get(true).forEach(it -> CoffeeModEntitySupport.INSTANCE.getCatchupPhantomSpawnList().put(it,
                        CoffeeModEntitySupport.INSTANCE.getCatchupPhantomSpawnList().getOrDefault(it, 0) + 1
                ));
            return playersByHasCoffee.get(false);
        }

        return instance.getPlayers();
    }
}
