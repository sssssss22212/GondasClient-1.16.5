package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;

public class Speed extends Module {
   private Setting.Mode mode = new Setting.Mode("Mode", new String[]{"Strafe", "Bhop", "OnGround", "Packet"});
   private Setting.Double speedVal = new Setting.Double("Speed", 1.5D, 0.5D, 5.0D);
   private Setting.Boolean autoJump = new Setting.Boolean("AutoJump", true);

   public Speed() {
      super("Speed", "Move faster", Module.Category.MOVEMENT);
      this.addSettings(new Setting[]{this.mode, this.speedVal, this.autoJump});
   }

   public void onTick() {
      if (mc.field_71439_g != null && mc.field_71441_e != null) {
         double speed = this.speedVal.getValue();
         String m = this.mode.getValue();
         if (m.equals("Strafe")) {
            if (mc.field_71439_g.func_233570_aj_() && this.autoJump.getValue() && this.isMoving()) {
               mc.field_71439_g.func_70664_aZ();
            }

            if (!mc.field_71439_g.func_233570_aj_()) {
               this.strafe(speed);
            }
         } else if (m.equals("Bhop")) {
            if (mc.field_71439_g.func_233570_aj_() && this.isMoving()) {
               mc.field_71439_g.func_70664_aZ();
            }

            if (!mc.field_71439_g.func_233570_aj_()) {
               this.strafe(speed);
            }
         } else if (m.equals("OnGround")) {
            if (mc.field_71439_g.func_233570_aj_() && this.isMoving()) {
               this.strafe(speed);
            }
         } else if (m.equals("Packet") && this.isMoving()) {
            this.strafe(speed);
         }

      }
   }

   private boolean isMoving() {
      return mc.field_71474_y.field_74351_w.func_151470_d() || mc.field_71474_y.field_74368_y.func_151470_d() || mc.field_71474_y.field_74370_x.func_151470_d() || mc.field_71474_y.field_74366_z.func_151470_d();
   }

   private void strafe(double speed) {
      double yaw = Math.toRadians((double)mc.field_71439_g.field_70177_z);
      double forward = 0.0D;
      double strafe = 0.0D;
      if (mc.field_71474_y.field_74351_w.func_151470_d()) {
         forward = 1.0D;
      }

      if (mc.field_71474_y.field_74368_y.func_151470_d()) {
         forward = -1.0D;
      }

      if (mc.field_71474_y.field_74370_x.func_151470_d()) {
         strafe = 1.0D;
      }

      if (mc.field_71474_y.field_74366_z.func_151470_d()) {
         strafe = -1.0D;
      }

      if (forward != 0.0D && strafe != 0.0D) {
         yaw += forward > 0.0D ? 0.7853981633974483D : -0.7853981633974483D;
         strafe = 0.0D;
      }

      double motionX = -Math.sin(yaw) * speed * Math.max(Math.abs(forward), Math.abs(strafe));
      double motionZ = Math.cos(yaw) * speed * Math.max(Math.abs(forward), Math.abs(strafe));
      mc.field_71439_g.func_213293_j(motionX, mc.field_71439_g.func_213322_ci().field_72448_b, motionZ);
   }
}
