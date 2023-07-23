package net.mvndicraft.townywaypoints;

import co.aikar.commands.PaperCommandManager;
import com.github.Anon8281.universalScheduler.UniversalScheduler;
import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import com.google.common.collect.ImmutableList;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.*;
import net.mvndicraft.townywaypoints.commands.TownyWaypointsCommand;
import net.mvndicraft.townywaypoints.listeners.TownyListener;
import net.mvndicraft.townywaypoints.settings.Settings;
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

    Settings.loadConfigAndLang();

    taskScheduler = UniversalScheduler.getScheduler(instance);

    PaperCommandManager manager = new PaperCommandManager(instance);
    manager.registerCommand(new TownyWaypointsCommand());
    manager.getCommandCompletions().registerAsyncCompletion("waypointed_towns", c -> {
      ArrayList<String> towns = new ArrayList<>();
      TownyAPI.getInstance().getTowns().forEach(town -> getWaypoints().keySet().forEach(waypoint -> {
       if (town.getTownBlockTypeCache().getNumTownBlocks(TownBlockTypeHandler.getType(waypoint), TownBlockTypeCache.CacheType.ALL) > 0)
         towns.add(town.getName());
      }));
     return towns;
    });
    manager.getCommandCompletions().registerAsyncCompletion("town_waypoints", c -> {
      Town town = TownyAPI.getInstance().getTown(c.getContextValue(String.class, 1));
      ArrayList<String> waypoints = new ArrayList<>();

      if (town == null)
        return waypoints;

      getWaypoints().keySet().forEach(waypoint -> {
        if (town.getTownBlockTypeCache().getNumTownBlocks(TownBlockTypeHandler.getType(waypoint), TownBlockTypeCache.CacheType.ALL) > 0)
          waypoints.add(waypoint);
      });
     return waypoints;
    });
    manager.getCommandCompletions().registerAsyncCompletion("waypoint_plot_names", c -> {
      Town town = TownyAPI.getInstance().getTown(c.getContextValue(String.class, 1));

      ArrayList<String> plots = new ArrayList<>();

      if (town == null)
        return plots;

      town.getTownBlocks().forEach(townBlock -> {
        if (townBlock.getType().getName().equals(c.getContextValue(String.class, 2))) {
          if (townBlock.getName().equals("")) {
            plots.add(Translatable.of("townywaypoints_plot_unnamed").defaultLocale());
          } else {
            plots.add(townBlock.getName());
          }
        }
      });

      return plots;
    });
    manager.getCommandCompletions().registerAsyncCompletion("open_statuses", c -> ImmutableList.of(Translatable.of("open_status_all").defaultLocale(), Translatable.of("open_status_allies").defaultLocale(), Translatable.of("open_status_nation").translate(), Translatable.of("open_status_town").translate(), Translatable.of("open_status_none").translate()));

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
