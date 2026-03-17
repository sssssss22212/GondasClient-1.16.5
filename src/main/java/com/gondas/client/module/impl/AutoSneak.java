package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;

public class AutoSneak extends Module {
   private Setting.Mode mode = new Setting.Mode("Mode", new String[]{"Always", "Move"});

   public AutoSneak() {
      super("AutoSneak", "Auto sneak", Module.Category.MISC);
      this.addSettings(new Setting[]{this.mode});
   }

   public void onEnable() {
      mc.field_71474_y.field_228046_af_.func_225593_a_(true);
   }

   public void onDisable() {
      mc.field_71474_y.field_228046_af_.func_225593_a_(false);
   }

   public void onTick() {
      if (this.mode.getValue().equals("Move")) {
         mc.field_71474_y.field_228046_af_.func_225593_a_(mc.field_71474_y.field_74351_w.func_151470_d() || mc.field_71474_y.field_74368_y.func_151470_d() || mc.field_71474_y.field_74370_x.func_151470_d() || mc.field_71474_y.field_74366_z.func_151470_d());
      }

   }
}
