package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import com.gondas.client.util.Render2D;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TargetHUD extends Module {
   private Setting.Double range = new Setting.Double("Range", 6.0D, 3.0D, 10.0D);
   private LivingEntity target = null;

   public TargetHUD() {
      super("TargetHUD", "Display target info", Module.Category.RENDER);
      this.addSettings(new Setting[]{this.range});
   }

   public void onTick() {
      if (mc.field_71441_e != null && mc.field_71439_g != null) {
         this.target = null;
         double closest = this.range.getValue();
         Iterator var3 = mc.field_71441_e.func_217416_b().iterator();

         while(var3.hasNext()) {
            Entity e = (Entity)var3.next();
            if (e instanceof LivingEntity && e != mc.field_71439_g) {
               double dist = (double)mc.field_71439_g.func_70032_d(e);
               if (dist < closest) {
                  closest = dist;
                  this.target = (LivingEntity)e;
               }
            }
         }

      } else {
         this.target = null;
      }
   }

   @SubscribeEvent
   public void onRenderOverlay(Post event) {
      if (this.target != null && event.getType() == ElementType.TEXT) {
         int x = mc.func_228018_at_().func_198107_o() - 160;
         Render2D.drawRect((double)x, 10.0D, 150.0D, 50.0D, Integer.MIN_VALUE);
         MatrixStack matrixStack = new MatrixStack();
         mc.field_71466_p.func_238405_a_(matrixStack, this.target.func_200200_C_().getString(), (float)(x + 5), 15.0F, -1);
         float health = this.target.func_110143_aJ() / this.target.func_110138_aP();
         Render2D.drawRect((double)(x + 5), 35.0D, (double)((int)(140.0F * health)), 10.0D, -16711936);
      }
   }
}
