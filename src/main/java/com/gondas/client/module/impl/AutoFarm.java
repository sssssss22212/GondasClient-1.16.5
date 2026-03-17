package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.util.math.BlockPos;

public class AutoFarm extends Module {
   private Setting.Double range = new Setting.Double("Range", 4.0D, 1.0D, 6.0D);

   public AutoFarm() {
      super("AutoFarm", "Auto harvest crops", Module.Category.WORLD);
      this.addSettings(new Setting[]{this.range});
   }

   public void onTick() {
      if (mc.field_71441_e != null && mc.field_71439_g != null) {
         BlockPos playerPos = mc.field_71439_g.func_233580_cy_();
         int r = (int)this.range.getValue();

         for(int x = -r; x <= r; ++x) {
            for(int y = -2; y <= 2; ++y) {
               for(int z = -r; z <= r; ++z) {
                  BlockPos pos = playerPos.func_177982_a(x, y, z);
                  BlockState state = mc.field_71441_e.func_180495_p(pos);
                  Block block = state.func_177230_c();
                  if (block instanceof CropsBlock) {
                     CropsBlock crop = (CropsBlock)block;
                     if (crop.func_185525_y(state)) {
                        mc.field_71441_e.func_175655_b(pos, true);
                     }
                  }
               }
            }
         }

      }
   }
}
