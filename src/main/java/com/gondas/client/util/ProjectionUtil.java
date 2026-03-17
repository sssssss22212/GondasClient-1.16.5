package com.gondas.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.util.math.vector.Vector3d;

public class ProjectionUtil {
   private static final Minecraft mc = Minecraft.func_71410_x();

   public static double[] project(double x, double y, double z) {
      if (mc.field_71439_g == null) {
         return null;
      } else {
         ActiveRenderInfo camera = mc.field_71460_t.func_215316_n();
         Vector3d camPos = camera.func_216785_c();
         double relX = x - camPos.field_72450_a;
         double relY = y - camPos.field_72448_b;
         double relZ = z - camPos.field_72449_c;
         double dist = Math.sqrt(relX * relX + relY * relY + relZ * relZ);
         if (dist > 500.0D) {
            return null;
         } else {
            float yaw = mc.field_71439_g.field_70177_z;
            float pitch = mc.field_71439_g.field_70125_A;
            float cosYaw = (float)Math.cos(Math.toRadians((double)(yaw + 180.0F)));
            float sinYaw = (float)Math.sin(Math.toRadians((double)(yaw + 180.0F)));
            float cosPitch = (float)Math.cos(Math.toRadians((double)(-pitch)));
            float sinPitch = (float)Math.sin(Math.toRadians((double)(-pitch)));
            float x1 = (float)(relX * (double)cosYaw - relZ * (double)sinYaw);
            float z1 = (float)(relX * (double)sinYaw + relZ * (double)cosYaw);
            float y1 = (float)(relY * (double)cosPitch - (double)(z1 * sinPitch));
            float z2 = (float)(relY * (double)sinPitch + (double)(z1 * cosPitch));
            if (z2 > 0.0F) {
               return null;
            } else {
               int screenWidth = mc.func_228018_at_().func_198107_o();
               int screenHeight = mc.func_228018_at_().func_198087_p();
               double fov = mc.field_71474_y.field_74334_X;
               double scale = (double)screenHeight / 2.0D / Math.tan(Math.toRadians(fov / 2.0D));
               double screenX = (double)screenWidth / 2.0D + (double)(x1 / -z2) * scale;
               double screenY = (double)screenHeight / 2.0D - (double)(y1 / -z2) * scale;
               return new double[]{screenX, screenY};
            }
         }
      }
   }

   public static boolean isOnScreen(double x, double y) {
      int w = mc.func_228018_at_().func_198107_o();
      int h = mc.func_228018_at_().func_198087_p();
      return x >= -50.0D && x <= (double)(w + 50) && y >= -50.0D && y <= (double)(h + 50);
   }
}
