package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import net.minecraft.util.math.BlockPos;

public class Parkour extends Module {
   public Parkour() {
      super("Parkour", "Auto jump at edges", Module.Category.MOVEMENT);
   }

   public void onTick() {
      if (mc.field_71439_g != null) {
         if (mc.field_71439_g.func_233570_aj_() && mc.field_71474_y.field_74351_w.func_151470_d()) {
            BlockPos pos = mc.field_71439_g.func_233580_cy_().func_177977_b();
            if (mc.field_71441_e.func_175623_d(pos)) {
               mc.field_71439_g.func_70664_aZ();
            }
         }

      }
   }
}
