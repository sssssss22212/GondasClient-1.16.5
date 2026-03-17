package com.gondas.client.module;

import com.gondas.client.core.GondasClient;
import com.gondas.client.setting.Setting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

/**
 * Base class for all modules
 * Improved version with better mobile support and performance optimizations
 */
public abstract class Module {
    protected static final Minecraft mc = Minecraft.func_71410_x();

    private final String name;
    private final String description;
    private final Category category;
    private int key;
    private boolean toggled;
    private final List<Setting> settings;

    // Performance optimization - cache frequently accessed values
    private long lastToggleTime = 0;
    private static final long TOGGLE_COOLDOWN = 50; // ms

    public Module(String name, String description, Category category, int key) {
        this.settings = new ArrayList<>();
        this.name = name;
        this.description = description;
        this.category = category;
        this.key = key;
        this.toggled = false;
    }

    public Module(String name, String description, Category category) {
        this(name, description, category, 0);
    }

    public Module(String name, Category category) {
        this(name, "", category, 0);
    }

    protected void addSettings(Setting... settingArray) {
        for (Setting s : settingArray) {
            this.settings.add(s);
        }
    }

    /**
     * Toggle module on/off with cooldown for mobile devices
     */
    public void toggle() {
        // Cooldown to prevent rapid toggling (especially on mobile)
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastToggleTime < TOGGLE_COOLDOWN && GondasClient.isMobileDevice()) {
            return;
        }
        lastToggleTime = currentTime;

        this.toggled = !this.toggled;

        if (this.toggled) {
            onEnable();
            GondasClient.LOGGER.debug("Module enabled: " + name);
        } else {
            onDisable();
            GondasClient.LOGGER.debug("Module disabled: " + name);
        }
    }

    /**
     * Force enable the module
     */
    public void enable() {
        if (!this.toggled) {
            this.toggled = true;
            onEnable();
            GondasClient.LOGGER.debug("Module force enabled: " + name);
        }
    }

    /**
     * Force disable the module
     */
    public void disable() {
        if (this.toggled) {
            this.toggled = false;
            onDisable();
            GondasClient.LOGGER.debug("Module force disabled: " + name);
        }
    }

    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onDisable() {
        try {
            MinecraftForge.EVENT_BUS.unregister(this);
        } catch (Exception e) {
            // Module might not be registered, ignore
        }
    }

    public void onTick() {
        // Override in subclasses for tick-based logic
    }

    /**
     * Called when settings are changed
     */
    public void onSettingsChanged() {
        // Override in subclasses if needed
    }

    // Getters
    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Category getCategory() {
        return this.category;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean isToggled() {
        return this.toggled;
    }

    public void setToggled(boolean toggled) {
        if (this.toggled != toggled) {
            this.toggled = toggled;
            if (toggled) {
                onEnable();
            } else {
                onDisable();
            }
        }
    }

    public List<Setting> getSettings() {
        return this.settings;
    }

    public boolean hasSettings() {
        return !this.settings.isEmpty();
    }

    /**
     * Get a setting by name
     */
    public Setting getSettingByName(String name) {
        for (Setting s : settings) {
            if (s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Module categories
     */
    public enum Category {
        COMBAT("Combat", 0xFFff4444),
        MOVEMENT("Movement", 0xFF44ff44),
        RENDER("Render", 0xFF4444ff),
        PLAYER("Player", 0xFFffaa00),
        WORLD("World", 0xFFaa44ff),
        MISC("Misc", 0xFF44aaff);

        public final String name;
        public final int color;

        Category(String name, int color) {
            this.name = name;
            this.color = color;
        }
    }
}
