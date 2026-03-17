package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;

public class TimerMod extends Module {
   private Setting.Double speed = new Setting.Double("Speed", 1.5D, 0.1D, 5.0D);

   public TimerMod() {
      super("Timer", "Change game speed", Module.Category.MISC);
      this.addSettings(new Setting[]{this.speed});
   }
}
