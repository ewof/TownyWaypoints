package net.mvndicraft.townywaypoints.settings;

public enum ConfigNodes {
    VERSION(
            "version",
            "",
            "# This is the current version. Please do not edit."),
    LANGUAGE("language",
            "en_US.yml",
            "# The language file you wish to use."),
    WAYPOINTS("waypoints","",""),
    WAYPOINTS_RESTRICTIONS(
            "waypoints.restrictions",
            "",
            "",
            "",
            "############################################################",
            "# +------------------------------------------------------+ #",
            "# |                   Restrictions                       | #",
            "# +------------------------------------------------------+ #",
            "############################################################",
    ""),
    WAYPOINTS_RESTRICTIONS_MAX_DISTANCE(
            "waypoints.restrictions.max_distance",
            "5000",
            "",
            "# If the maximum number of blocks a player can travel between waypoints."),
    WAYPOINTS_RESTRICTIONS_COOLDOWN(
            "waypoints.restrictions.cooldown",
            "1800",
            "",
            "# The amount of minutes a player must wait between waypoint travels.");

    private final String Root;
    private final String Default;
    private final String[] comments;

    ConfigNodes(String root, String def, String... comments)
    {

        this.Root = root;
        this.Default = def;
        this.comments = comments;
    }

    /**
     * Retrieves the root for a config option
     *
     * @return The root for a config option
     */
    public String getRoot()
    {

        return Root;
    }

    /**
     * Retrieves the default value for a config path
     *
     * @return The default value for a config path
     */
    public String getDefault()
    {

        return Default;
    }

    /**
     * Retrieves the comment for a config path
     *
     * @return The comments for a config path
     */
    public String[] getComments()
    {
        if (comments != null) {
            return comments;
        }

        String[] comments = new String[1];
        comments[0] = "";
        return comments;
    }
}
