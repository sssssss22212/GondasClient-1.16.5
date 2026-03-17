package com.gondas.client.gui;

import com.gondas.client.config.ConfigManager;
import com.gondas.client.core.GondasClient;
import com.gondas.client.module.Module;
import com.gondas.client.module.ModuleManager;
import com.gondas.client.setting.Setting;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import java.util.List;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.glfw.GLFW;

/**
 * ClickGUI - Main GUI for managing modules
 * Improved with mobile/touch support and better animations
 */
public class ClickGUI extends Screen {
    private static int selectedCategory = 0;
    private int moduleScrollOffset = 0;
    private Module selectedModule = null;
    private int settingScrollOffset = 0;
    private long openTime = System.currentTimeMillis();
    private Setting draggingSetting = null;
    private float animation = 0.0F;

    // Mobile/Touch support
    private double lastTouchY = 0;
    private boolean isDragging = false;
    private int touchCooldown = 0;

    // GUI scaling for mobile devices
    private float getScale() {
        return GondasClient.isMobileDevice() ? 1.2f : 1.0f;
    }

    public ClickGUI() {
        super(new StringTextComponent("Gondas Client v" + GondasClient.getVersion()));
    }

    @Override
    public void func_230430_a_(MatrixStack ms, int mx, int my, float pt) {
        // Animation
        long elapsed = System.currentTimeMillis() - this.openTime;
        float target = Math.min(1.0F, (float) elapsed / 150.0F);
        this.animation += (target - this.animation) * 0.2F;

        float scale = getScale();
        int bgAlpha = (int) (180.0F * this.animation);
        func_238467_a_(ms, 0, 0, this.field_230708_k_, this.field_230709_l_, bgAlpha << 24 | 657930);

        int panelW = (int) (600 * scale);
        int panelH = (int) (350 * scale);
        float easedX = (float) this.field_230708_k_ / 2.0F - (float) panelW / 2.0F * this.animation;
        float easedY = (float) this.field_230709_l_ / 2.0F - (float) panelH / 2.0F * this.animation;
        int panelX = (int) easedX;
        int panelY = (int) easedY;

        // Shadow
        func_238467_a_(ms, panelX + 4, panelY + 4, panelX + panelW + 4, panelY + panelH + 4, 805306368);
        // Background
        func_238467_a_(ms, panelX, panelY, panelX + panelW, panelY + panelH, -267053803);

        // Animated border
        float hue = (float) (System.currentTimeMillis() % 4000L) / 4000.0F;
        int borderColor = Color.HSBtoRGB(hue, 0.6F, 0.8F);
        this.drawBorder(ms, panelX, panelY, panelW, panelH, borderColor, 2);

        // Title
        String title = "GONDAS CLIENT";
        int titleColor = Color.HSBtoRGB((float) (System.currentTimeMillis() % 2500L) / 2500.0F, 0.9F, 1.0F);
        int titleX = panelX + panelW / 2 - this.field_230712_o_.func_78256_a(title) / 2;
        this.field_230712_o_.func_238405_a_(ms, title, (float) titleX, (float) (panelY + 8), titleColor);

        // Version
        this.field_230712_o_.func_238405_a_(ms, "v" + GondasClient.getVersion(), (float) (panelX + panelW - 45), (float) (panelY + 8), -16733696);

        // Mobile indicator
        if (GondasClient.isMobileDevice()) {
            this.field_230712_o_.func_238405_a_(ms, "[" + GondasClient.getLauncherType() + "]", (float) (panelX + 8), (float) (panelY + 8), 0xFFAA00AA);
        }

        // Categories
        int catY = panelY + 28;
        int catIndex = 0;
        String[] catNames = new String[]{"COMBAT", "MOVE", "PLAYER", "RENDER", "MISC", "WORLD"};
        Module.Category[] categories = Module.Category.values();

        for (Module.Category ignored : categories) {
            int catBtnX = panelX + 8 + catIndex * (int) (70 * scale);
            boolean selected = catIndex == selectedCategory;
            boolean hover = mx >= catBtnX && mx <= catBtnX + (int) (65 * scale) && my >= catY && my <= catY + (int) (22 * scale);
            int btnColor = selected ? -16729344 : (hover ? -13290187 : -14671840);
            func_238467_a_(ms, catBtnX, catY, catBtnX + (int) (65 * scale), catY + (int) (22 * scale), btnColor);
            if (selected) {
                this.drawBorder(ms, catBtnX, catY, (int) (65 * scale), (int) (22 * scale), -16711936, 1);
            }
            this.field_230712_o_.func_238405_a_(ms, catNames[catIndex], (float) (catBtnX + 5), (float) (catY + 6), selected ? -16777216 : -3355444);
            ++catIndex;
        }

        // Config buttons
        int cfgY = panelY + panelH - 25;
        this.renderConfigButtons(ms, mx, my, panelX + 8, cfgY);

        // Modules panel
        int modX = panelX + 8;
        int modY = panelY + 58;
        int modW = (int) (200 * scale);
        int modH = panelH - 95;
        this.renderModulesPanel(ms, mx, my, modX, modY, modW, modH);

        // Settings panel
        int setX = panelX + (int) (215 * scale);
        int setW = panelW - (int) (225 * scale);
        this.renderSettingsPanel(ms, mx, my, setX, modY, setW, modH);

        super.func_230430_a_(ms, mx, my, pt);
    }

