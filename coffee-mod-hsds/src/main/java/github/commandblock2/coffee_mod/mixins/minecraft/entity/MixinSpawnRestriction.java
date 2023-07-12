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

package github.commandblock2.coffee_mod.mixins.minecraft.entity;

import github.commandblock2.coffee_mod.CoffeeMod;
import github.commandblock2.coffee_mod.entity.CoffeeModEntitySupport;
import github.commandblock2.coffee_mod.entity.effect.CoffeeModEffects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Heightmap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(SpawnRestriction.class)
public class MixinSpawnRestriction {
    @Shadow
    @Final
    private static Map<EntityType<?>, SpawnRestriction.Entry> RESTRICTIONS;

    @Inject(method = "<init>", at = @At("TAIL"))
    void init(CallbackInfo ci) {
        RESTRICTIONS.put(EntityType.PHANTOM, new SpawnRestriction.Entry(
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                SpawnRestriction.Location.NO_RESTRICTIONS,
                (entityType, world, reason, pos, random) -> {
                    final var player = world.getClosestPlayer(
                            pos.getX(),
                            pos.getY(),
                            pos.getZ(),
                            -1.0,
                            false);

                    if (player == null)
                        return false;

                    final var canSpawnByCoffee =
                            !CoffeeMod.INSTANCE.getConfig().getDisablePhantomSpawning() ||
                                    (reason == SpawnReason.NATURAL &&
                                            !player.hasStatusEffect(CoffeeModEffects.INSTANCE.getCoffeeBuzzStatusEffect()));

                    final var canVanillaSpawn = MobEntity.canMobSpawn(
                            (EntityType<? extends MobEntity>)(Object) entityType,
                            world,
                            reason,
                            pos,
                            random);

                    if (CoffeeMod.INSTANCE.getConfig().getRespawnPhantomWhenEffectFades() && canVanillaSpawn && !canSpawnByCoffee)
                        CoffeeModEntitySupport.INSTANCE
                                .getCatchupPhantomSpawnList().put(player, CoffeeModEntitySupport.INSTANCE
                                        .getCatchupPhantomSpawnList().getOrDefault(player, 0) + 1);

                    return canSpawnByCoffee && canVanillaSpawn;
                }
        ));
    }
}
