package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.Items;

public class AutoTotem extends Module {
   private Setting.Mode setting = new Setting.Mode("Hand", new String[]{"Offhand", "Mainhand"});
   private Setting.Boolean always = new Setting.Boolean("Always", true);
   private Setting.Int health = new Setting.Int("Health", 15, 1, 20);

   public AutoTotem() {
      super("AutoTotem", "Auto equip totem", Module.Category.COMBAT);
      this.addSettings(new Setting[]{this.setting, this.always, this.health});
   }

   public void onTick() {
      if (mc.field_71439_g != null) {
         boolean shouldEquip = this.always.getValue() || mc.field_71439_g.func_110143_aJ() <= (float)this.health.getValue();
         if (shouldEquip) {
            int totemSlot = this.findTotem();
            if (totemSlot != -1) {
               if (this.setting.getValue().equals("Offhand")) {
                  if (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY) {
                     return;
                  }

                  mc.field_71442_b.func_187098_a(0, totemSlot, 0, ClickType.PICKUP, mc.field_71439_g);
                  mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, mc.field_71439_g);
                  if (!mc.field_71439_g.field_71071_by.func_70445_o().func_190926_b()) {
                     mc.field_71442_b.func_187098_a(0, totemSlot, 0, ClickType.PICKUP, mc.field_71439_g);
                  }
               }

            }
         }
      }
   }

   private int findTotem() {
      for(int i = 0; i < 36; ++i) {
         if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == Items.field_190929_cY) {
            return i < 9 ? i + 36 : i;
         }
      }

      return -1;
   }
}
