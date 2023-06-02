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

package github.commandblock2.coffee_mod.client.texture

import com.mojang.blaze3d.platform.TextureUtil
import github.commandblock2.coffee_mod.CoffeeMod
import net.minecraft.client.render.entity.VillagerEntityRenderer
import net.minecraft.client.texture.AbstractTexture
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.client.texture.PlayerSkinTexture
import net.minecraft.client.texture.ResourceTexture
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.texture.TextureManager
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier


object PlayerSkinTextureWithNose {

    private val skinCache = HashMap<Identifier, Identifier>()
    val nativeImageCache = HashMap<PlayerSkinTexture, NativeImage>()

    fun add(
        textureManager: TextureManager,
        resourceManager: ResourceManager,
        playerSkinIdentifier: Identifier
    ): Identifier {
        if (skinCache.containsKey(playerSkinIdentifier))
            return skinCache[playerSkinIdentifier]!!

        textureManager.bindTexture(playerSkinIdentifier)
        val playerSkinResource = resourceManager.getResource(playerSkinIdentifier)

        val texture = textureManager.getTexture(playerSkinIdentifier)

        val skinImage =
            if (playerSkinResource.isEmpty && nativeImageCache.containsKey(texture))
                nativeImageCache[texture]!!
            else if (!nativeImageCache.containsKey(texture)) {
                return playerSkinIdentifier
            } else
                NativeImage.read(playerSkinResource.get().inputStream)


        val villagerTextureIdentifier = VillagerEntityRenderer.TEXTURE
        textureManager.bindTexture(villagerTextureIdentifier)
        val villagerImage = NativeImage.read(resourceManager.getResource(villagerTextureIdentifier).get().inputStream)

        val playerSkinImageWithNose = NativeImage(skinImage.format, skinImage.width, skinImage.height, false)
        println("skinImage.format:${skinImage.format}")
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

        skinCache[playerSkinIdentifier] = addedTextureIdentifier

        return addedTextureIdentifier
    }
}