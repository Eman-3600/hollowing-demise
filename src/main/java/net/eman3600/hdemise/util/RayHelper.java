package net.eman3600.hdemise.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public interface RayHelper {
    static Vec3d rayZVector(float yaw, float pitch) {
        Vec3d rot = new Vec3d(0, 0, 1d);
        rot = rot.rotateX((float)Math.toRadians(-pitch));
        rot = rot.rotateY((float)Math.toRadians(-yaw));

        return rot;
    }

    static Vec3d rotateVector(Vec3d vec, float yaw, float pitch) {
        vec = vec.rotateX((float)Math.toRadians(-pitch));
        vec = vec.rotateY((float)Math.toRadians(-yaw));

        return vec;
    }

    static Vec3d flatRayZVector(float yaw, float pitch) {
        Vec3d rot = new Vec3d(0, 0, 0.1d);
        rot = rot.rotateX((float)Math.toRadians(-pitch));
        rot = rot.add(0, 0, 1);
        rot = rot.rotateY((float)Math.toRadians(-yaw));

        return rot;
    }

    static Vec3d rollYVector(float yaw, float pitch, float roll) {
        Vec3d rot = new Vec3d(0, 1d, 0);

        rot = rot.rotateZ((float)Math.toRadians(-roll));
        rot = rot.rotateX((float)Math.toRadians(-pitch));
        rot = rot.rotateY((float)Math.toRadians(-yaw));

        return rot;
    }

    static Vec3d rayXVector(float yaw, float pitch) {
        return rayZVector(yaw + 90, pitch);
    }

    @Nullable
    static EntityHitResult castWithDistance(@NotNull Entity caster, double distance, Predicate<Entity> predicate) {

        Vec3d pos = caster.getCameraPosVec(0);
        Vec3d angle = caster.getRotationVec(1.0f).multiply(distance);
        Vec3d extent = pos.add(angle.x, angle.y, angle.z);
        Box box = caster.getBoundingBox().stretch(angle).expand(1.0, 1.0, 1.0);

        return ProjectileUtil.raycast(caster, pos, extent, box, predicate, distance * distance);
    }
}
