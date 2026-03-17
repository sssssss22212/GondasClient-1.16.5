package com.gondas.client.config;

import com.gondas.client.core.GondasClient;
import com.gondas.client.module.Module;
import com.gondas.client.module.ModuleManager;
import com.gondas.client.setting.Setting;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;

/**
 * ConfigManager - Handles saving and loading module configurations
 * Improved with better error handling and mobile compatibility
 */
public class ConfigManager {
    private static final String CONFIG_DIR = "gondas";
    private static String currentConfig = "default";

    public static String getCurrentConfig() {
        return currentConfig;
    }

    public static void saveConfig() {
        saveConfig(currentConfig);
    }

    public static void saveConfig(String name) {
        try {
            Path configPath = Minecraft.func_71410_x().field_71412_D.toPath().resolve(CONFIG_DIR);
            if (!Files.exists(configPath, new LinkOption[0])) {
                Files.createDirectories(configPath);
            }

            File configFile = configPath.resolve(name + ".json").toFile();
            JsonObject root = new JsonObject();

            // Add metadata
            root.addProperty("version", GondasClient.getVersion());
            root.addProperty("created", System.currentTimeMillis());

            JsonObject modulesObj = new JsonObject();
            Iterator<Module> moduleIterator = ModuleManager.getModules().iterator();

            while (moduleIterator.hasNext()) {
                Module m = moduleIterator.next();
                JsonObject moduleObj = new JsonObject();
                moduleObj.addProperty("toggled", m.isToggled());
                moduleObj.addProperty("key", m.getKey());

                JsonObject settingsObj = new JsonObject();
                Iterator<Setting> settingIterator = m.getSettings().iterator();

                while (settingIterator.hasNext()) {
                    Setting s = settingIterator.next();
                    if (s instanceof Setting.Boolean) {
                        settingsObj.addProperty(s.getName(), ((Setting.Boolean) s).getValue());
                    } else if (s instanceof Setting.Int) {
                        settingsObj.addProperty(s.getName(), ((Setting.Int) s).getValue());
                    } else if (s instanceof Setting.Double) {
                        settingsObj.addProperty(s.getName(), ((Setting.Double) s).getValue());
                    } else if (s instanceof Setting.Mode) {
                        settingsObj.addProperty(s.getName(), ((Setting.Mode) s).getValue());
                    } else if (s instanceof Setting.Str) {
                        settingsObj.addProperty(s.getName(), ((Setting.Str) s).getValue());
                    }
                }

                moduleObj.add("settings", settingsObj);
                modulesObj.add(m.getName(), moduleObj);
            }

            root.add("modules", modulesObj);
            Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
            Files.write(configFile.toPath(), gson.toJson(root).getBytes(), new OpenOption[0]);
            currentConfig = name;
            GondasClient.LOGGER.info("Config saved: " + name);
        } catch (Exception var11) {
            GondasClient.LOGGER.error("Failed to save config: " + var11.getMessage());
        }
    }

    public static void loadConfig() {
        loadConfig(currentConfig);
    }

