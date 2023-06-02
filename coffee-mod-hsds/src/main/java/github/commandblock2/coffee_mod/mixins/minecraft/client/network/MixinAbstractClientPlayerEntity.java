package github.commandblock2.coffee_mod.mixins.minecraft.client.network;

import github.commandblock2.coffee_mod.client.texture.PlayerSkinTextureWithNose;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public class MixinAbstractClientPlayerEntity {

    @Inject(method = "getSkinTexture", at = @At("RETURN"), cancellable = true)
    void addNoseToSkin(CallbackInfoReturnable<Identifier> cir) {
        final var originalSkin = cir.getReturnValue();
        final var skinCache = PlayerSkinTextureWithNose.INSTANCE.getSkinCache();
        if (!skinCache.containsKey(originalSkin))
            skinCache.put(originalSkin, PlayerSkinTextureWithNose.INSTANCE.add(
                            MinecraftClient.getInstance().getTextureManager(),
                            MinecraftClient.getInstance().getResourceManager(),
                            originalSkin
                    )
            );

        cir.setReturnValue(skinCache.get(originalSkin));
    }

}
