package net.cookedseafood.magicianstaff.world.explosion;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;

public class ExplosiveBatExplosionBehavior extends ExplosionBehavior {
    @Override
    public boolean canDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power) {
        return false;
    }
}
