package com.gondas.client.module;

import com.gondas.client.core.GondasClient;
import com.gondas.client.module.impl.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ModuleManager - Manages all modules
 * Improved with better organization and search functionality
 */
public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();

    public static void init() {
        GondasClient.LOGGER.info("Initializing modules...");

        // COMBAT
        modules.add(new KillAura());
        modules.add(new BowAim());
        modules.add(new AutoTotem());
        modules.add(new Velocity());
        modules.add(new Criticals());
        modules.add(new Reach());
        modules.add(new AimAssist());
        modules.add(new TriggerBot());
        modules.add(new AutoArmor());

        // MOVEMENT
        modules.add(new Fly());
        modules.add(new Speed());
        modules.add(new NoFall());
        modules.add(new AutoSprint());
        modules.add(new Sprint());
        modules.add(new Step());
        modules.add(new HighJump());
        modules.add(new LongJump());
        modules.add(new Spider());
        modules.add(new Jesus());
        modules.add(new Dolphin());
        modules.add(new BoatFly());
        modules.add(new IceSpeed());
        modules.add(new Parkour());
        modules.add(new AirJump());
        modules.add(new AntiCobweb());
        modules.add(new SafeWalk());

        // RENDER
        modules.add(new FullBright());
        modules.add(new ESP());
        modules.add(new Tracers());
        modules.add(new Xray());
        modules.add(new ChestESP());
        modules.add(new ItemESP());
        modules.add(new MobESP());
        modules.add(new StorageESP());
        modules.add(new HoleESP());
        modules.add(new TargetHUD());
        modules.add(new Chams());
        modules.add(new NoRender());
        modules.add(new CameraClip());
        modules.add(new HUD());

        // PLAYER
        modules.add(new AutoFish());
        modules.add(new AutoFarm());
        modules.add(new Scaffold());
        modules.add(new Nuker());
        modules.add(new AutoMine());
        modules.add(new Breaker());
        modules.add(new AutoEat());
        modules.add(new AutoTool());
        modules.add(new ChestStealer());
        modules.add(new InventoryManager());

        // MISC
        modules.add(new ClickGUIModule());
        modules.add(new Zoom());
        modules.add(new TimerMod());
        modules.add(new Spammer());
        modules.add(new AntiAFK());
        modules.add(new AutoLeave());
        modules.add(new FreeCam());
        modules.add(new NoWeather());
        modules.add(new TimeChanger());
        modules.add(new AutoSneak());
        modules.add(new AutoWalk());

        // Sort modules alphabetically within categories
        modules.sort(Comparator.comparing(Module::getName));

        GondasClient.LOGGER.info("Loaded " + modules.size() + " modules");
    }

    public static List<Module> getModules() {
        return modules;
    }

    public static List<Module> getByCategory(Module.Category category) {
        return modules.stream()
                .filter(m -> m.getCategory() == category)
                .collect(Collectors.toList());
    }

    public static Module getByKey(int key) {
        return modules.stream()
                .filter(m -> m.getKey() == key)
                .findFirst()
                .orElse(null);
    }

    public static Module getModuleByName(String name) {
        return modules.stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Search modules by name (partial match)
     */
    public static List<Module> searchModules(String query) {
        String lowerQuery = query.toLowerCase();
        return modules.stream()
                .filter(m -> m.getName().toLowerCase().contains(lowerQuery) ||
                        m.getDescription().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    /**
     * Get all enabled modules
     */
    public static List<Module> getEnabledModules() {
        return modules.stream()
                .filter(Module::isToggled)
                .collect(Collectors.toList());
    }

    /**
     * Disable all modules
     */
    public static void disableAll() {
        modules.stream()
                .filter(Module::isToggled)
                .forEach(Module::disable);
    }

    /**
     * Get module count by category
     */
    public static int getModuleCount(Module.Category category) {
        return (int) modules.stream()
                .filter(m -> m.getCategory() == category)
                .count();
    }
}
