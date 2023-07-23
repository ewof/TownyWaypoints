package net.mvndicraft.townywaypoints.listeners;

import com.palmergames.bukkit.towny.event.PlotPreChangeTypeEvent;
import com.palmergames.bukkit.towny.event.TownBlockTypeRegisterEvent;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.*;
import net.mvndicraft.townywaypoints.TownyWaypoints;
import net.mvndicraft.townywaypoints.Waypoint;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class TownyListener implements Listener
{
  private final static TownyWaypoints instance = TownyWaypoints.getInstance();
  public static void registerPlot(String name, String mapKey, double cost)
  {
    if (TownBlockTypeHandler.exists(name))
      return;

    TownBlockType townBlockType = new TownBlockType(name, new TownBlockData() {
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
      instance.getLogger().info("registering new plot type " + name);
      TownBlockTypeHandler.registerType(townBlockType);
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

    if (!TownyWaypoints.getWaypoints().containsKey(plotTypeName))
        return;
    Waypoint waypoint = TownyWaypoints.getWaypoints().get(plotTypeName);

    World world = townBlock.getWorld().getBukkitWorld();
    if (world == null)
      return;
    Player p = event.getResident().getPlayer();
    if (p == null)
      return;
    Location loc = p.getLocation();

    String biomeName = loc.getBlock().getBiome().toString();
    if  (waypoint.getAllowedBiomes().size() != 0 && !waypoint.getAllowedBiomes().contains(biomeName)) {
       event.setCancelMessage(Translatable.of("msg_err_biome_not_allowed",biomeName).defaultLocale());
       event.setCancelled(true);
    }

    int max = waypoint.getMax();

    if (getPlotTypeCount(townBlock.getTown(), plotTypeName) >= max) {
      event.setCancelMessage(Translatable.of("msg_err_max_plots",max).defaultLocale());
      event.setCancelled(true);
    }
  }
}
