package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

public class IceSpeed extends Module {
   private Setting.Double speed = new Setting.Double("Speed", 0.5D, 0.1D, 1.0D);

   public IceSpeed() {
      super("IceSpeed", "Move fast on ice", Module.Category.MOVEMENT);
      this.addSettings(new Setting[]{this.speed});
   }

   public void onTick() {
      if (mc.field_71439_g != null && mc.field_71441_e != null) {
         BlockPos pos = mc.field_71439_g.func_233580_cy_();
         if (mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150432_aD || mc.field_71441_e.func_180495_p(pos.func_177977_b()).func_177230_c() == Blocks.field_150432_aD) {
            double yaw = Math.toRadians((double)mc.field_71439_g.field_70177_z);
            float speedVal = (float)this.speed.getValue();
            mc.field_71439_g.func_213293_j(-Math.sin(yaw) * (double)speedVal, mc.field_71439_g.func_213322_ci().field_72448_b, Math.cos(yaw) * (double)speedVal);
         }

      }
   }
}
