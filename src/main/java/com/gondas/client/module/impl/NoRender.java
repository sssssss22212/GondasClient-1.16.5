package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;

public class NoRender extends Module {
   private Setting.Boolean fire = new Setting.Boolean("Fire", true);
   private Setting.Boolean blindness = new Setting.Boolean("Blindness", true);

   public NoRender() {
      super("NoRender", "Disable visual effects", Module.Category.RENDER);
      this.addSettings(new Setting[]{this.fire, this.blindness});
   }
}
