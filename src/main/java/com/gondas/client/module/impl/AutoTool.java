package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;

public class AutoTool extends Module {
   public AutoTool() {
      super("AutoTool", "Auto select best tool", Module.Category.PLAYER);
   }

   public void onTick() {
      if (mc.field_71439_g != null && mc.field_71441_e != null) {
         if (mc.field_71474_y.field_74312_F.func_151470_d()) {
            if (mc.field_71476_x != null && mc.field_71476_x.func_216346_c() == Type.BLOCK) {
               BlockRayTraceResult result = (BlockRayTraceResult)mc.field_71476_x;
               BlockState state = mc.field_71441_e.func_180495_p(result.func_216350_a());
               int bestSlot = -1;
               float bestSpeed = 1.0F;

               for(int i = 0; i < 9; ++i) {
                  ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
                  if (!stack.func_190926_b()) {
                     float speed = stack.func_150997_a(state);
                     if (speed > bestSpeed) {
                        bestSpeed = speed;
                        bestSlot = i;
                     }
                  }
               }

               if (bestSlot != -1 && mc.field_71439_g.field_71071_by.field_70461_c != bestSlot) {
                  mc.field_71439_g.field_71071_by.field_70461_c = bestSlot;
               }

            }
         }
      }
   }
}
