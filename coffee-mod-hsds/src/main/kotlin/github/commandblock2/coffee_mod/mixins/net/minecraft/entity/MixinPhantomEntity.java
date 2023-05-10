package github.commandblock2.coffee_mod.mixins.net.minecraft.entity;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PhantomEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Comparator;

//@Mixin(PhantomEntity.class)
//public class MixinPhantomEntity {
//
//}

//@Mixin(targets = "net.minecraft.entity.mob.PhantomEntity$FindTargetGoal")
//class FindNonBuzzingPlayerTarget {
//
//    @Shadow
//    private int delay;
//
//    @Final
//    @Shadow
//    private TargetPredicate PLAYERS_IN_RANGE_PREDICATE;
//
//    @Unique
//    private PhantomEntity phantomEntity;
//
//    @Inject(
//            method = "<init>",
//            at = @At("TAIL"),
//            remap = false,
//            locals = LocalCapture.CAPTURE_FAILHARD
//    )
//    private void init(PhantomEntity phantomEntity, CallbackInfo ci) {
//        this.phantomEntity = phantomEntity;
//    }
//
//    /**
//     * @author: commandblock2
//     * @reason: exempting Buzzing Players
//     */
//    @Overwrite
//    public boolean canStart() {
//        PhantomEntity.FindTargetGoal findTargetGoal = (PhantomEntity.FindTargetGoal) (Object) this;
//
//        if (delay > 0) {
//            delay--;
//            return false;
//        }
//
//        delay = PhantomEntity.FindTargetGoal.toGoalTicks(60);
//
//        final var playerList = phantomEntity.world.getPlayers(PLAYERS_IN_RANGE_PREDICATE, phantomEntity, phantomEntity.getBoundingBox().expand(16, 64, 16));
//        if (!playerList.isEmpty()) {
//            playerList.sort(Comparator.comparing(Entity::getY).reversed());
//            for (final var player : playerList) {
//                if (!PhantomEntity)
//            }
//        }
//
//    }
//}