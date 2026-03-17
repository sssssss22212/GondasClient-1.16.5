package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import net.minecraft.item.FishingRodItem;
import net.minecraft.util.Hand;

public class AutoFish extends Module {
   private Setting.Int delay = new Setting.Int("Delay", 5, 0, 20);
   private boolean waiting = false;
   private int timer = 0;

   public AutoFish() {
      super("AutoFish", "Auto fish", Module.Category.WORLD);
      this.addSettings(new Setting[]{this.delay});
   }

   public void onTick() {
      if (mc.field_71439_g != null) {
         if (mc.field_71439_g.func_184614_ca().func_77973_b() instanceof FishingRodItem) {
            if (mc.field_71439_g.field_71104_cf != null && !this.waiting && mc.field_71439_g.field_71104_cf.func_213322_ci().field_72448_b < -0.1D) {
               mc.field_71442_b.func_187101_a(mc.field_71439_g, mc.field_71441_e, Hand.MAIN_HAND);
               this.waiting = true;
               this.timer = this.delay.getValue();
            }

            if (this.waiting) {
               --this.timer;
               if (this.timer <= 0) {
                  mc.field_71442_b.func_187101_a(mc.field_71439_g, mc.field_71441_e, Hand.MAIN_HAND);
                  this.waiting = false;
               }
            }

         }
      }
   }
}
