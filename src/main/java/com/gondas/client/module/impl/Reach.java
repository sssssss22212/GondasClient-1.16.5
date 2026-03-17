package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;

public class Reach extends Module {
   private Setting.Double distance = new Setting.Double("Distance", 3.5D, 3.0D, 6.0D);

   public Reach() {
      super("Reach", "Extend attack range", Module.Category.COMBAT);
      this.addSettings(new Setting[]{this.distance});
   }

   public float getReachDistance() {
      return (float)this.distance.getValue();
   }
}
