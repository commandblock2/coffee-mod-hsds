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

package github.commandblock2.coffee_mod.mixins.minecraft.server;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {
    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    private void sendAllPlayerEffect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci){
        player
                .getServerWorld()
                .getPlayers()
                .forEach(p ->
                        p.getStatusEffects().forEach(
                                effect ->
                                        player
                                                .networkHandler
                                                .sendPacket(
                                                        new EntityStatusEffectS2CPacket(
                                                                p.getId(), effect
                                                        )
                                                )
                                )
                );

        player
                .getServerWorld()
                .getPlayers()
                .forEach(p ->
                        player.getStatusEffects().forEach(
                                effect ->
                                        p
                                                .networkHandler
                                                .sendPacket(
                                                        new EntityStatusEffectS2CPacket(
                                                                player.getId(), effect
                                                        )
                                                )
                        )
                );
    }
}
