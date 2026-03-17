package com.gondas.client.module.impl;

import com.gondas.client.module.Module;

public class AirJump extends Module {
   public AirJump() {
      super("AirJump", "Jump in air", Module.Category.MOVEMENT);
   }

   public void onTick() {
      if (mc.field_71439_g != null) {
         if (mc.field_71474_y.field_74314_A.func_151468_f()) {
            mc.field_71439_g.func_213293_j(mc.field_71439_g.func_213322_ci().field_72450_a, 0.42D, mc.field_71439_g.func_213322_ci().field_72449_c);
         }

      }
   }
}
