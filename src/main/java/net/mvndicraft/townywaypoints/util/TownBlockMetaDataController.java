package net.mvndicraft.townywaypoints.util;

import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.metadata.CustomDataField;
import com.palmergames.bukkit.towny.object.metadata.IntegerDataField;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class TownBlockMetaDataController {

    private static final String spawnWorldKey = "townywaypoints_spawn_world";
    private static final String spawnXKey = "townywaypoints_spawn_x";
    private static final String spawnYKey = "townywaypoints_spawn_y";
    private static final String spawnZKey = "townywaypoints_spawn_z";

    public static int getIdf(TownBlock townBlock, String key)
    {
        if (townBlock.hasMeta(key)) {
            CustomDataField<?> cdf = townBlock.getMetadata(key);
            if (cdf instanceof IntegerDataField)
                return ((IntegerDataField) cdf).getValue();
        }
        return 0;
    }

    public static void setIdf(TownBlock townBlock, String key, int num)
    {
        if (townBlock.hasMeta(key)) {
            CustomDataField<?> cdf = townBlock.getMetadata(key);
            if (num == 0 && cdf != null)
                townBlock.removeMetaData(cdf);
            else {
                if (cdf instanceof IntegerDataField) {
                    ((IntegerDataField) cdf).setValue(num);
                    townBlock.save();
                }
            }
        } else if (num != 0)
            townBlock.addMetaData(new IntegerDataField(key, num));
    }

    public static String getSdf(TownBlock townBlock, String key)
    {
        if (townBlock.hasMeta(key)) {
            CustomDataField<?> cdf = townBlock.getMetadata(key);
            if (cdf instanceof StringDataField)
                return ((StringDataField) cdf).getValue();
        }
        return "";
    }

    public static void setSdf(TownBlock townBlock, String key, String value)
    {
        if (townBlock.hasMeta(key)) {
            CustomDataField<?> cdf = townBlock.getMetadata(key);
            if (value.equals("") && cdf != null)
                townBlock.removeMetaData(cdf);
            else {
                if (cdf instanceof StringDataField) {
                    ((StringDataField) cdf).setValue(value);
                    townBlock.save();
                }
            }
        } else if (!value.equals(""))
            townBlock.addMetaData(new StringDataField(key, value));
    }

    public static void setSpawn(TownBlock townBlock, Location loc)
    {
        setSdf(townBlock, spawnWorldKey, loc.getWorld().getName());
        setIdf(townBlock, spawnXKey, loc.getBlockX());
        setIdf(townBlock, spawnYKey, loc.getBlockY());
        setIdf(townBlock, spawnZKey, loc.getBlockZ());
    }

    public static Location getSpawn(TownBlock townBlock)
    {
        return new Location(Bukkit.getServer().getWorld(getSdf(townBlock, spawnWorldKey)), getIdf(townBlock, spawnXKey), getIdf(townBlock, spawnYKey), getIdf(townBlock, spawnZKey));
    }
}