    private void renderConfigButtons(MatrixStack ms, int mx, int my, int x, int y) {
        String[] buttons = new String[]{"Save", "Load", "Reset"};
        int[] colors = new int[]{-16733696, -16750900, -5636096};

        for (int i = 0; i < buttons.length; ++i) {
            int btnX = x + i * 60;
            boolean hover = mx >= btnX && mx <= btnX + 55 && my >= y && my <= y + 18;
            func_238467_a_(ms, btnX, y, btnX + 55, y + 18, hover ? -12566464 : colors[i]);
            this.field_230712_o_.func_238405_a_(ms, buttons[i], (float) (btnX + 10), (float) (y + 5), -1);
        }

        String cfgName = "Config: " + ConfigManager.getCurrentConfig();
        this.field_230712_o_.func_238405_a_(ms, cfgName, (float) (x + 190), (float) (y + 5), -7829368);
    }

    private void renderModulesPanel(MatrixStack ms, int mx, int my, int x, int y, int w, int h) {
        func_238467_a_(ms, x, y, x + w, y + h, Integer.MIN_VALUE);
        this.drawBorder(ms, x, y, w, h, -14342875, 1);
        this.field_230712_o_.func_238405_a_(ms, "MODULES", (float) (x + 6), (float) (y + 4), -16720640);

        List<Module> modules = ModuleManager.getByCategory(Module.Category.values()[selectedCategory]);
        int moduleY = y + 18 - this.moduleScrollOffset;

        for (Module m : modules) {
            if (moduleY >= y - 14 && moduleY <= y + h - 14) {
                boolean toggled = m.isToggled();
                boolean hover = mx >= x + 3 && mx <= x + w - 3 && my >= moduleY && my <= moduleY + 16;
                int modColor = toggled ? -16738048 : (hover ? -13290187 : -15066598);
                func_238467_a_(ms, x + 3, moduleY, x + w - 3, moduleY + 16, modColor);
                if (toggled) {
                    this.drawBorder(ms, x + 3, moduleY, w - 6, 16, -16711936, 1);
                }

                this.field_230712_o_.func_238405_a_(ms, m.getName(), (float) (x + 8), (float) (moduleY + 4), toggled ? -1 : -6710887);

                // Keybind display
                if (m.getKey() != 0) {
                    String key = GLFW.glfwGetKeyName(m.getKey(), 0);
                    if (key != null) {
                        String keyText = "[" + key.toUpperCase() + "]";
                        this.field_230712_o_.func_238405_a_(ms, keyText, (float) (x + w - 10 - this.field_230712_o_.func_78256_a(keyText)), (float) (moduleY + 4), -10066330);
                    }
                }

                // Settings indicator
                if (!m.getSettings().isEmpty()) {
                    this.field_230712_o_.func_238405_a_(ms, ">", (float) (x + w - 8), (float) (moduleY + 4), this.selectedModule == m ? -16711936 : -11184811);
                }
            }
            moduleY += 18;
        }
    }

