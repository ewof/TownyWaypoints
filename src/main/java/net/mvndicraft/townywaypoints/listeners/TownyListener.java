package net.mvndicraft.townywaypoints.listeners;

import com.palmergames.bukkit.towny.event.PlotPreChangeTypeEvent;
import com.palmergames.bukkit.towny.event.TownBlockTypeRegisterEvent;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.*;
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
    TownyWaypoints.loadWaypoints();
  }

  private int getPlotTypeCount(Town town, String name)
  {
    int count = 0;

    for (TownBlock townBlock : town.getTownBlocks())
    {
      if (townBlock.getType().getName().equalsIgnoreCase(name))
        count++;
    }

    return count;
  }

  @EventHandler
  public void onPlotChangeTypeEvent(PlotPreChangeTypeEvent event) throws NotRegisteredException {
    TownBlock townBlock = event.getTownBlock();
    String plotTypeName = event.getNewType().getName();
    int max = TownyWaypoints.getWaypoints().get(plotTypeName).getMax();

    if (!TownyWaypoints.getWaypoints().containsKey(plotTypeName))
      return;

    if (getPlotTypeCount(townBlock.getTown(), plotTypeName) >= max) {
      event.setCancelMessage(String.format("Only %d plot(s) with this type allowed!", max));
      event.setCancelled(true);
    }
  }
}
