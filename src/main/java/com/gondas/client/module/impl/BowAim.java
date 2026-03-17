package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.util.math.MathHelper;

public class BowAim extends Module {
   private Setting.Double range = new Setting.Double("Range", 50.0D, 10.0D, 100.0D);
   private Setting.Boolean players = new Setting.Boolean("Players", true);
   private Setting.Boolean mobs = new Setting.Boolean("Mobs", true);
   private Setting.Boolean animals = new Setting.Boolean("Animals", false);
   private Setting.Mode targetMode = new Setting.Mode("Target", new String[]{"Closest", "Health", "Angle"});
   private Setting.Double prediction = new Setting.Double("Prediction", 1.5D, 0.0D, 5.0D);
   private Setting.Double rotSpeed = new Setting.Double("RotSpeed", 180.0D, 10.0D, 180.0D);
   private Setting.Boolean silent = new Setting.Boolean("Silent", false);
   private Setting.Double jitter = new Setting.Double("Jitter", 1.0D, 0.0D, 5.0D);
   private Setting.Boolean autoRelease = new Setting.Boolean("AutoRelease", false);
   private Setting.Double releaseCharge = new Setting.Double("ReleaseCharge", 1.0D, 0.1D, 1.0D);
   private Random random = new Random();
   private LivingEntity target = null;
   private float serverYaw;
   private float serverPitch;
   private long startUseTime = 0L;
   private boolean wasUsingBow = false;

   public BowAim() {
      super("BowAim", "Auto aim with bow", Module.Category.COMBAT);
      this.addSettings(new Setting[]{this.range, this.players, this.mobs, this.animals, this.targetMode, this.prediction, this.rotSpeed, this.silent, this.jitter, this.autoRelease, this.releaseCharge});
   }

   public void onEnable() {
      super.onEnable();
      if (mc.field_71439_g != null) {
         this.serverYaw = mc.field_71439_g.field_70177_z;
         this.serverPitch = mc.field_71439_g.field_70125_A;
      }

      this.target = null;
      this.wasUsingBow = false;
   }

   public void onTick() {
      if (mc.field_71439_g != null && mc.field_71441_e != null) {
         if (!this.isHoldingBow()) {
            this.target = null;
            this.wasUsingBow = false;
         } else {
            boolean isUsingBow = mc.field_71439_g.func_184587_cr() && mc.field_71439_g.func_184607_cu().func_77973_b() instanceof BowItem;
            if (!isUsingBow) {
               this.wasUsingBow = false;
               if (this.silent.getValue()) {
                  this.serverYaw = mc.field_71439_g.field_70177_z;
                  this.serverPitch = mc.field_71439_g.field_70125_A;
               }

            } else {
               if (!this.wasUsingBow) {
                  this.startUseTime = System.currentTimeMillis();
                  this.wasUsingBow = true;
               }

               this.target = this.findTarget();
               if (this.target != null) {
                  float[] rotations = this.calculateBowRotations(this.target);
                  if (this.jitter.getValue() > 0.0D && this.random.nextBoolean()) {
                     rotations[0] += (this.random.nextFloat() - 0.5F) * (float)this.jitter.getValue();
                     rotations[1] += (this.random.nextFloat() - 0.5F) * (float)this.jitter.getValue() * 0.5F;
                     rotations[1] = MathHelper.func_76131_a(rotations[1], -90.0F, 90.0F);
                  }

                  float speed = (float)this.rotSpeed.getValue();
                  float yawDiff = MathHelper.func_76142_g(rotations[0] - this.serverYaw);
                  float pitchDiff = MathHelper.func_76142_g(rotations[1] - this.serverPitch);
                  float yawChange = MathHelper.func_76131_a(yawDiff, -speed, speed);
                  float pitchChange = MathHelper.func_76131_a(pitchDiff, -speed, speed);
                  this.serverYaw += yawChange;
                  this.serverPitch += pitchChange;
                  this.serverPitch = MathHelper.func_76131_a(this.serverPitch, -90.0F, 90.0F);
                  if (this.silent.getValue()) {
                     mc.field_71439_g.field_70177_z = this.serverYaw;
                     mc.field_71439_g.field_70125_A = this.serverPitch;
                  } else {
                     mc.field_71439_g.field_70177_z = this.serverYaw;
                     mc.field_71439_g.field_70125_A = this.serverPitch;
                  }

                  if (this.autoRelease.getValue()) {
                     int useTime = (int)(System.currentTimeMillis() - this.startUseTime);
                     int chargeTime = (int)(this.releaseCharge.getValue() * 1000.0D);
                     if (useTime >= chargeTime && useTime <= 3000) {
                        mc.field_71439_g.func_184597_cx();
                        this.wasUsingBow = false;
                     }
                  }

               }
            }
         }
      }
   }