    private void renderSettingsPanel(MatrixStack ms, int mx, int my, int x, int y, int w, int h) {
        func_238467_a_(ms, x, y, x + w, y + h, Integer.MIN_VALUE);
        this.drawBorder(ms, x, y, w, h, -14342875, 1);
        this.field_230712_o_.func_238405_a_(ms, "SETTINGS", (float) (x + 6), (float) (y + 4), -16720640);

        if (this.selectedModule != null && !this.selectedModule.getSettings().isEmpty()) {
            this.field_230712_o_.func_238405_a_(ms, "for " + this.selectedModule.getName(), (float) (x + 65), (float) (y + 4), -10066330);

            int sY = y + 20 - this.settingScrollOffset;
            for (Setting s : this.selectedModule.getSettings()) {
                if (sY >= y - 20 && sY <= y + h - 20) {
                    this.field_230712_o_.func_238405_a_(ms, s.getName(), (float) (x + 6), (float) sY, -2236963);

                    boolean hover;
                    if (s instanceof Setting.Boolean) {
                        Setting.Boolean bool = (Setting.Boolean) s;
                        hover = mx >= x + w - 45 && mx <= x + w - 6 && my >= sY - 1 && my <= sY + 12;
                        String text = bool.getValue() ? "ON" : "OFF";
                        int textColor = bool.getValue() ? -16711936 : -48060;
                        func_238467_a_(ms, x + w - 48, sY - 1, x + w - 6, sY + 12, hover ? -13290187 : -14671840);
                        this.field_230712_o_.func_238405_a_(ms, text, (float) (x + w - 42), (float) sY, textColor);
                    } else if (s instanceof Setting.Int) {
                        this.renderSlider(ms, x, w, sY, (Setting.Int) s, mx, my);
                    } else if (s instanceof Setting.Double) {
                        this.renderSlider(ms, x, w, sY, (Setting.Double) s, mx, my);
                    } else if (s instanceof Setting.Mode) {
                        Setting.Mode mode = (Setting.Mode) s;
                        hover = mx >= x + w - this.field_230712_o_.func_78256_a(mode.getValue()) - 18 && mx <= x + w - 6 && my >= sY - 1 && my <= sY + 12;
                        func_238467_a_(ms, x + w - this.field_230712_o_.func_78256_a(mode.getValue()) - 15, sY - 1, x + w - 6, sY + 12, hover ? -13290187 : -14671840);
                        this.field_230712_o_.func_238405_a_(ms, mode.getValue(), (float) (x + w - this.field_230712_o_.func_78256_a(mode.getValue()) - 10), (float) sY, -16720640);
                        this.field_230712_o_.func_238405_a_(ms, "<", (float) (x + w - this.field_230712_o_.func_78256_a(mode.getValue()) - 25), (float) sY, -10066330);
                        this.field_230712_o_.func_238405_a_(ms, ">", (float) (x + w - 8), (float) sY, -10066330);
                    }
                }
                sY += 24;
            }
        } else {
            this.field_230712_o_.func_238405_a_(ms, "Select a module", (float) (x + 15), (float) (y + 40), -11184811);
        }
    }

