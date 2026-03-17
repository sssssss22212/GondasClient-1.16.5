package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import net.minecraft.util.math.vector.Vector3d;

public class FreeCam extends Module {
   private Setting.Double speed = new Setting.Double("Speed", 1.0D, 0.1D, 5.0D);
   private Vector3d originalPos = null;

   public FreeCam() {
      super("FreeCam", "Free camera movement", Module.Category.MISC);
      this.addSettings(new Setting[]{this.speed});
   }

   public void onEnable() {
      if (mc.field_71439_g != null) {
         this.originalPos = mc.field_71439_g.func_213303_ch();
      }

   }

   public void onDisable() {
      if (mc.field_71439_g != null && this.originalPos != null) {
         mc.field_71439_g.func_70107_b(this.originalPos.field_72450_a, this.originalPos.field_72448_b, this.originalPos.field_72449_c);
      }

   }

   public void onTick() {
      if (mc.field_71439_g != null) {
         double speedVal = this.speed.getValue();
         double yaw = Math.toRadians((double)mc.field_71439_g.field_70177_z);
         Vector3d motion = Vector3d.field_186680_a;
         if (mc.field_71474_y.field_74351_w.func_151470_d()) {
            motion = motion.func_72441_c(-Math.sin(yaw) * speedVal, 0.0D, Math.cos(yaw) * speedVal);
         }

         if (mc.field_71474_y.field_74368_y.func_151470_d()) {
            motion = motion.func_72441_c(Math.sin(yaw) * speedVal, 0.0D, -Math.cos(yaw) * speedVal);
         }

         if (mc.field_71474_y.field_74314_A.func_151470_d()) {
            motion = motion.func_72441_c(0.0D, speedVal, 0.0D);
         }

         if (mc.field_71474_y.field_228046_af_.func_151470_d()) {
            motion = motion.func_72441_c(0.0D, -speedVal, 0.0D);
         }

         mc.field_71439_g.func_70107_b(mc.field_71439_g.func_226277_ct_() + motion.field_72450_a, mc.field_71439_g.func_226278_cu_() + motion.field_72448_b, mc.field_71439_g.func_226281_cx_() + motion.field_72449_c);
      }
   }
}