    public static void loadConfig(String name) {
        try {
            Path configPath = Minecraft.func_71410_x().field_71412_D.toPath().resolve(CONFIG_DIR);
            File configFile = configPath.resolve(name + ".json").toFile();
            if (!configFile.exists()) {
                GondasClient.LOGGER.info("Config not found: " + name + ", using defaults");
                return;
            }

            String json = new String(Files.readAllBytes(configFile.toPath()));
            JsonParser parser = new JsonParser();
            JsonObject root = parser.parse(json).getAsJsonObject();

            // Check version compatibility
            if (root.has("version")) {
                String configVersion = root.get("version").getAsString();
                GondasClient.LOGGER.info("Loading config version: " + configVersion);
            }

            JsonObject modulesObj = root.getAsJsonObject("modules");
            if (modulesObj == null) {
                GondasClient.LOGGER.warn("No modules found in config");
                return;
            }

            Iterator<Entry<String, JsonElement>> entryIterator = modulesObj.entrySet().iterator();

            while (entryIterator.hasNext()) {
                Entry<String, JsonElement> entry = entryIterator.next();
                Module m = ModuleManager.getModuleByName(entry.getKey());
                if (m == null) continue;

                JsonObject moduleObj = entry.getValue().getAsJsonObject();
                if (moduleObj.has("toggled")) {
                    boolean toggled = moduleObj.get("toggled").getAsBoolean();
                    if (toggled != m.isToggled()) {
                        m.toggle();
                    }
                }

                if (!moduleObj.has("settings")) continue;

                JsonObject settingsObj = moduleObj.getAsJsonObject("settings");
                Iterator<Entry<String, JsonElement>> settingIterator = settingsObj.entrySet().iterator();

                while (settingIterator.hasNext()) {
                    Entry<String, JsonElement> settingEntry = settingIterator.next();
                    Setting s = getSettingByName(m, settingEntry.getKey());
                    if (s == null) continue;

                    JsonElement val = settingEntry.getValue();
                    if (s instanceof Setting.Boolean) {
                        ((Setting.Boolean) s).setValue(val.getAsBoolean());
                    } else if (s instanceof Setting.Int) {
                        ((Setting.Int) s).setValue(val.getAsInt());
                    } else if (s instanceof Setting.Double) {
                        ((Setting.Double) s).setValue(val.getAsDouble());
                    } else if (s instanceof Setting.Mode) {
                        ((Setting.Mode) s).setValue(val.getAsString());
                    } else if (s instanceof Setting.Str) {
                        ((Setting.Str) s).setValue(val.getAsString());
                    }
                }
            }

            currentConfig = name;
            GondasClient.LOGGER.info("Config loaded: " + name);
        } catch (Exception var16) {
            GondasClient.LOGGER.error("Failed to load config: " + var16.getMessage());
        }
    }

    public static void resetConfig() {
        Iterator<Module> moduleIterator = ModuleManager.getModules().iterator();

        while (moduleIterator.hasNext()) {
            Module m = moduleIterator.next();
            if (m.isToggled()) {
                m.toggle();
            }

            // Reset all settings to defaults
            for (Setting s : m.getSettings()) {
                if (s instanceof Setting.Boolean) {
                    ((Setting.Boolean) s).reset();
                } else if (s instanceof Setting.Int) {
                    ((Setting.Int) s).reset();
                } else if (s instanceof Setting.Double) {
                    ((Setting.Double) s).reset();
                } else if (s instanceof Setting.Mode) {
                    ((Setting.Mode) s).reset();
                } else if (s instanceof Setting.Str) {
                    ((Setting.Str) s).reset();
                }
            }
        }

        currentConfig = "default";
        GondasClient.LOGGER.info("Config reset to defaults");
    }

    private static Setting getSettingByName(Module module, String name) {
        for (Setting s : module.getSettings()) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        return null;
    }

    public static String[] listConfigs() {
        try {
            Path configPath = Minecraft.func_71410_x().field_71412_D.toPath().resolve(CONFIG_DIR);
            if (!Files.exists(configPath, new LinkOption[0])) {
                return new String[]{"default"};
            } else {
                File[] files = configPath.toFile().listFiles((dir, n) -> n.endsWith(".json"));
                if (files != null && files.length != 0) {
                    String[] names = new String[files.length];

                    for (int i = 0; i < files.length; ++i) {
                        names[i] = files[i].getName().replace(".json", "");
                    }

                    return names;
                } else {
                    return new String[]{"default"};
                }
            }
        } catch (Exception var4) {
            return new String[]{"default"};
        }
    }

    public static void deleteConfig(String name) {
        if (name.equals("default")) {
            GondasClient.LOGGER.warn("Cannot delete default config");
            return;
        }

        try {
            Path configPath = Minecraft.func_71410_x().field_71412_D.toPath().resolve(CONFIG_DIR);
            File configFile = configPath.resolve(name + ".json").toFile();
            if (configFile.exists()) {
                configFile.delete();
                GondasClient.LOGGER.info("Config deleted: " + name);
                if (currentConfig.equals(name)) {
                    currentConfig = "default";
                }
            }
        } catch (Exception e) {
            GondasClient.LOGGER.error("Failed to delete config: " + e.getMessage());
        }
    }
}
