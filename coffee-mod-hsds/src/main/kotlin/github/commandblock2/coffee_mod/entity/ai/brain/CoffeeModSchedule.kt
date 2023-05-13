package github.commandblock2.coffee_mod.entity.ai.brain

import net.minecraft.entity.ai.brain.Activity
import net.minecraft.entity.ai.brain.Schedule

object CoffeeModSchedule {
    val VILLAGER_BABY_COFFEE =
        Schedule.register("villager_baby_coffee").withActivity(10, Activity.IDLE).withActivity(3000, Activity.PLAY)
            .withActivity(6000, Activity.IDLE).withActivity(10000, Activity.PLAY).build()


    val VILLAGER_COFFEE =
        Schedule.register("villager_coffee").withActivity(10, Activity.IDLE).withActivity(2000, Activity.WORK)
            .withActivity(9000, Activity.MEET).withActivity(11000, Activity.IDLE).withActivity(12000, Activity.WORK)
            .build()
    init {
        VILLAGER_COFFEE
        VILLAGER_BABY_COFFEE
    }
}