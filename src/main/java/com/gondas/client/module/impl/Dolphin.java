package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;

public class Dolphin extends Module {
   private Setting.Double speed = new Setting.Double("Speed", 1.5D, 1.0D, 3.0D);

   public Dolphin() {
      super("Dolphin", "Swim faster", Module.Category.MOVEMENT);
      this.addSettings(new Setting[]{this.speed});
   }

   public void onTick() {
      if (mc.field_71439_g != null) {
         if (mc.field_71439_g.func_70090_H()) {
            double yaw = Math.toRadians((double)mc.field_71439_g.field_70177_z);
            float speedVal = (float)this.speed.getValue();
            mc.field_71439_g.func_213293_j(-Math.sin(yaw) * (double)speedVal * 0.2D, mc.field_71439_g.func_213322_ci().field_72448_b, Math.cos(yaw) * (double)speedVal * 0.2D);
         }

      }
   }
}