   private float[] calculateBowRotations(LivingEntity targetEntity) {
      double x = targetEntity.func_226277_ct_() - mc.field_71439_g.func_226277_ct_();
      double y = targetEntity.func_226280_cw_() - mc.field_71439_g.func_226280_cw_();
      double z = targetEntity.func_226281_cx_() - mc.field_71439_g.func_226281_cx_();
      double pred = this.prediction.getValue();
      x += targetEntity.func_213322_ci().field_72450_a * pred;
      y += targetEntity.func_213322_ci().field_72448_b * pred;
      z += targetEntity.func_213322_ci().field_72449_c * pred;
      double dist = Math.sqrt(x * x + z * z);
      float charge = 0.0F;
      if (mc.field_71439_g.func_184587_cr()) {
         int useCount = mc.field_71439_g.func_184605_cv();
         charge = BowItem.func_185059_b(72000 - useCount);
      }

      double velocity = (double)charge * 3.0D;
      if (velocity < 0.1D) {
         velocity = 0.1D;
      }

      double g = 0.05D;
      double v2 = velocity * velocity;
      double v4 = v2 * v2;
      double term = v4 - g * (g * dist * dist + 2.0D * y * v2);
      double pitch;
      if (term >= 0.0D) {
         pitch = Math.atan2(v2 - Math.sqrt(term), g * dist);
      } else {
         pitch = Math.atan2(v2, g * dist);
      }

      float yaw = (float)(Math.toDegrees(Math.atan2(z, x)) - 90.0D);
      float finalPitch = (float)(-Math.toDegrees(pitch));
      finalPitch = MathHelper.func_76131_a(finalPitch, -90.0F, 90.0F);
      return new float[]{yaw, finalPitch};
   }

   private LivingEntity findTarget() {
      List<LivingEntity> possibleTargets = new ArrayList();
      Iterator var2 = mc.field_71441_e.func_217416_b().iterator();

      while(true) {
         Entity e;
         LivingEntity living;
         do {
            do {
               do {
                  do {
                     do {
                        if (!var2.hasNext()) {
                           if (possibleTargets.isEmpty()) {
                              return null;
                           }

                           String mode = this.targetMode.getValue();
                           if (mode.equals("Health")) {
                              possibleTargets.sort((a, b) -> {
                                 return Float.compare(a.func_110143_aJ(), b.func_110143_aJ());
                              });
                           } else if (mode.equals("Angle")) {
                              possibleTargets.sort((a, b) -> {
                                 double angleA = this.getAngleTo(a);
                                 double angleB = this.getAngleTo(b);
                                 return Double.compare(angleA, angleB);
                              });
                           } else {
                              possibleTargets.sort((a, b) -> {
                                 return Double.compare((double)mc.field_71439_g.func_70032_d(a), (double)mc.field_71439_g.func_70032_d(b));
                              });
                           }

                           return (LivingEntity)possibleTargets.get(0);
                        }

                        e = (Entity)var2.next();
                     } while(!(e instanceof LivingEntity));
                  } while(e == mc.field_71439_g);

                  living = (LivingEntity)e;
               } while(living instanceof PlayerEntity && !this.players.getValue());
            } while(living instanceof MonsterEntity && !this.mobs.getValue());
         } while(living instanceof AnimalEntity && !this.animals.getValue());

         if (!(living.func_110143_aJ() <= 0.0F)) {
            double dist = (double)mc.field_71439_g.func_70032_d(e);
            if (!(dist > this.range.getValue()) && !(dist <= 0.0D)) {
               possibleTargets.add(living);
            }
         }
      }
   }

   private double getAngleTo(LivingEntity entity) {
      double x = entity.func_226277_ct_() - mc.field_71439_g.func_226277_ct_();
      double z = entity.func_226281_cx_() - mc.field_71439_g.func_226281_cx_();
      float yaw = (float)(Math.toDegrees(Math.atan2(z, x)) - 90.0D);
      float yawDiff = MathHelper.func_76142_g(yaw - mc.field_71439_g.field_70177_z);
      return (double)Math.abs(yawDiff);
   }

   private boolean isHoldingBow() {
      return mc.field_71439_g.func_184614_ca().func_77973_b() instanceof BowItem || mc.field_71439_g.func_184592_cb().func_77973_b() instanceof BowItem;
   }

   public LivingEntity getTarget() {
      return this.target;
   }
}
