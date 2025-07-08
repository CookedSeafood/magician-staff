package net.cookedseafood.magicianstaff.mixin;

import net.cookedseafood.magicianstaff.MagicianStaff;
import net.cookedseafood.magicianstaff.api.BatEntityApi;
import net.cookedseafood.magicianstaff.data.MagicianStaffConfig;
import net.cookedseafood.magicianstaff.world.explosion.ExplosiveBatExplosionBehavior;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BatEntity.class)
public abstract class BatEntityMixin implements BatEntityApi {
    @Inject(
        method = "tick()V",
        at = @At("RETURN")
    )
    private void tickExplosiveBat(CallbackInfo ci) {
        BatEntity bat = (BatEntity)(Object)this;

        if (!MagicianStaff.EXPLOSIVE_BAT_CUSTOM_ID.equals(bat.getCustomId())) {
            return;
        }

        short fuse = bat.getCustomFuse();

        if (bat.horizontalCollision || bat.verticalCollision || !bat.getWorld().getOtherEntities(bat, bat.getBoundingBox()).isEmpty() || fuse == 0) {
            bat.explode();
            return;
        }

        bat.setCustomFuse(--fuse);
    }

    @ModifyArg(
        method = "mobTick(Lnet/minecraft/server/world/ServerWorld;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/BatEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"),
        index = 0
    )
    private Vec3d modifyExplosiveBatVelocity(Vec3d velocity) {
        return MagicianStaff.EXPLOSIVE_BAT_CUSTOM_ID.equals(((BatEntity)(Object)this).getCustomId()) ? ((BatEntity)(Object)this).getCustomOwner().getRotationVector().multiply(MagicianStaffConfig.movementSpeed) : velocity;
    }

    @ModifyArg(
        method = "mobTick(Lnet/minecraft/server/world/ServerWorld;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/BatEntity;setYaw(F)V"),
        index = 0
    )
    private float modifyExplosiveBatYaw(float yaw) {
        return MagicianStaff.EXPLOSIVE_BAT_CUSTOM_ID.equals(((BatEntity)(Object)this).getCustomId()) ? ((BatEntity)(Object)this).getCustomOwner().getYaw() : yaw;
    }

    @Override
    public void explode() {
        BatEntity bat = (BatEntity)(Object)this;
        World world = bat.getWorld();
        bat.setDead(true);
        world.createExplosion(
            bat,
            world.getDamageSources().explosion(bat, bat.getCustomOwner()),
            new ExplosiveBatExplosionBehavior(),
            bat.getPos(),
            bat.getCustomExplosionRadius(),
            false,
            World.ExplosionSourceType.MOB
        );
        bat.discard();
    }
}
