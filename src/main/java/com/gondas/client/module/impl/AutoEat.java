package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class AutoEat extends Module {
   private Setting.Int health = new Setting.Int("Health", 10, 1, 20);
   private Setting.Int hunger = new Setting.Int("Hunger", 10, 1, 20);
   private Setting.Boolean gapples = new Setting.Boolean("Gapples", false);
   private boolean eating = false;
   private int prevSlot = -1;

   public AutoEat() {
      super("AutoEat", "Auto eat food", Module.Category.PLAYER);
      this.addSettings(new Setting[]{this.health, this.hunger, this.gapples});
   }

   public void onTick() {
      if (mc.field_71439_g != null) {
         if (this.eating) {
            if (!mc.field_71439_g.func_184587_cr()) {
               this.eating = false;
               if (this.prevSlot != -1) {
                  mc.field_71439_g.field_71071_by.field_70461_c = this.prevSlot;
                  this.prevSlot = -1;
               }
            }

         } else {
            if (mc.field_71439_g.func_110143_aJ() <= (float)this.health.getValue() || mc.field_71439_g.func_71024_bL().func_75116_a() <= this.hunger.getValue()) {
               int foodSlot = this.findFood();
               if (foodSlot != -1) {
                  this.prevSlot = mc.field_71439_g.field_71071_by.field_70461_c;
                  mc.field_71439_g.field_71071_by.field_70461_c = foodSlot;
                  mc.field_71442_b.func_187101_a(mc.field_71439_g, mc.field_71441_e, Hand.MAIN_HAND);
                  this.eating = true;
               }
            }

         }
      }
   }

   private int findFood() {
      for(int i = 0; i < 9; ++i) {
         Item item = mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b();
         if (item.func_219971_r() && (item != Items.field_151153_ao || this.gapples.getValue())) {
            return i;
         }
      }

      return -1;
   }
}
