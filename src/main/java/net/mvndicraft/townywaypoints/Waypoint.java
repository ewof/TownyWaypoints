package net.mvndicraft.townywaypoints;

public final class Waypoint {
    private static String name;
    private static String mapKey;
    private static double cost;
    private static boolean sea;
    private static String permission;

    public Waypoint(String name, String mapKey, double cost, boolean sea, String permission)
    {
        Waypoint.name = name;
        Waypoint.mapKey = mapKey;
        Waypoint.cost = cost;
        Waypoint.sea = sea;
        Waypoint.permission = permission;
    }

    public String getName()
    {
        return name;
    }

    public String getMapKey()
    {
        return mapKey;
    }

    public double getCost()
    {
        return cost;
    }

    public boolean isSea()
    {
        return sea;
    }

    public String getPermission()
    {
        return permission;
    }
}
