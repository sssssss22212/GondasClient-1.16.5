package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Iterator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Tracers extends Module {
   private Setting.Boolean players = new Setting.Boolean("Players", true);
   private Setting.Boolean hostile = new Setting.Boolean("Hostile", true);
   private Setting.Boolean passive = new Setting.Boolean("Passive", false);
   private Setting.Double range = new Setting.Double("Range", 100.0D, 10.0D, 200.0D);
   private Setting.Mode from = new Setting.Mode("From", new String[]{"Eyes", "Feet", "Center"});
   private Setting.Double lineWidth = new Setting.Double("LineWidth", 1.5D, 0.5D, 5.0D);
   private Setting.Boolean distanceColor = new Setting.Boolean("DistanceColor", true);
   private Setting.Boolean healthColor = new Setting.Boolean("HealthColor", false);
   private Setting.Boolean throughWalls = new Setting.Boolean("ThroughWalls", true);

   public Tracers() {
      super("Tracers", "Draw lines to entities", Module.Category.RENDER);
      this.addSettings(new Setting[]{this.players, this.hostile, this.passive, this.range, this.from, this.lineWidth, this.distanceColor, this.healthColor, this.throughWalls});
   }

   @SubscribeEvent
   public void onRenderWorld(RenderWorldLastEvent event) {
      if (mc.field_71441_e != null && mc.field_71439_g != null) {
         MatrixStack matrix = event.getMatrixStack();
         Vector3d camPos = mc.field_71460_t.func_215316_n().func_216785_c();
         double playerX = mc.field_71439_g.func_226277_ct_();
         String fromMode = this.from.getValue();
         double playerY;
         if (fromMode.equals("Eyes")) {
            playerY = mc.field_71439_g.func_226280_cw_();
         } else if (fromMode.equals("Center")) {
            playerY = mc.field_71439_g.func_226278_cu_() + (double)mc.field_71439_g.func_213302_cg() / 2.0D;
         } else {
            playerY = mc.field_71439_g.func_226278_cu_();
         }

         double playerZ = mc.field_71439_g.func_226281_cx_();
         double startX = playerX - camPos.field_72450_a;
         double startY = playerY - camPos.field_72448_b;
         double startZ = playerZ - camPos.field_72449_c;
         RenderSystem.enableBlend();
         RenderSystem.disableDepthTest();
         RenderSystem.disableLighting();
         RenderSystem.disableTexture();
         if (this.throughWalls.getValue()) {
            RenderSystem.depthMask(false);
         }

         RenderSystem.lineWidth((float)this.lineWidth.getValue());
         Tessellator tessellator = Tessellator.func_178181_a();
         BufferBuilder buffer = tessellator.func_178180_c();
         Iterator var19 = mc.field_71441_e.func_217416_b().iterator();

         while(true) {
            LivingEntity living;
            double dist;
            do {
               do {
                  Entity e;
                  do {
                     do {
                        if (!var19.hasNext()) {
                           if (this.throughWalls.getValue()) {
                              RenderSystem.depthMask(true);
                           }

                           RenderSystem.enableDepthTest();
                           RenderSystem.enableTexture();
                           RenderSystem.disableBlend();
                           return;
                        }

                        e = (Entity)var19.next();
                     } while(!(e instanceof LivingEntity));
                  } while(e == mc.field_71439_g);

                  living = (LivingEntity)e;
                  dist = (double)mc.field_71439_g.func_70032_d(e);
               } while(dist > this.range.getValue());
            } while(living.func_110143_aJ() <= 0.0F);

            boolean shouldRender = false;
            float r = 1.0F;
            float g = 1.0F;
            float b = 1.0F;
            if (living instanceof PlayerEntity && this.players.getValue()) {
               if (this.healthColor.getValue()) {
                  float healthPercent = living.func_110143_aJ() / living.func_110138_aP();
                  r = healthPercent > 0.5F ? (1.0F - healthPercent) * 2.0F : 1.0F;
                  g = healthPercent > 0.5F ? 1.0F : healthPercent * 2.0F;
                  b = 0.0F;
               } else if (this.distanceColor.getValue()) {
                  Color c = Color.getHSBColor((float)(dist / this.range.getValue() * 0.699999988079071D), 0.8F, 1.0F);
                  r = (float)c.getRed() / 255.0F;
                  g = (float)c.getGreen() / 255.0F;
                  b = (float)c.getBlue() / 255.0F;
               } else {
                  r = 1.0F;
                  g = 0.2F;
                  b = 0.2F;
               }

               shouldRender = true;
            } else if (living instanceof MonsterEntity && this.hostile.getValue()) {
               r = 1.0F;
               g = 0.0F;
               b = 1.0F;
               shouldRender = true;
            } else if (living instanceof AnimalEntity && this.passive.getValue()) {
               r = 0.0F;
               g = 1.0F;
               b = 0.0F;
               shouldRender = true;
            }

            if (shouldRender) {
               double targetX = living.func_226277_ct_() - camPos.field_72450_a;
               double targetY = living.func_226278_cu_() + (double)living.func_213302_cg() / 2.0D - camPos.field_72448_b;
               double targetZ = living.func_226281_cx_() - camPos.field_72449_c;
               matrix.func_227860_a_();
               Matrix4f matrix4f = matrix.func_227866_c_().func_227870_a_();
               buffer.func_181668_a(1, DefaultVertexFormats.field_181706_f);
               buffer.func_227888_a_(matrix4f, (float)startX, (float)startY, (float)startZ).func_227885_a_(r, g, b, 0.8F).func_181675_d();
               buffer.func_227888_a_(matrix4f, (float)targetX, (float)targetY, (float)targetZ).func_227885_a_(r, g, b, 0.8F).func_181675_d();
               tessellator.func_78381_a();
               matrix.func_227865_b_();
            }
         }
      }
   }
}
