package com.gondas.client.module.impl;

import com.gondas.client.core.GondasClient;
import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.network.play.client.CPlayerPacket.RotationPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

/**
 * KillAura Module - Auto attack nearby entities
 * Improved with better anticheat bypass and mobile optimization
 */
public class KillAura extends Module {
    // Target settings
    private Setting.Double range = new Setting.Double("Range", 3.2, 1.0, 6.0);
    private Setting.Double wallsRange = new Setting.Double("WallsRange", 3.0, 1.0, 6.0);
    private Setting.Boolean players = new Setting.Boolean("Players", true);
    private Setting.Boolean mobs = new Setting.Boolean("Mobs", false);
    private Setting.Boolean animals = new Setting.Boolean("Animals", false);
    private Setting.Boolean invisible = new Setting.Boolean("Invisible", true);
    private Setting.Boolean walls = new Setting.Boolean("Walls", false);

    // Attack settings
    private Setting.Double minCps = new Setting.Double("MinCPS", 8.0, 1.0, 20.0);
    private Setting.Double maxCps = new Setting.Double("MaxCPS", 12.0, 1.0, 20.0);
    private Setting.Boolean swing = new Setting.Boolean("Swing", true);
    private Setting.Boolean keepSprint = new Setting.Boolean("KeepSprint", true);
    private Setting.Boolean onlyWeapon = new Setting.Boolean("OnlyWeapon", true);

    // Rotation settings
    private Setting.Mode rotation = new Setting.Mode("Rotation", "Silent", "None", "Normal");
    private Setting.Mode targetMode = new Setting.Mode("Target", "Closest", "Health", "Angle");
    private Setting.Double rotSpeed = new Setting.Double("RotSpeed", 90.0, 1.0, 180.0);
    private Setting.Boolean smoothRot = new Setting.Boolean("SmoothRot", true);
    private Setting.Double jitter = new Setting.Double("Jitter", 1.5, 0.0, 5.0);
    private Setting.Double aimNoise = new Setting.Double("AimNoise", 0.5, 0.0, 3.0);

    // Anticheat settings
    private Setting.Mode acMode = new Setting.Mode("ACMode", "NCP", "Vanilla", "AAC", "Matrix", "Grim");
    private Setting.Boolean fovCheck = new Setting.Boolean("FOVCheck", false);
    private Setting.Double maxFov = new Setting.Double("MaxFOV", 180.0, 30.0, 360.0);

    // Performance optimization for mobile
    private Setting.Boolean optimizeMobile = new Setting.Boolean("OptimizeMobile", true);

    private final Random random = new Random();
    private int attackCooldown = 0;
    private float targetYaw;
    private float targetPitch;
    private float currentYaw;
    private float currentPitch;
    private LivingEntity target = null;

