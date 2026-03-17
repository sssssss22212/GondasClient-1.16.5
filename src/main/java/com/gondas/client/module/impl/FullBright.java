package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class FullBright extends Module {
   private Setting.Mode mode = new Setting.Mode("Mode", new String[]{"Gamma", "Potion", "Both"});
   private double oldGamma = 1.0D;

   public FullBright() {
      super("FullBright", "See in the dark", Module.Category.RENDER);
      this.addSettings(new Setting[]{this.mode});
   }

   public void onEnable() {
      this.oldGamma = mc.field_71474_y.field_74333_Y;
   }

   public void onTick() {
      String m = this.mode.getValue();
      if (m.equals("Gamma") || m.equals("Both")) {
         mc.field_71474_y.field_74333_Y = 16.0D;
      }

      if ((m.equals("Potion") || m.equals("Both")) && mc.field_71439_g != null) {
         mc.field_71439_g.func_195064_c(new EffectInstance(Effects.field_76439_r, 10000, 0, false, false));
      }

   }

   public void onDisable() {
      mc.field_71474_y.field_74333_Y = this.oldGamma;
      if (mc.field_71439_g != null && mc.field_71439_g.func_70644_a(Effects.field_76439_r)) {
         mc.field_71439_g.func_195063_d(Effects.field_76439_r);
      }

   }
}
