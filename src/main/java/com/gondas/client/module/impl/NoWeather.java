package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;

public class NoWeather extends Module {
   private Setting.Boolean rain = new Setting.Boolean("NoRain", true);

   public NoWeather() {
      super("NoWeather", "Disable weather", Module.Category.MISC);
      this.addSettings(new Setting[]{this.rain});
   }
}
