package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class Breaker extends Module {
   private Setting.Int range = new Setting.Int("Range", 5, 1, 10);

   public Breaker() {
      super("Breaker", "Break cobwebs", Module.Category.WORLD);
      this.addSettings(new Setting[]{this.range});
   }

   public void onTick() {
      if (mc.field_71441_e != null && mc.field_71439_g != null) {
         BlockPos pos = mc.field_71439_g.func_233580_cy_();
         int r = this.range.getValue();

         for(int x = -r; x <= r; ++x) {
            for(int y = -r; y <= r; ++y) {
               for(int z = -r; z <= r; ++z) {
                  BlockPos check = pos.func_177982_a(x, y, z);
                  if (mc.field_71441_e.func_180495_p(check).func_177230_c() == Blocks.field_196553_aF) {
                     mc.field_71442_b.func_180512_c(check, Direction.UP);
                  }
               }
            }
         }

      }
   }
}
