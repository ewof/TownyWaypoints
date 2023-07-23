package net.mvndicraft.townywaypoints.settings;

public class TownyWaypointsSettings {
    public static boolean getCooldownsEnabled()
    {
        return Settings.getBoolean(ConfigNodes.WAYPOINTS_COOLDOWNS_ENABLED);
    }

    public static int getMaxCooldown()
    {
        return Settings.getInt(ConfigNodes.WAYPOINTS_COOLDOWNS_MAX_COOLDOWN);
    }
}
