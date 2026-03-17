package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AutoSprint extends Module {
   public AutoSprint() {
      super("AutoSprint", "Auto sprint", Module.Category.PLAYER, 73);
   }

   @SubscribeEvent
   public void onTick(ClientTickEvent e) {
      if (e.phase == Phase.END && mc.field_71439_g != null) {
         if (mc.field_71439_g.field_71158_b.field_187255_c && !mc.field_71439_g.func_70051_ag()) {
            mc.field_71439_g.func_70031_b(true);
         }

      }
   }
}