    private void renderSlider(MatrixStack ms, int panelX, int panelW, int y, Setting setting, int mx, int my) {
        double value;
        double min;
        double max;
        String valueStr;

        if (setting instanceof Setting.Int) {
            Setting.Int intSet = (Setting.Int) setting;
            value = intSet.getValue();
            min = intSet.getMin();
            max = intSet.getMax();
            valueStr = String.valueOf((int) value);
        } else {
            Setting.Double doubleSet = (Setting.Double) setting;
            value = doubleSet.getValue();
            min = doubleSet.getMin();
            max = doubleSet.getMax();
            valueStr = String.format("%.1f", value);
        }

        int sliderX = panelX + 65;
        int sliderW = panelW - 130;
        int sliderH = 6;
        int sliderY = y + 4;

        func_238467_a_(ms, sliderX, sliderY, sliderX + sliderW, sliderY + sliderH, -13290187);

        double percent = (value - min) / (max - min);
        int filledW = (int) ((double) sliderW * percent);
        func_238467_a_(ms, sliderX, sliderY, sliderX + filledW, sliderY + sliderH, -16729344);

        int handleX = sliderX + filledW - 2;
        func_238467_a_(ms, handleX, sliderY - 1, handleX + 4, sliderY + sliderH + 1, -1);

        this.field_230712_o_.func_238405_a_(ms, valueStr, (float) (panelX + panelW - 38), (float) y, -16720640);

        if (mx >= sliderX && mx <= sliderX + sliderW && my >= sliderY && my <= sliderY + sliderH) {
            func_238467_a_(ms, sliderX, sliderY, sliderX + sliderW, sliderY + sliderH, 822083583);
        }
    }

    @Override
    public boolean func_231044_a_(double mx, double my, int btn) {
        // Touch cooldown for mobile
        if (touchCooldown > 0) {
            touchCooldown--;
            return true;
        }

        float scale = getScale();
        int panelX = this.field_230708_k_ / 2 - (int) (300 * scale);
        int panelY = this.field_230709_l_ / 2 - (int) (175 * scale);
        int panelW = (int) (600 * scale);
        int panelH = (int) (350 * scale);
        int cfgY = panelY + panelH - 25;

        // Config buttons
        String[] actions = new String[]{"Save", "Load", "Reset"};
        for (int i = 0; i < actions.length; ++i) {
            int btnX = panelX + 8 + i * 60;
            if (mx >= btnX && mx <= btnX + 55 && my >= cfgY && my <= cfgY + 18) {
                if (i == 0) {
                    ConfigManager.saveConfig();
                } else if (i == 1) {
                    ConfigManager.loadConfig();
                } else {
                    ConfigManager.resetConfig();
                }
                touchCooldown = GondasClient.isMobileDevice() ? 10 : 0;
                return true;
            }
        }

        // Categories
        int catY = panelY + 28;
        for (int catIndex = 0; catIndex < 6; ++catIndex) {
            int catBtnX = panelX + 8 + catIndex * (int) (70 * scale);
            if (mx >= catBtnX && mx <= catBtnX + (int) (65 * scale) && my >= catY && my <= catY + (int) (22 * scale)) {
                selectedCategory = catIndex;
                this.moduleScrollOffset = 0;
                this.selectedModule = null;
                return true;
            }
        }

        // Modules
        int modX = panelX + 8;
        int modY = panelY + 58;
        int modW = (int) (200 * scale);
        int modH = panelH - 95;
        List<Module> modules = ModuleManager.getByCategory(Module.Category.values()[selectedCategory]);
        int moduleY = modY + 18 - this.moduleScrollOffset;

        for (Module m : modules) {
            if (moduleY >= modY && moduleY <= modY + modH - 14 && mx >= modX + 3 && mx <= modX + modW - 3 && my >= moduleY && my <= moduleY + 16) {
                if (btn == 0) {
                    m.toggle();
                } else if (btn == 1 || GondasClient.isMobileDevice()) {
                    this.selectedModule = m;
                    this.settingScrollOffset = 0;
                }
                touchCooldown = GondasClient.isMobileDevice() ? 10 : 0;
                return true;
            }
            moduleY += 18;
        }

        // Settings
        int setX = panelX + (int) (215 * scale);
        int setW = panelW - (int) (225 * scale);
        if (this.selectedModule != null) {
            int sY = modY + 20 - this.settingScrollOffset;

            for (Setting s : this.selectedModule.getSettings()) {
                if (mx >= setX + 3 && mx <= setX + setW - 3 && my >= sY - 1 && my <= sY + 18) {
                    if (s instanceof Setting.Boolean) {
                        ((Setting.Boolean) s).toggle();
                    } else if (s instanceof Setting.Mode) {
                        ((Setting.Mode) s).cycleForward();
                    } else if (s instanceof Setting.Int || s instanceof Setting.Double) {
                        this.draggingSetting = s;
                        this.updateSliderValue(s, mx, setX + 65, setW - 130);
                    }
                    touchCooldown = GondasClient.isMobileDevice() ? 10 : 0;
                    return true;
                }
                sY += 24;
            }
        }

        return super.func_231044_a_(mx, my, btn);
    }

