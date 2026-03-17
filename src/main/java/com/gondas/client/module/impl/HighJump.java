package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;

public class HighJump extends Module {
   private Setting.Double height = new Setting.Double("Height", 2.0D, 0.5D, 10.0D);

   public HighJump() {
      super("HighJump", "Jump higher", Module.Category.MOVEMENT);
      this.addSettings(new Setting[]{this.height});
   }

   public void onTick() {
      if (mc.field_71439_g != null) {
         if (mc.field_71439_g.func_233570_aj_() && mc.field_71474_y.field_74314_A.func_151470_d()) {
            mc.field_71439_g.func_70024_g(0.0D, this.height.getValue() - 0.42D, 0.0D);
         }

      }
   }
}
