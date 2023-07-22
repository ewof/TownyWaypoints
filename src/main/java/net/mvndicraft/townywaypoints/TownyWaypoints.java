package net.mvndicraft.townywaypoints;

import com.github.Anon8281.universalScheduler.UniversalScheduler;
import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import net.mvndicraft.townywaypoints.listeners.TownyListener;
import net.mvndicraft.townywaypoints.settings.WaypointsSettings;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TownyWaypoints extends JavaPlugin
{
  private static TownyWaypoints instance;
  private static TaskScheduler taskScheduler;
  protected static final ConcurrentHashMap<String, Waypoint> waypoints = new ConcurrentHashMap<>();
  private final String biomeKey = "allowed_biomes";

  @Override
  public void onEnable()
  {
    PluginManager plugMan = Bukkit.getPluginManager();

    WaypointsSettings.loadConfigAndLang();

    taskScheduler = UniversalScheduler.getScheduler(instance);

    TownyListener townyListener = new TownyListener();

    plugMan.registerEvents(townyListener, instance);

    getLogger().info("enabled!");
  }

  @Override
  public void onLoad()
  {
    instance = this;
    loadWaypoints();
  }

  public static void loadWaypoints()
  {
    File waypointsDataFile = new File(instance.getDataFolder(), "waypoints.yml");

    if (!waypointsDataFile.exists())
      instance.saveResource("waypoints.yml", true);

    FileConfiguration waypointsData = YamlConfiguration.loadConfiguration(waypointsDataFile);
    Set<String> waypointsConfig = waypointsData.getKeys(false);
    waypointsConfig.forEach(waypointConfig -> {
      ConfigurationSection waypointConfigSection = waypointsData.getConfigurationSection(waypointConfig);
      if (waypointConfigSection == null)
        return;
      Waypoint waypoint = createWaypoint(waypointConfigSection);
      TownyListener.registerPlot(waypoint.getName(), waypoint.getMapKey(), waypoint.getCost());
      waypoints.put(waypoint.getName(), waypoint);
    });
  }

  @Override
  public void onDisable()
  {
    getLogger().info("disabled!");
  }

  private static Waypoint createWaypoint(ConfigurationSection config)
  {
    return new Waypoint(
      config.getString("name"),
      config.getString("mapKey"),
      config.getDouble("cost"),
      config.getInt("max"),
      config.getBoolean("sea"),
      config.getString("permission"),
      config.contains(instance.biomeKey) ? config.getStringList(instance.biomeKey) : new ArrayList<>()
    );
  }

  public static TownyWaypoints getInstance()
  {
    return instance;
  }
  public String getVersion() {
    return instance.getPluginMeta().getVersion();
  }
  public static TaskScheduler getScheduler()
  {
    return taskScheduler;
  }
  public static ConcurrentHashMap<String, Waypoint> getWaypoints()
  {
    return waypoints;
  }
}
