package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NoFall extends Module {
   public NoFall() {
      super("NoFall", "No fall damage", Module.Category.MOVEMENT, 74);
   }

   @SubscribeEvent
   public void onTick(ClientTickEvent e) {
      if (e.phase == Phase.END && mc.field_71439_g != null) {
         if (mc.field_71439_g.field_70143_R > 2.0F) {
            mc.field_71439_g.field_71174_a.func_147297_a(new CPlayerPacket(true));
         }

      }
   }
}
