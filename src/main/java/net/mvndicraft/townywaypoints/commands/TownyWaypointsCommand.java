package net.mvndicraft.townywaypoints.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.Translatable;
import net.kyori.adventure.text.Component;
import net.mvndicraft.townywaypoints.TownyWaypoints;
import net.mvndicraft.townywaypoints.settings.Settings;
import net.mvndicraft.townywaypoints.util.Messaging;
import net.mvndicraft.townywaypoints.util.TownBlockMetaDataController;
import org.bukkit.Location;
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
        TownBlock townBlock = TownyAPI.getInstance().getTownBlock(player);

        if (townBlock == null) {
            Messaging.sendErrorMsg(player,Translatable.of("msg_err_not_in_townblock"));
            return;
        }

        TownBlockMetaDataController.setSdf(townBlock, "townywaypoints_status", status);

        Messaging.sendMsg(player,Translatable.of("msg_status_set",status));
    }

    @Subcommand("set spawn")
    @Syntax("set <property> <value>")
    @Description("Set the block a player gets teleported to on arival for a waypoint plot.")
    public static void onSetSpawn(Player player)
    {
        TownBlock townBlock = TownyAPI.getInstance().getTownBlock(player);

        if (townBlock == null) {
            Messaging.sendErrorMsg(player,Translatable.of("msg_err_not_in_townblock"));
            return;
        }

        Location loc = player.getLocation();

        Messaging.sendMsg(player,Translatable.of("msg_spawn_set",loc.toString()));
        TownBlockMetaDataController.setSpawn(townBlock, loc);
    }

    @Subcommand("travel")
    @Syntax("<town> <waypoint> <plot name>")
    @CommandCompletion("@waypointed_towns @town_waypoints @waypoint_plot_names @nothing")
    @Description("Travel between different waypoints.")
    public static void onTravel(Player player, String townName, String waypointName, String waypointPlotName)
    {
        Town town = TownyAPI.getInstance().getTown(townName);

        if (town == null)
            return;

        TownBlock townBlock = null;
        for (TownBlock _townBlock:town.getTownBlocks())
        {
            String plotName = _townBlock.getName();
            if (plotName.equals(""))
                plotName = Translatable.of("townywaypoints_plot_unnamed").defaultLocale();

            if (_townBlock.getType().getName().equals(waypointName) && plotName.equals(waypointPlotName)) {
                townBlock = _townBlock;
                break;
            }
        }

        if (townBlock == null)
            return;

        Location loc = TownBlockMetaDataController.getSpawn(townBlock);

        if (loc.getWorld() == null) {
            Messaging.sendErrorMsg(player,Translatable.of("msg_err_waypoint_spawn_not_set"));
            return;
        }

        player.teleportAsync(loc);
    }
}
