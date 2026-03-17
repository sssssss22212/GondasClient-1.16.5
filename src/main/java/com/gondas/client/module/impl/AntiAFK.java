package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import net.minecraft.client.entity.player.ClientPlayerEntity;

public class AntiAFK extends Module {
   private Setting.Boolean rotate = new Setting.Boolean("Rotate", true);
   private Setting.Boolean jump = new Setting.Boolean("Jump", false);
   private int timer = 0;

   public AntiAFK() {
      super("AntiAFK", "Prevent AFK kick", Module.Category.MISC);
      this.addSettings(new Setting[]{this.rotate, this.jump});
   }

   public void onTick() {
      if (mc.field_71439_g != null) {
         ++this.timer;
         if (this.timer >= 100) {
            this.timer = 0;
            if (this.rotate.getValue()) {
               ClientPlayerEntity var10000 = mc.field_71439_g;
               var10000.field_70177_z += 180.0F;
            }

            if (this.jump.getValue() && mc.field_71439_g.func_233570_aj_()) {
               mc.field_71439_g.func_70664_aZ();
            }
         }

      }
   }
}
