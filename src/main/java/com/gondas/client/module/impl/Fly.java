package com.gondas.client.module.impl;

import com.gondas.client.core.GondasClient;
import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import net.minecraft.network.play.client.CPlayerPacket.PositionPacket;
import net.minecraft.util.math.vector.Vector3d;

/**
 * Fly Module - Allows flying in survival mode
 * Improved with multiple modes and anticheat bypass
 */
public class Fly extends Module {
    private Setting.Mode mode = new Setting.Mode("Mode", "Vanilla", "Glide", "Packet", "Creative");
    private Setting.Double speed = new Setting.Double("Speed", 2.0, 0.1, 10.0);
    private Setting.Boolean antiKick = new Setting.Boolean("AntiKick", true);
    private Setting.Double glideSpeed = new Setting.Double("GlideSpeed", 0.1, 0.01, 0.5);

    // Mobile optimization
    private Setting.Boolean smoothMovement = new Setting.Boolean("SmoothMovement", true);

    private int tickCounter = 0;
    private double startY = 0.0D;

    public Fly() {
        super("Fly", "Fly like a bird in survival mode", Module.Category.MOVEMENT);
        addSettings(mode, speed, antiKick, glideSpeed, smoothMovement);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.field_71439_g != null) {
            startY = mc.field_71439_g.func_226278_cu_();
            GondasClient.LOGGER.info("Fly enabled - Mode: " + mode.getValue());
        }
    }

    @Override
    public void onTick() {
        if (mc.field_71439_g == null) return;

        String m = mode.getValue();
        double speedVal = speed.getValue();

        // Apply mobile smooth movement if enabled
        if (smoothMovement.getValue() && GondasClient.isMobileDevice()) {
            speedVal *= 0.9; // Slightly reduce speed on mobile for smoother movement
        }

        switch (m) {
            case "Vanilla":
                handleVanilla(speedVal);
                break;
            case "Creative":
                handleCreative(speedVal);
                break;
            case "Glide":
                handleGlide(speedVal);
                break;
            case "Packet":
                handlePacket(speedVal);
                break;
        }
    }

    private void handleVanilla(double speedVal) {
        mc.field_71439_g.field_71075_bZ.field_75100_b = true;
        mc.field_71439_g.field_71075_bZ.func_195931_a((float) (speedVal / 5.0D));
    }

    private void handleCreative(double speedVal) {
        mc.field_71439_g.field_71075_bZ.field_75100_b = true;
        mc.field_71439_g.field_71075_bZ.func_195931_a((float) (speedVal / 5.0D));
        if (!mc.field_71439_g.field_71075_bZ.field_75098_d) {
            mc.field_71439_g.field_71075_bZ.field_75102_a = true;
        }
    }

    private void handleGlide(double speedVal) {
        Vector3d motion = mc.field_71439_g.func_213322_ci();
        double yaw = Math.toRadians(mc.field_71439_g.field_70177_z);
        double moveX = 0.0D;
        double moveZ = 0.0D;

        // Movement input
        if (mc.field_71474_y.field_74351_w.func_151470_d()) { // Forward
            moveX += Math.cos(yaw) * speedVal;
            moveZ += Math.sin(yaw) * speedVal;
        }
        if (mc.field_71474_y.field_74368_y.func_151470_d()) { // Back
            moveX -= Math.cos(yaw) * speedVal;
            moveZ -= Math.sin(yaw) * speedVal;
        }
        if (mc.field_71474_y.field_74370_x.func_151470_d()) { // Left
            moveX += Math.sin(yaw) * speedVal;
            moveZ -= Math.cos(yaw) * speedVal;
        }
        if (mc.field_71474_y.field_74366_z.func_151470_d()) { // Right
            moveX -= Math.sin(yaw) * speedVal;
            moveZ += Math.cos(yaw) * speedVal;
        }

        // Vertical movement
        double glide = -glideSpeed.getValue();
        if (mc.field_71474_y.field_74314_A.func_151470_d()) { // Jump
            glide = speedVal;
        } else if (mc.field_71474_y.field_228046_af_.func_151470_d()) { // Sneak
            glide = -speedVal;
        }

        mc.field_71439_g.func_213293_j(moveX, glide, moveZ);
    }

    private void handlePacket(double speedVal) {
        double yaw = Math.toRadians(mc.field_71439_g.field_70177_z);
        double moveX = 0.0D;
        double moveY = 0.0D;
        double moveZ = 0.0D;

        // Movement input
        if (mc.field_71474_y.field_74351_w.func_151470_d()) {
            moveX += Math.cos(yaw) * speedVal;
            moveZ += Math.sin(yaw) * speedVal;
        }
        if (mc.field_71474_y.field_74368_y.func_151470_d()) {
            moveX -= Math.cos(yaw) * speedVal;
            moveZ -= Math.sin(yaw) * speedVal;
        }
        if (mc.field_71474_y.field_74314_A.func_151470_d()) {
            moveY = speedVal;
        }
        if (mc.field_71474_y.field_228046_af_.func_151470_d()) {
            moveY = -speedVal;
        }

        double newX = mc.field_71439_g.func_226277_ct_() + moveX;
        double newY = mc.field_71439_g.func_226278_cu_() + moveY;
        double newZ = mc.field_71439_g.func_226281_cx_() + moveZ;

        mc.field_71439_g.func_70107_b(newX, newY, newZ);
        mc.field_71439_g.field_71174_a.func_147297_a(
                new PositionPacket(newX, newY, newZ, mc.field_71439_g.func_233570_aj_()));

        // Anti-kick
        tickCounter++;
        if (antiKick.getValue() && tickCounter >= 40) {
            tickCounter = 0;
            mc.field_71439_g.func_70107_b(newX, newY - 0.1D, newZ);
            mc.field_71439_g.field_71174_a.func_147297_a(
                    new PositionPacket(newX, newY - 0.1D, newZ, true));
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.field_71439_g != null) {
            mc.field_71439_g.field_71075_bZ.field_75100_b = false;
            mc.field_71439_g.field_71075_bZ.func_195931_a(0.05F);
            if (!mc.field_71439_g.field_71075_bZ.field_75098_d) {
                mc.field_71439_g.field_71075_bZ.field_75102_a = false;
            }
        }
        tickCounter = 0;
    }
}
