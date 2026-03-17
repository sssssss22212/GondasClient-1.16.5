package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;

public class TriggerBot extends Module {
   private Setting.Int delay = new Setting.Int("Delay", 0, 0, 20);
   private Setting.Boolean players = new Setting.Boolean("Players", true);
   private Setting.Boolean mobs = new Setting.Boolean("Mobs", false);
   private int ticks = 0;

   public TriggerBot() {
      super("TriggerBot", "Auto attack when looking at entity", Module.Category.COMBAT);
      this.addSettings(new Setting[]{this.delay, this.players, this.mobs});
   }

   public void onTick() {
      if (mc.field_71439_g != null && mc.field_71441_e != null) {
         if (this.ticks > 0) {
            --this.ticks;
         } else {
            RayTraceResult result = mc.field_71476_x;
            if (result != null && result.func_216346_c() == Type.ENTITY) {
               EntityRayTraceResult entityResult = (EntityRayTraceResult)result;
               if (entityResult.func_216348_a() instanceof LivingEntity) {
                  LivingEntity target = (LivingEntity)entityResult.func_216348_a();
                  if (!(target instanceof PlayerEntity) || this.players.getValue()) {
                     if (target instanceof PlayerEntity || this.mobs.getValue()) {
                        mc.field_71442_b.func_78764_a(mc.field_71439_g, target);
                        mc.field_71439_g.func_184609_a(Hand.MAIN_HAND);
                        this.ticks = this.delay.getValue();
                     }
                  }
               }
            }
         }
      }
   }
}
