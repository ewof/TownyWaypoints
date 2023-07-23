package net.mvndicraft.townywaypoints.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;
import net.kyori.adventure.text.Component;
import net.mvndicraft.townywaypoints.TownyWaypoints;
import net.mvndicraft.townywaypoints.settings.Settings;
import org.bukkit.entity.Player;

@CommandAlias("townywaypoints|twaypoints|twp")
public class TownyWaypointsCommand extends BaseCommand
{
    @Default
    @Description("Lists the version of the plugin")
    public static void onTownyWaypoints(Player player)
    {
        player.sendMessage(Component.text(TownyWaypoints.getInstance().toString()));
    }

    @Subcommand("reload") @CommandPermission("townywaypoints.admin")
    @Description("Reloads the plugin config and locales.")
    public static void onReload(Player player)
    {
        Settings.loadConfigAndLang();
        player.sendMessage(Component.text(TownyWaypoints.getInstance().getName() + " reloaded! If you updated waypoints.yml you must do /ta reload aswell."));
    }

    @Subcommand("set open")
    @Syntax("set <property> [<value>]")
    @CommandCompletion("@open_statuses @nothing")
    @Description("Change which people the plot is open to teleports from.")
    public static void onSetOpen(Player player, String status)
    {
        player.sendMessage("Changing status to " + status);
    }

    @Subcommand("set spawn")
    @Syntax("set <property> [<value>]")
    @Description("Set the block a player gets teleported to on arival for a waypoint plot.")
    public static void onSetSpawn(Player player)
    {
        player.sendMessage("lol");
    }

    @Subcommand("travel")
    @Syntax("<town> <waypoint> <plot name>")
    @CommandCompletion("@waypointed_towns @town_waypoints @waypoint_plot_names @nothing")
    @Description("Travel between different waypoints.")
    public static void onTravel(Player player, String townName, String waypointName)
    {
        Town town = TownyAPI.getInstance().getTown(townName);

        if (town == null)
            return;

        player.sendMessage(town.getName());
    }
}
