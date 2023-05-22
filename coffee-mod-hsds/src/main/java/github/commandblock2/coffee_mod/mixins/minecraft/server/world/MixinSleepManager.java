/*
 * This file is part of CoffeeMod (https://github.com/commandblock2/CoffeeMod)
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

package github.commandblock2.coffee_mod.mixins.minecraft.server.world;

import github.commandblock2.coffee_mod.entity.effect.CoffeeModEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.SleepManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(SleepManager.class)
public class MixinSleepManager {

    @Shadow
    private int sleeping;

    @Shadow
    private int total;

    /**
     * @author commandblock2
     * @reason player who has coffee buzz does not count as sleeping
     */
    @Overwrite
    public boolean update(List<ServerPlayerEntity> players) {



        int updated_total = 0, updated_sleeping = 0;
        for (final var player: players) {
            if (player.isSpectator())
                continue;
            updated_total++;

            if (!player.isSleeping() || player.hasStatusEffect(CoffeeModEffects.INSTANCE.getCoffeeBuzzStatusEffect()))
                continue;

            updated_sleeping++;
        }

        final boolean updates = !(updated_sleeping <= 0 && sleeping <= 0 || updated_total == total && updated_sleeping == sleeping);

        sleeping = updated_sleeping;
        total = updated_total;

        return updates;
    }
}
