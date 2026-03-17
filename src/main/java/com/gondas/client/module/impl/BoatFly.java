package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import net.minecraft.entity.item.BoatEntity;

public class BoatFly extends Module {
   private Setting.Double speed = new Setting.Double("Speed", 2.0D, 0.5D, 5.0D);
   private Setting.Double upSpeed = new Setting.Double("UpSpeed", 1.0D, 0.5D, 3.0D);

   public BoatFly() {
      super("BoatFly", "Fly with boats", Module.Category.MOVEMENT);
      this.addSettings(new Setting[]{this.speed, this.upSpeed});
   }

   public void onTick() {
      if (mc.field_71439_g != null && mc.field_71439_g.func_184187_bx() != null) {
         if (mc.field_71439_g.func_184187_bx() instanceof BoatEntity) {
            BoatEntity boat = (BoatEntity)mc.field_71439_g.func_184187_bx();
            double yaw = Math.toRadians((double)mc.field_71439_g.field_70177_z);
            double speedVal = this.speed.getValue();
            double upVal = this.upSpeed.getValue();
            boat.func_213293_j(0.0D, 0.0D, 0.0D);
            if (mc.field_71474_y.field_74351_w.func_151470_d()) {
               boat.func_70024_g(-Math.sin(yaw) * speedVal * 0.1D, 0.0D, Math.cos(yaw) * speedVal * 0.1D);
            }

            if (mc.field_71474_y.field_74368_y.func_151470_d()) {
               boat.func_70024_g(Math.sin(yaw) * speedVal * 0.1D, 0.0D, -Math.cos(yaw) * speedVal * 0.1D);
            }

            if (mc.field_71474_y.field_74314_A.func_151470_d()) {
               boat.func_70024_g(0.0D, upVal * 0.1D, 0.0D);
            }

            if (mc.field_71474_y.field_228046_af_.func_151470_d()) {
               boat.func_70024_g(0.0D, -upVal * 0.1D, 0.0D);
            }

         }
      }
   }
}
