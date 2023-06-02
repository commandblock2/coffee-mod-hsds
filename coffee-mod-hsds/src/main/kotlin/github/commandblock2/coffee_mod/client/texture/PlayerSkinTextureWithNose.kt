package github.commandblock2.coffee_mod.client.texture

import com.google.common.cache.Cache
import com.mojang.blaze3d.platform.TextureUtil
import github.commandblock2.coffee_mod.CoffeeMod
import net.minecraft.client.render.entity.VillagerEntityRenderer
import net.minecraft.client.texture.AbstractTexture
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.client.texture.ResourceTexture
import net.minecraft.client.texture.TextureManager
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier


object PlayerSkinTextureWithNose {

    val skinCache = HashMap<Identifier, Identifier>()

    fun add(textureManager: TextureManager, resourceManager: ResourceManager, playerSkinIdentifier: Identifier): Identifier {
        textureManager.bindTexture(playerSkinIdentifier)
        val skinImage = NativeImage.read(resourceManager.getResource(playerSkinIdentifier).get().inputStream)

        val villagerTextureIdentifier = VillagerEntityRenderer.TEXTURE
        textureManager.bindTexture(villagerTextureIdentifier)
        val villagerImage = NativeImage.read(resourceManager.getResource(villagerTextureIdentifier).get().inputStream)

        val playerSkinImageWithNose = NativeImage(skinImage.width, skinImage.height, false)
        playerSkinImageWithNose.copyFrom(skinImage)

        // net/minecraft/client/render/entity/model/VillagerResemblingModel.java:91 for (24, 0)
        // copy to player skin (0, 0)
        // the area is (8, 8)
        villagerImage.copyRect(playerSkinImageWithNose, 24, 0, 0, 0, 8, 8, false, false)

        val addedTextureIdentifier = Identifier(CoffeeMod.MOD_ID, playerSkinIdentifier.path)
        val addedTexture = object : AbstractTexture() {
            override fun load(manager: ResourceManager?) {
                // this is never gonna be called lmao
            }
        }

        // unused goes null
        addedTexture.registerTexture(textureManager, null, addedTextureIdentifier, null)
        TextureUtil.prepareImage(addedTexture.glId, playerSkinImageWithNose.width, playerSkinImageWithNose.height)
        playerSkinImageWithNose.upload(0, 0, 0, true)

        return addedTextureIdentifier
    }
}