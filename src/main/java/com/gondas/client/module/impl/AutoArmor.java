package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

public class AutoArmor extends Module {
   private Setting.Int delay = new Setting.Int("Delay", 0, 0, 10);
   private int timer = 0;

   public AutoArmor() {
      super("AutoArmor", "Auto equip best armor", Module.Category.COMBAT);
      this.addSettings(new Setting[]{this.delay});
   }

   public void onTick() {
      if (mc.field_71439_g != null) {
         if (this.timer > 0) {
            --this.timer;
         } else {
            EquipmentSlotType[] var1 = new EquipmentSlotType[]{EquipmentSlotType.FEET, EquipmentSlotType.LEGS, EquipmentSlotType.CHEST, EquipmentSlotType.HEAD};
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               EquipmentSlotType slot = var1[var3];
               ItemStack current = mc.field_71439_g.func_184582_a(slot);
               int bestSlot = -1;
               int bestValue = this.getArmorValue(current);

               int i;
               for(i = 0; i < 36; ++i) {
                  ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
                  if (stack.func_77973_b() instanceof ArmorItem) {
                     ArmorItem armor = (ArmorItem)stack.func_77973_b();
                     if (armor.func_185083_B_() == slot) {
                        int value = this.getArmorValue(stack);
                        if (value > bestValue) {
                           bestValue = value;
                           bestSlot = i;
                        }
                     }
                  }
               }

               if (bestSlot != -1) {
                  i = 8 - slot.func_188454_b();
                  mc.field_71442_b.func_187098_a(0, bestSlot < 9 ? bestSlot + 36 : bestSlot, 0, ClickType.PICKUP, mc.field_71439_g);
                  mc.field_71442_b.func_187098_a(0, i, 0, ClickType.PICKUP, mc.field_71439_g);
                  if (!current.func_190926_b()) {
                     mc.field_71442_b.func_187098_a(0, bestSlot < 9 ? bestSlot + 36 : bestSlot, 0, ClickType.PICKUP, mc.field_71439_g);
                  }

                  this.timer = this.delay.getValue();
                  return;
               }
            }

         }
      }
   }

   private int getArmorValue(ItemStack stack) {
      if (!stack.func_190926_b() && stack.func_77973_b() instanceof ArmorItem) {
         ArmorItem armor = (ArmorItem)stack.func_77973_b();
         return armor.func_200881_e() + (int)(armor.func_234657_f_() * 10.0F);
      } else {
         return 0;
      }
   }
}
