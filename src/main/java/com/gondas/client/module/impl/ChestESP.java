package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Iterator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.EnderChestTileEntity;
import net.minecraft.tileentity.ShulkerBoxTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TrappedChestTileEntity;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChestESP extends Module {
   private Setting.Boolean chests = new Setting.Boolean("Chests", true);
   private Setting.Boolean enderChests = new Setting.Boolean("EnderChests", true);
   private Setting.Boolean shulkers = new Setting.Boolean("Shulkers", true);
   private Setting.Boolean trapped = new Setting.Boolean("Trapped", true);
   private Setting.Double lineWidth = new Setting.Double("LineWidth", 2.0D, 1.0D, 5.0D);
   private Setting.Boolean filled = new Setting.Boolean("Filled", true);
   private Setting.Double range = new Setting.Double("Range", 100.0D, 10.0D, 200.0D);

   public ChestESP() {
      super("ChestESP", "Highlight storage blocks", Module.Category.RENDER);
      this.addSettings(new Setting[]{this.chests, this.enderChests, this.shulkers, this.trapped, this.lineWidth, this.filled, this.range});
   }

   @SubscribeEvent
   public void onRender(RenderWorldLastEvent event) {
      if (mc.field_71441_e != null && mc.field_71439_g != null) {
         MatrixStack matrix = event.getMatrixStack();
         Vector3d camPos = mc.field_71460_t.func_215316_n().func_216785_c();
         RenderSystem.enableBlend();
         RenderSystem.disableDepthTest();
         RenderSystem.disableLighting();
         RenderSystem.disableTexture();
         RenderSystem.lineWidth((float)this.lineWidth.getValue());
         Iterator var4 = mc.field_71441_e.field_147482_g.iterator();

         while(true) {
            TileEntity te;
            double dist;
            do {
               if (!var4.hasNext()) {
                  RenderSystem.enableDepthTest();
                  RenderSystem.enableTexture();
                  RenderSystem.disableBlend();
                  return;
               }

               te = (TileEntity)var4.next();
               dist = mc.field_71439_g.func_70092_e((double)te.func_174877_v().func_177958_n(), (double)te.func_174877_v().func_177956_o(), (double)te.func_174877_v().func_177952_p());
            } while(dist > this.range.getValue() * this.range.getValue());

            float r = 0.0F;
            float g = 0.8F;
            float b = 0.8F;
            float a = 0.5F;
            boolean shouldRender = false;
            if (te instanceof ChestTileEntity && !(te instanceof TrappedChestTileEntity) && this.chests.getValue()) {
               r = 0.0F;
               g = 0.8F;
               b = 0.8F;
               shouldRender = true;
            } else if (te instanceof TrappedChestTileEntity && this.trapped.getValue()) {
               r = 0.8F;
               g = 0.8F;
               b = 0.0F;
               shouldRender = true;
            } else if (te instanceof EnderChestTileEntity && this.enderChests.getValue()) {
               r = 0.8F;
               g = 0.0F;
               b = 0.8F;
               shouldRender = true;
            } else if (te instanceof ShulkerBoxTileEntity && this.shulkers.getValue()) {
               r = 0.8F;
               g = 0.2F;
               b = 0.8F;
               shouldRender = true;
            }

            if (shouldRender) {
               double x = (double)te.func_174877_v().func_177958_n() - camPos.field_72450_a;
               double y = (double)te.func_174877_v().func_177956_o() - camPos.field_72448_b;
               double z = (double)te.func_174877_v().func_177952_p() - camPos.field_72449_c;
               matrix.func_227860_a_();
               Matrix4f matrix4f = matrix.func_227866_c_().func_227870_a_();
               Tessellator tessellator = Tessellator.func_178181_a();
               BufferBuilder buffer = tessellator.func_178180_c();
               if (this.filled.getValue()) {
                  buffer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
                  buffer.func_227888_a_(matrix4f, (float)x, (float)y, (float)z).func_227885_a_(r, g, b, a * 0.5F).func_181675_d();
                  buffer.func_227888_a_(matrix4f, (float)x + 1.0F, (float)y, (float)z).func_227885_a_(r, g, b, a * 0.5F).func_181675_d();
                  buffer.func_227888_a_(matrix4f, (float)x + 1.0F, (float)y, (float)z + 1.0F).func_227885_a_(r, g, b, a * 0.5F).func_181675_d();
                  buffer.func_227888_a_(matrix4f, (float)x, (float)y, (float)z + 1.0F).func_227885_a_(r, g, b, a * 0.5F).func_181675_d();
                  buffer.func_227888_a_(matrix4f, (float)x, (float)y + 1.0F, (float)z).func_227885_a_(r, g, b, a * 0.5F).func_181675_d();
                  buffer.func_227888_a_(matrix4f, (float)x + 1.0F, (float)y + 1.0F, (float)z).func_227885_a_(r, g, b, a * 0.5F).func_181675_d();
                  buffer.func_227888_a_(matrix4f, (float)x + 1.0F, (float)y + 1.0F, (float)z + 1.0F).func_227885_a_(r, g, b, a * 0.5F).func_181675_d();
                  buffer.func_227888_a_(matrix4f, (float)x, (float)y + 1.0F, (float)z + 1.0F).func_227885_a_(r, g, b, a * 0.5F).func_181675_d();
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
         }
      }
   }
}
