package com.gondas.client.setting;

import java.util.Arrays;
import java.util.List;

/**
 * Setting class for module configurations
 * Improved version with better type safety and additional setting types
 */
public abstract class Setting {
    protected String name;
    protected String description;

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Boolean setting type
     */
    public static class Boolean extends Setting {
        private boolean value;
        private boolean defaultValue;

        public Boolean(String name, boolean value) {
            this.name = name;
            this.value = value;
            this.defaultValue = value;
        }

        public Boolean(String name, String description, boolean value) {
            this.name = name;
            this.description = description;
            this.value = value;
            this.defaultValue = value;
        }

        public boolean getValue() {
            return this.value;
        }

        public void setValue(boolean value) {
            this.value = value;
        }

        public void toggle() {
            this.value = !this.value;
        }

        public void reset() {
            this.value = this.defaultValue;
        }

        public boolean getDefaultValue() {
            return defaultValue;
        }
    }

    /**
     * Mode (enum) setting type
     */
    public static class Mode extends Setting {
        private String value;
        private List<String> modes;
        private String defaultValue;

        public Mode(String name, String... modes) {
            this.name = name;
            this.modes = Arrays.asList(modes);
            this.value = modes[0];
            this.defaultValue = modes[0];
        }

        public Mode(String name, String description, String... modes) {
            this.name = name;
            this.description = description;
            this.modes = Arrays.asList(modes);
            this.value = modes[0];
            this.defaultValue = modes[0];
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            if (this.modes.contains(value)) {
                this.value = value;
            }
        }

        public void cycleForward() {
            int idx = modes.indexOf(value);
            value = modes.get((idx + 1) % modes.size());
        }

        public void cycleBackward() {
            int idx = modes.indexOf(value);
            value = modes.get((idx - 1 + modes.size()) % modes.size());
        }

        public List<String> getModes() {
            return this.modes;
        }

        public void reset() {
            this.value = this.defaultValue;
        }

        public String getDefaultValue() {
            return defaultValue;
        }
    }

    /**
     * Double setting type with precision support
     */
    public static class Double extends Setting {
        private double value;
        private double min;
        private double max;
        private double defaultValue;
        private int precision = 2;

        public Double(String name, double value, double min, double max) {
            this.name = name;
            this.value = value;
            this.min = min;
            this.max = max;
            this.defaultValue = value;
        }

        public Double(String name, String description, double value, double min, double max) {
            this.name = name;
            this.description = description;
            this.value = value;
            this.min = min;
            this.max = max;
            this.defaultValue = value;
        }

        public Double setPrecision(int precision) {
            this.precision = precision;
            return this;
        }

        public double getValue() {
            return this.value;
        }

        public void setValue(double value) {
            this.value = Math.max(this.min, Math.min(this.max, value));
        }

        public double getMin() {
            return this.min;
        }

        public double getMax() {
            return this.max;
        }

        public int getPrecision() {
            return precision;
        }

        public void reset() {
            this.value = this.defaultValue;
        }

        public double getDefaultValue() {
            return defaultValue;
        }

        public float getValueFloat() {
            return (float) value;
        }

        public String getFormattedValue() {
            return String.format("%." + precision + "f", value);
        }
    }

    /**
     * Integer setting type
     */
    public static class Int extends Setting {
        private int value;
        private int min;
        private int max;
        private int defaultValue;

        public Int(String name, int value, int min, int max) {
            this.name = name;
            this.value = value;
            this.min = min;
            this.max = max;
            this.defaultValue = value;
        }

        public Int(String name, String description, int value, int min, int max) {
            this.name = name;
            this.description = description;
            this.value = value;
            this.min = min;
            this.max = max;
            this.defaultValue = value;
        }

        public int getValue() {
            return this.value;
        }

        public void setValue(int value) {
            this.value = Math.max(this.min, Math.min(this.max, value));
        }

        public int getMin() {
            return this.min;
        }

        public int getMax() {
            return this.max;
        }

        public void reset() {
            this.value = this.defaultValue;
        }

        public int getDefaultValue() {
            return defaultValue;
        }

        public void increment() {
            if (value < max) value++;
        }

        public void decrement() {
            if (value > min) value--;
        }
    }

    /**
     * String setting type
     */
    public static class Str extends Setting {
        private String value;
        private String defaultValue;

        public Str(String name, String value) {
            this.name = name;
            this.value = value;
            this.defaultValue = value;
        }

        public Str(String name, String description, String value) {
            this.name = name;
            this.description = description;
            this.value = value;
            this.defaultValue = value;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public void reset() {
            this.value = this.defaultValue;
        }

        public String getDefaultValue() {
            return defaultValue;
        }
    }

    /**
     * Color setting type
     */
    public static class ColorSetting extends Setting {
        private int color;
        private int defaultColor;
        private boolean rainbow;

        public ColorSetting(String name, int color) {
            this.name = name;
            this.color = color;
            this.defaultColor = color;
            this.rainbow = false;
        }

        public ColorSetting(String name, String description, int color) {
            this.name = name;
            this.description = description;
            this.color = color;
            this.defaultColor = color;
            this.rainbow = false;
        }

        public int getColor() {
            if (rainbow) {
                return getRainbowColor();
            }
            return this.color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public boolean isRainbow() {
            return rainbow;
        }

        public void setRainbow(boolean rainbow) {
            this.rainbow = rainbow;
        }

        public void reset() {
            this.color = this.defaultColor;
            this.rainbow = false;
        }

        private int getRainbowColor() {
            float hue = (System.currentTimeMillis() % 4000L) / 4000.0f;
            return java.awt.Color.HSBtoRGB(hue, 0.7f, 1.0f);
        }

        public int getDefaultColor() {
            return defaultColor;
        }
    }
}
