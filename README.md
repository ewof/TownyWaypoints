# TownyWaypoints

Configurable plot types for Towny that players can teleport between.

### Traveling

Players can travel between different waypoints with `/twp travel <town> <waypoint> <plot name>`

---

### Config

Default config:

```yaml
# This is the current version. Please do not edit.
version: '1.0'
# The language file you wish to use.
language: en_US.yml

waypoints:


  ############################################################
  # +------------------------------------------------------+ #
  # |                   Restrictions                       | #
  # +------------------------------------------------------+ #
  ############################################################

  restrictions:

    # The maximum number of blocks a player can travel between waypoints.
    # Disabled with value of -1
    max_distance: '5000'

    # The amount of minutes a player must wait between waypoint travels.
    cooldown: '1800'

    # If true players can only teleport from one waypoint type to another.
    peer_to_peer: 'true'
```

- `waypoints.restrictions.max_distance` is the maximum number of blocks a player can travel via waypoints. A player cannot travel to a waypoint that is `max_distance` blocks away from their current location.
- `waypoints.restrictions.cooldown` is the amount of time in seconds a player must  way between waypoint travels.
- `waypoints.restrictions.peer_to_peer` when set to true means that players must be standing in a waypoint of the same type to teleport to another waypoint, meaning I must be standing in a stable plot if I want to travel to another stable waypoint.

---

### The `waypoints.yml` file

In `waypoints.yml` you define different types of waypoints.

```yaml
stable:
  name: "stable"
  mapKey: 'S' # A single character to be shown on the /towny map and /towny map hud.
  cost: 100.0 # A cost that will be paid to set the plot type.
  max: 1 # Max number of plots of this type allowed per town.
  permission: townywaypoints.landpoint.stable # Permission node required to set a plot to a type of this waypoint, if no permission is set anyone can create this waypoint, grant it in townyperms.yml
seaport:
  name: "seaport"
  mapKey: 'P' # A single character to be shown on the /towny map and /towny map hud.
  cost: 200.0 # A cost that will be paid to set the plot type.
  max: 2 # Max number of plots of this type allowed per town.
  allowed_biomes: # List of biomes this plot type can be created on. If it's not provided the plot type can be created on any biome.
    - BEACH
```

A `waypoints.yml` like this means two plot types will be available to players, stables and seaports.

Stables
- Have a map key of `S`
- Cost $100 to travel to
- Are limited to 2 per town
- Players need the `townywaypoints.landpoint.stable` permission node to set a plot to this type
- Can be created in any biome

Seaports
- Have a map key of `P`
- cost $200 to travel to
- Are limited to 2 per town
- Any player can set a plot to this type
- Can only be created in `BEACH` biomes

A player designates a plot as a waypoint by doing `/plot set <waypoint type name>`. <br/>
They can change things about the waypoint with `/twp set <open> [value]`. <br/>
Waypoints travel can be open to everyone or limited to allies, nation members, town members, or noone (closed)  with `/twp set open <status>`. <br/>
The block a player gets teleported to on traveling to a waypoint can be changed with `/twp set spawn` a waypoints default spawn is where the player was standing when they designated the plot as a waypoint.

---

### Permission nodes

- `townywaypoints.admin` Allows use of the reload command and disables cooldown/distance/p2p check on travel.

---

### Translations

If you'd like to help translating TownyWaypoints into the available languages or add entirely new languages, [we're on Crowdin!](https://crowdin.com/project/townywaypoints)

[![Crowdin](https://badges.crowdin.net/townywaypoints/localized.svg)](https://crowdin.com/project/townywaypoints)

---

Inspired by [TownyPorts](https://github.com/darthpeti/TownyPorts/)