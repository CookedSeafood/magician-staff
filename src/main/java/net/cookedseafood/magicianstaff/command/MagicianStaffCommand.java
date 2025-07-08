package net.cookedseafood.magicianstaff.command;

import com.mojang.brigadier.CommandDispatcher;
import net.cookedseafood.magicianstaff.MagicianStaff;
import net.cookedseafood.magicianstaff.data.MagicianStaffConfig;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class MagicianStaffCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(
            CommandManager.literal("magicianstaff")
            .then(
                CommandManager.literal("reload")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> executeReload((ServerCommandSource)context.getSource()))
            )
            .then(
                CommandManager.literal("version")
                .executes(context -> executeVersion((ServerCommandSource)context.getSource()))
            )
        );
    }

    public static int executeReload(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("Reloading Magician Staff!"), true);
        return MagicianStaffConfig.reload();
    }

    public static int executeVersion(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("Magician Staff " + MagicianStaff.VERSION_MAJOR + "." + MagicianStaff.VERSION_MINOR + "." + MagicianStaff.VERSION_PATCH), false);
        return 0;
    }
}