    public KillAura() {
        super("KillAura", "Auto attack nearby entities with anticheat bypass", Module.Category.COMBAT);
        addSettings(range, wallsRange, players, mobs, animals, invisible, walls,
                minCps, maxCps, swing, keepSprint, onlyWeapon,
                rotation, targetMode, rotSpeed, smoothRot, jitter, aimNoise,
                acMode, fovCheck, maxFov, optimizeMobile);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.field_71439_g != null) {
            currentYaw = mc.field_71439_g.field_70177_z;
            currentPitch = mc.field_71439_g.field_70125_A;
            targetYaw = currentYaw;
            targetPitch = currentPitch;
        }
        target = null;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        target = null;
    }

    @Override
    public void onTick() {
        if (mc.field_71439_g == null || mc.field_71441_e == null) {
            return;
        }

        // Mobile optimization - skip ticks if needed
        if (optimizeMobile.getValue() && GondasClient.isMobileDevice()) {
            if (random.nextInt(100) < 10) {
                return; // Skip 10% of ticks on mobile for performance
            }
        }

        // Calculate CPS with variation
        double baseCps = minCps.getValue() + random.nextDouble() * (maxCps.getValue() - minCps.getValue());
        if (random.nextInt(100) < 15) {
            baseCps += random.nextDouble() * 2.0 - 1.0;
        }

        int cooldownMax = Math.max(1, (int) (20.0 / baseCps));

        if (attackCooldown > 0) {
            attackCooldown--;
            if (target != null && !rotation.getValue().equals("None")) {
                updateRotationsSmooth();
            }
            return;
        }

        if (onlyWeapon.getValue() && !isHoldingWeapon()) {
            target = null;
            return;
        }

        target = findTarget();

        if (target == null) {
            if (rotation.getValue().equals("Silent") && mc.field_71439_g != null) {
                targetYaw = mc.field_71439_g.field_70177_z;
                targetPitch = mc.field_71439_g.field_70125_A;
                updateRotationsSmooth();
            }
            return;
        }

        float[] targetRotations = getRotations(target);
        if (aimNoise.getValue() > 0.0) {
            targetRotations[0] += (random.nextFloat() - 0.5F) * (float) aimNoise.getValue();
            targetRotations[1] += (random.nextFloat() - 0.5F) * (float) aimNoise.getValue() * 0.5F;
            targetRotations[1] = MathHelper.func_76131_a(targetRotations[1], -90.0F, 90.0F);
        }

        targetYaw = targetRotations[0];
        targetPitch = targetRotations[1];

        if (!rotation.getValue().equals("None")) {
            updateRotationsSmooth();
        }

        double dist = mc.field_71439_g.func_70032_d(target);
        double maxRange = canSeeTarget(target) ? range.getValue() : wallsRange.getValue();

        if (walls.getValue() || canSeeTarget(target)) {
            if (dist <= maxRange) {
                attack(target);
                attackCooldown = Math.max(1, cooldownMax + random.nextInt(3) - 1);
            }
        }
    }

    private void updateRotationsSmooth() {
        if (mc.field_71439_g == null) return;

        float speed = (float) rotSpeed.getValue();
        float gcd = (float) (mc.field_71474_y.field_74341_c * 0.6000000238418579 + 0.20000000298023224);
        gcd = gcd * gcd * gcd * 8.0F;

        float yawDiff = MathHelper.func_76142_g(targetYaw - currentYaw);
        float pitchDiff = MathHelper.func_76142_g(targetPitch - currentPitch);

        float yawChange = MathHelper.func_76131_a(yawDiff, -speed, speed);
        float pitchChange = MathHelper.func_76131_a(pitchDiff, -speed, speed);

        if (smoothRot.getValue()) {
            float dist = Math.abs(yawDiff) + Math.abs(pitchDiff);
            if (dist < 30.0F) {
                float factor = dist / 30.0F;
                yawChange *= 0.5F + factor * 0.5F;
                pitchChange *= 0.5F + factor * 0.5F;
            }
        }

        if (jitter.getValue() > 0.0 && random.nextInt(10) < 3) {
            yawChange += (random.nextFloat() - 0.5F) * (float) jitter.getValue();
            pitchChange += (random.nextFloat() - 0.5F) * (float) jitter.getValue() * 0.3F;
        }

        yawChange = (float) Math.round(yawChange / gcd) * gcd;
        pitchChange = (float) Math.round(pitchChange / gcd) * gcd;

        currentYaw += yawChange;
        currentPitch += pitchChange;
        currentPitch = MathHelper.func_76131_a(currentPitch, -90.0F, 90.0F);

        if (rotation.getValue().equals("Silent")) {
            mc.field_71439_g.field_71174_a.func_147297_a(
                    new RotationPacket(currentYaw, currentPitch, mc.field_71439_g.func_233570_aj_()));
        } else {
            mc.field_71439_g.field_70177_z = currentYaw;
            mc.field_71439_g.field_70125_A = currentPitch;
        }
    }

    private LivingEntity findTarget() {
        List<LivingEntity> possibleTargets = new ArrayList<>();

        for (Entity e : mc.field_71441_e.func_217416_b()) {
            if (!(e instanceof LivingEntity)) continue;
            if (e == mc.field_71439_g) continue;

            LivingEntity living = (LivingEntity) e;

            // Filter by type
            if (living instanceof PlayerEntity && !players.getValue()) continue;
            if (living instanceof MonsterEntity && !mobs.getValue()) continue;
            if (living instanceof AnimalEntity && !animals.getValue()) continue;
            if (living.func_82150_aj() && !invisible.getValue()) continue;
            if (living.func_110143_aJ() <= 0.0F) continue;

            double dist = mc.field_71439_g.func_70032_d(living);
            double maxRange = canSeeTarget(living) ? range.getValue() : wallsRange.getValue();

            if (!walls.getValue() && !canSeeTarget(living)) continue;
            if (dist > maxRange || dist <= 0.0D) continue;

            // FOV check
            if (fovCheck.getValue()) {
                float[] rot = getRotations(living);
                float yawDiff = Math.abs(MathHelper.func_76142_g(rot[0] - currentYaw));
                if (yawDiff > maxFov.getValue() / 2.0D) continue;
            }

            possibleTargets.add(living);
        }

        if (possibleTargets.isEmpty()) return null;

        // Sort by target mode
        String mode = targetMode.getValue();
        switch (mode) {
            case "Health":
                possibleTargets.sort(Comparator.comparingDouble(LivingEntity::func_110143_aJ));
                break;
            case "Angle":
                possibleTargets.sort(Comparator.comparingDouble(this::getAngleDiff));
                break;
            default:
                possibleTargets.sort(Comparator.comparingDouble(e -> mc.field_71439_g.func_70032_d(e)));
        }

        return possibleTargets.get(0);
    }

    private boolean canSeeTarget(LivingEntity entity) {
        return mc.field_71439_g.func_70685_l(entity);
    }

    private double getAngleDiff(LivingEntity entity) {
        float[] rot = getRotations(entity);
        return Math.abs(MathHelper.func_76142_g(rot[0] - currentYaw)) +
                Math.abs(MathHelper.func_76142_g(rot[1] - currentPitch));
    }

    private float[] getRotations(LivingEntity targetEntity) {
        double x = targetEntity.func_226277_ct_() - mc.field_71439_g.func_226277_ct_();
        double y = targetEntity.func_226280_cw_() - mc.field_71439_g.func_226280_cw_();
        double z = targetEntity.func_226281_cx_() - mc.field_71439_g.func_226281_cx_();
        double dist = Math.sqrt(x * x + z * z);
        float yaw = (float) (Math.toDegrees(Math.atan2(z, x)) - 90.0D);
        float pitch = (float) (-Math.toDegrees(Math.atan2(y, dist)));
        return new float[]{yaw, MathHelper.func_76131_a(pitch, -90.0F, 90.0F)};
    }

    private void attack(LivingEntity targetEntity) {
        if (swing.getValue()) {
            mc.field_71439_g.func_184609_a(Hand.MAIN_HAND);
        }

        boolean wasSprinting = mc.field_71439_g.func_70051_ag();
        if (wasSprinting) {
            mc.field_71439_g.func_70031_b(false);
        }

        mc.field_71442_b.func_78764_a(mc.field_71439_g, targetEntity);

        if (keepSprint.getValue() && wasSprinting) {
            mc.field_71439_g.func_70031_b(true);
        }
    }

    private boolean isHoldingWeapon() {
        return mc.field_71439_g.func_184614_ca().func_77973_b() instanceof SwordItem ||
                mc.field_71439_g.func_184614_ca().func_77973_b() instanceof AxeItem;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public float getCurrentYaw() {
        return currentYaw;
    }

    public float getCurrentPitch() {
        return currentPitch;
    }
}
