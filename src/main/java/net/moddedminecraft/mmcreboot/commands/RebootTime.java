package net.moddedminecraft.mmcreboot.commands;

import net.moddedminecraft.mmcreboot.Config.Config;
import net.moddedminecraft.mmcreboot.Config.Messages;
import net.moddedminecraft.mmcreboot.Main;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

public class RebootTime implements CommandExecutor {

    private final Main plugin;
    public RebootTime(Main instance) {
        plugin = instance;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(!plugin.tasksScheduled) {
            throw new CommandException(plugin.fromLegacy(Messages.getErrorNoTaskScheduled()));
        }

            double timeLeft = (Config.restartInterval * 3600) - ((double)(System.currentTimeMillis() - plugin.startTimestamp) / 1000);
            int hours = (int)(timeLeft / 3600);
            int minutes = (int)((timeLeft - hours * 3600) / 60);
            int seconds = (int)timeLeft % 60;

            plugin.sendMessage(src, Messages.getRestartMessageWithoutReason()
                    .replace("%hours%", String.valueOf(hours))
                    .replace("%minutes%", String.valueOf(minutes))
                    .replace("%seconds%", String.valueOf(seconds)));
            return CommandResult.success();
        } else if(Config.restartType.equalsIgnoreCase("realtime")) {
            double timeLeft = plugin.nextRealTimeRestart - ((double)(System.currentTimeMillis() - plugin.startTimestamp) / 1000);
            int hours = (int)(timeLeft / 3600);
            int minutes = (int)((timeLeft - hours * 3600) / 60);
            int seconds = (int)timeLeft % 60;

            plugin.sendMessage(src, Messages.getRestartMessageWithoutReason()
                    .replace("%hours%", String.valueOf(hours))
                    .replace("%minutes%", String.valueOf(minutes))
                    .replace("%seconds%", String.valueOf(seconds)));
            return CommandResult.success();
        } else {
            throw new CommandException(plugin.fromLegacy(Messages.getErrorNoTaskScheduled()));
        }
    }
}
