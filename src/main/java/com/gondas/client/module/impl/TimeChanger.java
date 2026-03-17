package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;

public class TimeChanger extends Module {
   private Setting.Mode time = new Setting.Mode("Time", new String[]{"Day", "Noon", "Night", "Custom"});
   private Setting.Int customTime = new Setting.Int("CustomTime", 6000, 0, 24000);

   public TimeChanger() {
      super("TimeChanger", "Change world time", Module.Category.MISC);
      this.addSettings(new Setting[]{this.time, this.customTime});
   }

   public long getTime() {
      if (!this.isToggled()) {
         return -1L;
      } else {
         String t = this.time.getValue();
         if (t.equals("Day")) {
            return 1000L;
         } else if (t.equals("Noon")) {
            return 6000L;
         } else if (t.equals("Night")) {
            return 13000L;
         } else {
            return t.equals("Custom") ? (long)this.customTime.getValue() : -1L;
         }
      }
   }
}
