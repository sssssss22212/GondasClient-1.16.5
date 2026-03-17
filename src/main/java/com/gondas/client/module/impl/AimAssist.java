package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import java.util.Iterator;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

public class AimAssist extends Module {
   private Setting.Double speed = new Setting.Double("Speed", 5.0D, 1.0D, 20.0D);
   private Setting.Double distance = new Setting.Double("Distance", 4.5D, 3.0D, 6.0D);
   private Setting.Boolean players = new Setting.Boolean("Players", true);
   private Setting.Boolean mobs = new Setting.Boolean("Mobs", false);

   public AimAssist() {
      super("AimAssist", "Help aim at targets", Module.Category.COMBAT);
      this.addSettings(new Setting[]{this.speed, this.distance, this.players, this.mobs});
   }

   public void onTick() {
      if (mc.field_71439_g != null && mc.field_71462_r == null) {
         LivingEntity target = this.findTarget();
         if (target != null) {
            Vector3d eyes = mc.field_71439_g.func_174824_e(1.0F);
            Vector3d targetPos = target.func_213303_ch().func_72441_c(0.0D, (double)(target.func_70047_e() / 2.0F), 0.0D);
            double yaw = Math.toDegrees(Math.atan2(targetPos.field_72449_c - eyes.field_72449_c, targetPos.field_72450_a - eyes.field_72450_a)) - 90.0D;
            double pitch = Math.toDegrees(Math.atan2(eyes.field_72448_b - targetPos.field_72448_b, Math.sqrt(Math.pow(targetPos.field_72450_a - eyes.field_72450_a, 2.0D) + Math.pow(targetPos.field_72449_c - eyes.field_72449_c, 2.0D))));
            float yawDiff = (float)(yaw - (double)mc.field_71439_g.field_70177_z);
            float pitchDiff = (float)(pitch - (double)mc.field_71439_g.field_70125_A);
            float speedVal = (float)this.speed.getValue();
            yawDiff = Math.max(-speedVal, Math.min(speedVal, yawDiff));
            pitchDiff = Math.max(-speedVal / 2.0F, Math.min(speedVal / 2.0F, pitchDiff));
            ClientPlayerEntity var10000 = mc.field_71439_g;
            var10000.field_70177_z += yawDiff * 0.5F;
            var10000 = mc.field_71439_g;
            var10000.field_70125_A += pitchDiff * 0.5F;
         }
      }
   }

   private LivingEntity findTarget() {
      LivingEntity closest = null;
      double closestDist = this.distance.getValue();
      Iterator var4 = mc.field_71441_e.func_217416_b().iterator();

      while(true) {
         Entity e;
         do {
            do {
               do {
                  do {
                     if (!var4.hasNext()) {
                        return closest;
                     }

                     e = (Entity)var4.next();
                  } while(!(e instanceof LivingEntity));
               } while(e == mc.field_71439_g);
            } while(e instanceof PlayerEntity && !this.players.getValue());
         } while(!(e instanceof PlayerEntity) && !this.mobs.getValue());

         double dist = (double)mc.field_71439_g.func_70032_d(e);
         if (dist < closestDist) {
            closestDist = dist;
            closest = (LivingEntity)e;
         }
      }
   }
}
