package net.cookedseafood.magicianstaff;

import net.cookedseafood.magicianstaff.command.MagicianStaffCommand;
import net.cookedseafood.magicianstaff.data.MagicianStaffConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MagicianStaff implements ModInitializer {
    public static final String MOD_ID = "magician-staff";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final byte VERSION_MAJOR = 1;
    public static final byte VERSION_MINOR = 3;
    public static final byte VERSION_PATCH = 0;

    public static final String MOD_NAMESPACE = "magician_staff";
    public static final String MAGICIAN_STAFF_CUSTOM_ID = Identifier.of(MOD_NAMESPACE, "magician_staff").toString();
    public static final String EXPLOSIVE_BAT_CUSTOM_ID = Identifier.of(MOD_NAMESPACE, "explosive_bat").toString();

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> MagicianStaffCommand.register(dispatcher, registryAccess));

        ServerLifecycleEvents.SERVER_STARTED.register(server -> MagicianStaffConfig.reload());

        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (Hand.OFF_HAND.equals(hand)) {
                return ActionResult.PASS;
            }

            ItemStack stack = player.getMainHandStack();
            if (!MAGICIAN_STAFF_CUSTOM_ID.equals(stack.getCustomId())) {
                return ActionResult.PASS;
            }

            return usedBy(player, (ServerWorld)world) ? ActionResult.SUCCESS : ActionResult.FAIL;
        });
    }

    public static boolean usedBy(LivingEntity entity, ServerWorld world) {
        if (!entity.consumMana(MagicianStaffConfig.manaConsumption)) {
            return false;
        }

        BatEntity bat = new BatEntity(EntityType.BAT, world);
        bat.setCustomId(EXPLOSIVE_BAT_CUSTOM_ID);
        bat.setCustomFuse(MagicianStaffConfig.explosionFuse);
        bat.setCustomExplosionRadius(MagicianStaffConfig.explosionRadius);
        bat.setCustomOwner(entity);
        bat.setRoosting(false);
        bat.refreshPositionAndAngles(entity.getEyePos(), entity.getYaw(), entity.getPitch());
        bat.initialize(world, world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.EVENT, null);
        world.spawnEntity(bat);
        return true;
    }
}
