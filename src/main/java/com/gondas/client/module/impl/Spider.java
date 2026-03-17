package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;

public class Spider extends Module {
   private Setting.Double speed = new Setting.Double("Speed", 0.3D, 0.1D, 1.0D);

   public Spider() {
      super("Spider", "Climb walls", Module.Category.MOVEMENT);
      this.addSettings(new Setting[]{this.speed});
   }

   public void onTick() {
      if (mc.field_71439_g != null) {
         if (mc.field_71439_g.field_70123_F) {
            mc.field_71439_g.func_70024_g(0.0D, this.speed.getValue(), 0.0D);
         }

      }
   }
}
