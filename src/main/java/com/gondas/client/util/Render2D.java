package com.gondas.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class Render2D {
   public static void drawRect(double x, double y, double width, double height, int color) {
      float a = (float)(color >> 24 & 255) / 255.0F;
      float r = (float)(color >> 16 & 255) / 255.0F;
      float g = (float)(color >> 8 & 255) / 255.0F;
      float b = (float)(color & 255) / 255.0F;
      RenderSystem.disableTexture();
      RenderSystem.enableBlend();
      RenderSystem.disableLighting();
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder buffer = tessellator.func_178180_c();
      buffer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
      buffer.func_225582_a_(x, y + height, 0.0D).func_227885_a_(r, g, b, a).func_181675_d();
      buffer.func_225582_a_(x + width, y + height, 0.0D).func_227885_a_(r, g, b, a).func_181675_d();
      buffer.func_225582_a_(x + width, y, 0.0D).func_227885_a_(r, g, b, a).func_181675_d();
      buffer.func_225582_a_(x, y, 0.0D).func_227885_a_(r, g, b, a).func_181675_d();
      tessellator.func_78381_a();
      RenderSystem.enableTexture();
      RenderSystem.disableBlend();
   }

   public static void drawRectOutline(double x, double y, double width, double height, int color, float lineWidth) {
      float a = (float)(color >> 24 & 255) / 255.0F;
      float r = (float)(color >> 16 & 255) / 255.0F;
      float g = (float)(color >> 8 & 255) / 255.0F;
      float b = (float)(color & 255) / 255.0F;
      RenderSystem.disableTexture();
      RenderSystem.enableBlend();
      RenderSystem.disableLighting();
      GL11.glLineWidth(lineWidth);
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder buffer = tessellator.func_178180_c();
      buffer.func_181668_a(2, DefaultVertexFormats.field_181706_f);
      buffer.func_225582_a_(x, y, 0.0D).func_227885_a_(r, g, b, a).func_181675_d();
      buffer.func_225582_a_(x + width, y, 0.0D).func_227885_a_(r, g, b, a).func_181675_d();
      buffer.func_225582_a_(x + width, y + height, 0.0D).func_227885_a_(r, g, b, a).func_181675_d();
      buffer.func_225582_a_(x, y + height, 0.0D).func_227885_a_(r, g, b, a).func_181675_d();
      tessellator.func_78381_a();
      RenderSystem.enableTexture();
      RenderSystem.disableBlend();
   }

   public static void drawOutline(float minX, float minY, float maxX, float maxY, float lineWidth, int color) {
      drawRectOutline((double)minX, (double)minY, (double)(maxX - minX), (double)(maxY - minY), color, lineWidth);
   }

   public static void drawLine(double x1, double y1, double x2, double y2, int color, float width) {
      float a = (float)(color >> 24 & 255) / 255.0F;
      float r = (float)(color >> 16 & 255) / 255.0F;
      float g = (float)(color >> 8 & 255) / 255.0F;
      float b = (float)(color & 255) / 255.0F;
      RenderSystem.disableTexture();
      RenderSystem.enableBlend();
      GL11.glLineWidth(width);
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder buffer = tessellator.func_178180_c();
      buffer.func_181668_a(1, DefaultVertexFormats.field_181706_f);
      buffer.func_225582_a_(x1, y1, 0.0D).func_227885_a_(r, g, b, a).func_181675_d();
      buffer.func_225582_a_(x2, y2, 0.0D).func_227885_a_(r, g, b, a).func_181675_d();
      tessellator.func_78381_a();
      RenderSystem.enableTexture();
      RenderSystem.disableBlend();
   }
}
