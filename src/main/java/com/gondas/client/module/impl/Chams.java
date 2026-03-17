package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;

public class Chams extends Module {
   private Setting.Boolean players = new Setting.Boolean("Players", true);
   private Setting.Boolean mobs = new Setting.Boolean("Mobs", false);

   public Chams() {
      super("Chams", "See entities through walls", Module.Category.RENDER);
      this.addSettings(new Setting[]{this.players, this.mobs});
   }
}
