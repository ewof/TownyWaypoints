package net.mvndicraft.townywaypoints;

import java.util.List;

public final class Waypoint {
    private final String name;
    private final String mapKey;
    private final double cost;
    private final double travelCost;
    private final int max;
    private final boolean sea;
    private final boolean travelWithVehicule;
    private final String permission;
    private final List<String> allowedBiomes;

    public Waypoint(String name, String mapKey, double cost, double travelCost, int max, boolean sea, boolean travelWithVehicule, String permission, List<String> allowedBiomes)
    {
        this.name = name;
        this.mapKey = mapKey;
        this.cost = cost;
        this.travelCost = travelCost;
        this.max = max;
        this.sea = sea;
        this.travelWithVehicule = travelWithVehicule;
        this.permission = permission;
        this.allowedBiomes = allowedBiomes;
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

    public double getTravelCost() { return travelCost; }

    public int getMax()
    {
        return max;
    }

    public boolean isSea()
    {
        return sea;
    }
    public boolean isTravelWithHorses()
    {
        return travelWithVehicule;
    }

    public String getPermission()
    {
        return permission;
    }

    public List<String> getAllowedBiomes()
    {
        return allowedBiomes;
    }
}
