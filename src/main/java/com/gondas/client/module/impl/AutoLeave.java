package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;

public class AutoLeave extends Module {
   private Setting.Int health = new Setting.Int("Health", 5, 1, 20);
   private Setting.Mode mode = new Setting.Mode("Mode", new String[]{"Disconnect", "Command", "Kick"});

   public AutoLeave() {
      super("AutoLeave", "Leave when low health", Module.Category.MISC);
      this.addSettings(new Setting[]{this.health, this.mode});
   }

   public void onTick() {
      if (mc.field_71439_g != null) {
         if (mc.field_71439_g.func_110143_aJ() <= (float)this.health.getValue()) {
            String m = this.mode.getValue();
            if (m.equals("Disconnect")) {
               mc.field_71441_e.func_72882_A();
            } else if (m.equals("Command")) {
               mc.field_71439_g.func_71165_d("/hub");
            } else if (m.equals("Kick")) {
               mc.field_71439_g.func_71165_d("/kill");
            }

            this.toggle();
         }

      }
   }
}
