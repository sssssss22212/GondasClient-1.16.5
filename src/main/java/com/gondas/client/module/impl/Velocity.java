package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Velocity extends Module {
   private Setting.Int horizontal = new Setting.Int("Horizontal", 0, 0, 100);
   private Setting.Int vertical = new Setting.Int("Vertical", 0, 0, 100);

   public Velocity() {
      super("Velocity", "Reduce knockback", Module.Category.COMBAT);
      this.addSettings(new Setting[]{this.horizontal, this.vertical});
   }

   @SubscribeEvent
   public void onKnockback(LivingKnockBackEvent event) {
      if (mc.field_71439_g != null && event.getEntityLiving() == mc.field_71439_g) {
         event.setStrength((float)this.horizontal.getValue() / 100.0F);
      }

   }
}
