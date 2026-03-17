package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Xray extends Module {
   private Setting.Boolean diamonds = new Setting.Boolean("Diamonds", true);
   private Setting.Boolean gold = new Setting.Boolean("Gold", true);
   private Setting.Boolean iron = new Setting.Boolean("Iron", false);
   private Setting.Boolean coal = new Setting.Boolean("Coal", false);
   private Setting.Boolean emeralds = new Setting.Boolean("Emeralds", true);
   private Setting.Boolean lapis = new Setting.Boolean("Lapis", false);
   private Setting.Boolean redstone = new Setting.Boolean("Redstone", false);
   private Setting.Boolean netherite = new Setting.Boolean("Netherite", true);
   private Setting.Boolean ancient = new Setting.Boolean("AncientDebris", true);
   private Setting.Double range = new Setting.Double("Range", 50.0D, 10.0D, 100.0D);
   private Setting.Double lineWidth = new Setting.Double("LineWidth", 1.5D, 0.5D, 3.0D);
   private Setting.Boolean filled = new Setting.Boolean("Filled", true);
   private Setting.Boolean showNames = new Setting.Boolean("ShowNames", true);
   private List<BlockPos> oresToRender = new ArrayList();
   private int scanTick = 0;

   public Xray() {
      super("Xray", "See ores through walls", Module.Category.RENDER);
      this.addSettings(new Setting[]{this.diamonds, this.gold, this.iron, this.coal, this.emeralds, this.lapis, this.redstone, this.netherite, this.ancient, this.range, this.lineWidth, this.filled, this.showNames});
   }

   public void onTick() {
      if (mc.field_71441_e != null && mc.field_71439_g != null) {
         ++this.scanTick;
         if (this.scanTick % 5 == 0) {
            this.oresToRender.clear();
            int rangeI = (int)this.range.getValue();
            BlockPos playerPos = mc.field_71439_g.func_233580_cy_();

            for(int x = -rangeI; x <= rangeI; ++x) {
               for(int y = -rangeI; y <= rangeI; ++y) {
                  for(int z = -rangeI; z <= rangeI; ++z) {
                     BlockPos pos = playerPos.func_177982_a(x, y, z);
                     Block block = mc.field_71441_e.func_180495_p(pos).func_177230_c();
                     if (this.shouldShow(block)) {
                        this.oresToRender.add(pos);
                     }
                  }
               }
            }

         }
      }
   }

   private boolean shouldShow(Block block) {
      if (block == Blocks.field_150482_ag && this.diamonds.getValue()) {
         return true;
      } else if (block == Blocks.field_150484_ah && this.diamonds.getValue()) {
         return true;
      } else if (block == Blocks.field_150352_o && this.gold.getValue()) {
         return true;
      } else if (block == Blocks.field_150340_R && this.gold.getValue()) {
         return true;
      } else if (block == Blocks.field_150366_p && this.iron.getValue()) {
         return true;
      } else if (block == Blocks.field_150339_S && this.iron.getValue()) {
         return true;
      } else if (block == Blocks.field_150365_q && this.coal.getValue()) {
         return true;
      } else if (block == Blocks.field_150412_bA && this.emeralds.getValue()) {
         return true;
      } else if (block == Blocks.field_150475_bE && this.emeralds.getValue()) {
         return true;
      } else if (block == Blocks.field_150369_x && this.lapis.getValue()) {
         return true;
      } else if (block == Blocks.field_150450_ax && this.redstone.getValue()) {
         return true;
      } else if (block == Blocks.field_235334_I_ && this.gold.getValue()) {
         return true;
      } else {
         return block == Blocks.field_235398_nh_ && this.ancient.getValue();
      }
   }

   @SubscribeEvent
   public void onRenderWorld(RenderWorldLastEvent event) {
      if (mc.field_71441_e != null && mc.field_71439_g != null && !this.oresToRender.isEmpty()) {
         MatrixStack matrix = event.getMatrixStack();
         Vector3d camPos = mc.field_71460_t.func_215316_n().func_216785_c();
         RenderSystem.enableBlend();
         RenderSystem.disableDepthTest();
         RenderSystem.disableLighting();
         RenderSystem.disableTexture();
         RenderSystem.lineWidth((float)this.lineWidth.getValue());
         Iterator var4 = this.oresToRender.iterator();

         while(var4.hasNext()) {
            BlockPos pos = (BlockPos)var4.next();
            Block block = mc.field_71441_e.func_180495_p(pos).func_177230_c();
            float[] color = this.getColor(block);
            float r = color[0];
            float g = color[1];
            float b = color[2];
            double x = (double)pos.func_177958_n() - camPos.field_72450_a;
            double y = (double)pos.func_177956_o() - camPos.field_72448_b;
            double z = (double)pos.func_177952_p() - camPos.field_72449_c;
            matrix.func_227860_a_();
            Matrix4f matrix4f = matrix.func_227866_c_().func_227870_a_();
            Tessellator tessellator = Tessellator.func_178181_a();
            BufferBuilder buffer = tessellator.func_178180_c();
            if (this.filled.getValue()) {
               buffer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
               buffer.func_227888_a_(matrix4f, (float)x, (float)y, (float)z).func_227885_a_(r, g, b, 0.3F).func_181675_d();
               buffer.func_227888_a_(matrix4f, (float)x + 1.0F, (float)y, (float)z).func_227885_a_(r, g, b, 0.3F).func_181675_d();
               buffer.func_227888_a_(matrix4f, (float)x + 1.0F, (float)y, (float)z + 1.0F).func_227885_a_(r, g, b, 0.3F).func_181675_d();
               buffer.func_227888_a_(matrix4f, (float)x, (float)y, (float)z + 1.0F).func_227885_a_(r, g, b, 0.3F).func_181675_d();
               buffer.func_227888_a_(matrix4f, (float)x, (float)y + 1.0F, (float)z).func_227885_a_(r, g, b, 0.3F).func_181675_d();
               buffer.func_227888_a_(matrix4f, (float)x + 1.0F, (float)y + 1.0F, (float)z).func_227885_a_(r, g, b, 0.3F).func_181675_d();
               buffer.func_227888_a_(matrix4f, (float)x + 1.0F, (float)y + 1.0F, (float)z + 1.0F).func_227885_a_(r, g, b, 0.3F).func_181675_d();
               buffer.func_227888_a_(matrix4f, (float)x, (float)y + 1.0F, (float)z + 1.0F).func_227885_a_(r, g, b, 0.3F).func_181675_d();
               tessellator.func_78381_a();
            }

            buffer.func_181668_a(1, DefaultVertexFormats.field_181706_f);
            buffer.func_227888_a_(matrix4f, (float)x, (float)y, (float)z).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 1.0F, (float)y, (float)z).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 1.0F, (float)y, (float)z).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 1.0F, (float)y, (float)z + 1.0F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 1.0F, (float)y, (float)z + 1.0F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x, (float)y, (float)z + 1.0F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x, (float)y, (float)z + 1.0F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x, (float)y, (float)z).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x, (float)y + 1.0F, (float)z).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 1.0F, (float)y + 1.0F, (float)z).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 1.0F, (float)y + 1.0F, (float)z).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 1.0F, (float)y + 1.0F, (float)z + 1.0F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 1.0F, (float)y + 1.0F, (float)z + 1.0F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x, (float)y + 1.0F, (float)z + 1.0F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x, (float)y + 1.0F, (float)z + 1.0F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x, (float)y + 1.0F, (float)z).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x, (float)y, (float)z).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x, (float)y + 1.0F, (float)z).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 1.0F, (float)y, (float)z).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 1.0F, (float)y + 1.0F, (float)z).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 1.0F, (float)y, (float)z + 1.0F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 1.0F, (float)y + 1.0F, (float)z + 1.0F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x, (float)y, (float)z + 1.0F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x, (float)y + 1.0F, (float)z + 1.0F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            tessellator.func_78381_a();
            matrix.func_227865_b_();
         }

         RenderSystem.enableDepthTest();
         RenderSystem.enableTexture();
         RenderSystem.disableBlend();
      }
   }

   private float[] getColor(Block block) {
      if (block != Blocks.field_150482_ag && block != Blocks.field_150484_ah) {
         if (block != Blocks.field_150352_o && block != Blocks.field_150340_R && block != Blocks.field_235334_I_) {
            if (block != Blocks.field_150366_p && block != Blocks.field_150339_S) {
               if (block == Blocks.field_150365_q) {
                  return new float[]{0.2F, 0.2F, 0.2F};
               } else if (block != Blocks.field_150412_bA && block != Blocks.field_150475_bE) {
                  if (block == Blocks.field_150369_x) {
                     return new float[]{0.2F, 0.3F, 0.9F};
                  } else if (block == Blocks.field_150450_ax) {
                     return new float[]{0.8F, 0.0F, 0.0F};
                  } else {
                     return block == Blocks.field_235398_nh_ ? new float[]{0.3F, 0.2F, 0.1F} : new float[]{1.0F, 1.0F, 1.0F};
                  }
               } else {
                  return new float[]{0.0F, 1.0F, 0.3F};
               }
            } else {
               return new float[]{0.9F, 0.9F, 0.9F};
            }
         } else {
            return new float[]{1.0F, 0.85F, 0.0F};
         }
      } else {
         return new float[]{0.0F, 0.8F, 1.0F};
      }
   }
}
