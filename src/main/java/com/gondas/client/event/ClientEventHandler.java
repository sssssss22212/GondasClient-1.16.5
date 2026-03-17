package com.gondas.client.event;

import com.gondas.client.core.GondasClient;
import com.gondas.client.module.Module;
import com.gondas.client.module.ModuleManager;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * ClientEventHandler - Handles client-side events
 * Improved with performance optimizations
 */
public class ClientEventHandler {

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        if (event.phase == Phase.END) {
            // Get enabled modules once per tick
            for (Module m : ModuleManager.getModules()) {
                if (m.isToggled()) {
                    try {
                        m.onTick();
                    } catch (Exception e) {
                        GondasClient.LOGGER.error("Error in module " + m.getName() + ": " + e.getMessage());
                    }
                }
            }
        }
    }
}
