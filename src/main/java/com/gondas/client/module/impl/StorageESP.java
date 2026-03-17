package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import com.gondas.client.util.Render3D;
import net.minecraft.block.Block;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class StorageESP extends Module {
   private Setting.Int range = new Setting.Int("Range", 50, 10, 100);

   public StorageESP() {
      super("StorageESP", "Highlight storage blocks", Module.Category.RENDER);
      this.addSettings(new Setting[]{this.range});
   }

   @SubscribeEvent
   public void onRender(RenderWorldLastEvent event) {
      if (mc.field_71441_e != null && mc.field_71439_g != null) {
         BlockPos playerPos = mc.field_71439_g.func_233580_cy_();
         int r = this.range.getValue();

         for(int x = -r; x <= r; ++x) {
            for(int y = -r; y <= r; ++y) {
               for(int z = -r; z <= r; ++z) {
                  BlockPos pos = playerPos.func_177982_a(x, y, z);
                  Block block = mc.field_71441_e.func_180495_p(pos).func_177230_c();
                  float red = 0.0F;
                  float green = 0.0F;
                  float blue = 0.0F;
                  boolean render = false;
                  if (block instanceof ChestBlock) {
                     red = 0.0F;
                     green = 0.8F;
                     blue = 0.8F;
                     render = true;
                  } else if (block instanceof EnderChestBlock) {
                     red = 0.8F;
                     green = 0.0F;
                     blue = 0.8F;
                     render = true;
                  } else if (block instanceof ShulkerBoxBlock) {
                     red = 0.8F;
                     green = 0.2F;
                     blue = 0.8F;
                     render = true;
                  }

                  if (render) {
                     Vector3d vec = new Vector3d((double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p());
                     Render3D.drawBox(vec, 1.0D, 1.0D, red, green, blue, 0.4F);
                  }
               }
            }
         }

      }
   }
}
