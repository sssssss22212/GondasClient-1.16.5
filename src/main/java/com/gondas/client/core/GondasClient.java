package com.gondas.client.core;

import com.gondas.client.config.ConfigManager;
import com.gondas.client.event.ClientEventHandler;
import com.gondas.client.module.ModuleManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Gondas Client - Advanced Minecraft Utility Mod
 * Version: 2.0.0
 * Optimized for: Desktop & Mobile (PojavLauncher, MCLauncher, etc.)
 */
@Mod("gondasclient")
public class GondasClient {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "gondasclient";
    public static final String VERSION = "2.0.0";
    public static final String AUTHOR = "Gondas";

    public static GondasClient INSTANCE;

    // Mobile device detection
    private static boolean isMobileDevice = false;
    private static String launcherType = "Unknown";

    public GondasClient() {
        INSTANCE = this;

        // Detect mobile launcher environment
        detectMobileLauncher();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("========================================");
        LOGGER.info("  Gondas Client v" + VERSION + " by " + AUTHOR);
        LOGGER.info("========================================");
        LOGGER.info("Platform: " + (isMobileDevice ? "Mobile" : "Desktop"));
        LOGGER.info("Launcher: " + launcherType);
        LOGGER.info("========================================");
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        // Initialize modules first
        ModuleManager.init();

        // Register key bindings
        KeyBindingHandler.register();

        // Load saved config
        event.enqueueWork(() -> {
            try {
                ConfigManager.loadConfig();
                LOGGER.info("Configuration loaded successfully!");
            } catch (Exception e) {
                LOGGER.warn("Could not load configuration: " + e.getMessage());
            }
        });

        LOGGER.info("Gondas Client loaded successfully!");
        LOGGER.info("Press RIGHT SHIFT to open ClickGUI");
    }

    /**
     * Detects if running on a mobile launcher (PojavLauncher, MCLauncher, etc.)
     */
    private void detectMobileLauncher() {
        String javaVendor = System.getProperty("java.vendor", "").toLowerCase();
        String osName = System.getProperty("os.name", "").toLowerCase();
        String userDir = System.getProperty("user.home", "").toLowerCase();

        // Check for PojavLauncher
        if (userDir.contains("pojav") || osName.contains("pojav")) {
            isMobileDevice = true;
            launcherType = "PojavLauncher";
            LOGGER.info("Detected PojavLauncher environment");
        }
        // Check for MCLauncher / Fold Craft Launcher
        else if (userDir.contains("mclaucher") || userDir.contains("foldcraft") || osName.contains("foldcraft")) {
            isMobileDevice = true;
            launcherType = "MCLauncher/FCL";
            LOGGER.info("Detected MCLauncher/Fold Craft Launcher environment");
        }
        // Check for generic Android environment
        else if (javaVendor.contains("android") || osName.contains("android")) {
            isMobileDevice = true;
            launcherType = "Android";
            LOGGER.info("Detected Android environment");
        }
        // Check for ARM architecture (common on mobile)
        else if (System.getProperty("os.arch", "").toLowerCase().contains("arm")) {
            // ARM doesn't always mean mobile, but it's worth noting
            LOGGER.info("ARM architecture detected - may be mobile device");
        }
    }

    /**
     * @return true if running on a mobile device/launcher
     */
    public static boolean isMobileDevice() {
        return isMobileDevice;
    }

    /**
     * @return the detected launcher type
     */
    public static String getLauncherType() {
        return launcherType;
    }

    /**
     * @return the mod version
     */
    public static String getVersion() {
        return VERSION;
    }
}
