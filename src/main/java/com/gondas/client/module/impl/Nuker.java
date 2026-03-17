package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class Nuker extends Module {
   private Setting.Double range = new Setting.Double("Range", 4.0D, 1.0D, 6.0D);
   private Setting.Mode mode = new Setting.Mode("Mode", new String[]{"All", "Flat", "Smash"});

   public Nuker() {
      super("Nuker", "Break blocks around you", Module.Category.WORLD);
      this.addSettings(new Setting[]{this.range, this.mode});
   }

   public void onTick() {
      if (mc.field_71441_e != null && mc.field_71439_g != null) {
         BlockPos playerPos = mc.field_71439_g.func_233580_cy_();
         int r = (int)this.range.getValue();

         for(int x = -r; x <= r; ++x) {
            for(int y = -r; y <= r; ++y) {
               for(int z = -r; z <= r; ++z) {
                  BlockPos pos = playerPos.func_177982_a(x, y, z);
                  String m = this.mode.getValue();
                  if ((!m.equals("Flat") || y <= 0) && (!m.equals("Smash") || y == 0) && !mc.field_71441_e.func_175623_d(pos)) {
                     mc.field_71442_b.func_180512_c(pos, Direction.UP);
                  }
               }
            }
         }

      }
   }
}
