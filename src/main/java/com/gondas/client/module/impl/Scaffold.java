package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import java.util.Random;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class Scaffold extends Module {
   private Setting.Mode mode = new Setting.Mode("Mode", new String[]{"Normal", "Expand", "Telly", "Legit"});
   private Setting.Boolean tower = new Setting.Boolean("Tower", true);
   private Setting.Boolean rotate = new Setting.Boolean("Rotate", true);
   private Setting.Boolean swing = new Setting.Boolean("Swing", true);
   private Setting.Boolean sneak = new Setting.Boolean("Sneak", false);
   private Setting.Int expand = new Setting.Int("Expand", 1, 1, 6);
   private Setting.Boolean keepY = new Setting.Boolean("KeepY", false);
   private Setting.Boolean sameY = new Setting.Boolean("SameY", false);
   private Setting.Boolean telly = new Setting.Boolean("Telly", false);
   private Setting.Double timer = new Setting.Double("Timer", 1.0D, 0.1D, 2.0D);
   private double startY = 0.0D;
   private int lastSlot = -1;
   private int ticks = 0;
   private boolean shouldSneak = false;
   private Random random = new Random();

   public Scaffold() {
      super("Scaffold", "Auto bridge builder", Module.Category.PLAYER);
      this.addSettings(new Setting[]{this.mode, this.tower, this.rotate, this.swing, this.sneak, this.expand, this.keepY, this.sameY, this.telly, this.timer});
   }

   public void onEnable() {
      super.onEnable();
      if (mc.field_71439_g != null) {
         this.startY = mc.field_71439_g.func_226278_cu_();
         this.lastSlot = mc.field_71439_g.field_71071_by.field_70461_c;
      }

      this.ticks = 0;
      this.shouldSneak = false;
   }

   public void onDisable() {
      super.onDisable();
      if (mc.field_71439_g != null) {
         mc.field_71439_g.func_226284_e_(false);
         if (this.lastSlot != -1) {
            mc.field_71439_g.field_71071_by.field_70461_c = this.lastSlot;
         }
      }

   }

   public void onTick() {
      if (mc.field_71439_g != null && mc.field_71441_e != null) {
         ++this.ticks;
         int blockSlot = this.findBlockSlot();
         if (blockSlot != -1) {
            if (mc.field_71439_g.field_71071_by.field_70461_c != blockSlot) {
               mc.field_71439_g.field_71071_by.field_70461_c = blockSlot;
            }

            if (this.tower.getValue() && mc.field_71474_y.field_74314_A.func_151470_d()) {
               this.doTower(blockSlot);
            } else {
               BlockPos placePos = this.getPlacePos();
               if (placePos != null) {
                  Direction side = this.getPlaceSide(placePos);
                  if (side != null) {
                     BlockPos neighbor = placePos.func_177972_a(side);
                     if (this.rotate.getValue()) {
                        float[] rot = this.getRotations(neighbor, side);
                        mc.field_71439_g.field_70177_z = rot[0];
                        mc.field_71439_g.field_70125_A = rot[1];
                     }

                     if (this.sneak.getValue()) {
                        BlockPos under = mc.field_71439_g.func_233580_cy_().func_177977_b();
                        if (mc.field_71441_e.func_175623_d(under)) {
                           mc.field_71439_g.func_226284_e_(true);
                           this.shouldSneak = true;
                        } else if (this.shouldSneak) {
                           mc.field_71439_g.func_226284_e_(false);
                           this.shouldSneak = false;
                        }
                     }

                     this.placeBlock(neighbor, side);
                  }
               }
            }
         }
      }
   }

   private BlockPos getPlacePos() {
      BlockPos playerPos = mc.field_71439_g.func_233580_cy_();
      double dx = mc.field_71439_g.func_226277_ct_() - mc.field_71439_g.field_70169_q;
      double dz = mc.field_71439_g.func_226281_cx_() - mc.field_71439_g.field_70166_s;
      double y = mc.field_71439_g.func_226278_cu_() - 1.0D;
      if (this.keepY.getValue() || this.sameY.getValue()) {
         y = this.startY - 1.0D;
      }

      BlockPos under = new BlockPos((double)playerPos.func_177958_n(), y, (double)playerPos.func_177952_p());
      if (mc.field_71441_e.func_175623_d(under) && this.canPlaceAt(under)) {
         return under;
      } else {
         int expandDist = this.expand.getValue();
         String m = this.mode.getValue();
         if (!m.equals("Expand") && !m.equals("Legit")) {
            BlockPos pos = this.getPositionAhead(1, y);
            if (pos != null && mc.field_71441_e.func_175623_d(pos) && this.canPlaceAt(pos)) {
               return pos;
            }
         } else {
            for(int i = 1; i <= expandDist; ++i) {
               BlockPos pos = this.getPositionAhead(i, y);
               if (pos != null && mc.field_71441_e.func_175623_d(pos) && this.canPlaceAt(pos)) {
                  return pos;
               }
            }
         }

         return null;
      }
   }

   private BlockPos getPositionAhead(int dist, double y) {
      double yaw = Math.toRadians((double)mc.field_71439_g.field_70177_z);
      double moveX = 0.0D;
      double moveZ = 0.0D;
      if (mc.field_71474_y.field_74351_w.func_151470_d()) {
         moveX -= Math.sin(yaw);
         moveZ += Math.cos(yaw);
      }

      if (mc.field_71474_y.field_74368_y.func_151470_d()) {
         moveX += Math.sin(yaw);
         moveZ -= Math.cos(yaw);
      }

      if (mc.field_71474_y.field_74370_x.func_151470_d()) {
         moveX += Math.cos(yaw);
         moveZ += Math.sin(yaw);
      }

      if (mc.field_71474_y.field_74366_z.func_151470_d()) {
         moveX -= Math.cos(yaw);
         moveZ -= Math.sin(yaw);
      }

      if (moveX == 0.0D && moveZ == 0.0D) {
         return null;
      } else {
         double len = Math.sqrt(moveX * moveX + moveZ * moveZ);
         moveX /= len;
         moveZ /= len;
         double x = mc.field_71439_g.func_226277_ct_() + moveX * (double)dist;
         double z = mc.field_71439_g.func_226281_cx_() + moveZ * (double)dist;
         return new BlockPos(x, y, z);
      }
   }

   private boolean canPlaceAt(BlockPos pos) {
      Direction[] var2 = Direction.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Direction dir = var2[var4];
         BlockPos neighbor = pos.func_177972_a(dir);
         if (!mc.field_71441_e.func_175623_d(neighbor)) {
            return true;
         }
      }

      return false;
   }

   private Direction getPlaceSide(BlockPos pos) {
      Direction[] var2 = Direction.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Direction dir = var2[var4];
         BlockPos neighbor = pos.func_177972_a(dir);
         if (!mc.field_71441_e.func_175623_d(neighbor)) {
            return dir;
         }
      }

      return null;
   }

   private void placeBlock(BlockPos neighbor, Direction side) {
      Vector3d hitVec = Vector3d.func_237491_b_(neighbor).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e(Vector3d.func_237491_b_(side.func_176730_m()).func_186678_a(0.5D));
      BlockRayTraceResult rayTraceResult = new BlockRayTraceResult(hitVec, side.func_176734_d(), neighbor, false);
      mc.field_71442_b.func_217292_a(mc.field_71439_g, mc.field_71441_e, Hand.MAIN_HAND, rayTraceResult);
      if (this.swing.getValue()) {
         mc.field_71439_g.func_184609_a(Hand.MAIN_HAND);
      }

   }

   private void doTower(int blockSlot) {
      if (mc.field_71439_g.func_233570_aj_()) {
         mc.field_71439_g.func_70664_aZ();
      }

      BlockPos under = mc.field_71439_g.func_233580_cy_().func_177977_b();
      if (mc.field_71441_e.func_175623_d(under)) {
         Direction side = this.getPlaceSide(under);
         if (side != null) {
            BlockPos neighbor = under.func_177972_a(side);
            this.placeBlock(neighbor, side);
         }
      }

   }

   private int findBlockSlot() {
      ItemStack current = mc.field_71439_g.func_184614_ca();
      if (current.func_77973_b() instanceof BlockItem) {
         return mc.field_71439_g.field_71071_by.field_70461_c;
      } else {
         for(int i = 0; i < 9; ++i) {
            ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack.func_77973_b() instanceof BlockItem) {
               return i;
            }
         }

         return -1;
      }
   }

   private float[] getRotations(BlockPos pos, Direction side) {
      double x = (double)pos.func_177958_n() + 0.5D - mc.field_71439_g.func_226277_ct_();
      double y = (double)pos.func_177956_o() + 0.5D - mc.field_71439_g.func_226280_cw_();
      double z = (double)pos.func_177952_p() + 0.5D - mc.field_71439_g.func_226281_cx_();
      switch(side) {
      case UP:
         y += 0.5D;
         break;
      case DOWN:
         y -= 0.5D;
         break;
      case NORTH:
         z -= 0.5D;
         break;
      case SOUTH:
         z += 0.5D;
         break;
      case WEST:
         x -= 0.5D;
         break;
      case EAST:
         x += 0.5D;
      }

      double dist = Math.sqrt(x * x + z * z);
      float yaw = (float)(Math.toDegrees(Math.atan2(z, x)) - 90.0D);
      float pitch = (float)(-Math.toDegrees(Math.atan2(y, dist)));
      return new float[]{yaw, MathHelper.func_76131_a(pitch, -90.0F, 90.0F)};
   }
}
