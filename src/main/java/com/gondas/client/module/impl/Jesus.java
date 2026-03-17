package com.gondas.client.module.impl;

import com.gondas.client.module.Module;

public class Jesus extends Module {
   public Jesus() {
      super("Jesus", "Walk on water", Module.Category.MOVEMENT);
   }

   public void onTick() {
      if (mc.field_71439_g != null && mc.field_71441_e != null) {
         if (mc.field_71439_g.func_70090_H() || mc.field_71439_g.func_180799_ab()) {
            mc.field_71439_g.func_213293_j(mc.field_71439_g.func_213322_ci().field_72450_a, 0.1D, mc.field_71439_g.func_213322_ci().field_72449_c);
         }

      }
   }
}
