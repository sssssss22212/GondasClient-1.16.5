package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;

public class Zoom extends Module {
   private Setting.Double fov = new Setting.Double("FOV", 30.0D, 5.0D, 100.0D);
   private float prevFOV = 70.0F;

   public Zoom() {
      super("Zoom", "Zoom in like sniper", Module.Category.MISC);
      this.addSettings(new Setting[]{this.fov});
   }

   public void onEnable() {
      if (mc.field_71439_g != null) {
         this.prevFOV = (float)mc.field_71474_y.field_74334_X;
      }

   }

   public void onDisable() {
      mc.field_71474_y.field_74334_X = (double)this.prevFOV;
   }

   public void onTick() {
      mc.field_71474_y.field_74334_X = (double)((float)this.fov.getValue());
   }
}
