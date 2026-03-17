package com.gondas.client.module.impl;

import com.gondas.client.core.GondasClient;
import com.gondas.client.module.Module;
import com.gondas.client.module.ModuleManager;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * HUD Module - Displays enabled modules on screen
 * Improved with rainbow colors and better formatting
 */
public class HUD extends Module {
    private Setting.Boolean rainbow = new Setting.Boolean("Rainbow", true);
    private Setting.Boolean showCategory = new Setting.Boolean("ShowCategory", false);
    private Setting.Double scale = new Setting.Double("Scale", 1.0, 0.5, 2.0);
    private Setting.Int offset = new Setting.Int("Offset", 2, 0, 10);

    public HUD() {
        super("HUD", "Display module list on screen", Module.Category.RENDER);
        addSettings(rainbow, showCategory, scale, offset);
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }

        if (mc.field_71466_p == null) {
            return;
        }

        MatrixStack matrixStack = new MatrixStack();
        float scaleValue = (float) scale.getValue();
        int y = offset.getValue();

        // Get sorted enabled modules
        List<Module> enabledModules = ModuleManager.getEnabledModules().stream()
                .sorted(Comparator.comparingInt(m -> -mc.field_71466_p.func_78256_a(getDisplayText(m))))
                .collect(Collectors.toList());

        for (Module m : enabledModules) {
            if (m == this) continue; // Don't show HUD itself

            String text = getDisplayText(m);
            int color = getColor();

            matrixStack.func_227862_a_(scaleValue, scaleValue, scaleValue);
            mc.field_71466_p.func_238405_a_(matrixStack, text, 2.0F / scaleValue, (float) y / scaleValue, color);
            matrixStack.func_227865_b_();

            y += (int) (10 * scaleValue);
        }

        // Draw watermark
        drawWatermark(matrixStack);
    }

    private String getDisplayText(Module m) {
        if (showCategory.getValue()) {
            return m.getName() + " [" + m.getCategory().name + "]";
        }
        return m.getName();
    }

    private int getColor() {
        if (rainbow.getValue()) {
            float hue = (System.currentTimeMillis() % 4000L) / 4000.0F;
            return Color.HSBtoRGB(hue, 0.7F, 1.0F);
        }
        return 0xFF00FF00; // Green
    }

    private void drawWatermark(MatrixStack matrixStack) {
        String watermark = "GondasClient v" + GondasClient.getVersion();
        if (GondasClient.isMobileDevice()) {
            watermark += " [" + GondasClient.getLauncherType() + "]";
        }

        int screenWidth = mc.func_228018_at_().func_198107_o();
        int watermarkWidth = mc.field_71466_p.func_78256_a(watermark);
        int watermarkColor = rainbow.getValue() ?
                Color.HSBtoRGB((System.currentTimeMillis() % 3000L) / 3000.0F, 0.8F, 1.0F) :
                0xFFAA00AA;

        mc.field_71466_p.func_238405_a_(matrixStack, watermark,
                (float) (screenWidth - watermarkWidth - 2), 2.0F, watermarkColor);
    }
}
