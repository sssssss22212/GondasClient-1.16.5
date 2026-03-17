package com.gondas.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Vector3d;

public class Render3D {
   public static void drawBox(Vector3d pos, double width, double height, float r, float g, float b, float a) {
      Minecraft mc = Minecraft.func_71410_x();
      if (mc.field_71460_t != null) {
         ActiveRenderInfo camera = mc.field_71460_t.func_215316_n();
         Vector3d camPos = camera.func_216785_c();
         double x = pos.field_72450_a - camPos.field_72450_a;
         double y = pos.field_72448_b - camPos.field_72448_b;
         double z = pos.field_72449_c - camPos.field_72449_c;
         RenderSystem.enableBlend();
         RenderSystem.disableDepthTest();
         RenderSystem.disableLighting();
         RenderSystem.disableTexture();
         RenderSystem.lineWidth(2.0F);
         Tessellator tessellator = Tessellator.func_178181_a();
         BufferBuilder buffer = tessellator.func_178180_c();
         buffer.func_181668_a(3, DefaultVertexFormats.field_181706_f);
         buffer.func_225582_a_(x, y, z).func_227885_a_(r, g, b, 1.0F).func_181675_d();
         buffer.func_225582_a_(x + width, y, z).func_227885_a_(r, g, b, 1.0F).func_181675_d();
         buffer.func_225582_a_(x + width, y, z + width).func_227885_a_(r, g, b, 1.0F).func_181675_d();
         buffer.func_225582_a_(x, y, z + width).func_227885_a_(r, g, b, 1.0F).func_181675_d();
         buffer.func_225582_a_(x, y, z).func_227885_a_(r, g, b, 1.0F).func_181675_d();
         tessellator.func_78381_a();
         buffer.func_181668_a(3, DefaultVertexFormats.field_181706_f);
         buffer.func_225582_a_(x, y + height, z).func_227885_a_(r, g, b, 1.0F).func_181675_d();
         buffer.func_225582_a_(x + width, y + height, z).func_227885_a_(r, g, b, 1.0F).func_181675_d();
         buffer.func_225582_a_(x + width, y + height, z + width).func_227885_a_(r, g, b, 1.0F).func_181675_d();
         buffer.func_225582_a_(x, y + height, z + width).func_227885_a_(r, g, b, 1.0F).func_181675_d();
         buffer.func_225582_a_(x, y + height, z).func_227885_a_(r, g, b, 1.0F).func_181675_d();
         tessellator.func_78381_a();
         buffer.func_181668_a(1, DefaultVertexFormats.field_181706_f);
         buffer.func_225582_a_(x, y, z).func_227885_a_(r, g, b, 1.0F).func_181675_d();
         buffer.func_225582_a_(x, y + height, z).func_227885_a_(r, g, b, 1.0F).func_181675_d();
         buffer.func_225582_a_(x + width, y, z).func_227885_a_(r, g, b, 1.0F).func_181675_d();
         buffer.func_225582_a_(x + width, y + height, z).func_227885_a_(r, g, b, 1.0F).func_181675_d();
         buffer.func_225582_a_(x + width, y, z + width).func_227885_a_(r, g, b, 1.0F).func_181675_d();
         buffer.func_225582_a_(x + width, y + height, z + width).func_227885_a_(r, g, b, 1.0F).func_181675_d();
         buffer.func_225582_a_(x, y, z + width).func_227885_a_(r, g, b, 1.0F).func_181675_d();
         buffer.func_225582_a_(x, y + height, z + width).func_227885_a_(r, g, b, 1.0F).func_181675_d();
         tessellator.func_78381_a();
         RenderSystem.enableDepthTest();
         RenderSystem.enableTexture();
         RenderSystem.disableBlend();
      }
   }
}
