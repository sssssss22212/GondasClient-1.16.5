package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import java.util.Random;
import net.minecraft.network.play.client.CPlayerPacket.PositionPacket;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Criticals extends Module {
   private Setting.Mode mode = new Setting.Mode("Mode", new String[]{"Packet", "Jump", "MiniJump", "NCP", "AAC", "Matrix", "Grim"});
   private Setting.Boolean onlyOnGround = new Setting.Boolean("OnlyGround", true);
   private Setting.Boolean movingOnly = new Setting.Boolean("MovingOnly", false);
   private Setting.Double delay = new Setting.Double("Delay", 0.0D, 0.0D, 500.0D);
   private Random random = new Random();
   private long lastCrit = 0L;

   public Criticals() {
      super("Criticals", "Auto critical hits", Module.Category.COMBAT);
      this.addSettings(new Setting[]{this.mode, this.onlyOnGround, this.movingOnly, this.delay});
   }

   @SubscribeEvent
   public void onAttack(AttackEntityEvent event) {
      if (mc.field_71439_g != null && event.getTarget() != null) {
         if (!((double)(System.currentTimeMillis() - this.lastCrit) < this.delay.getValue())) {
            if (!this.onlyOnGround.getValue() || mc.field_71439_g.func_233570_aj_()) {
               if (this.movingOnly.getValue()) {
                  double dx = mc.field_71439_g.func_226277_ct_() - mc.field_71439_g.field_70169_q;
                  double dz = mc.field_71439_g.func_226281_cx_() - mc.field_71439_g.field_70166_s;
                  if (dx * dx + dz * dz < 0.01D) {
                     return;
                  }
               }

               String m = this.mode.getValue();
               if (m.equals("Packet")) {
                  this.doPacketCrit();
               } else if (m.equals("Jump")) {
                  this.doJumpCrit();
               } else if (m.equals("MiniJump")) {
                  this.doMiniJumpCrit();
               } else if (m.equals("NCP")) {
                  this.doNCPCrit();
               } else if (m.equals("AAC")) {
                  this.doAACCrit();
               } else if (m.equals("Matrix")) {
                  this.doMatrixCrit();
               } else if (m.equals("Grim")) {
                  this.doGrimCrit();
               }

               this.lastCrit = System.currentTimeMillis();
            }
         }
      }
   }

   private void doPacketCrit() {
      double x = mc.field_71439_g.func_226277_ct_();
      double y = mc.field_71439_g.func_226278_cu_();
      double z = mc.field_71439_g.func_226281_cx_();
      mc.field_71439_g.field_71174_a.func_147297_a(new PositionPacket(x, y + 0.0625D, z, false));
      mc.field_71439_g.field_71174_a.func_147297_a(new PositionPacket(x, y, z, false));
   }

   private void doJumpCrit() {
      mc.field_71439_g.func_70664_aZ();
   }

   private void doMiniJumpCrit() {
      mc.field_71439_g.func_70024_g(0.0D, 0.1D, 0.0D);
   }

   private void doNCPCrit() {
      double x = mc.field_71439_g.func_226277_ct_();
      double y = mc.field_71439_g.func_226278_cu_();
      double z = mc.field_71439_g.func_226281_cx_();
      mc.field_71439_g.field_71174_a.func_147297_a(new PositionPacket(x, y + 0.11D, z, false));
      mc.field_71439_g.field_71174_a.func_147297_a(new PositionPacket(x, y + 0.1100013579D, z, false));
      mc.field_71439_g.field_71174_a.func_147297_a(new PositionPacket(x, y + 1.3579E-6D, z, false));
   }

   private void doAACCrit() {
      double x = mc.field_71439_g.func_226277_ct_();
      double y = mc.field_71439_g.func_226278_cu_();
      double z = mc.field_71439_g.func_226281_cx_();
      mc.field_71439_g.field_71174_a.func_147297_a(new PositionPacket(x, y + 0.42D, z, false));
      mc.field_71439_g.field_71174_a.func_147297_a(new PositionPacket(x, y + 0.75D, z, false));
   }

   private void doMatrixCrit() {
      double x = mc.field_71439_g.func_226277_ct_();
      double y = mc.field_71439_g.func_226278_cu_();
      double z = mc.field_71439_g.func_226281_cx_();
      mc.field_71439_g.field_71174_a.func_147297_a(new PositionPacket(x, y + 0.2D, z, false));
      mc.field_71439_g.field_71174_a.func_147297_a(new PositionPacket(x, y + 0.12D, z, false));
   }

   private void doGrimCrit() {
      double x = mc.field_71439_g.func_226277_ct_();
      double y = mc.field_71439_g.func_226278_cu_();
      double z = mc.field_71439_g.func_226281_cx_();
      mc.field_71439_g.field_71174_a.func_147297_a(new PositionPacket(x, y + 0.06D, z, false));
      mc.field_71439_g.field_71174_a.func_147297_a(new PositionPacket(x, y, z, false));
   }
}
