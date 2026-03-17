package com.gondas.client.module.impl;

import com.gondas.client.gui.ClickGUI;
import com.gondas.client.module.Module;

/**
 * ClickGUIModule - Opens the ClickGUI
 * Auto-disables after opening the GUI
 */
public class ClickGUIModule extends Module {
    public ClickGUIModule() {
        super("ClickGUI", "Open the module GUI", Module.Category.MISC, 0);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.field_71439_g != null) {
            mc.func_147108_a(new ClickGUI());
        }
        // Auto-disable after opening
        this.setToggled(false);
    }
}
