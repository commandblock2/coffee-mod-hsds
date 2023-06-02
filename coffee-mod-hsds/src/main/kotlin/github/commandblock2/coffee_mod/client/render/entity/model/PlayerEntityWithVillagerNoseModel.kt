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

package github.commandblock2.coffee_mod.client.render.entity.model

import net.minecraft.client.model.*
import net.minecraft.client.render.entity.model.EntityModelPartNames
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.entity.LivingEntity

class PlayerEntityWithVillagerNoseModel<T : LivingEntity>(
    root: ModelPart,
    thinArms: Boolean
) : PlayerEntityModel<T>(root, thinArms) {

    val nose: ModelPart = root.getChild(EntityModelPartNames.HEAD)
        .getChild(EntityModelPartNames.NOSE)

    companion object {
        fun getTexturedModelData(dilation: Dilation, slim: Boolean): ModelData? {
            val modelData = PlayerEntityModel.getTexturedModelData(dilation, slim)
            val headData = modelData.root.getChild(EntityModelPartNames.HEAD)


            headData.addChild(
                EntityModelPartNames.NOSE,
                ModelPartBuilder.create().uv(0, 0).cuboid(-1.0f, -1.0f, -6.0f, 2.0f, 4.0f, 2.0f),
                ModelTransform.pivot(0.0f, -2.0f, 0.0f)
            )

            return modelData
        }
    }
}