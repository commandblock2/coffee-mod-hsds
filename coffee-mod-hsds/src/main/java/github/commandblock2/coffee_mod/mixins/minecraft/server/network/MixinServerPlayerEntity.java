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

package github.commandblock2.coffee_mod.mixins.minecraft.server.network;

import github.commandblock2.coffee_mod.CoffeeMod;
import github.commandblock2.coffee_mod.entity.CoffeeModEntitySupport;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity {


    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    void sendDeathMessage(DamageSource damageSource, CallbackInfo ci) {

        // This is HACKY
        // TODO: make it a proper damage source
        final var this_ = (ServerPlayerEntity) (Object) this;

        if (CoffeeModEntitySupport.INSTANCE.getCoffeeDeathList().contains(this_)) {
            CoffeeModEntitySupport.INSTANCE.getCoffeeDeathList().remove(this_);

            if (!this_.getWorld().getGameRules().getBoolean(GameRules.SHOW_DEATH_MESSAGES))
                return;

            final var deathMessage = Text.translatable("death.%s.overdose".formatted(CoffeeMod.MOD_ID), this_.getDisplayName());

            this_.networkHandler.sendPacket(new DeathMessageS2CPacket(
                    this_.getId(), deathMessage
            ));

            AbstractTeam abstractTeam = this_.getScoreboardTeam();
            if (abstractTeam == null || abstractTeam.getDeathMessageVisibilityRule() == AbstractTeam.VisibilityRule.ALWAYS) {
                this_.server.getPlayerManager().broadcast(deathMessage, false);
            } else if (abstractTeam.getDeathMessageVisibilityRule() == AbstractTeam.VisibilityRule.HIDE_FOR_OTHER_TEAMS) {
                this_.server.getPlayerManager().sendToTeam(this_, deathMessage);
            } else if (abstractTeam.getDeathMessageVisibilityRule() == AbstractTeam.VisibilityRule.HIDE_FOR_OWN_TEAM) {
                this_.server.getPlayerManager().sendToOtherTeams(this_, deathMessage);
            }
            ci.cancel();
        }
    }
}
