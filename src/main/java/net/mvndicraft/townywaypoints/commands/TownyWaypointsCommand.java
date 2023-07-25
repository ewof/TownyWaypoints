package net.mvndicraft.townywaypoints.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.*;
import com.palmergames.bukkit.towny.tasks.CooldownTimerTask;
import net.kyori.adventure.text.Component;
import net.mvndicraft.townywaypoints.TownyWaypoints;
import net.mvndicraft.townywaypoints.Waypoint;
import net.mvndicraft.townywaypoints.settings.Settings;
import net.mvndicraft.townywaypoints.settings.TownyWaypointsSettings;
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

    @Subcommand("reload") @CommandPermission(TownyWaypoints.ADMIN_PERMISSION)
    @Description("Reloads the plugin config and locales.")
    public static void onReload(Player player)
    {
        Settings.loadConfigAndLang();
        Messaging.sendMsg(player, Translatable.of("townywaypoints_msg_reload", TownyWaypoints.getInstance().getName()));
    }

    @Subcommand("set open")
    @Syntax("set <property> [<value>]")
    @CommandCompletion("@open_statuses @nothing")
    @Description("Change which people the plot is open to teleports from.")
    public static void onSetOpen(Player player, String status)
    {
        if (!player.hasPermission(TownyWaypoints.ADMIN_PERMISSION) && !player.hasPermission("towny.command.town.toggle.public")) {
            Messaging.sendErrorMsg(player, Translatable.of("msg_err_waypoint_set_open_insufficient_permission"));
            return;
        }

        TownBlock townBlock = TownyAPI.getInstance().getTownBlock(player);
        if (townBlock == null) {
            Messaging.sendErrorMsg(player,Translatable.of("msg_err_not_in_townblock"));
            return;
        }

        TownBlockMetaDataController.setSdf(townBlock, TownBlockMetaDataController.statusKey, status);

        Messaging.sendMsg(player,Translatable.of("msg_status_set",status));
    }

    @Subcommand("set spawn")
    @Syntax("set <property> <value>")
    @Description("Set the block a player gets teleported to on arival for a waypoint plot.")
    public static void onSetSpawn(Player player)
    {
        if (!player.hasPermission(TownyWaypoints.ADMIN_PERMISSION) && !player.hasPermission("towny.command.town.set.spawn")) {
            Messaging.sendErrorMsg(player, Translatable.of("msg_err_waypoint_set_spawn_insufficient_permission"));
            return;
        }

        TownBlock townBlock = TownyAPI.getInstance().getTownBlock(player);
        if (townBlock == null) {
            Messaging.sendErrorMsg(player, Translatable.of("msg_err_not_in_townblock"));
            return;
        }

        Location loc = player.getLocation();

        Messaging.sendMsg(player, Translatable.of("msg_spawn_set",loc.toString()));
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

        Waypoint waypoint = TownyWaypoints.getWaypoints().get(waypointName);
        double travelcost = waypoint.getTravelCost();

        String plotName = townBlock.getName();
        if (plotName.equals(""))
            plotName = Translatable.of("townywaypoints_plot_unnamed").defaultLocale();

        if (!player.hasPermission(TownyWaypoints.ADMIN_PERMISSION) && TownyWaypoints.getEconomy().getBalance(player) - travelcost < 0) {
            Messaging.sendErrorMsg(player, Translatable.of("msg_err_waypoint_travel_insufficient_funds", plotName, travelcost));
            return;
        }

        Location loc = TownBlockMetaDataController.getSpawn(townBlock);

        if (loc.getWorld() == null) {
            Messaging.sendErrorMsg(player, Translatable.of("msg_err_waypoint_spawn_not_set"));
            return;
        }

        if (!player.hasPermission(TownyWaypoints.ADMIN_PERMISSION) && (TownyWaypointsSettings.getMaxDistance() != -1 && player.getLocation().distance(loc) > TownyWaypointsSettings.getMaxDistance())) {
            Messaging.sendErrorMsg(player, Translatable.of("msg_err_waypoint_travel_too_far", townBlock.getName(), TownyWaypointsSettings.getMaxDistance()));
            return;
        }

        TownyAPI townyAPI = TownyAPI.getInstance();

        TownBlock playerTownBlock = townyAPI.getTownBlock(player);

        if (!player.hasPermission(TownyWaypoints.ADMIN_PERMISSION) && (playerTownBlock == null || TownyWaypointsSettings.getPeerToPeer() && !playerTownBlock.getType().getName().equals(waypointName))) {
            Messaging.sendErrorMsg(player, Translatable.of("msg_err_waypoint_p2p", waypointName, waypointName));
            return;
        }

        Resident res = townyAPI.getResident(player);
        if (res == null)
            return;

        int cooldown = CooldownTimerTask.getCooldownRemaining(player.getName(), "waypoint");
        if (player.hasPermission(TownyWaypoints.ADMIN_PERMISSION) || cooldown == 0) {
            TownyWaypoints.getEconomy().withdrawPlayer(player, travelcost);
            Messaging.sendMsg(player, Translatable.of("msg_waypoint_travel_warmup"));
            townyAPI.requestTeleport(player, loc);
            if (!CooldownTimerTask.hasCooldown(player.getName(), "waypoint"))
                CooldownTimerTask.addCooldownTimer(player.getName(), "waypoint", TownyWaypointsSettings.getCooldown());
        } else {
            Messaging.sendErrorMsg(player, Translatable.of("msg_err_waypoint_travel_cooldown", cooldown, townBlock.getName()));
        }
    }
}
