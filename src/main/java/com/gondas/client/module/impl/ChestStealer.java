package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ChestStealer extends Module {
   private Setting.Int delay = new Setting.Int("Delay", 0, 0, 10);
   private int timer = 0;

   public ChestStealer() {
      super("ChestStealer", "Auto steal chests", Module.Category.PLAYER);
      this.addSettings(new Setting[]{this.delay});
   }

   public void onTick() {
      if (mc.field_71439_g != null) {
         if (mc.field_71439_g.field_71070_bA instanceof ChestContainer) {
            if (this.timer > 0) {
               --this.timer;
            } else {
               ChestContainer chest = (ChestContainer)mc.field_71439_g.field_71070_bA;

               for(int i = 0; i < chest.field_75151_b.size() - 36; ++i) {
                  Slot slot = (Slot)chest.field_75151_b.get(i);
                  ItemStack stack = slot.func_75211_c();
                  if (!stack.func_190926_b()) {
                     mc.field_71442_b.func_187098_a(chest.field_75152_c, slot.field_75222_d, 0, ClickType.QUICK_MOVE, mc.field_71439_g);
                     this.timer = this.delay.getValue();
                     return;
                  }
               }

            }
         }
      }
   }
}
