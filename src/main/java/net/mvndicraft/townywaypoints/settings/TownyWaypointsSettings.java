package net.mvndicraft.townywaypoints.settings;

public class TownyWaypointsSettings {
    public static int getMaxDistance()
    {
        return Settings.getInt(ConfigNodes.WAYPOINTS_RESTRICTIONS_MAX_DISTANCE);
    }

    public static int getCooldown()
    {
        return Settings.getInt(ConfigNodes.WAYPOINTS_RESTRICTIONS_COOLDOWN);
    }
}
