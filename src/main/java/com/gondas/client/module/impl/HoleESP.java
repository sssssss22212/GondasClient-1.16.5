package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import com.gondas.client.util.Render3D;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HoleESP extends Module {
   private Setting.Int range = new Setting.Int("Range", 10, 3, 20);

   public HoleESP() {
      super("HoleESP", "Highlight safe holes", Module.Category.RENDER);
      this.addSettings(new Setting[]{this.range});
   }

   @SubscribeEvent
   public void onRender(RenderWorldLastEvent event) {
      if (mc.field_71441_e != null && mc.field_71439_g != null) {
         BlockPos playerPos = mc.field_71439_g.func_233580_cy_();
         int r = this.range.getValue();

         for(int x = -r; x <= r; ++x) {
            for(int z = -r; z <= r; ++z) {
               for(int y = -5; y <= 5; ++y) {
                  BlockPos pos = playerPos.func_177982_a(x, y, z);
                  if (this.isHole(pos)) {
                     Vector3d vec = new Vector3d((double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p());
                     Render3D.drawBox(vec, 1.0D, 1.0D, 0.0F, 1.0F, 0.0F, 0.3F);
                  }
               }
            }
         }

      }
   }

   private boolean isHole(BlockPos pos) {
      if (!mc.field_71441_e.func_175623_d(pos)) {
         return false;
      } else if (!mc.field_71441_e.func_175623_d(pos.func_177984_a())) {
         return false;
      } else {
         return mc.field_71441_e.func_180495_p(pos.func_177977_b()).func_200132_m() && mc.field_71441_e.func_180495_p(pos.func_177978_c()).func_200132_m() && mc.field_71441_e.func_180495_p(pos.func_177968_d()).func_200132_m() && mc.field_71441_e.func_180495_p(pos.func_177974_f()).func_200132_m() && mc.field_71441_e.func_180495_p(pos.func_177976_e()).func_200132_m();
      }
   }
}
