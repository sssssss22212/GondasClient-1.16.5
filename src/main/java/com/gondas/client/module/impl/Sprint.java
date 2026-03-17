package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Sprint extends Module {
   public Sprint() {
      super("Sprint", "Auto sprint", Module.Category.MOVEMENT, 75);
   }

   @SubscribeEvent
   public void onTick(ClientTickEvent event) {
      if (mc.field_71439_g != null && event.phase == Phase.END) {
         if (mc.field_71439_g.field_71158_b.field_192832_b > 0.0F && !mc.field_71439_g.func_70051_ag()) {
            mc.field_71439_g.func_70031_b(true);
         }

      }
   }
}
