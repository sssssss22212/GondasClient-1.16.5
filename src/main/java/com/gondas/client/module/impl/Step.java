package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;

public class Step extends Module {
   private Setting.Double height = new Setting.Double("Height", 2.0D, 1.0D, 5.0D);

   public Step() {
      super("Step", "Step up blocks", Module.Category.MOVEMENT);
      this.addSettings(new Setting[]{this.height});
   }

   public void onEnable() {
      if (mc.field_71439_g != null) {
         mc.field_71439_g.field_70138_W = (float)this.height.getValue();
      }

   }

   public void onDisable() {
      if (mc.field_71439_g != null) {
         mc.field_71439_g.field_70138_W = 0.6F;
      }

   }

   public void onTick() {
      if (mc.field_71439_g != null && mc.field_71439_g.field_70138_W != (float)this.height.getValue()) {
         mc.field_71439_g.field_70138_W = (float)this.height.getValue();
      }

   }
}
