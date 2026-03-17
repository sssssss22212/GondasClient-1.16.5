package com.gondas.client.core;

import com.gondas.client.gui.ClickGUI;
import com.gondas.client.module.Module;
import com.gondas.client.module.ModuleManager;
import com.gondas.client.module.impl.ClickGUIModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * KeyBindingHandler - Handles key bindings for the mod
 * Improved with mobile launcher compatibility
 */
public class KeyBindingHandler {
    // Default key: RIGHT_SHIFT (344)
    public static KeyBinding clickGUI = new KeyBinding("key.gondas.clickgui", 344, "key.categories.gondas");

    public static void register() {
        ClientRegistry.registerKeyBinding(clickGUI);
        MinecraftForge.EVENT_BUS.register(new KeyBindingHandler());
        GondasClient.LOGGER.info("Key bindings registered");
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        // Open ClickGUI
        if (clickGUI.func_151468_f()) {
            Minecraft.func_71410_x().func_147108_a(new ClickGUI());
        }

        // Toggle modules by keybind
        if (event.getAction() == 1) {
            int key = event.getKey();
            Module m = ModuleManager.getByKey(key);
            if (m != null && !(m instanceof ClickGUIModule)) {
                m.toggle();
            }
        }
    }
}
