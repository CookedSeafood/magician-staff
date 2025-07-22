package net.cookedseafood.magicianstaff.mixin;

import net.cookedseafood.magicianstaff.MagicianStaff;
import net.cookedseafood.magicianstaff.api.BatEntityApi;
import net.cookedseafood.magicianstaff.data.MagicianStaffConfig;
import net.cookedseafood.magicianstaff.world.explosion.ExplosiveBatExplosionBehavior;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import java.util.Collection;
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

        if (MagicianStaffConfig.isParticleVisible) {
            this.spawnTrailParticle();
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
            world.getDamageSources().create(DamageTypes.EXPLOSION, bat, bat.getCustomOwner()),
            new ExplosiveBatExplosionBehavior(),
            bat.getPos(),
            bat.getCustomExplosionRadius(),
            false,
            World.ExplosionSourceType.MOB
        );
        bat.spawnEffectsCloud();
        bat.discard();
    }

    @Override
    public void spawnEffectsCloud() {
        BatEntity bat = (BatEntity)(Object)this;
        Collection<StatusEffectInstance> collection = bat.getStatusEffects();
        if (collection.isEmpty()) {
            return;
        }

        AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(bat.getWorld(), bat.getX(), bat.getY(), bat.getZ());
        areaEffectCloudEntity.setRadius(2.5F);
        areaEffectCloudEntity.setRadiusOnUse(-0.5F);
        areaEffectCloudEntity.setWaitTime(10);
        areaEffectCloudEntity.setDuration(300);
        areaEffectCloudEntity.setPotionDurationScale(0.25F);
        areaEffectCloudEntity.setRadiusGrowth(-areaEffectCloudEntity.getRadius() / areaEffectCloudEntity.getDuration());

        for (StatusEffectInstance statusEffectInstance : collection) {
            areaEffectCloudEntity.addEffect(new StatusEffectInstance(statusEffectInstance));
        }

        bat.getWorld().spawnEntity(areaEffectCloudEntity);
    }

    @Override
    public void spawnTrailParticle() {
        BatEntity bat = (BatEntity)(Object)this;
        ((ServerWorld)bat.getWorld()).spawnParticles(
            ParticleTypes.SMOKE,
            bat.getX(),
            bat.getEyeY(),
            bat.getZ(),
            1,
            0,
            0,
            0,
            0
        );
    }
}
