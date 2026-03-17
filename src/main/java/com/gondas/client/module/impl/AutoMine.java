package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class AutoMine extends Module {
   private Setting.Mode target = new Setting.Mode("Target", new String[]{"Diamond", "Gold", "Iron", "All"});
   private Setting.Int range = new Setting.Int("Range", 5, 1, 10);

   public AutoMine() {
      super("AutoMine", "Auto mine ores", Module.Category.WORLD);
      this.addSettings(new Setting[]{this.target, this.range});
   }

   public void onTick() {
      if (mc.field_71441_e != null && mc.field_71439_g != null) {
         BlockPos playerPos = mc.field_71439_g.func_233580_cy_();
         int r = this.range.getValue();

         for(int x = -r; x <= r; ++x) {
            for(int y = -r; y <= r; ++y) {
               for(int z = -r; z <= r; ++z) {
                  BlockPos pos = playerPos.func_177982_a(x, y, z);
                  if (this.shouldMine(pos)) {
                     mc.field_71442_b.func_180512_c(pos, Direction.UP);
                     return;
                  }
               }
            }
         }

      }
   }

   private boolean shouldMine(BlockPos pos) {
      String t = this.target.getValue();
      if (t.equals("Diamond")) {
         return mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150482_ag;
      } else if (t.equals("Gold")) {
         return mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150352_o;
      } else if (t.equals("Iron")) {
         return mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150366_p;
      } else {
         return !mc.field_71441_e.func_175623_d(pos);
      }
   }
}
