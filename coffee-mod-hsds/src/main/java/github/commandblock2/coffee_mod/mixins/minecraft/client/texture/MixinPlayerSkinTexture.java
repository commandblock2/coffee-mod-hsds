/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 */
package github.commandblock2.coffee_mod.mixins.minecraft.client.texture;

import github.commandblock2.coffee_mod.client.texture.PlayerSkinTextureWithNose;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.PlayerSkinTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerSkinTexture.class)
public class MixinPlayerSkinTexture {
    @Inject(method = "uploadTexture", at = @At("HEAD"))
    void upload(NativeImage image, CallbackInfo ci) {
        final var this_ = (PlayerSkinTexture) (Object) this;
        final var img = new NativeImage(image.getFormat(), image.getWidth(), image.getHeight(), false);
        img.copyFrom(image);
        PlayerSkinTextureWithNose.INSTANCE.getNativeImageCache().put(this_, img);
    }
}
