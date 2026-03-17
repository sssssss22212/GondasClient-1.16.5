package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;

public class LongJump extends Module {
   private Setting.Double boost = new Setting.Double("Boost", 2.0D, 1.0D, 5.0D);

   public LongJump() {
      super("LongJump", "Jump further", Module.Category.MOVEMENT);
      this.addSettings(new Setting[]{this.boost});
   }

   public void onTick() {
      if (mc.field_71439_g != null) {
         if (!mc.field_71439_g.func_233570_aj_() && mc.field_71439_g.field_70143_R > 0.0F && (double)mc.field_71439_g.field_70143_R < 0.5D) {
            double yaw = Math.toRadians((double)mc.field_71439_g.field_70177_z);
            double boostVal = this.boost.getValue();
            mc.field_71439_g.func_70024_g(-Math.sin(yaw) * boostVal * 0.1D, 0.0D, Math.cos(yaw) * boostVal * 0.1D);
         }

      }
   }
}