    @Override
    public boolean func_231045_a_(double mx, double my, int btn, double dx, double dy) {
        if (this.draggingSetting != null) {
            int panelX = this.field_230708_k_ / 2 - 300;
            this.updateSliderValue(this.draggingSetting, mx, panelX + 280, 255);
            return true;
        }
        return super.func_231045_a_(mx, my, btn, dx, dy);
    }

    @Override
    public boolean func_231048_c_(double mx, double my, int btn) {
        this.draggingSetting = null;
        return super.func_231048_c_(mx, my, btn);
    }

    private void updateSliderValue(Setting setting, double mx, int sliderX, int sliderW) {
        double percent = (mx - sliderX) / sliderW;
        percent = Math.max(0.0D, Math.min(1.0D, percent));

        if (setting instanceof Setting.Int) {
            Setting.Int intSet = (Setting.Int) setting;
            double min = intSet.getMin();
            double max = intSet.getMax();
            int value = (int) Math.round(min + percent * (max - min));
            intSet.setValue(value);
        } else if (setting instanceof Setting.Double) {
            Setting.Double doubleSet = (Setting.Double) setting;
            double min = doubleSet.getMin();
            double max = doubleSet.getMax();
            double value = min + percent * (max - min);
            doubleSet.setValue(value);
        }
    }

    @Override
    public boolean func_231043_a_(double mx, double my, double delta) {
        int panelX = this.field_230708_k_ / 2 - 300;
        int panelY = this.field_230709_l_ / 2 - 175;
        int modX = panelX + 8;
        int modY = panelY + 58;
        int modW = 200;
        int modH = 255;

        // Module scroll
        if (mx >= modX && mx <= modX + modW && my >= modY && my <= modY + modH) {
            this.moduleScrollOffset = Math.max(0, this.moduleScrollOffset - (int) (delta * 12.0D));
        }

        // Settings scroll
        int setX = panelX + 215;
        int setW = 385;
        if (mx >= setX && mx <= setX + setW && my >= modY && my <= modY + modH) {
            this.settingScrollOffset = Math.max(0, this.settingScrollOffset - (int) (delta * 12.0D));
        }

        return super.func_231043_a_(mx, my, delta);
    }

    @Override
    public boolean func_231046_a_(int key, int scan, int mods) {
        if (key == 344 || key == 256) { // RIGHT_SHIFT or ESCAPE
            this.field_230706_i_.func_147108_a(null);
            return true;
        }
        return super.func_231046_a_(key, scan, mods);
    }

    private void drawBorder(MatrixStack ms, int x, int y, int w, int h, int color, int lineWidth) {
        func_238467_a_(ms, x, y, x + w, y + lineWidth, color);
        func_238467_a_(ms, x, y + h - lineWidth, x + w, y + h, color);
        func_238467_a_(ms, x, y, x + lineWidth, y + h, color);
        func_238467_a_(ms, x + w - lineWidth, y, x + w, y + h, color);
    }

    @Override
    public boolean func_231177_au__() {
        return false; // Does not pause game
    }
}
