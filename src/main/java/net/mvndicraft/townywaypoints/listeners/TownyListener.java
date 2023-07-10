package net.mvndicraft.townywaypoints.listeners;

import com.palmergames.bukkit.towny.event.TownBlockTypeRegisterEvent;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.TownBlockData;
import com.palmergames.bukkit.towny.object.TownBlockType;
import com.palmergames.bukkit.towny.object.TownBlockTypeHandler;
import net.mvndicraft.townywaypoints.TownyWaypoints;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TownyListener implements Listener
{
  private final static JavaPlugin plugin = TownyWaypoints.getPlugin();
  public static void registerPlot(String name, String mapKey, double cost)
  {
    if (TownBlockTypeHandler.exists(name))
      return;

    TownBlockType plot = new TownBlockType(name, new TownBlockData() {
      @Override
      public String getMapKey()
      {
        return mapKey;
      }

      @Override
      public double getCost()
      {
        return cost;
      }
    });

    try {
      plugin.getLogger().info("registering new plot type " + name);
      TownBlockTypeHandler.registerType(plot);
    } catch (TownyException e) {
      Bukkit.getLogger().severe(e.getMessage());
    }
  }

  @EventHandler
  public void onTownyLoadTownBlockTypes(TownBlockTypeRegisterEvent event)
  {
    TownyWaypoints.getWaypoints().forEach(waypoint -> registerPlot(waypoint.getName(), waypoint.getMapKey(), waypoint.getCost()));
  }
}
