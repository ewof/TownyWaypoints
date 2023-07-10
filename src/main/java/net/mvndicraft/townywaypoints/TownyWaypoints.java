package net.mvndicraft.townywaypoints;

import net.mvndicraft.townywaypoints.listeners.TownyListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

public class TownyWaypoints extends JavaPlugin
{
  private static JavaPlugin plugin;

  private static final ArrayList<Waypoint> waypoints = new ArrayList<>();

  @Override
  public void onEnable()
  {
    PluginManager plugMan = Bukkit.getPluginManager();

    TownyListener townyListener = new TownyListener();

    plugMan.registerEvents(townyListener, plugin);

    getLogger().info("enabled!");
  }

  @Override
  public void onLoad()
  {
    plugin = this;

    File waypointsDataFile = new File(getDataFolder(), "waypoints.yml");

    if (!waypointsDataFile.exists())
      saveResource("waypoints.yml", true);

    FileConfiguration waypointsData = YamlConfiguration.loadConfiguration(waypointsDataFile);
    Set<String> waypointsConfig = waypointsData.getKeys(false);
    waypointsConfig.forEach(waypointConfig -> {
      Waypoint waypoint = createWaypoint(waypointsData.getConfigurationSection(waypointConfig));
      waypoints.add(waypoint);
      TownyListener.registerPlot(waypoint.getName(), waypoint.getMapKey(), waypoint.getCost());
    });
  }

  @Override
  public void onDisable()
  {
    getLogger().info("disabled!");
  }

  private Waypoint createWaypoint(ConfigurationSection config)
  {
    return new Waypoint(
      config.getString("name"),
      config.getString("mapKey"),
      config.getDouble("cost"),
      config.getBoolean("sea"),
      config.getString("permission")
    );
  }

  public static JavaPlugin getPlugin()
  {
    return plugin;
  }

  public static ArrayList<Waypoint> getWaypoints()
  {
    return waypoints;
  }
}
