package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.ItemStack;

public class InventoryManager extends Module {
   private Setting.Boolean clean = new Setting.Boolean("Clean", true);
   private Setting.Int delay = new Setting.Int("Delay", 2, 0, 10);
   private int timer = 0;

   public InventoryManager() {
      super("InvManager", "Manage inventory", Module.Category.PLAYER);
      this.addSettings(new Setting[]{this.clean, this.delay});
   }

   public void onTick() {
      if (mc.field_71439_g != null && mc.field_71462_r == null) {
         if (this.timer > 0) {
            --this.timer;
         } else {
            if (this.clean.getValue()) {
               for(int i = 9; i < 45; ++i) {
                  int idx = i >= 36 ? i - 36 : i;
                  ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(idx);
                  if (!stack.func_190926_b()) {
                     String name = stack.func_77973_b().getRegistryName().toString();
                     if ((name.contains("cobblestone") || name.contains("dirt") || name.contains("gravel")) && stack.func_190916_E() > 32) {
                        mc.field_71442_b.func_187098_a(0, i, 1, ClickType.THROW, mc.field_71439_g);
                        this.timer = this.delay.getValue();
                        return;
                     }
                  }
               }
            }

         }
      }
   }
}
